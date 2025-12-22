package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationConditions;
import net.emilsg.clutterbestiary.animation_handling.AnimationStateMachine;
import net.emilsg.clutterbestiary.animation_handling.animation_states.CapybaraEntityAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class CapybaraEntity extends ParentTameableEntity {
    private static final TrackedData<Boolean> IS_SLEEPING = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FORCE_SLEEPING = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SLEEPER = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.MELON);
    public final AnimationState earTwitchAnimationStateOne = new AnimationState();
    public final AnimationState earTwitchAnimationStateTwo = new AnimationState();

    public final AnimationState idlingAnimationState = new AnimationState();
    public final AnimationState layingDownAnimationState = new AnimationState();
    public final AnimationState sleepingAnimationState = new AnimationState();
    public final AnimationState standingUpAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();

    private final AnimationStateMachine<CapybaraEntity, CapybaraEntityAnimationState> animMachine = new AnimationStateMachine<>(this, CapybaraEntityAnimationState.IDLING, CapybaraEntityAnimationState.class);
    public int earTwitchAnimationTimeout = 0;
    public int swimAnimationTimeout = 0;
    private CapybaraEntityAnimationState lastSyncedAnimState = CapybaraEntityAnimationState.IDLING;

    public CapybaraEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setupAnimationStateMachine();
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        int sleeperType = random.nextBetween(0, 1);
        this.setSleeperType(sleeperType);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_SLEEPING, false);
        builder.add(FORCE_SLEEPING, false);
        builder.add(SLEEPER, 0);
        builder.add(ANIMATION_STATE, CapybaraEntityAnimationState.IDLING.getIndex());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new CapybaraSitGoal(this));
        this.goalSelector.add(3, new CapybaraEscapeDangerGoal(this, 1.25));
        this.goalSelector.add(4, new CapybaraMateGoal(this, 1));
        this.goalSelector.add(5, new CapybaraFollowOwnerGoal(this, 1.2, 10.0F, 2.0F));
        this.goalSelector.add(6, new CapybaraTemptGoal(this, 1.2, BREEDING_INGREDIENT, false));
        this.goalSelector.add(7, new FollowParentGoal(this, 1.2));
        this.goalSelector.add(8, new CapybaraWanderGoal(this, 1.0, 0.3f));
        this.goalSelector.add(9, new CapybaraLookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(10, new CapybaraLookAroundGoal(this));
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setIsSleeping(nbt.getBoolean("IsSleeping"));
        this.setIsForceSleeping(nbt.getBoolean("IsForceSleeping"));
        this.setSleeperType(nbt.getInt("Sleeper"));
        if (!this.isTamed()) this.setIsForceSleeping(false);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putBoolean("IsForceSleeping", this.isForceSleeping());
        nbt.putInt("Sleeper", this.sleeperType());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.225f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.CAPYBARAS_SPAWN_ON);
    }

    public boolean canEat() {
        return super.canEat() && !this.isSleeping();
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.CAPYBARA.get().create(world);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getSource() instanceof ProjectileEntity projectile && this.isSleeping()) {
            projectile.setVelocity(projectile.getVelocity().multiply(-1));
            return false;
        }

        return super.damage(source, amount);
    }

    public void healNearbyEntities(Entity centerEntity, double radius) {
        if (random.nextInt(1000) != 0) return;

        Box area = new Box(
                centerEntity.getX() - radius, centerEntity.getY() - radius, centerEntity.getZ() - radius,
                centerEntity.getX() + radius, centerEntity.getY() + radius, centerEntity.getZ() + radius
        );

        List<LivingEntity> nearbyEntities = centerEntity.getWorld().getEntitiesByClass(LivingEntity.class, area, e -> e != centerEntity);

        for (LivingEntity entity : nearbyEntities) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(1);
            }
        }
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        Item item = stackInHand.getItem();

        Item itemForTaming = Items.MELON_SLICE;

        if (item == itemForTaming && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().creativeMode) {
                stackInHand.decrement(1);
            }

            FoodComponent foodComponent = stackInHand.get(DataComponentTypes.FOOD);
            float nutrition = foodComponent != null ? (float) foodComponent.nutrition() : 1.0F;
            this.heal(2.0F * nutrition);
            return ActionResult.SUCCESS;
        }

        if (item == itemForTaming && !isTamed()) {
            this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.0F, 1.25F);
            if (this.getWorld().isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    stackInHand.decrement(1);
                }

                if (this.random.nextInt(3) == 0 && !this.getWorld().isClient()) {
                    super.setOwner(player);
                    this.navigation.recalculatePath();
                    this.setHealth(this.getMaxHealth());
                    this.setTarget(null);
                    this.setTamed(true, true);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }

                return ActionResult.SUCCESS;
            }
        }

        if (isTamed() && isOwner(player) && hand == Hand.MAIN_HAND
                && !(stackInHand.isOf(Items.MELON_SLICE)) && !(stackInHand.isOf(Items.MELON))) {
            boolean next = !this.isForceSleeping();
            this.setIsForceSleeping(next);
            this.setIsSleeping(next);
            return ActionResult.SUCCESS;
        }

        if (stackInHand.getItem() == itemForTaming) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.MELON);
    }

    public boolean isForceSleeping() {
        return this.dataTracker.get(FORCE_SLEEPING);
    }

    public boolean isSleeping() {
        return this.dataTracker.get(IS_SLEEPING);
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(CapybaraEntityAnimationState.IDLING);
        super.onDeath(damageSource);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            int state = this.getState();
            this.stopAnimations();
            switch (state) {
                case 1 -> this.layingDownAnimationState.startIfNotRunning(this.age);
                case 2 -> this.sleepingAnimationState.startIfNotRunning(this.age);
                case 3 -> this.standingUpAnimationState.startIfNotRunning(this.age);
                default -> this.idlingAnimationState.startIfNotRunning(this.age);
            }
        }

        super.onTrackedDataSet(data);
    }

    public void setIsForceSleeping(boolean isForceSleeping) {
        if (!this.isTamed()) isForceSleeping = false;
        this.dataTracker.set(FORCE_SLEEPING, isForceSleeping);
    }

    public void setIsSleeping(boolean isSleeping) {
        this.dataTracker.set(IS_SLEEPING, isSleeping);
    }

    public void setSleeperType(int sleeperType) {
        this.dataTracker.set(SLEEPER, sleeperType);
    }

    public void setupAnimationStateMachine() {
        animMachine.addTransition(
                CapybaraEntityAnimationState.IDLING,
                CapybaraEntityAnimationState.LAYING_DOWN,
                (e, s, age) -> e.isSleeping() || e.isForceSleeping()
        );

        animMachine.addTransition(
                CapybaraEntityAnimationState.LAYING_DOWN,
                CapybaraEntityAnimationState.SLEEPING,
                AnimationConditions.and(
                        (e, s, age) -> e.isSleeping() || e.isForceSleeping(),
                        AnimationConditions.timeAtLeast(10)
                )
        );

        animMachine.addTransition(
                CapybaraEntityAnimationState.SLEEPING,
                CapybaraEntityAnimationState.STANDING_UP,
                (e, s, age) -> !e.isSleeping() && !e.isForceSleeping()

        );

        animMachine.addTransition(
                CapybaraEntityAnimationState.STANDING_UP,
                CapybaraEntityAnimationState.IDLING,
                AnimationConditions.and(
                        (e, s, age) -> !e.isSleeping() && !e.isForceSleeping(),
                        AnimationConditions.timeAtLeast(10)
                )
        );
    }

    public int sleeperType() {
        return this.dataTracker.get(SLEEPER);
    }

    public void startState(CapybaraEntityAnimationState state) {
        if (!this.getWorld().isClient) {
            animMachine.forceState(state);
            this.setState(state.getIndex());
            lastSyncedAnimState = state;
        }
    }

    public void stopAnimations() {
        this.idlingAnimationState.stop();
        this.layingDownAnimationState.stop();
        this.sleepingAnimationState.stop();
        this.standingUpAnimationState.stop();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.setupAnimationStates();
            return;
        }

        if (!this.getWorld().isClient) {
            boolean changed = animMachine.tick();
            CapybaraEntityAnimationState current = animMachine.getCurrent();

            if (!changed) {
                CapybaraEntityAnimationState tracked = CapybaraEntityAnimationState.fromIndex(this.getState());
                if (tracked != current) {
                    animMachine.forceState(tracked);
                    current = tracked;
                }
            }

            if (current != lastSyncedAnimState) {
                this.setState(current.getIndex());
                lastSyncedAnimState = current;
            }

        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (!this.getWorld().isClient) {
            boolean night = this.getWorld().isNight();
            boolean shouldSleep;

            if (this.isTamed()) {
                shouldSleep = this.isForceSleeping();
            } else {
                shouldSleep = night;
            }

            if (shouldSleep && !this.isSleeping()) {
                this.setIsSleeping(true);
                this.startState(CapybaraEntityAnimationState.LAYING_DOWN);
            } else if (!shouldSleep && this.isSleeping()) {
                this.setIsSleeping(false);
                this.startState(CapybaraEntityAnimationState.STANDING_UP);
            }
        }

        if (this.isSleeping()) {
            this.healNearbyEntities(this, 4);
        }
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 2f, 0.3F);
    }

    private int getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    private void setState(int state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    private void pickRandomIdleAnim(boolean bl) {
        if (bl) {
            this.earTwitchAnimationStateOne.start(this.age);
        } else {
            this.earTwitchAnimationStateTwo.start(this.age);
        }
    }

    private void setupAnimationStates() {
        if (this.earTwitchAnimationTimeout <= 0 && random.nextInt(100) == 0) {
            this.earTwitchAnimationTimeout = 3;
            this.pickRandomIdleAnim(random.nextBoolean());
        } else {
            --this.earTwitchAnimationTimeout;
        }

        if (this.swimAnimationTimeout <= 0 && this.isTouchingWater()) {
            this.swimAnimationTimeout = 20;
            this.swimAnimationState.start(this.age);
        } else {
            --this.swimAnimationTimeout;
        }
    }

    private static class CapybaraSitGoal extends SitGoal {
        private final CapybaraEntity capybara;

        public CapybaraSitGoal(CapybaraEntity capybara) {
            super(capybara);
            this.capybara = capybara;
            this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            super.canStart();
            return capybara.isTamed() && capybara.isForceSleeping();
        }

        @Override
        public boolean shouldContinue() {
            return capybara.isTamed() && capybara.isForceSleeping();
        }

        @Override
        public void start() {
            capybara.getNavigation().stop();
            capybara.setIsSleeping(true);
            capybara.startState(CapybaraEntityAnimationState.LAYING_DOWN);
        }

        @Override
        public void stop() {
            capybara.setIsForceSleeping(false);
            capybara.setInSittingPose(false);
            capybara.setIsSleeping(false);
            capybara.startState(CapybaraEntityAnimationState.STANDING_UP);
        }
    }

    private class CapybaraWanderGoal extends WanderAroundFarGoal {

        public CapybaraWanderGoal(PathAwareEntity mob, double speed, float probability) {
            super(mob, speed, probability);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSleeping();
        }

        @Override
        public void tick() {
            if (isSleeping()) {
                this.stop();
                mob.getNavigation().stop();
            }
            super.tick();
        }
    }

    private class CapybaraLookAtEntityGoal extends LookAtEntityGoal {

        public CapybaraLookAtEntityGoal(MobEntity mob, Class<? extends LivingEntity> targetType, float range) {
            super(mob, targetType, range);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSleeping();
        }
    }

    private class CapybaraLookAroundGoal extends LookAroundGoal {

        public CapybaraLookAroundGoal(MobEntity mob) {
            super(mob);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSleeping();
        }
    }

    private class CapybaraFollowOwnerGoal extends FollowOwnerGoal {

        public CapybaraFollowOwnerGoal(ParentTameableEntity tameable, double speed, float minDistance, float maxDistance) {
            super(tameable, speed, minDistance, maxDistance);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSleeping();
        }
    }

    private class CapybaraTemptGoal extends TemptGoal {

        public CapybaraTemptGoal(PathAwareEntity entity, double speed, Ingredient food, boolean canBeScared) {
            super(entity, speed, food, canBeScared);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSleeping();
        }
    }

    private class CapybaraEscapeDangerGoal extends EscapeDangerGoal {

        public CapybaraEscapeDangerGoal(PathAwareEntity mob, double speed) {
            super(mob, speed);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSleeping();
        }
    }

    private class CapybaraMateGoal extends AnimalMateGoal {

        public CapybaraMateGoal(AnimalEntity animal, double speed) {
            super(animal, speed);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSleeping();
        }
    }
}
