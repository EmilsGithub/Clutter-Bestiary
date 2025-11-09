package net.emilsg.clutterbestiary.entity.custom;

import io.netty.buffer.ByteBuf;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.ModTrackedDataHandler;
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
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.ValueLists;
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
import java.util.function.IntFunction;

public class CapybaraEntity extends ParentTameableEntity {
    private static final TrackedData<Boolean> IS_SLEEPING = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FORCE_SLEEPING = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SLEEPER = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<CapybaraEntity.CapybaraEntityAnimationState> ANIMATION_STATE = DataTracker.registerData(CapybaraEntity.class, ModTrackedDataHandler.CAPYBARA_ANIMATION_STATE);

    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.MELON);
    public final AnimationState earTwitchAnimationStateOne = new AnimationState();
    public final AnimationState earTwitchAnimationStateTwo = new AnimationState();

    public final AnimationState idlingAnimationState = new AnimationState();
    public final AnimationState layingDownAnimationState = new AnimationState();
    public final AnimationState sleepingAnimationState = new AnimationState();
    public final AnimationState standingUpAnimationState = new AnimationState();

    public int earTwitchAnimationTimeout = 0;

    public CapybaraEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
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

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        int sleeperType = random.nextBetween(0, 1);
        this.setSleeperType(sleeperType);
        return super.initialize(world, difficulty, spawnReason, entityData);
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
            float nutrition = foodComponent != null ? (float)foodComponent.nutrition() : 1.0F;
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
                    setIsForceSleeping(true);
                    setIsSleeping(true);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }

                return ActionResult.SUCCESS;
            }
        }

        if (isTamed() && !this.getWorld().isClient() && hand == Hand.MAIN_HAND && !(stackInHand.isOf(Items.MELON_SLICE)) && !(stackInHand.isOf(Items.MELON)) && isOwner(player)) {
            setIsForceSleeping(!isSleeping());
            setIsSleeping(!isSleeping());
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

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setIsSleeping(nbt.getBoolean("IsSleeping"));
        this.setIsForceSleeping(nbt.getBoolean("IsForceSleeping"));
        this.setSleeperType(nbt.getInt("Sleeper"));
    }

    public int sleeperType() {
        return this.dataTracker.get(SLEEPER);
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient && !this.isTamed()) this.setIsSleeping(!this.getWorld().isDay());

        if (!this.getWorld().isClient && this.isTamed()) this.setIsSleeping(isForceSleeping());

        if (this.isSleeping()) {
            healNearbyEntities(this, 4);
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putBoolean("IsForceSleeping", this.isForceSleeping());
        nbt.putInt("Sleeper", this.sleeperType());
    }

    void setIsForceSleeping(boolean isSleeping) {
        this.dataTracker.set(FORCE_SLEEPING, isSleeping);
    }

    void setIsSleeping(boolean isSleeping) {
        this.dataTracker.set(IS_SLEEPING, isSleeping);
    }

    void setSleeperType(int type) {
        this.dataTracker.set(SLEEPER, type);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_SLEEPING, false);
        builder.add(FORCE_SLEEPING, false);
        builder.add(SLEEPER, 0);
        builder.add(ANIMATION_STATE, CapybaraEntityAnimationState.IDLING);
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
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.CAPYBARAS_SPAWN_ON);
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

    private static class CapybaraSitGoal extends SitGoal {
        private final CapybaraEntity capybara;
        private int sleepingTime;

        public CapybaraSitGoal(CapybaraEntity capybara) {
            super(capybara);
            this.capybara = capybara;
            this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
        }

        @Override
        public void tick() {
            sleepingTime++;

            if (sleepingTime >= 10) {
                this.capybara.startState(CapybaraEntityAnimationState.SLEEPING);
            }

            if (sleepingTime >= 30) sleepingTime = 21;

            super.tick();
        }

        @Override
        public boolean canStart() {
            super.canStart();
            return this.capybara.isForceSleeping() || this.capybara.isSitting();
        }

        @Override
        public boolean shouldContinue() {
            return this.capybara.isForceSleeping() || this.capybara.isSitting();
        }

        @Override
        public void start() {
            this.capybara.getNavigation().stop();
            this.capybara.setIsForceSleeping(true);
            this.capybara.startState(CapybaraEntityAnimationState.LAYING_DOWN);
        }

        @Override
        public void stop() {
            this.capybara.setIsForceSleeping(false);
            this.capybara.setInSittingPose(false);
            this.sleepingTime = 0;
            this.capybara.startState(CapybaraEntityAnimationState.STANDING_UP);
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

    //TODO Revamp Animations

    public static enum CapybaraEntityAnimationState {
        IDLING(0),
        LAYING_DOWN(1),
        SLEEPING(2),
        STANDING_UP(3);

        public static final IntFunction<CapybaraEntity.CapybaraEntityAnimationState> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(CapybaraEntity.CapybaraEntityAnimationState::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO);
        public static final PacketCodec<ByteBuf, CapybaraEntity.CapybaraEntityAnimationState> PACKET_CODEC = PacketCodecs.indexed(INDEX_TO_VALUE, CapybaraEntity.CapybaraEntityAnimationState::getIndex);
        private final int index;

        CapybaraEntityAnimationState(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
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

    private CapybaraEntity.CapybaraEntityAnimationState getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    private void setState(CapybaraEntity.CapybaraEntityAnimationState state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            CapybaraEntity.CapybaraEntityAnimationState state = this.getState();
            this.stopAnimations();
            switch (state.ordinal()) {
                case 1:
                    this.layingDownAnimationState.startIfNotRunning(this.age);
                    break;
                case 2:
                    this.sleepingAnimationState.startIfNotRunning(this.age);
                    break;
                case 3:
                    this.standingUpAnimationState.startIfNotRunning(this.age);
                    break;
                default:
                    this.idlingAnimationState.startIfNotRunning(this.age);
                    break;
            }

            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    public void stopAnimations() {
        this.idlingAnimationState.stop();
        this.layingDownAnimationState.stop();
        this.sleepingAnimationState.stop();
        this.standingUpAnimationState.stop();
    }

    public void startState(CapybaraEntity.CapybaraEntityAnimationState state) {
        switch (state.ordinal()) {
            case 0:
                this.setState(CapybaraEntity.CapybaraEntityAnimationState.IDLING);
                break;
            case 1:
                this.setState(CapybaraEntityAnimationState.LAYING_DOWN);
                break;
            case 2:
                this.setState(CapybaraEntityAnimationState.SLEEPING);
                break;
            case 3:
                this.setState(CapybaraEntityAnimationState.STANDING_UP);
                break;
        }
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(CapybaraEntity.CapybaraEntityAnimationState.IDLING);
        super.onDeath(damageSource);
    }
}
