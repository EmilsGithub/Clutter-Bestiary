package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.custom.goal.JellyfishAvoidSurfaceGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.JellyfishSwimGoal;
import net.emilsg.clutterbestiary.entity.variants.JellyfishVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class JellyfishEntity extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(JellyfishEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(JellyfishEntity.class, EntityDataSerializers.BOOLEAN);

    private static final TargetingConditions.Selector TARGET_FILTER = (entity, level) -> {
        if (entity instanceof Player && ((Player) entity).isCreative()) {
            return false;
        }
        return !entity.is(EntityTypeTags.AQUATIC);
    };
    private static final TargetingConditions TARGET_PREDICATE = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().selector(TARGET_FILTER);
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

    public JellyfishEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
        this.thrustTimerSpeed = 0.25f / (this.random.nextFloat() + 1.0f) * 0.2f;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        this.setVariant(JellyfishVariant.getRandom());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, JellyfishVariant.GREEN.getId());
        builder.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new JellyfishSwimGoal(this));
        this.goalSelector.addGoal(1, new JellyfishAvoidSurfaceGoal(this));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
        this.setFromBucket(nbt.getBooleanOr("FromBucket", false));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void loadFromBucketTag(CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag(this, nbt);
        if (nbt.contains("Variant")) {
            this.setVariant(JellyfishVariant.fromId(nbt.getStringOr("Variant", "")));
        }
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbt -> nbt.putString("Variant", this.getTypeVariant()));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return WaterAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterAnimal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos).is(ModBlockTags.JELLYFISHES_SPAWN_ON);
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.JELLYFISH_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElseGet(() -> super.mobInteract(player, hand));
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        boolean damaged = super.hurtServer(serverLevel, source, amount);
        if (damaged && source.getEntity() instanceof LivingEntity attacker
                && attacker.level() instanceof ServerLevel && !attacker.is(EntityTypeTags.AQUATIC)) {
            if (random.nextInt(3) == 0) {
                attacker.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1), this);
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
        return this.entityData.get(VARIANT);
    }

    public JellyfishVariant getVariant() {
        return JellyfishVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(JellyfishVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
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
        Level world = this.level();
        if (world.isClientSide()) {
            AnimationPlayback.updateLoop(this, this.swimmingAnimationState, this.isAlive());
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isAlive() && !this.level().isClientSide()) {
            List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.3), entity -> this.level() instanceof ServerLevel serverLevel && TARGET_PREDICATE.test(serverLevel, this, entity));
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
            if (this.level().isClientSide()) {
                this.thrustTimer = (float) Math.PI * 2;
            } else {
                this.thrustTimer -= (float) Math.PI * 2;
                if (this.random.nextInt(10) == 0) {
                    this.thrustTimerSpeed = 0.25f / (this.random.nextFloat() + 1.0f) * 0.1f;
                }
            }
        }
        if (this.isInWater()) {
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
            if (!this.level().isClientSide()) {
                this.setDeltaMovement(this.swimX * this.swimVelocityScale * 0.3f,
                        this.swimY * this.swimVelocityScale * 0.3f,
                        this.swimZ * this.swimVelocityScale * 0.3f);
            }
            Vec3 vec3d = this.getDeltaMovement();
            double d = vec3d.horizontalDistance();
            float targetYaw = (float) (-Mth.atan2(vec3d.x, vec3d.z) * 57.295776f); // Target yaw from direction vector
            float deltaYaw = Mth.wrapDegrees(targetYaw - this.getYRot()); // Shortest direction to rotate
            this.setYRot(this.getYRot() + deltaYaw * 0.1f); // Gradual yaw update (smooth rotation)
            float targetPitch = (float) -Math.toDegrees(Mth.atan2(vec3d.y, vec3d.horizontalDistance())); // Target pitch
            float deltaPitch = Mth.wrapDegrees(targetPitch - this.getXRot()); // Shortest vertical rotation
            this.setXRot(this.getXRot() + deltaPitch * 0.1f); // Gradual pitch update
            this.rollAngle += (float) Math.PI * this.turningSpeed * 1.5f;
            this.tiltAngle += (-((float) Mth.atan2(d, vec3d.y)) * 57.295776f - this.tiltAngle) * 0.05f; // Reduce tilt speed
            if (Math.abs(this.tiltAngle) > 30.0f) {
                this.tiltAngle *= 0.9f; // Dampen large tilt oscillations
            }

        } else {
            if (!this.level().isClientSide()) {
                double e = this.getDeltaMovement().y;
                MobEffectInstance levitation = this.getEffect(MobEffects.LEVITATION);
                if (levitation != null) {
                    e = 0.05 * (double) (levitation.getAmplifier() + 1);
                } else if (!this.isNoGravity()) {
                    e -= 0.08;
                }
                this.setDeltaMovement(0.0, e * (double) 0.49f, 0.0);
            }
            this.tiltAngle += (-90.0f - this.tiltAngle) * 0.02f;
        }
    }

    @Override
    public void travel(Vec3 movementInput) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0f, 1.0f);
        } else {
            f = 0.0f;
        }

        this.walkAnimation.update(f * 1.25f, 0.5F, 1.0F);
    }

    private void sting(LivingEntity mob) {
        if (this.level() instanceof ServerLevel serverLevel && mob.hurtServer(serverLevel, this.damageSources().mobAttack(this), 2)) {
            mob.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * 2, 1), this);
            this.playSound(SoundEvents.SLIME_ATTACK, 1.0f, 1.0f);
        }
    }

}
