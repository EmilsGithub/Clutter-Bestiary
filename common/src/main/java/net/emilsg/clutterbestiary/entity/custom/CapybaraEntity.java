package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.IdleAnimationGroup;
import net.emilsg.clutterbestiary.animation_handling.animation_states.CapybaraEntityAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraEscapeDangerGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraFollowOwnerGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraLookAroundGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraLookAtEntityGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraMateGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraSitGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraTemptGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.CapybaraWanderGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.util.ModAdvancements;
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

import java.util.List;

public class CapybaraEntity extends ParentTameableEntity implements HandledEntityAnimations<CapybaraEntity, CapybaraEntityAnimationState> {
    private static final int LAY_DOWN_TICKS = 10;
    private static final int STAND_UP_TICKS = 10;
    private static final TrackedData<Boolean> IS_SLEEPING = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FORCE_SLEEPING = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SLEEPER = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(CapybaraEntity.class, TrackedDataHandlerRegistry.LONG);

    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.MELON);
    private static final Item TAMING_ITEM = Items.MELON_SLICE;
    public final AnimationState earTwitchAnimationStateOne = new AnimationState();
    public final AnimationState earTwitchAnimationStateTwo = new AnimationState();

    public final AnimationState swimAnimationState = new AnimationState();

    private final EntityAnimationController<CapybaraEntity, CapybaraEntityAnimationState> animationController = new EntityAnimationController<>(this, CapybaraEntityAnimationState.IDLING, CapybaraEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(3, 3, 100)
            .add(1, earTwitchAnimationStateOne)
            .add(1, earTwitchAnimationStateTwo);

    public CapybaraEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setupAnimationController();
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
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, -1L);
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
            if (!this.getWorld().isClient) projectile.setVelocity(projectile.getVelocity().multiply(-1));
            return false;
        }

        return super.damage(source, amount);
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public void healNearbyEntities(Entity centerEntity, double radius) {
        if (this.getWorld().isClient) return;
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

        Item itemForTaming = this.getTamingItem();

        if (item == itemForTaming && this.getHealth() < this.getMaxHealth()) {
            if (this.getWorld().isClient) return ActionResult.CONSUME;
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
                    ModAdvancements.grant(player, ModAdvancements.MELON_FRIENDS);
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
        return BREEDING_INGREDIENT.test(stack);
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

    private void setupAnimationController() {
        animationController.addTransition(CapybaraEntityAnimationState.IDLING, CapybaraEntityAnimationState.LAYING_DOWN, (e, s, age) -> e.isSleeping() || e.isForceSleeping());
        animationController.addTransition(CapybaraEntityAnimationState.LAYING_DOWN, CapybaraEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isSleeping() && !e.isForceSleeping());
        animationController.addCompletion(CapybaraEntityAnimationState.LAYING_DOWN, CapybaraEntityAnimationState.SLEEPING, LAY_DOWN_TICKS);
        animationController.addTransition(CapybaraEntityAnimationState.SLEEPING, CapybaraEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isSleeping() && !e.isForceSleeping());
        animationController.addTransition(CapybaraEntityAnimationState.STANDING_UP, CapybaraEntityAnimationState.LAYING_DOWN, (e, s, age) -> e.isSleeping() || e.isForceSleeping());
        animationController.addCompletion(CapybaraEntityAnimationState.STANDING_UP, CapybaraEntityAnimationState.IDLING, STAND_UP_TICKS);
    }

    public int sleeperType() {
        return this.dataTracker.get(SLEEPER);
    }

    @Override
    public EntityAnimationController<CapybaraEntity, CapybaraEntityAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        super.tick();
        this.animationController.tick();
        this.idleAnimations.tick(this, this.isAlive());
        AnimationPlayback.updateLoop(this, this.swimAnimationState, this.isAlive() && this.isTouchingWater());
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
            } else if (!shouldSleep && this.isSleeping()) {
                this.setIsSleeping(false);
            }
        }

        if (!this.getWorld().isClient && this.isSleeping()) {
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

}
