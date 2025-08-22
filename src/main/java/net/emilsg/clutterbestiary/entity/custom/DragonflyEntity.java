package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.DragonflyFastWanderGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.DragonflyHoverLilypadGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.EscapeWaterGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.HoverGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.DragonflyVariant;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class DragonflyEntity extends ParentAnimalEntity {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(DragonflyEntity.class, TrackedDataHandlerRegistry.STRING);
    public final AnimationState flyingAnimState = new AnimationState();
    private int animationTimeout = 0;

    public DragonflyEntity(EntityType<? extends ParentAnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new SnappyFlightMoveControl(this);
        this.lookControl = new DragonflyLookControl(this);
        this.setNoGravity(true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -2.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 3f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public DragonflyVariant getVariant() {
        return DragonflyVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(DragonflyVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        DragonflyVariant variant = DragonflyVariant.getRandom();
        this.setVariant(variant);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, SeahorseVariant.YELLOW.getId());
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.DRAGONFLY.create(world);
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeWaterGoal(this));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0f, 1.0f, 1.2f));
        this.goalSelector.add(2, new DragonflyHoverLilypadGoal(this));
        this.goalSelector.add(3, new HoverGoal(this));
        this.goalSelector.add(3, new DragonflyFastWanderGoal(this));
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    protected EntityNavigation createNavigation(World world) {

        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return this.world.getBlockState(pos.down()).isAir();
            }
        };

        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    private String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private void setupAnimationStates() {
        if (this.animationTimeout <= 0) {
            this.animationTimeout = 20;
            this.flyingAnimState.start(this.age);
        } else {
            --this.animationTimeout;
        }
    }

    private static class DragonflyLookControl extends LookControl {
        DragonflyLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            super.tick();
        }

        protected boolean shouldStayHorizontal() {
            return true;
        }

    }

    public static class SnappyFlightMoveControl extends MoveControl {
        private final MobEntity mob;
        private final float multiplier = 0.05f;

        public SnappyFlightMoveControl(MobEntity mob) {
            super(mob);
            this.mob = mob;
        }

        @Override public void tick() {
            if (state != State.MOVE_TO) return;

            Vec3d to = new Vec3d(targetX - mob.getX(), targetY - mob.getY(), targetZ - mob.getZ());
            if (to.lengthSquared() < 0.01) {
                state = State.WAIT; return;
            }

            Vec3d dir = to.normalize();
            double boost = this.speed * multiplier;
            mob.setVelocity(mob.getVelocity().multiply(0.6).add(dir.multiply(boost)));
            mob.setYaw((float)(MathHelper.atan2(dir.z, dir.x) * (180f/Math.PI)) - 90f);
            mob.bodyYaw = mob.getYaw();
        }
    }

}