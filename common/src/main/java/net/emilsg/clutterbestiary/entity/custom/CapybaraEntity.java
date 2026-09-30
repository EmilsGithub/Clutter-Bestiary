package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

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
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CapybaraEntity extends ParentTameableEntity implements HandledEntityAnimations<CapybaraEntity, CapybaraEntityAnimationState> {
    private static final int LAY_DOWN_TICKS = 10;
    private static final int STAND_UP_TICKS = 10;
    private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(CapybaraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FORCE_SLEEPING = SynchedEntityData.defineId(CapybaraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SLEEPER = SynchedEntityData.defineId(CapybaraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(CapybaraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(CapybaraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(CapybaraEntity.class, EntityDataSerializers.LONG);

    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.MELON);
    private static final Item TAMING_ITEM = Items.MELON_SLICE;
    public final AnimationState earTwitchAnimationStateOne = new AnimationState();
    public final AnimationState earTwitchAnimationStateTwo = new AnimationState();

    public final AnimationState swimAnimationState = new AnimationState();

    private final EntityAnimationController<CapybaraEntity, CapybaraEntityAnimationState> animationController = new EntityAnimationController<>(this, CapybaraEntityAnimationState.IDLING, CapybaraEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(3, 3, 100)
            .add(1, earTwitchAnimationStateOne)
            .add(1, earTwitchAnimationStateTwo);

    public CapybaraEntity(EntityType<? extends ParentTameableEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setupAnimationController();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        int sleeperType = random.nextIntBetweenInclusive(0, 1);
        this.setSleeperType(sleeperType);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_SLEEPING, false);
        builder.define(FORCE_SLEEPING, false);
        builder.define(SLEEPER, 0);
        builder.define(ANIMATION_STATE, CapybaraEntityAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CapybaraSitGoal(this));
        this.goalSelector.addGoal(3, new CapybaraEscapeDangerGoal(this, 1.25));
        this.goalSelector.addGoal(4, new CapybaraMateGoal(this, 1));
        this.goalSelector.addGoal(5, new CapybaraFollowOwnerGoal(this, 1.2, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new CapybaraTemptGoal(this, 1.2, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.2));
        this.goalSelector.addGoal(8, new CapybaraWanderGoal(this, 1.0, 0.3f));
        this.goalSelector.addGoal(9, new CapybaraLookAtEntityGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new CapybaraLookAroundGoal(this));
    }

    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setIsSleeping(nbt.getBooleanOr("IsSleeping", false));
        this.setIsForceSleeping(nbt.getBooleanOr("IsForceSleeping", false));
        this.setSleeperType(nbt.getIntOr("Sleeper", 0));
        if (!this.isTame()) this.setIsForceSleeping(false);
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putBoolean("IsForceSleeping", this.isForceSleeping());
        nbt.putInt("Sleeper", this.sleeperType());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.225f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.CAPYBARAS_SPAWN_ON);
    }

    public boolean canFallInLove() {
        return super.canFallInLove() && !this.isSleeping();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.CAPYBARA.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof Projectile projectile && this.isSleeping()) {
            if (!this.level().isClientSide()) projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-1));
            return false;
        }

        return super.hurtServer(serverLevel, source, amount);
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public void healNearbyEntities(Entity centerEntity, double radius) {
        if (this.level().isClientSide()) return;
        if (random.nextInt(1000) != 0) return;

        AABB area = new AABB(
                centerEntity.getX() - radius, centerEntity.getY() - radius, centerEntity.getZ() - radius,
                centerEntity.getX() + radius, centerEntity.getY() + radius, centerEntity.getZ() + radius
        );

        List<LivingEntity> nearbyEntities = centerEntity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != centerEntity);

        for (LivingEntity entity : nearbyEntities) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(1);
            }
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        Item item = stackInHand.getItem();

        Item itemForTaming = this.getTamingItem();

        if (item == itemForTaming && this.getHealth() < this.getMaxHealth()) {
            if (this.level().isClientSide()) return InteractionResult.CONSUME;
            if (!player.getAbilities().instabuild) {
                stackInHand.shrink(1);
            }

            FoodProperties foodComponent = stackInHand.get(DataComponents.FOOD);
            float nutrition = foodComponent != null ? (float) foodComponent.nutrition() : 1.0F;
            this.heal(2.0F * nutrition);
            return InteractionResult.SUCCESS;
        }

        if (item == itemForTaming && !isTame()) {
            this.playSound(SoundEvents.HORSE_EAT, 1.0F, 1.25F);
            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    stackInHand.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !this.level().isClientSide()) {
                    super.tame(player);
                    ModAdvancements.grant(player, ModAdvancements.MELON_FRIENDS);
                    this.navigation.recomputePath();
                    this.setHealth(this.getMaxHealth());
                    this.setTarget(null);
                    this.setTame(true, true);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (isTame() && isOwnedBy(player) && hand == InteractionHand.MAIN_HAND
                && !(stackInHand.is(Items.MELON_SLICE)) && !(stackInHand.is(Items.MELON))) {
            boolean next = !this.isForceSleeping();
            this.setIsForceSleeping(next);
            this.setIsSleeping(next);
            return InteractionResult.SUCCESS;
        }

        if (stackInHand.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isForceSleeping() {
        return this.entityData.get(FORCE_SLEEPING);
    }

    public boolean isSleeping() {
        return this.entityData.get(IS_SLEEPING);
    }

    public void die(DamageSource damageSource) {
        this.startState(CapybaraEntityAnimationState.IDLING);
        super.die(damageSource);
    }

    public void setIsForceSleeping(boolean isForceSleeping) {
        if (!this.isTame()) isForceSleeping = false;
        this.entityData.set(FORCE_SLEEPING, isForceSleeping);
    }

    public void setIsSleeping(boolean isSleeping) {
        this.entityData.set(IS_SLEEPING, isSleeping);
    }

    public void setSleeperType(int sleeperType) {
        this.entityData.set(SLEEPER, sleeperType);
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
        return this.entityData.get(SLEEPER);
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
        AnimationPlayback.updateLoop(this, this.swimAnimationState, this.isAlive() && this.isInWater());
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            boolean night = this.level().isDarkOutside();
            boolean shouldSleep;

            if (this.isTame()) {
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

        if (!this.level().isClientSide() && this.isSleeping()) {
            this.healNearbyEntities(this, 4);
        }
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 2f, 0.3F, 1.0F);
    }

}
