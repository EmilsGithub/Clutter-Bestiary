package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.IdleAnimationGroup;
import net.emilsg.clutterbestiary.animation_handling.animation_states.ChorusBeetleAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.ChorusBeetleBreakFlowerGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ChorusBeetleFlyAroundGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ChorusBeetleReturnFlowerGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChorusBeetleEntity extends ParentAnimalEntity implements HandledEntityAnimations<ChorusBeetleEntity, ChorusBeetleAnimationState> {
    private static final int FLOWER_FETCH_COOLDOWN_TICKS = 200;
    private static final double MAX_REQUESTER_DISTANCE = 128.0;
    private static final double MAX_REQUESTER_DISTANCE_SQUARED = MAX_REQUESTER_DISTANCE * MAX_REQUESTER_DISTANCE;
    private static final double LANDING_DESCENT_SPEED = 0.06;
    private static final double LANDING_DRIFT_SPEED = 0.035;
    private static final double MIN_LANDING_ANIMATION_SPEED = 0.35;
    private static final double MAX_LANDING_ANIMATION_SPEED = 1.5;
    private static final double MAX_LANDING_ANIMATION_SPEED_HEIGHT = 8.0;
    private static final double GROUND_CHECK_DISTANCE = 32.0;
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.CHORUS_FLOWER);
    private static final TrackedData<Boolean> FLYING = DataTracker.registerData(ChorusBeetleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CARRYING_CHORUS_FLOWER = DataTracker.registerData(ChorusBeetleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(ChorusBeetleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(ChorusBeetleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(ChorusBeetleEntity.class, TrackedDataHandlerRegistry.LONG);

    public final AnimationState wingFlickAnimationState = new AnimationState();
    public final AnimationState mandibleNibbleAnimationState = new AnimationState();

    private final EntityNavigation landNavigation;
    private final EntityNavigation flightNavigation;
    private final MoveControl landMoveControl;
    private final MoveControl flightMoveControl;
    private final EntityAnimationController<ChorusBeetleEntity, ChorusBeetleAnimationState> animationController = new EntityAnimationController<>(this, ChorusBeetleAnimationState.IDLING, ChorusBeetleAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(3, 3, 100)
            .add(1, wingFlickAnimationState)
            .add(2, mandibleNibbleAnimationState);
    private boolean landing;
    private int postBreakHoverTicks;
    private int pendingChorusFlowerDrops;
    private boolean flowerFetchRequested;
    private int flowerFetchCooldown;
    @Nullable
    private UUID flowerRequesterUuid;
    private double landingDriftX;
    private double landingDriftZ;
    private int landingAnimationSpeedAge = Integer.MIN_VALUE;
    private double landingAnimationSpeed = MIN_LANDING_ANIMATION_SPEED;

    public ChorusBeetleEntity(EntityType<? extends ParentAnimalEntity> entityType, World world) {
        super(entityType, world);
        this.landNavigation = this.navigation;
        this.flightNavigation = this.createFlightNavigation(world);
        this.landMoveControl = this.moveControl;
        this.flightMoveControl = new FlightMoveControl(this, 20, true);
        this.jumpControl = new ChorusBeetleJumpControl(this);
        this.setupAnimationController();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FLYING, false);
        builder.add(CARRYING_CHORUS_FLOWER, false);
        builder.add(ANIMATION_STATE, ChorusBeetleAnimationState.IDLING.getIndex());
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, -1L);
    }

    @Override
    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f, 1.25F);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.pendingChorusFlowerDrops = Math.max(0, nbt.getInt("PendingChorusFlowerDrops"));
        this.flowerFetchRequested = nbt.getBoolean("FlowerFetchRequested");
        this.flowerFetchCooldown = Math.max(0, nbt.getInt("FlowerFetchCooldown"));
        this.flowerRequesterUuid = nbt.containsUuid("FlowerRequester") ? nbt.getUuid("FlowerRequester") : null;
        this.dataTracker.set(CARRYING_CHORUS_FLOWER, this.pendingChorusFlowerDrops > 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("PendingChorusFlowerDrops", this.pendingChorusFlowerDrops);
        nbt.putBoolean("FlowerFetchRequested", this.flowerFetchRequested);
        nbt.putInt("FlowerFetchCooldown", this.flowerFetchCooldown);
        if (this.flowerRequesterUuid != null) nbt.putUuid("FlowerRequester", this.flowerRequesterUuid);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new ChorusBeetleBreakFlowerGoal(this, 1.15, 12));
        this.goalSelector.add(2, new ChorusBeetleReturnFlowerGoal(this, 1.15));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(4, new ChorusBeetleFlyAroundGoal(this, 1.0));
        this.goalSelector.add(5, new TemptGoal(this, 1.1, BREEDING_INGREDIENT, false));
        this.goalSelector.add(6, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(9, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.35f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.CHORUS_BEETLES_SPAWN_ON);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.CHORUS_BEETLE.get().create(world);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 240;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 1.2f;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.CHORUS_FRUIT)) return super.interactMob(player, hand);
        if (this.getWorld().isClient) return ActionResult.CONSUME;
        if (!this.canAcceptFlowerFetchRequest()) return ActionResult.PASS;

        stack.decrementUnlessCreative(1, player);
        this.flowerFetchRequested = true;
        this.flowerFetchCooldown = FLOWER_FETCH_COOLDOWN_TICKS;
        this.flowerRequesterUuid = player.getUuid();
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY() + this.getHeight() * 0.5,
                    this.getZ(), 7, 0.25, 0.2, 0.25, 0.0);
        }
        return ActionResult.SUCCESS;
    }

    public boolean hasFlowerFetchRequest() {
        return this.flowerFetchRequested;
    }

    public boolean hasFlowerRequester() {
        return this.flowerRequesterUuid != null;
    }

    @Nullable
    public PlayerEntity getFlowerRequester() {
        return this.flowerRequesterUuid == null ? null : this.getWorld().getPlayerByUuid(this.flowerRequesterUuid);
    }

    public boolean isCarryingChorusFlower() {
        return this.dataTracker.get(CARRYING_CHORUS_FLOWER);
    }

    public void consumeFlowerFetchRequest() {
        this.flowerFetchRequested = false;
    }

    private boolean canAcceptFlowerFetchRequest() {
        return !this.isBaby() && !this.isFlying() && !this.flowerFetchRequested && this.pendingChorusFlowerDrops == 0 && this.flowerFetchCooldown == 0;
    }

    public boolean isFlying() {
        return this.dataTracker.get(FLYING);
    }

    public boolean isLanding() {
        return this.landing;
    }

    public boolean isPostBreakHovering() {
        return this.postBreakHoverTicks > 0;
    }

    public void setFlying(boolean flying) {
        if (this.isFlying() == flying) return;

        this.navigation.stop();
        this.landing = false;
        this.postBreakHoverTicks = 0;
        this.landingDriftX = 0.0;
        this.landingDriftZ = 0.0;
        this.dataTracker.set(FLYING, flying);
        this.setNoGravity(false);
        this.navigation = flying ? this.flightNavigation : this.landNavigation;
        this.moveControl = flying ? this.flightMoveControl : this.landMoveControl;
    }

    public void beginLanding() {
        if (!this.isFlying() || this.landing) return;

        this.navigation.stop();
        this.landing = true;
        this.postBreakHoverTicks = 0;
        this.setNoGravity(true);
        this.moveControl = this.landMoveControl;
        double driftAngle = this.getRandom().nextDouble() * Math.PI * 2.0;
        this.landingDriftX = Math.cos(driftAngle) * LANDING_DRIFT_SPEED;
        this.landingDriftZ = Math.sin(driftAngle) * LANDING_DRIFT_SPEED;
        this.setVelocity(this.landingDriftX, -LANDING_DESCENT_SPEED, this.landingDriftZ);
    }

    public void beginPostBreakHover(int hoverTicks) {
        if (!this.isFlying()) return;
        if (hoverTicks <= 0) {
            if (this.isCarryingChorusFlower()) {
                this.resumeFlying();
            } else {
                this.beginLanding();
            }
            return;
        }

        this.navigation.stop();
        this.landing = false;
        this.postBreakHoverTicks = hoverTicks;
        this.setNoGravity(true);
        this.moveControl = this.flightMoveControl;
        this.setVelocity(0.0, 0.0, 0.0);
    }

    public void queueChorusFlowerDrop() {
        this.pendingChorusFlowerDrops++;
        this.dataTracker.set(CARRYING_CHORUS_FLOWER, true);
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient) this.validateFlowerRequester();
        super.tick();
        if (!this.getWorld().isClient) {
            if (this.flowerFetchCooldown > 0) this.flowerFetchCooldown--;
            if (this.postBreakHoverTicks > 0) {
                this.navigation.stop();
                this.setNoGravity(true);
                this.setVelocity(this.getVelocity().multiply(0.5, 0.0, 0.5));
                if (--this.postBreakHoverTicks == 0) {
                    if (this.isCarryingChorusFlower()) {
                        this.resumeFlying();
                    } else {
                        this.beginLanding();
                    }
                }
            } else if (this.landing) {
                if (this.isTouchingWater()) {
                    this.setFlying(false);
                    this.setVelocity(Vec3d.ZERO);
                } else {
                    this.navigation.stop();
                    this.setNoGravity(true);
                    this.setVelocity(this.landingDriftX, -LANDING_DESCENT_SPEED, this.landingDriftZ);
                    this.fallDistance = 0.0f;
                    if (this.isOnGround()) {
                        this.setFlying(false);
                        this.setVelocity(Vec3d.ZERO);
                        if (this.pendingChorusFlowerDrops == 0 && !this.flowerFetchRequested) this.flowerRequesterUuid = null;
                    }
                }
            }

            this.givePendingChorusFlowersToRequester();
        }

        this.animationController.tick();
        if (this.getWorld().isClient) this.idleAnimations.tick(this, this.isAlive());
    }

    private void resumeFlying() {
        this.landing = false;
        this.setNoGravity(false);
        this.navigation = this.flightNavigation;
        this.moveControl = this.flightMoveControl;
        this.setVelocity(0.0, 0.0, 0.0);
    }

    private void givePendingChorusFlowersToRequester() {
        if (this.pendingChorusFlowerDrops <= 0 || this.flowerRequesterUuid == null) return;

        PlayerEntity player = this.getWorld().getPlayerByUuid(this.flowerRequesterUuid);
        if (player != null && this.getBoundingBox().intersects(player.getBoundingBox())
                && player.giveItemStack(new ItemStack(Items.CHORUS_FLOWER, this.pendingChorusFlowerDrops))) {
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2f, 1.0f);
            ModAdvancements.grant(player, ModAdvancements.SPECIAL_DELIVERY);
            this.pendingChorusFlowerDrops = 0;
            this.flowerRequesterUuid = null;
            this.dataTracker.set(CARRYING_CHORUS_FLOWER, false);
        }
    }

    private void validateFlowerRequester() {
        if (this.flowerRequesterUuid == null) {
            if (this.flowerFetchRequested || this.pendingChorusFlowerDrops > 0) this.clearFlowerFetchTask();
            return;
        }

        PlayerEntity player = this.getWorld().getPlayerByUuid(this.flowerRequesterUuid);
        if (player == null || !player.isAlive() || this.squaredDistanceTo(player) > MAX_REQUESTER_DISTANCE_SQUARED) {
            this.clearFlowerFetchTask();
        }
    }

    private void clearFlowerFetchTask() {
        if (this.pendingChorusFlowerDrops > 0 && !this.getWorld().isClient) {
            this.dropStack(new ItemStack(Items.CHORUS_FLOWER, this.pendingChorusFlowerDrops));
        }
        this.flowerFetchRequested = false;
        this.pendingChorusFlowerDrops = 0;
        this.flowerRequesterUuid = null;
        this.dataTracker.set(CARRYING_CHORUS_FLOWER, false);
        if (this.isFlying() && !this.isLanding()) this.beginLanding();
    }

    public void cancelFlowerFetchTaskWithAnger() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            double particleY = this.getY() + this.getHeight() * 0.5;
            serverWorld.spawnParticles(ParticleTypes.ANGRY_VILLAGER, this.getX(), particleY, this.getZ(), 5, 0.3, 0.2, 0.3, 0.0);
            serverWorld.spawnParticles(ParticleTypes.CLOUD, this.getX(), particleY, this.getZ(), 10, 0.3, 0.2, 0.3, 0.02);
        }
        this.clearFlowerFetchTask();
    }

    public double getLandingAnimationSpeed() {
        if (this.landingAnimationSpeedAge == this.age) return this.landingAnimationSpeed;

        this.landingAnimationSpeedAge = this.age;
        Vec3d start = this.getPos();
        Vec3d end = start.add(0.0, -GROUND_CHECK_DISTANCE, 0.0);
        BlockHitResult hit = this.getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        double distance = hit.getType() == HitResult.Type.MISS ? GROUND_CHECK_DISTANCE : start.y - hit.getPos().y;
        double heightFactor = MathHelper.clamp(distance / MAX_LANDING_ANIMATION_SPEED_HEIGHT, 0.0, 1.0);
        this.landingAnimationSpeed = MathHelper.lerp(heightFactor, MIN_LANDING_ANIMATION_SPEED, MAX_LANDING_ANIMATION_SPEED);
        return this.landingAnimationSpeed;
    }

    private void setupAnimationController() {
        this.animationController.addTransition(ChorusBeetleAnimationState.IDLING, ChorusBeetleAnimationState.WALKING, (entity, state, age) -> entity.isWalking());
        this.animationController.addTransition(ChorusBeetleAnimationState.IDLING, ChorusBeetleAnimationState.FLYING, (entity, state, age) -> entity.isFlying() && !entity.isPostBreakHovering() && !entity.isLanding());
        this.animationController.addTransition(ChorusBeetleAnimationState.IDLING, ChorusBeetleAnimationState.HOVERING, (entity, state, age) -> entity.isPostBreakHovering());
        this.animationController.addTransition(ChorusBeetleAnimationState.IDLING, ChorusBeetleAnimationState.LANDING, (entity, state, age) -> entity.isLanding());
        this.animationController.addTransition(ChorusBeetleAnimationState.WALKING, ChorusBeetleAnimationState.IDLING, (entity, state, age) -> !entity.isWalking());
        this.animationController.addTransition(ChorusBeetleAnimationState.WALKING, ChorusBeetleAnimationState.FLYING, (entity, state, age) -> entity.isFlying());
        this.animationController.addTransition(ChorusBeetleAnimationState.FLYING, ChorusBeetleAnimationState.HOVERING, (entity, state, age) -> entity.isPostBreakHovering());
        this.animationController.addTransition(ChorusBeetleAnimationState.FLYING, ChorusBeetleAnimationState.LANDING, (entity, state, age) -> entity.isLanding());
        this.animationController.addTransition(ChorusBeetleAnimationState.FLYING, ChorusBeetleAnimationState.IDLING, (entity, state, age) -> !entity.isFlying());
        this.animationController.addTransition(ChorusBeetleAnimationState.HOVERING, ChorusBeetleAnimationState.LANDING, (entity, state, age) -> entity.isLanding());
        this.animationController.addTransition(ChorusBeetleAnimationState.HOVERING, ChorusBeetleAnimationState.FLYING, (entity, state, age) -> entity.isFlying() && !entity.isPostBreakHovering());
        this.animationController.addTransition(ChorusBeetleAnimationState.LANDING, ChorusBeetleAnimationState.IDLING, (entity, state, age) -> !entity.isFlying());
    }

    private boolean isWalking() {
        return !this.isFlying() && this.getVelocity().horizontalLengthSquared() > 1.0E-4;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public EntityAnimationController<ChorusBeetleEntity, ChorusBeetleAnimationState> getAnimationController() {
        return this.animationController;
    }

    private EntityNavigation createFlightNavigation(World world) {
        BirdNavigation navigation = new BirdNavigation(this, world);
        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(false);
        navigation.setCanEnterOpenDoors(true);
        return navigation;
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.5f : 1.0f;
    }

    private static class ChorusBeetleJumpControl extends JumpControl {
        private static final double MIN_UPWARD_STEP = 0.25;
        private final ChorusBeetleEntity chorusBeetle;

        public ChorusBeetleJumpControl(ChorusBeetleEntity chorusBeetle) {
            super(chorusBeetle);
            this.chorusBeetle = chorusBeetle;
        }

        @Override
        public void setActive() {
            if (this.chorusBeetle.isTouchingWater() || this.chorusBeetle.isInLava()
                    || this.chorusBeetle.isOnGround() && this.chorusBeetle.getMoveControl().getTargetY() - this.chorusBeetle.getY() > MIN_UPWARD_STEP) {
                super.setActive();
            }
        }
    }
}
