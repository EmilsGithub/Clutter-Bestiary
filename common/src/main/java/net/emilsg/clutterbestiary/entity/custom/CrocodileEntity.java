package net.emilsg.clutterbestiary.entity.custom;

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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class CrocodileEntity extends ParentTameableEntity implements IEggLayingAnimal, HandledEntityAnimations<CrocodileEntity, CrocodileEntityAnimationState> {
    private static final int EGG_LAYING_DELAY_TICKS = 400;
    private static final Item TAMING_ITEM = ModItems.ARROWFISH.get();
    private static final TrackedData<Boolean> HAS_EGG = DataTracker.registerData(CrocodileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FOLLOWING_OWNER = DataTracker.registerData(CrocodileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> DISTRACTED = DataTracker.registerData(CrocodileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(CrocodileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(CrocodileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(CrocodileEntity.class, TrackedDataHandlerRegistry.LONG);

    private final EntityAnimationController<CrocodileEntity, CrocodileEntityAnimationState> animationController = new EntityAnimationController<>(this, CrocodileEntityAnimationState.IDLING, CrocodileEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private int eggTimer;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    private float swimAmount;
    private float swimBlend;
    private float prevSwimBlend;
    private float swimPitch;
    private float prevSwimPitch;

    public CrocodileEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);

        this.moveControl = new CrocodileMoveControl(this);
        this.setupAnimationController();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new CrocodileStayGoal(this));
        this.goalSelector.add(1, new CrocodileArrowfishDistractionGoal(this));
        this.goalSelector.add(2, new CrocodileBaskGoal(this, 0.01f));
        this.goalSelector.add(2, new CrocodileMeleeAttackGoal(this, 1.5));
        this.goalSelector.add(2, new CrocodileMateGoal(this, 1.0f));
        this.goalSelector.add(2, new CrocodileLayEggGoal(this, 1.0f, ModBlocks.CROCODILE_EGG.get().getDefaultState()));
        this.goalSelector.add(3, new CrocodileFollowOwnerGoal(this, 1.2, 3.0, 8.0f, 3.0f));
        this.goalSelector.add(3, new FollowParentGoal(this, 1.0f));
        this.goalSelector.add(4, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.add(5, new LeaveWaterGoal(this, 1.0f));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ChickenEntity.class, true));

    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HAS_EGG, false);
        builder.add(FOLLOWING_OWNER, false);
        builder.add(DISTRACTED, false);
        builder.add(ANIMATION_STATE, CrocodileEntityAnimationState.IDLING.getIndex());
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, 0L);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setHasEgg(nbt.getBoolean("HasEgg"));
        this.eggTimer = nbt.getInt("EggTimer");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HasEgg", this.hasEgg());
        nbt.putInt("EggTimer", this.eggTimer);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentTameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.18f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0f);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.CROCODILES_SPAWN_ON);
    }

    @Override
    public boolean canEat() {
        return super.canEat() && !this.hasEgg();
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.CROCODILE.get().create(world);
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.25F : 1.0F;
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.isTouchingWater() ? null : SoundEvents.ENTITY_TURTLE_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_TURTLE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_TURTLE_DEATH;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 300;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 0.55f;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.MEAT);
    }

    @Override
    public boolean hasEgg() {
        return this.dataTracker.get(HAS_EGG);
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
        this.dataTracker.set(HAS_EGG, hasEgg);
    }
    @Override
    public void onDeath(DamageSource damageSource) {
        this.startState(CrocodileEntityAnimationState.IDLING);
        super.onDeath(damageSource);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (this.isTamed() && stack.isIn(ItemTags.MEAT) && this.getHealth() < this.getMaxHealth()) {
            if (!this.getWorld().isClient) {
                stack.decrementUnlessCreative(1, player);
                FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
                float nutrition = foodComponent != null ? foodComponent.nutrition() : 1.0f;
                this.heal(2.0f * nutrition);
            }
            return ActionResult.success(this.getWorld().isClient);
        }

        if (this.isTamed() && this.isBreedingItem(stack)) {
            return super.interactMob(player, hand);
        }

        if (this.isTamed() && this.isOwner(player) && !stack.isIn(ItemTags.MEAT)) {
            ActionResult result = super.interactMob(player, hand);
            if (result.isAccepted()) return result;

            if (!this.getWorld().isClient) {
                boolean sitting = !this.isSitting();
                this.setSitting(sitting);
                this.setFollowingOwner(false);
                this.getNavigation().stop();
                if (sitting) this.setTarget(null);
            }
            return ActionResult.success(this.getWorld().isClient);
        }

        if (!this.isTamed() && stack.isOf(this.getTamingItem())) {
            if (!this.isAlive() || !this.isDistracted()) return ActionResult.PASS;

            if (!this.getWorld().isClient) {
                this.setDistracted(false);
                this.eat(player, hand, stack);
                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    ModAdvancements.grant(player, ModAdvancements.A_CROC_OF_TRUST);
                    this.setTarget(null);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }
            }
            return ActionResult.success(this.getWorld().isClient);
        }
        if (!this.isTamed() && this.isBreedingItem(stack)) return ActionResult.PASS;
        return super.interactMob(player, hand);
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
        return this.dataTracker.get(DISTRACTED);
    }

    public void setDistracted(boolean distracted) {
        this.dataTracker.set(DISTRACTED, distracted);
    }

    public boolean isFollowingOwner() {
        return this.dataTracker.get(FOLLOWING_OWNER);
    }

    public void setFollowingOwner(boolean followingOwner) {
        this.dataTracker.set(FOLLOWING_OWNER, followingOwner);
    }

    private void setupAnimationController() {
        animationController.addTransition(CrocodileEntityAnimationState.IDLING, CrocodileEntityAnimationState.STAYING, (e, s, age) -> e.isSitting());
        animationController.addTransition(CrocodileEntityAnimationState.STAYING, CrocodileEntityAnimationState.IDLING, (e, s, age) -> !e.isSitting());
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
        if (this.getWorld().isClient) {
            this.updateIdleAnimation();
            this.updateSwimmingAnimation();
        }
        this.animationController.tick();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient && this.hasEgg() && this.eggTimer < EGG_LAYING_DELAY_TICKS) {
            this.eggTimer++;
        }
    }

    private void updateIdleAnimation() {
        boolean canIdle = this.isAlive()
                && this.isOnGround()
                && !this.isTouchingWater()
                && this.getVelocity().horizontalLengthSquared() <= 1.0E-4
                && this.animationController.getState() == CrocodileEntityAnimationState.IDLING;
        AnimationPlayback.updateLoop(this, this.idleAnimationState, canIdle);
    }

    private void updateSwimmingAnimation() {
        this.prevSwimBlend = this.swimBlend;
        this.prevSwimPitch = this.swimPitch;
        boolean swimming = this.isAlive() && this.isTouchingWater();
        AnimationPlayback.updateLoop(this, this.swimAnimationState, swimming);
        Vec3d velocity = this.getVelocity();
        float speed = swimming ? MathHelper.clamp((float) velocity.length() * 5.0f, 0.0f, 1.0f) : 0.0f;
        this.swimAmount = MathHelper.lerp(0.2f, this.swimAmount, speed);
        this.swimBlend = MathHelper.stepTowards(this.swimBlend, swimming ? 1.0f : 0.0f, 0.1f);
        double verticalSpeed = Math.copySign(Math.max(Math.abs(velocity.y) - 0.01, 0.0), velocity.y);
        float targetPitch = swimming ? (float) MathHelper.atan2(-verticalSpeed, Math.max(velocity.horizontalLength(), 0.04)) : 0.0f;
        this.swimPitch = MathHelper.lerp(0.15f, this.swimPitch, MathHelper.clamp(targetPitch, -0.35f, 0.35f) * this.swimAmount);
    }

    public float getSwimBlend(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevSwimBlend, this.swimBlend);
    }

    public float getSwimPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevSwimPitch, this.swimPitch);
    }

    @Override
    protected Box getAttackBox() {
        return super.getAttackBox().expand(1.5, 0.0, 1.5);
    }

    public boolean isFacingTarget(LivingEntity target) {
        if (this.getBoundingBox().intersects(target.getBoundingBox())) return true;
        float targetYaw = (float) (MathHelper.atan2(target.getZ() - this.getZ(), target.getX() - this.getX()) * 180.0 / Math.PI) - 90.0f;
        return Math.abs(MathHelper.wrapDegrees(targetYaw - this.getYaw())) <= 60.0f;
    }

    public boolean canChasePlayer(@Nullable PlayerEntity player) {
        if (!this.isAlive() || player == null || !player.isAlive() || player.getWorld() != this.getWorld()) return false;
        if (player.isCreative() || player.isSpectator() || this.isOwner(player) || !this.canTarget(player)) return false;
        double range = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        return this.squaredDistanceTo(player) <= range * range;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new CrocodileSwimNavigation(this, world);
    }

    @Override
    protected int getNextAirUnderwater(int air) {
        return air;
    }

    @Override
    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.6F, 0.6F);
    }

    private static class CrocodileMoveControl extends MoveControl {
        private final CrocodileEntity crocodileEntity;

        public CrocodileMoveControl(CrocodileEntity crocodileEntity) {
            super(crocodileEntity);
            this.crocodileEntity = crocodileEntity;
        }

        @Override
        public void tick() {
            if (this.crocodileEntity.isTouchingWater()) {
                this.crocodileEntity.setVelocity(this.crocodileEntity.getVelocity().add(0.0, 0.005, 0.0));

                if (this.state != State.MOVE_TO || this.crocodileEntity.getNavigation().isIdle()) {
                    this.crocodileEntity.setMovementSpeed(0.0F);
                    return;
                }

                double d = this.targetX - this.crocodileEntity.getX();
                double e = this.targetY - this.crocodileEntity.getY();
                double f = this.targetZ - this.crocodileEntity.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                if (g < 1.0E-7) {
                    this.crocodileEntity.setMovementSpeed(0.0f);
                    return;
                }
                e /= g;
                float h = (float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.crocodileEntity.setYaw(this.wrapDegrees(this.crocodileEntity.getYaw(), h, 15.0f));
                this.crocodileEntity.bodyYaw = this.crocodileEntity.getYaw();
                float i = (float) (this.speed * this.crocodileEntity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125F, this.crocodileEntity.getMovementSpeed(), i);
                this.crocodileEntity.setMovementSpeed(j);
                this.crocodileEntity.setVelocity(this.crocodileEntity.getVelocity().add(0, this.crocodileEntity.getMovementSpeed() * e * 0.1, 0));
            } else {
                super.tick();
            }
        }
    }

    private static class CrocodileSwimNavigation extends AmphibiousSwimNavigation {
        CrocodileSwimNavigation(CrocodileEntity owner, World world) {
            super(owner, world);
        }

        @Override
        public boolean isValidPosition(BlockPos pos) {
            BlockState state = this.world.getBlockState(pos);
            BlockPos belowPos = pos.down();
            BlockState belowState = this.world.getBlockState(belowPos);
            return state.isOf(Blocks.WATER) || state.isReplaceable() && belowState.isSolidBlock(this.world, belowPos);
        }
    }
}
