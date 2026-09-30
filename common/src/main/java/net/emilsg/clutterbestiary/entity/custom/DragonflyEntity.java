package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.DragonflyFastWanderGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.DragonflyHoverLilypadGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.EscapeWaterGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.HoverGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.DragonflyVariant;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DragonflyEntity extends ParentAnimalEntity {
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(DragonflyEntity.class, EntityDataSerializers.STRING);
    public final AnimationState flyingAnimState = new AnimationState();

    public DragonflyEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new SnappyFlightMoveControl(this);
        this.lookControl = new DragonflyLookControl(this);
        this.setNoGravity(true);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -2.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        DragonflyVariant variant = DragonflyVariant.getRandom();
        this.setVariant(variant);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, SeahorseVariant.YELLOW.getID());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EscapeWaterGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 8.0f, 1.0f, 1.2f));
        this.goalSelector.addGoal(2, new DragonflyHoverLilypadGoal(this));
        this.goalSelector.addGoal(3, new HoverGoal(this));
        this.goalSelector.addGoal(3, new DragonflyFastWanderGoal(this));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", this.getTypeVariant());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.FLYING_SPEED, 3f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.DRAGONFLIES_SPAWN_ON);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.DRAGONFLY.get().create(world, EntitySpawnReason.BREEDING);
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    public DragonflyVariant getVariant() {
        return DragonflyVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(DragonflyVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (world.isClientSide()) {
            AnimationPlayback.updateLoop(this, this.flyingAnimState, this.isAlive());
        }
    }

    protected PathNavigation createNavigation(Level world) {

        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
            public boolean isStableDestination(BlockPos pos) {
                return this.level.getBlockState(pos.below()).isAir();
            }
        };

        birdNavigation.setCanOpenDoors(false);
        birdNavigation.setCanFloat(false);
        return birdNavigation;
    }

    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    private static class DragonflyLookControl extends LookControl {
        DragonflyLookControl(Mob entity) {
            super(entity);
        }

        public void tick() {
            super.tick();
        }

        protected boolean resetXRotOnTick() {
            return true;
        }

    }

    public static class SnappyFlightMoveControl extends MoveControl {
        private final Mob mob;
        private final float multiplier = 0.05f;

        public SnappyFlightMoveControl(Mob mob) {
            super(mob);
            this.mob = mob;
        }

        @Override
        public void tick() {
            if (operation != Operation.MOVE_TO) return;

            Vec3 to = new Vec3(wantedX - mob.getX(), wantedY - mob.getY(), wantedZ - mob.getZ());
            if (to.lengthSqr() < 0.01) {
                operation = Operation.WAIT;
                return;
            }

            Vec3 dir = to.normalize();
            double boost = this.speedModifier * multiplier;
            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.6).add(dir.scale(boost)));
            mob.setYRot((float) (Mth.atan2(dir.z, dir.x) * (180f / Math.PI)) - 90f);
            mob.yBodyRot = mob.getYRot();
        }
    }

}