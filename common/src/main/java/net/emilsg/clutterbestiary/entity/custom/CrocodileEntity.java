package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.animation_states.CrocodileEntityAnimationState;
import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.CrocodileArrowfishDistractionGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CrocodileBaskGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CrocodileFollowOwnerGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CrocodileLayEggGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CrocodileMateGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CrocodileMeleeAttackGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CrocodileStayGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.HighWanderAroundFarGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.LeaveWaterGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.IEggLayingAnimal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CrocodileEntity extends ParentTameableEntity implements IEggLayingAnimal, HandledEntityAnimations<CrocodileEntity, CrocodileEntityAnimationState> {
    private static final int EGG_LAYING_DELAY_TICKS = 400;
    private static final Item TAMING_ITEM = ModItems.ARROWFISH.get();
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FOLLOWING_OWNER = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DISTRACTED = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.LONG);

    private final EntityAnimationController<CrocodileEntity, CrocodileEntityAnimationState> animationController = new EntityAnimationController<>(this, CrocodileEntityAnimationState.IDLING, CrocodileEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private int eggTimer;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    private float swimAmount;
    private float swimBlend;
    private float prevSwimBlend;
    private float swimPitch;
    private float prevSwimPitch;

    public CrocodileEntity(EntityType<? extends ParentTameableEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.WATER, 0.0F);

        this.moveControl = new CrocodileMoveControl(this);
        this.setupAnimationController();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CrocodileStayGoal(this));
        this.goalSelector.addGoal(1, new CrocodileArrowfishDistractionGoal(this));
        this.goalSelector.addGoal(2, new CrocodileBaskGoal(this, 0.01f));
        this.goalSelector.addGoal(2, new CrocodileMeleeAttackGoal(this, 1.5));
        this.goalSelector.addGoal(2, new CrocodileMateGoal(this, 1.0f));
        this.goalSelector.addGoal(2, new CrocodileLayEggGoal(this, 1.0f, ModBlocks.CROCODILE_EGG.get().defaultBlockState()));
        this.goalSelector.addGoal(3, new CrocodileFollowOwnerGoal(this, 1.2, 3.0, 8.0f, 3.0f));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0f));
        this.goalSelector.addGoal(4, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.addGoal(5, new LeaveWaterGoal(this, 1.0f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Chicken.class, true));

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EGG, false);
        builder.define(FOLLOWING_OWNER, false);
        builder.define(DISTRACTED, false);
        builder.define(ANIMATION_STATE, CrocodileEntityAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, 0L);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setHasEgg(nbt.getBooleanOr("HasEgg", false));
        this.eggTimer = nbt.getIntOr("EggTimer", 0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("HasEgg", this.hasEgg());
        nbt.putInt("EggTimer", this.eggTimer);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentTameableEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 8.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f)
                .add(Attributes.STEP_HEIGHT, 1.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.CROCODILES_SPAWN_ON);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.CROCODILE.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.25F : 1.0F;
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.isInWater() ? null : SoundEvents.TURTLE_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.TURTLE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.55f;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.MEAT);
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public boolean isReadyToLayEgg() {
        return this.hasEgg() && this.eggTimer >= EGG_LAYING_DELAY_TICKS;
    }

    public void beginCarryingEgg() {
        this.eggTimer = 0;
        this.setHasEgg(true);
    }

    @Override
    public void finishLayingEgg() {
        this.setHasEgg(false);
        this.eggTimer = 0;
    }

    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }
    @Override
    public void die(DamageSource damageSource) {
        this.startState(CrocodileEntityAnimationState.IDLING);
        super.die(damageSource);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isTame() && stack.is(ItemTags.MEAT) && this.getHealth() < this.getMaxHealth()) {
            if (!this.level().isClientSide()) {
                stack.consume(1, player);
                FoodProperties foodComponent = stack.get(DataComponents.FOOD);
                float nutrition = foodComponent != null ? foodComponent.nutrition() : 1.0f;
                this.heal(2.0f * nutrition);
            }
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && this.isFood(stack)) {
            return super.mobInteract(player, hand);
        }

        if (this.isTame() && this.isOwnedBy(player) && !stack.is(ItemTags.MEAT)) {
            InteractionResult result = super.mobInteract(player, hand);
            if (result.consumesAction()) return result;

            if (!this.level().isClientSide()) {
                boolean sitting = !this.isOrderedToSit();
                this.setOrderedToSit(sitting);
                this.setFollowingOwner(false);
                this.getNavigation().stop();
                if (sitting) this.setTarget(null);
            }
            return InteractionResult.SUCCESS;
        }

        if (!this.isTame() && stack.is(this.getTamingItem())) {
            if (!this.isAlive() || !this.isDistracted()) return InteractionResult.PASS;

            if (!this.level().isClientSide()) {
                this.setDistracted(false);
                this.usePlayerItem(player, hand, stack);
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    ModAdvancements.grant(player, ModAdvancements.A_CROC_OF_TRUST);
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (!this.isTame() && this.isFood(stack)) return InteractionResult.PASS;
        return super.mobInteract(player, hand);
    }

    public boolean isBasking() {
        CrocodileEntityAnimationState state = this.animationController.getState();
        return state == CrocodileEntityAnimationState.OPENING_MOUTH || state == CrocodileEntityAnimationState.MOUTH_WAITING || state == CrocodileEntityAnimationState.SNAPPING_MOUTH_SHUT;
    }

    public boolean isMeleeAttacking() {
        CrocodileEntityAnimationState state = this.animationController.getState();
        return state == CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH || state == CrocodileEntityAnimationState.ATTACKING_SNAP_SHUT;
    }

    public boolean isDistracted() {
        return this.entityData.get(DISTRACTED);
    }

    public void setDistracted(boolean distracted) {
        this.entityData.set(DISTRACTED, distracted);
    }

    public boolean isFollowingOwner() {
        return this.entityData.get(FOLLOWING_OWNER);
    }

    public void setFollowingOwner(boolean followingOwner) {
        this.entityData.set(FOLLOWING_OWNER, followingOwner);
    }

    private void setupAnimationController() {
        animationController.addTransition(CrocodileEntityAnimationState.IDLING, CrocodileEntityAnimationState.STAYING, (e, s, age) -> e.isOrderedToSit());
        animationController.addTransition(CrocodileEntityAnimationState.STAYING, CrocodileEntityAnimationState.IDLING, (e, s, age) -> !e.isOrderedToSit());
        animationController.addCompletion(CrocodileEntityAnimationState.OPENING_MOUTH, CrocodileEntityAnimationState.MOUTH_WAITING, 40);
        animationController.addCompletion(CrocodileEntityAnimationState.MOUTH_WAITING, CrocodileEntityAnimationState.SNAPPING_MOUTH_SHUT, 200);
        animationController.addCompletion(CrocodileEntityAnimationState.SNAPPING_MOUTH_SHUT, CrocodileEntityAnimationState.IDLING, 5);
        animationController.addCompletion(CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH, CrocodileEntityAnimationState.ATTACKING_SNAP_SHUT, 10);
        animationController.addCompletion(CrocodileEntityAnimationState.ATTACKING_SNAP_SHUT, CrocodileEntityAnimationState.IDLING, 5);
    }

    @Override
    public EntityAnimationController<CrocodileEntity, CrocodileEntityAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.updateIdleAnimation();
            this.updateSwimmingAnimation();
        }
        this.animationController.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && this.hasEgg() && this.eggTimer < EGG_LAYING_DELAY_TICKS) {
            this.eggTimer++;
        }
    }

    private void updateIdleAnimation() {
        boolean canIdle = this.isAlive()
                && this.onGround()
                && !this.isInWater()
                && this.getDeltaMovement().horizontalDistanceSqr() <= 1.0E-4
                && this.animationController.getState() == CrocodileEntityAnimationState.IDLING;
        AnimationPlayback.updateLoop(this, this.idleAnimationState, canIdle);
    }

    private void updateSwimmingAnimation() {
        this.prevSwimBlend = this.swimBlend;
        this.prevSwimPitch = this.swimPitch;
        boolean swimming = this.isAlive() && this.isInWater();
        AnimationPlayback.updateLoop(this, this.swimAnimationState, swimming);
        Vec3 velocity = this.getDeltaMovement();
        float speed = swimming ? Mth.clamp((float) velocity.length() * 5.0f, 0.0f, 1.0f) : 0.0f;
        this.swimAmount = Mth.lerp(0.2f, this.swimAmount, speed);
        this.swimBlend = Mth.approach(this.swimBlend, swimming ? 1.0f : 0.0f, 0.1f);
        double verticalSpeed = Math.copySign(Math.max(Math.abs(velocity.y) - 0.01, 0.0), velocity.y);
        float targetPitch = swimming ? (float) Mth.atan2(-verticalSpeed, Math.max(velocity.horizontalDistance(), 0.04)) : 0.0f;
        this.swimPitch = Mth.lerp(0.15f, this.swimPitch, Mth.clamp(targetPitch, -0.35f, 0.35f) * this.swimAmount);
    }

    public float getSwimBlend(float tickDelta) {
        return Mth.lerp(tickDelta, this.prevSwimBlend, this.swimBlend);
    }

    public float getSwimPitch(float tickDelta) {
        return Mth.lerp(tickDelta, this.prevSwimPitch, this.swimPitch);
    }

    @Override
    protected AABB getAttackBoundingBox(double horizontalExpansion) {
        return super.getAttackBoundingBox(horizontalExpansion).inflate(1.5, 0.0, 1.5);
    }

    public boolean isFacingTarget(LivingEntity target) {
        if (this.getBoundingBox().intersects(target.getBoundingBox())) return true;
        float targetYaw = (float) (Mth.atan2(target.getZ() - this.getZ(), target.getX() - this.getX()) * 180.0 / Math.PI) - 90.0f;
        return Math.abs(Mth.wrapDegrees(targetYaw - this.getYRot())) <= 60.0f;
    }

    public boolean canChasePlayer(@Nullable Player player) {
        if (!this.isAlive() || player == null || !player.isAlive() || player.level() != this.level()) return false;
        if (player.isCreative() || player.isSpectator() || this.isOwnedBy(player) || !this.canAttack(player)) return false;
        double range = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        return this.distanceToSqr(player) <= range * range;
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isLocalInstanceAuthoritative() && this.isInWater()) {
            this.moveRelative(0.1F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new CrocodileSwimNavigation(this, world);
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 1.6F, 0.6F, 1.0F);
    }

    private static class CrocodileMoveControl extends MoveControl {
        private final CrocodileEntity crocodileEntity;

        public CrocodileMoveControl(CrocodileEntity crocodileEntity) {
            super(crocodileEntity);
            this.crocodileEntity = crocodileEntity;
        }

        @Override
        public void tick() {
            if (this.crocodileEntity.isInWater()) {
                this.crocodileEntity.setDeltaMovement(this.crocodileEntity.getDeltaMovement().add(0.0, 0.005, 0.0));

                if (this.operation != Operation.MOVE_TO || this.crocodileEntity.getNavigation().isDone()) {
                    this.crocodileEntity.setSpeed(0.0F);
                    return;
                }

                double d = this.wantedX - this.crocodileEntity.getX();
                double e = this.wantedY - this.crocodileEntity.getY();
                double f = this.wantedZ - this.crocodileEntity.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                if (g < 1.0E-7) {
                    this.crocodileEntity.setSpeed(0.0f);
                    return;
                }
                e /= g;
                float h = (float) (Mth.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.crocodileEntity.setYRot(this.rotlerp(this.crocodileEntity.getYRot(), h, 15.0f));
                this.crocodileEntity.yBodyRot = this.crocodileEntity.getYRot();
                float i = (float) (this.speedModifier * this.crocodileEntity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float j = Mth.lerp(0.125F, this.crocodileEntity.getSpeed(), i);
                this.crocodileEntity.setSpeed(j);
                this.crocodileEntity.setDeltaMovement(this.crocodileEntity.getDeltaMovement().add(0, this.crocodileEntity.getSpeed() * e * 0.1, 0));
            } else {
                super.tick();
            }
        }
    }

    private static class CrocodileSwimNavigation extends AmphibiousPathNavigation {
        CrocodileSwimNavigation(CrocodileEntity owner, Level world) {
            super(owner, world);
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            BlockState state = this.level.getBlockState(pos);
            BlockPos belowPos = pos.below();
            BlockState belowState = this.level.getBlockState(belowPos);
            return state.is(Blocks.WATER) || state.canBeReplaced() && belowState.isRedstoneConductor(this.level, belowPos);
        }
    }
}
