package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyLarvaCocoonGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyLarvaWanderGoal;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ButterflyLarvaEntity extends PathAwareEntity {
    public static final int WANDER_TICKS = 24000;
    public static final int EXPIRE_TICKS = 72000;
    public static final int HOME_RADIUS = 12;
    private static final TrackedData<String> VARIANT = DataTracker.registerData(ButterflyLarvaEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> CLIMBING = DataTracker.registerData(ButterflyLarvaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState walkingAnimState = new AnimationState();

    private BlockPos homePos;
    private boolean shouldValidateHome = true;
    private int lifeTicks;

    public ButterflyLarvaEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, ButterflyVariant.WHITE.getId());
        builder.add(CLIMBING, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new ButterflyLarvaCocoonGoal(this));
        this.goalSelector.add(2, new ButterflyLarvaWanderGoal(this));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.lifeTicks = nbt.getInt("LifeTicks");
        this.homePos = nbt.contains("HomePos") ? BlockPos.fromLong(nbt.getLong("HomePos")) : null;
        this.setVariant(ButterflyVariant.fromId(nbt.getString("Variant")));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("LifeTicks", this.lifeTicks);
        nbt.putLong("HomePos", this.getHomePos().asLong());
        nbt.putString("Variant", this.getVariant().getId());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.16)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
    }

    public BlockPos getHomePos() {
        return this.homePos == null ? this.getBlockPos() : this.homePos;
    }

    public void setHomePos(BlockPos homePos) {
        this.homePos = homePos.toImmutable();
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public ButterflyVariant getVariant() {
        return ButterflyVariant.fromId(this.dataTracker.get(VARIANT));
    }

    public void setVariant(ButterflyVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public boolean isClimbing() {
        return this.dataTracker.get(CLIMBING);
    }

    public void setClimbing(boolean climbing) {
        this.dataTracker.set(CLIMBING, climbing);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean isFireImmune() {
        return this.getVariant().isFireImmune();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            AnimationPlayback.updateLoop(this, this.walkingAnimState, this.isAlive() && this.getVelocity().lengthSquared() > 1.0E-4);
        } else if (this.getWorld() instanceof ServerWorld) {
            if (this.shouldValidateHome) {
                if (this.homePos == null || !this.homePos.isWithinDistance(this.getPos(), HOME_RADIUS * 2.0)) {
                    this.setHomePos(this.getBlockPos());
                }
                this.shouldValidateHome = false;
            }
            if (++this.lifeTicks >= EXPIRE_TICKS) this.discard();
        }
    }
}
