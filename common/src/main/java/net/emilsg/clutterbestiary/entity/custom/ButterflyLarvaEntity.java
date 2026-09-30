package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyLarvaCocoonGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyLarvaWanderGoal;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

public class ButterflyLarvaEntity extends PathfinderMob {
    public static final int WANDER_TICKS = 24000;
    public static final int EXPIRE_TICKS = 72000;
    public static final int HOME_RADIUS = 12;
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(ButterflyLarvaEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(ButterflyLarvaEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState walkingAnimState = new AnimationState();

    private BlockPos homePos;
    private boolean shouldValidateHome = true;
    private int lifeTicks;

    public ButterflyLarvaEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.LAVA, -1.0F);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, ButterflyVariant.WHITE.getId());
        builder.define(CLIMBING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ButterflyLarvaCocoonGoal(this));
        this.goalSelector.addGoal(2, new ButterflyLarvaWanderGoal(this));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.lifeTicks = nbt.getIntOr("LifeTicks", 0);
        this.homePos = nbt.getLong("HomePos").map(BlockPos::of).orElse(null);
        this.setVariant(ButterflyVariant.fromId(nbt.getStringOr("Variant", "")));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("LifeTicks", this.lifeTicks);
        nbt.putLong("HomePos", this.getHomePos().asLong());
        nbt.putString("Variant", this.getVariant().getId());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.16)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public BlockPos getHomePos() {
        return this.homePos == null ? this.blockPosition() : this.homePos;
    }

    public void setHomePos(BlockPos homePos) {
        this.homePos = homePos.immutable();
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public ButterflyVariant getVariant() {
        return ButterflyVariant.fromId(this.entityData.get(VARIANT));
    }

    public void setVariant(ButterflyVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    public boolean onClimbable() {
        return this.entityData.get(CLIMBING);
    }

    public void setClimbing(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return this.getVariant().isFireImmune();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            AnimationPlayback.updateLoop(this, this.walkingAnimState, this.isAlive() && this.getDeltaMovement().lengthSqr() > 1.0E-4);
        } else if (this.level() instanceof ServerLevel) {
            if (this.shouldValidateHome) {
                if (this.homePos == null || !this.homePos.closerToCenterThan(this.position(), HOME_RADIUS * 2.0)) {
                    this.setHomePos(this.blockPosition());
                }
                this.shouldValidateHome = false;
            }
            if (++this.lifeTicks >= EXPIRE_TICKS) this.discard();
        }
    }
}
