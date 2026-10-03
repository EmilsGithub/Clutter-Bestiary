package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.IdleAnimationGroup;
import net.emilsg.clutterbestiary.animation_handling.animation_states.ChorusBeetleAnimationState;
import net.emilsg.clutterbestiary.entity.custom.goal.ChorusBeetleBreakFlowerGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ChorusBeetleFlyAroundGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ChorusBeetleReturnFlowerGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
    private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(Items.CHORUS_FLOWER);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(ChorusBeetleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRYING_CHORUS_FLOWER = SynchedEntityData.defineId(ChorusBeetleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(ChorusBeetleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(ChorusBeetleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(ChorusBeetleEntity.class, EntityDataSerializers.LONG);

    public final AnimationState wingFlickAnimationState = new AnimationState();
    public final AnimationState mandibleNibbleAnimationState = new AnimationState();

    private final PathNavigation landNavigation;
    private final PathNavigation flightNavigation;
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

    public ChorusBeetleEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.landNavigation = this.navigation;
        this.flightNavigation = this.createFlightNavigation(world);
        this.landMoveControl = this.moveControl;
        this.flightMoveControl = new FlyingMoveControl(this, 20, true);
        this.jumpControl = new ChorusBeetleJumpControl(this);
        this.setupAnimationController();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLYING, false);
        builder.define(CARRYING_CHORUS_FLOWER, false);
        builder.define(ANIMATION_STATE, ChorusBeetleAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
    }

    @Override
    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f, 1.25F, 1.0F);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.pendingChorusFlowerDrops = Math.max(0, nbt.getIntOr("PendingChorusFlowerDrops", 0));
        this.flowerFetchRequested = nbt.getBooleanOr("FlowerFetchRequested", false);
        this.flowerFetchCooldown = Math.max(0, nbt.getIntOr("FlowerFetchCooldown", 0));
        this.flowerRequesterUuid = nbt.read("FlowerRequester", UUIDUtil.CODEC).orElse(null);
        this.entityData.set(CARRYING_CHORUS_FLOWER, this.pendingChorusFlowerDrops > 0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("PendingChorusFlowerDrops", this.pendingChorusFlowerDrops);
        nbt.putBoolean("FlowerFetchRequested", this.flowerFetchRequested);
        nbt.putInt("FlowerFetchCooldown", this.flowerFetchCooldown);
        if (this.flowerRequesterUuid != null) nbt.store("FlowerRequester", UUIDUtil.CODEC, this.flowerRequesterUuid);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new ChorusBeetleBreakFlowerGoal(this, 1.15, 12));
        this.goalSelector.addGoal(2, new ChorusBeetleReturnFlowerGoal(this, 1.15));
        this.goalSelector.addGoal(4, new ChorusBeetleFlyAroundGoal(this, 1.0));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.1, TEMPT_INGREDIENT, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.FLYING_SPEED, 0.35f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.STEP_HEIGHT, 1.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.CHORUS_BEETLES_SPAWN_ON);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        // Chorus beetles can't be bred, so never spawn them as babies either.
        return super.finalizeSpawn(world, difficulty, spawnReason, new AgeableMob.AgeableMobGroupData(false));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMITE_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.2f;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.CHORUS_FRUIT)) return super.mobInteract(player, hand);
        if (this.level().isClientSide()) return InteractionResult.CONSUME;
        if (!this.canAcceptFlowerFetchRequest()) return InteractionResult.PASS;

        stack.consume(1, player);
        this.flowerFetchRequested = true;
        this.flowerFetchCooldown = FLOWER_FETCH_COOLDOWN_TICKS;
        this.flowerRequesterUuid = player.getUUID();
        if (this.level() instanceof ServerLevel serverWorld) {
            serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY() + this.getBbHeight() * 0.5,
                    this.getZ(), 7, 0.25, 0.2, 0.25, 0.0);
        }
        return InteractionResult.SUCCESS;
    }

    public boolean hasFlowerFetchRequest() {
        return this.flowerFetchRequested;
    }

    public boolean hasFlowerRequester() {
        return this.flowerRequesterUuid != null;
    }

    @Nullable
    public Player getFlowerRequester() {
        return this.flowerRequesterUuid == null ? null : this.level().getPlayerByUUID(this.flowerRequesterUuid);
    }

    public boolean isCarryingChorusFlower() {
        return this.entityData.get(CARRYING_CHORUS_FLOWER);
    }

    public void consumeFlowerFetchRequest() {
        this.flowerFetchRequested = false;
    }

    private boolean canAcceptFlowerFetchRequest() {
        return !this.isBaby() && !this.isFlying() && !this.flowerFetchRequested && this.pendingChorusFlowerDrops == 0 && this.flowerFetchCooldown == 0;
    }

    public boolean isFlying() {
        return this.entityData.get(FLYING);
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
        this.entityData.set(FLYING, flying);
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
        this.setDeltaMovement(this.landingDriftX, -LANDING_DESCENT_SPEED, this.landingDriftZ);
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
        this.setDeltaMovement(0.0, 0.0, 0.0);
    }

    public void queueChorusFlowerDrop() {
        this.pendingChorusFlowerDrops++;
        this.entityData.set(CARRYING_CHORUS_FLOWER, true);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) this.validateFlowerRequester();
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.flowerFetchCooldown > 0) this.flowerFetchCooldown--;
            if (this.postBreakHoverTicks > 0) {
                this.navigation.stop();
                this.setNoGravity(true);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.0, 0.5));
                if (--this.postBreakHoverTicks == 0) {
                    if (this.isCarryingChorusFlower()) {
                        this.resumeFlying();
                    } else {
                        this.beginLanding();
                    }
                }
            } else if (this.landing) {
                if (this.isInWater()) {
                    this.setFlying(false);
                    this.setDeltaMovement(Vec3.ZERO);
                } else {
                    this.navigation.stop();
                    this.setNoGravity(true);
                    this.setDeltaMovement(this.landingDriftX, -LANDING_DESCENT_SPEED, this.landingDriftZ);
                    this.fallDistance = 0.0f;
                    if (this.onGround()) {
                        this.setFlying(false);
                        this.setDeltaMovement(Vec3.ZERO);
                        if (this.pendingChorusFlowerDrops == 0 && !this.flowerFetchRequested) this.flowerRequesterUuid = null;
                    }
                }
            }

            this.givePendingChorusFlowersToRequester();
        }

        this.animationController.tick();
        if (this.level().isClientSide()) this.idleAnimations.tick(this, this.isAlive());
    }

    private void resumeFlying() {
        this.landing = false;
        this.setNoGravity(false);
        this.navigation = this.flightNavigation;
        this.moveControl = this.flightMoveControl;
        this.setDeltaMovement(0.0, 0.0, 0.0);
    }

    private void givePendingChorusFlowersToRequester() {
        if (this.pendingChorusFlowerDrops <= 0 || this.flowerRequesterUuid == null) return;

        Player player = this.level().getPlayerByUUID(this.flowerRequesterUuid);
        if (player != null && this.getBoundingBox().intersects(player.getBoundingBox())
                && player.addItem(new ItemStack(Items.CHORUS_FLOWER, this.pendingChorusFlowerDrops))) {
            this.playSound(SoundEvents.ITEM_PICKUP, 0.2f, 1.0f);
            ModAdvancements.grant(player, ModAdvancements.SPECIAL_DELIVERY);
            this.pendingChorusFlowerDrops = 0;
            this.flowerRequesterUuid = null;
            this.entityData.set(CARRYING_CHORUS_FLOWER, false);
        }
    }

    private void validateFlowerRequester() {
        if (this.flowerRequesterUuid == null) {
            if (this.flowerFetchRequested || this.pendingChorusFlowerDrops > 0) this.clearFlowerFetchTask();
            return;
        }

        Player player = this.level().getPlayerByUUID(this.flowerRequesterUuid);
        if (player == null || !player.isAlive() || this.distanceToSqr(player) > MAX_REQUESTER_DISTANCE_SQUARED) {
            this.clearFlowerFetchTask();
        }
    }

    private void clearFlowerFetchTask() {
        if (this.pendingChorusFlowerDrops > 0 && !this.level().isClientSide()) {
            this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(Items.CHORUS_FLOWER, this.pendingChorusFlowerDrops));
        }
        this.flowerFetchRequested = false;
        this.pendingChorusFlowerDrops = 0;
        this.flowerRequesterUuid = null;
        this.entityData.set(CARRYING_CHORUS_FLOWER, false);
        if (this.isFlying() && !this.isLanding()) this.beginLanding();
    }

    public void cancelFlowerFetchTaskWithAnger() {
        if (this.level() instanceof ServerLevel serverWorld) {
            double particleY = this.getY() + this.getBbHeight() * 0.5;
            serverWorld.sendParticles(ParticleTypes.ANGRY_VILLAGER, this.getX(), particleY, this.getZ(), 5, 0.3, 0.2, 0.3, 0.0);
            serverWorld.sendParticles(ParticleTypes.CLOUD, this.getX(), particleY, this.getZ(), 10, 0.3, 0.2, 0.3, 0.02);
        }
        this.clearFlowerFetchTask();
    }

    public double getLandingAnimationSpeed() {
        if (this.landingAnimationSpeedAge == this.tickCount) return this.landingAnimationSpeed;

        this.landingAnimationSpeedAge = this.tickCount;
        Vec3 start = this.position();
        Vec3 end = start.add(0.0, -GROUND_CHECK_DISTANCE, 0.0);
        BlockHitResult hit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double distance = hit.getType() == HitResult.Type.MISS ? GROUND_CHECK_DISTANCE : start.y - hit.getLocation().y;
        double heightFactor = Mth.clamp(distance / MAX_LANDING_ANIMATION_SPEED_HEIGHT, 0.0, 1.0);
        this.landingAnimationSpeed = Mth.lerp(heightFactor, MIN_LANDING_ANIMATION_SPEED, MAX_LANDING_ANIMATION_SPEED);
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
        return !this.isFlying() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public EntityAnimationController<ChorusBeetleEntity, ChorusBeetleAnimationState> getAnimationController() {
        return this.animationController;
    }

    private PathNavigation createFlightNavigation(Level world) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, world);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    private static class ChorusBeetleJumpControl extends JumpControl {
        private static final double MIN_UPWARD_STEP = 0.25;
        private final ChorusBeetleEntity chorusBeetle;

        public ChorusBeetleJumpControl(ChorusBeetleEntity chorusBeetle) {
            super(chorusBeetle);
            this.chorusBeetle = chorusBeetle;
        }

        @Override
        public void jump() {
            if (this.chorusBeetle.isInWater() || this.chorusBeetle.isInLava()
                    || this.chorusBeetle.onGround() && this.chorusBeetle.getMoveControl().getWantedY() - this.chorusBeetle.getY() > MIN_UPWARD_STEP) {
                super.jump();
            }
        }
    }
}
