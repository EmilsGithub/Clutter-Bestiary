package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RiverTurtleAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RiverTurtleEntity extends ParentAnimalEntity implements Bucketable, HandledEntityAnimations<RiverTurtleEntity, RiverTurtleAnimationState> {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.APPLE);
    private static final int SIT_END_TICKS = 5;
    private static final int UNHIDE_TICKS = 50;
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Integer> BASKING_DURATION = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(RiverTurtleEntity.class, EntityDataSerializers.BOOLEAN);

    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation landNavigation;
    private final EntityAnimationController<RiverTurtleEntity, RiverTurtleAnimationState> animationController = new EntityAnimationController<>(this, RiverTurtleAnimationState.IDLING, RiverTurtleAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private int healthTicker = 0;
    private int healCooldown;
    private boolean canHealPassively = false;

    public RiverTurtleEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);

        this.moveControl = new RiverTurtleMoveControl(this);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, world);
        this.landNavigation = new GroundPathNavigation(this, world);
        this.setupAnimationController();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RiverTurtleHideGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.addGoal(2, new BaskGoal(this, 0.0001f));
        this.goalSelector.addGoal(3, new HarvestKelpGoal(this, 1, 12, 0.05f));
        this.goalSelector.addGoal(3, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.addGoal(4, new LeaveWaterGoal(this, 1.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6f));

        this.targetSelector.addGoal(1, new ConditionalActiveTargetGoal<>(this, Cod.class, true, 0.001f));
        this.targetSelector.addGoal(2, new ConditionalActiveTargetGoal<>(this, Salmon.class, true, 0.001f));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        this.setVariant(RiverTurtleVariant.getRandom());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_STATE, RiverTurtleAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
        builder.define(BASKING_DURATION, 0);
        builder.define(HIDING, false);
        builder.define(VARIANT, RiverTurtleVariant.SANDY.getID());
        builder.define(IS_SITTING, false);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public void loadFromBucketTag(CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag(this, nbt);
        if (nbt.contains("Variant")) {
            this.setVariant(RiverTurtleVariant.fromId(nbt.getStringOr("Variant", "")));
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setBaskingDuration(nbt.getIntOr("BaskingDuration", 0));
        this.setVariant(RiverTurtleVariant.fromId(nbt.getStringOr("Variant", "")));
        this.setSit(nbt.getBooleanOr("IsSitting", false));
        this.setFromBucket(nbt.getBooleanOr("FromBucket", false));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("BaskingDuration", this.getBaskingDuration());
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("IsSitting", this.isSat());
        nbt.putBoolean("FromBucket", this.fromBucket());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 12D)
                .add(Attributes.MOVEMENT_SPEED, 0.175F)
                .add(Attributes.ATTACK_DAMAGE, 2F);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.RIVER_TURTLES_SPAWN_ON);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbt -> nbt.putString("Variant", this.getTypeVariant()));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        RiverTurtleEntity child = ModEntityTypes.RIVER_TURTLE.get().create(world, EntitySpawnReason.BREEDING);
        if (child != null) child.setVariant(RiverTurtleVariant.getRandom());
        return child;
    }

    public int getBaskingDuration() {
        return this.entityData.get(BASKING_DURATION);
    }

    public void setBaskingDuration(int baskingDuration) {
        this.entityData.set(BASKING_DURATION, baskingDuration);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.25f;
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public RiverTurtleVariant getVariant() {
        return RiverTurtleVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(RiverTurtleVariant variant) {
        this.entityData.set(VARIANT, variant.getID());
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
        return new ItemStack(ModItems.RIVER_TURTLE_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElseGet(() -> super.mobInteract(player, hand));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isHiding() {
        return this.entityData.get(HIDING);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public boolean isSat() {
        return this.entityData.get(IS_SITTING);
    }

    public void die(DamageSource damageSource) {
        this.startState(RiverTurtleAnimationState.IDLING);
        super.die(damageSource);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (ANIMATION_STATE.equals(data)) this.refreshDimensions();
        super.onSyncedDataUpdated(data);
    }

    public void setIsHiding(boolean isHiding) {
        this.entityData.set(HIDING, isHiding);
    }

    public void setSit(boolean sitting) {
        this.entityData.set(IS_SITTING, sitting);
    }

    private void setupAnimationController() {
        animationController.addTransition(RiverTurtleAnimationState.IDLING, RiverTurtleAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addTransition(RiverTurtleAnimationState.SIT_START, RiverTurtleAnimationState.SIT_END, (e, s, age) -> !e.isSat());
        animationController.addTransition(RiverTurtleAnimationState.SIT_END, RiverTurtleAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addCompletion(RiverTurtleAnimationState.SIT_END, RiverTurtleAnimationState.IDLING, SIT_END_TICKS);
        animationController.addCompletion(RiverTurtleAnimationState.UNHIDING, RiverTurtleAnimationState.IDLING, UNHIDE_TICKS);
    }

    @Override
    public EntityAnimationController<RiverTurtleEntity, RiverTurtleAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            if (this.getBaskingDuration() > 0) this.setBaskingDuration(this.getBaskingDuration() - 1);

            if (this.getHealth() < this.getMaxHealth()) {
                if (this.getHealth() <= (this.getMaxHealth() / 2)) {
                    this.setIsHiding(true);
                    canHealPassively = true;
                }

                if (canHealPassively) {
                    if (this.healCooldown == 0) this.healCooldown = 150 + random.nextInt(100);
                    healthTicker++;

                    if (healthTicker >= this.healCooldown) {
                        this.heal(1);
                        healthTicker = 0;
                        this.healCooldown = 0;
                    }
                }
            } else {
                canHealPassively = false;
                healthTicker = 0;
                this.healCooldown = 0;
                this.setIsHiding(false);
            }
        }

        super.tick();
        this.animationController.tick();
    }

    public void travel(Vec3 movementInput) {
        if (this.isLocalInstanceAuthoritative() && this.isInWater()) {
            this.moveRelative(0.1F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8, 0.95, 0.8));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    protected PathNavigation createNavigation(Level world) {
        return new RiverTurtleSwimNavigation(this, world);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_RIVER_TURTLE_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_RIVER_TURTLE_HURT.get();
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 1.15f, 0.7F, 1.0F);
    }

    private static class RiverTurtleMoveControl extends MoveControl {
        private final RiverTurtleEntity riverTurtle;

        public RiverTurtleMoveControl(RiverTurtleEntity riverTurtle) {
            super(riverTurtle);
            this.riverTurtle = riverTurtle;
        }

        public void tick() {
            if (this.riverTurtle.isInWater()) {
                this.riverTurtle.setDeltaMovement(this.riverTurtle.getDeltaMovement().add(0.0, 0.005, 0.0));

                if (this.operation != Operation.MOVE_TO || this.riverTurtle.getNavigation().isDone()) {
                    this.riverTurtle.setSpeed(0.0F);
                    return;
                }

                double d = this.wantedX - this.riverTurtle.getX();
                double e = this.wantedY - this.riverTurtle.getY();
                double f = this.wantedZ - this.riverTurtle.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float) (Mth.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.riverTurtle.setYRot(this.rotlerp(this.riverTurtle.getYRot(), h, 90.0F));
                this.riverTurtle.yBodyRot = this.riverTurtle.getYRot();
                float i = (float) (this.speedModifier * this.riverTurtle.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float j = Mth.lerp(0.125F, this.riverTurtle.getSpeed(), i);
                this.riverTurtle.setSpeed(j);
                this.riverTurtle.setDeltaMovement(this.riverTurtle.getDeltaMovement().add(0, this.riverTurtle.getSpeed() * e * 0.1, 0));
            } else {
                this.riverTurtle.setDeltaMovement(this.riverTurtle.getDeltaMovement().add(0.0, 0, 0.0));
                super.tick();
            }
        }
    }

    private static class RiverTurtleSwimNavigation extends AmphibiousPathNavigation {
        RiverTurtleSwimNavigation(RiverTurtleEntity riverTurtleEntity, Level world) {
            super(riverTurtleEntity, world);
        }

        public boolean isStableDestination(BlockPos pos) {
            if (this.mob instanceof RiverTurtleEntity) {
                return this.level.getBlockState(pos).is(Blocks.WATER) || (this.level.getBlockState(pos).canBeReplaced() && this.level.getBlockState(pos.below()).isRedstoneConductor(this.level, pos.below()));
            }

            return !this.level.getBlockState(pos.below()).isAir();
        }
    }
}
