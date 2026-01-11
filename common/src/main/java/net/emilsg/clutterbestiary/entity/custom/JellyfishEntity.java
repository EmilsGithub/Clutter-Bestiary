package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.custom.goal.JellyfishAvoidSurfaceGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.JellyfishSwimGoal;
import net.emilsg.clutterbestiary.entity.variants.JellyfishVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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

public class JellyfishEntity extends WaterCreatureEntity implements Bucketable {

    private static final TrackedData<String> VARIANT = DataTracker.registerData(JellyfishEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(JellyfishEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final Predicate<LivingEntity> TARGET_FILTER = entity -> {
        if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) {
            return false;
        }
        return !entity.getType().isIn(EntityTypeTags.AQUATIC);
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

    public JellyfishEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        this.random.setSeed(this.getId());
        this.thrustTimerSpeed = 0.25f / (this.random.nextFloat() + 1.0f) * 0.2f;
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setVariant(JellyfishVariant.getRandom());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, JellyfishVariant.GREEN.getId());
        builder.add(FROM_BUCKET, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new JellyfishSwimGoal(this));
        this.goalSelector.add(1, new JellyfishAvoidSurfaceGoal(this));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
        this.setFromBucket(nbt.getBoolean("FromBucket"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("FromBucket", this.isFromBucket());
    }

    @Override
    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
        if (nbt.contains("Variant")) {
            this.setVariant(JellyfishVariant.fromId(nbt.getString("Variant")));
        }
    }

    @Override
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
        NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack, nbt -> nbt.putString("Variant", this.getTypeVariant()));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return WaterCreatureEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos).isIn(ModBlockTags.JELLYFISHES_SPAWN_ON);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isFromBucket() && !this.hasCustomName();
    }

    @Override
    public boolean cannotDespawn() {
        return super.cannotDespawn() || this.isFromBucket();
    }

    @Override
    public boolean isFromBucket() {
        return this.dataTracker.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.dataTracker.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ModItems.JELLYFISH_BUCKET.get());
    }

    @Override
    public SoundEvent getBucketFillSound() {
        return SoundEvents.ITEM_BUCKET_FILL_FISH;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return Bucketable.tryBucket(player, hand, this).orElseGet(() -> super.interactMob(player, hand));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean damaged = super.damage(source, amount);
        if (damaged && source.getAttacker() instanceof LivingEntity attacker
                && attacker.getWorld() instanceof ServerWorld && !attacker.getType().isIn(EntityTypeTags.AQUATIC)) {
            if (random.nextInt(3) == 0) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1), this);
            }
        }
        return damaged;
    }

    public float getSwimX() {
        return swimX;
    }

    public float getSwimZ() {
        return swimZ;
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
            AnimationPlayback.updateLoop(this, this.swimmingAnimationState, this.isAlive());
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.isAlive() && !this.getWorld().isClient) {
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
                StatusEffectInstance levitation = this.getStatusEffect(StatusEffects.LEVITATION);
                if (levitation != null) {
                    e = 0.05 * (double) (levitation.getAmplifier() + 1);
                } else if (!this.hasNoGravity()) {
                    e -= 0.08;
                }
                this.setVelocity(0.0, e * (double) 0.49f, 0.0);
            }
            this.tiltAngle += (-90.0f - this.tiltAngle) * 0.02f;
        }
    }

    @Override
    public void travel(Vec3d movementInput) {
        this.move(MovementType.SELF, this.getVelocity());
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
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

    private void sting(LivingEntity mob) {
        if (mob.damage(this.getDamageSources().mobAttack(this), 2)) {
            mob.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60 * 2, 1), this);
            this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0f, 1.0f);
        }
    }

}
