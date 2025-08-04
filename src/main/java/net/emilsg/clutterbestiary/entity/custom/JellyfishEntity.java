package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.custom.goal.JellyfishAvoidSurfaceGoal;
import net.emilsg.clutterbestiary.entity.variants.JellyfishVariant;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class JellyfishEntity extends WaterCreatureEntity {

    private static final TrackedData<String> VARIANT = DataTracker.registerData(JellyfishEntity.class, TrackedDataHandlerRegistry.STRING);

    private static final Predicate<LivingEntity> TARGET_FILTER = entity -> {
        if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) {
            return false;
        }
        return entity.getType() == EntityType.AXOLOTL || entity.getGroup() != EntityGroup.AQUATIC;
    };
    private static final TargetPredicate TARGET_PREDICATE = TargetPredicate.createNonAttackable().ignoreDistanceScalingFactor().ignoreVisibility().setPredicate(TARGET_FILTER);
    public final AnimationState swimmingAnimationState = new AnimationState();
    public float tiltAngle;
    public float prevTiltAngle;
    public float rollAngle;
    public float prevRollAngle;
    public float thrustTimer;
    public float prevThrustTimer;
    private float swimVelocityScale;
    private float thrustTimerSpeed;
    private float turningSpeed;
    private float swimX;
    private float swimY;
    private float swimZ;
    private int animationTimeout = 0;

    public JellyfishEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        this.random.setSeed(this.getId());
        this.thrustTimerSpeed = 0.25f / (this.random.nextFloat() + 1.0f) * 0.2f;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return WaterCreatureEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos).getFluidState().isOf(Fluids.WATER);
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return !this.isLeashed();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity attacker && attacker.getWorld() instanceof ServerWorld && attacker.getGroup() != EntityGroup.AQUATIC) {
            if (random.nextInt(3) == 0) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1), this);
            }
        }
        return super.damage(source, amount);
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public JellyfishVariant getVariant() {
        return JellyfishVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(JellyfishVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public boolean hasSwimmingVector() {
        return this.swimX != 0.0f || this.swimY != 0.0f || this.swimZ != 0.0f;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setVariant(JellyfishVariant.getRandom());
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
    }

    public void setSwimmingVector(float x, float y, float z) {
        this.swimX = x;
        this.swimY = y;
        this.swimZ = z;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    public float getSwimX() {
        return swimX;
    }

    public void setSwimX(float swimX) {
        this.swimX = swimX;
    }

    @Override
    public void travel(Vec3d movementInput) {
        this.move(MovementType.SELF, this.getVelocity());
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, JellyfishVariant.GREEN.getId());
    }

    public float getSwimY() {
        return swimY;
    }

    public void setSwimY(float swimY) {
        this.swimY = swimY;
    }

    private void sting(LivingEntity mob) {
        if (mob.damage(this.getDamageSources().mobAttack(this), 2)) {
            mob.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60 * 2, 1), this);
            this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0f, 1.0f);
        }
    }

    public float getSwimZ() {
        return swimZ;
    }

    public void setSwimZ(float swimZ) {
        this.swimZ = swimZ;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.isAlive()) {
            List<LivingEntity> list = this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(0.3), entity -> TARGET_PREDICATE.test(this, entity));
            for (LivingEntity mobEntity : list) {
                if (!mobEntity.isAlive()) continue;
                this.sting(mobEntity);
            }
        }
        this.prevTiltAngle = this.tiltAngle;
        this.prevRollAngle = this.rollAngle;
        this.prevThrustTimer = this.thrustTimer;
        this.thrustTimer += this.thrustTimerSpeed;
        if ((double) this.thrustTimer > Math.PI * 2) {
            if (this.getWorld().isClient) {
                this.thrustTimer = (float) Math.PI * 2;
            } else {
                this.thrustTimer -= (float) Math.PI * 2;
                if (this.random.nextInt(10) == 0) {
                    this.thrustTimerSpeed = 0.25f / (this.random.nextFloat() + 1.0f) * 0.1f;
                }
            }
        }
        if (this.isInsideWaterOrBubbleColumn()) {
            if (this.thrustTimer < (float) Math.PI) {
                float f = this.thrustTimer / (float) Math.PI;
                if ((double) f > 0.75) {
                    this.swimVelocityScale = 1.0f;
                    this.turningSpeed = 1.0f;
                } else {
                    this.turningSpeed *= 0.8f;
                }
            } else {
                this.swimVelocityScale *= 0.9f;
                this.turningSpeed *= 0.99f;
            }
            if (!this.getWorld().isClient) {
                this.setVelocity(this.swimX * this.swimVelocityScale * 0.3f,
                        this.swimY * this.swimVelocityScale * 0.3f,
                        this.swimZ * this.swimVelocityScale * 0.3f);
            }
            Vec3d vec3d = this.getVelocity();
            double d = vec3d.horizontalLength();
            float targetYaw = (float) (-MathHelper.atan2(vec3d.x, vec3d.z) * 57.295776f); // Target yaw from direction vector
            float deltaYaw = MathHelper.wrapDegrees(targetYaw - this.getYaw()); // Shortest direction to rotate
            this.setYaw(this.getYaw() + deltaYaw * 0.1f); // Gradual yaw update (smooth rotation)
            float targetPitch = (float) -Math.toDegrees(MathHelper.atan2(vec3d.y, vec3d.horizontalLength())); // Target pitch
            float deltaPitch = MathHelper.wrapDegrees(targetPitch - this.getPitch()); // Shortest vertical rotation
            this.setPitch(this.getPitch() + deltaPitch * 0.1f); // Gradual pitch update
            this.rollAngle += (float) Math.PI * this.turningSpeed * 1.5f;
            this.tiltAngle += (-((float) MathHelper.atan2(d, vec3d.y)) * 57.295776f - this.tiltAngle) * 0.05f; // Reduce tilt speed
            if (Math.abs(this.tiltAngle) > 30.0f) {
                this.tiltAngle *= 0.9f; // Dampen large tilt oscillations
            }

        } else {
            if (!this.getWorld().isClient) {
                double e = this.getVelocity().y;
                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    e = 0.05 * (double) (this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1);
                } else if (!this.hasNoGravity()) {
                    e -= 0.08;
                }
                this.setVelocity(0.0, e * (double) 0.49f, 0.0);
            }
            this.tiltAngle += (-90.0f - this.tiltAngle) * 0.02f;
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new JellyfishAvoidSurfaceGoal(this));
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0f, 1.0f);
        } else {
            f = 0.0f;
        }

        this.limbAnimator.updateLimbs(f * 1.25f, 0.5F);
    }

    private void setupAnimationStates() {
        if (this.animationTimeout <= 0) {
            this.animationTimeout = 80;
            this.swimmingAnimationState.start(this.age);
        } else {
            --this.animationTimeout;
        }
    }

    static class SwimGoal extends Goal {
        private final JellyfishEntity jellyfish;

        public SwimGoal(JellyfishEntity jellyfish) {
            this.jellyfish = jellyfish;
        }

        @Override
        public boolean canStart() {
            return this.jellyfish.isInsideWaterOrBubbleColumn();
        }

        @Override
        public void tick() {
            int i = this.jellyfish.getDespawnCounter();
            if (i > 100) {
                this.jellyfish.setSwimmingVector(0.0f, 0.0f, 0.0f);
            } else if (this.jellyfish.getRandom().nextInt(SwimGoal.toGoalTicks(50)) == 0 || !this.jellyfish.isTouchingWater() || !this.jellyfish.hasSwimmingVector()) {
                float f = this.jellyfish.getRandom().nextFloat() * ((float) Math.PI * 2);
                float g = MathHelper.cos(f) * 0.2f;
                float h = -0.1f + this.jellyfish.getRandom().nextFloat() * 0.2f;
                float j = MathHelper.sin(f) * 0.2f;
                this.jellyfish.setSwimmingVector(g, h, j);
            }
        }
    }

}
