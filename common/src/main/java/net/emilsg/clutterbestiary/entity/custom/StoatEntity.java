package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.IdleAnimationGroup;
import net.emilsg.clutterbestiary.animation_handling.animation_states.StoatEntityAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.TrackedFleeGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WanderAroundFarOftenGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.entity.variants.StoatVariant;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class StoatEntity extends ParentTameableEntity implements HandledEntityAnimations<StoatEntity, StoatEntityAnimationState> {
    private static final int SIT_START_TICKS = 10;
    private static final int SIT_END_TICKS = 5;
    private static final int LAY_DOWN_TICKS = 10;
    private static final int SIT_UP_TICKS = 10;
    private static final int STAND_UP_TICKS = 10;
    private static final Item TAMING_ITEM = Items.EGG;
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.RABBIT, Items.CHICKEN);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(StoatEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(StoatEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(StoatEntity.class, TrackedDataHandlerRegistry.LONG);
    private static final TrackedData<String> VARIANT = DataTracker.registerData(StoatEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> IS_SITTING = DataTracker.registerData(StoatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SLEEPING = DataTracker.registerData(StoatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState rightEarTwitchAnimationState = new AnimationState();
    public final AnimationState leftEarTwitchAnimationState = new AnimationState();

    private final EntityAnimationController<StoatEntity, StoatEntityAnimationState> animationController = new EntityAnimationController<>(this, StoatEntityAnimationState.IDLING, StoatEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(20, 119, 1)
            .add(33, leftEarTwitchAnimationState)
            .add(67, rightEarTwitchAnimationState);

    public StoatEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setupAnimationController();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANIMATION_STATE, StoatEntityAnimationState.IDLING.getIndex());
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, -1L);
        builder.add(VARIANT, StoatVariant.SUMMER.getId());
        builder.add(IS_SITTING, false);
        builder.add(IS_SLEEPING, false);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setVariant(StoatVariant.getRandom());

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new TrackedFleeGoal(this, 2.0f));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0f, 10.0f, 2.0f));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.add(5, new WanderAroundFarOftenGoal(this, 1.0f));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(StoatVariant.fromId(nbt.getString("Variant")));
        this.setIsSleeping(nbt.getBoolean("IsSleeping"));
        this.setSit(nbt.getBoolean("IsSitting"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putBoolean("IsSitting", this.isSat());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.21f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.STOATS_SPAWN_ON);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        StoatEntity child = ModEntityTypes.STOAT.get().create(world);
        if (child != null && this.isTamed() && this.getOwnerUuid() != null) {
            child.setOwnerUuid(this.getOwnerUuid());
            child.setTamed(true, true);
        }
        return child;
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public StoatVariant getVariant() {
        return StoatVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(StoatVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);
        World world = this.getWorld();

        if (this.isSleeping() && heldItem.getItem() == this.getTamingItem()) return ActionResult.PASS;

        if (this.isTamed() && this.getOwner() == player && !player.shouldCancelInteraction() && !this.isBreedingItem(heldItem)) {
            if (!world.isClient) {
                this.setSit(!this.isSat());
                if (this.isSat()) {
                    this.startState(StoatEntityAnimationState.SIT_START);
                } else if (!this.isSleeping()) {
                    this.startState(StoatEntityAnimationState.SIT_END);
                }
            }
            return ActionResult.SUCCESS;
        }

        if (heldItem.getItem() == this.getTamingItem() && !this.isBaby() && !this.isTamed()) {
            if (!world.isClient) {
                heldItem.decrement(1);
                if ((this.random.nextInt(3) == 0)) {
                    this.doTame(player);
                } else {
                    this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.isSleeping() ? SoundEvents.ENTITY_FOX_SLEEP : SoundEvents.ENTITY_FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_FOX_DEATH;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 240;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 1.35f;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isSat() {
        return this.dataTracker.get(IS_SITTING);
    }

    public boolean isSleeping() {
        return this.dataTracker.get(IS_SLEEPING);
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(StoatEntityAnimationState.IDLING);
        super.onDeath(damageSource);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) this.calculateDimensions();
        super.onTrackedDataSet(data);
    }

    public void setIsSleeping(boolean isSleeping) {
        this.dataTracker.set(IS_SLEEPING, isSleeping);
    }

    public void setSit(boolean sitting) {
        super.setSitting(sitting);
        this.dataTracker.set(IS_SITTING, sitting);
    }

    private void setupAnimationController() {
        animationController.addTransition(StoatEntityAnimationState.IDLING, StoatEntityAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addTransition(StoatEntityAnimationState.SIT_START, StoatEntityAnimationState.SIT_END, (e, s, age) -> !e.isSat());
        animationController.addCompletion(StoatEntityAnimationState.SIT_START, StoatEntityAnimationState.SITTING, SIT_START_TICKS);
        animationController.addTransition(StoatEntityAnimationState.SITTING, StoatEntityAnimationState.SIT_END, (e, s, age) -> !e.isSat());
        animationController.addTransition(StoatEntityAnimationState.SITTING, StoatEntityAnimationState.LAYING_DOWN, (e, s, age) -> e.isSleeping());
        animationController.addTransition(StoatEntityAnimationState.LAYING_DOWN, StoatEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isSat());
        animationController.addTransition(StoatEntityAnimationState.LAYING_DOWN, StoatEntityAnimationState.SITTING_UP, (e, s, age) -> !e.isSleeping());
        animationController.addCompletion(StoatEntityAnimationState.LAYING_DOWN, StoatEntityAnimationState.SLEEPING, LAY_DOWN_TICKS);
        animationController.addTransition(StoatEntityAnimationState.SLEEPING, StoatEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isSat());
        animationController.addTransition(StoatEntityAnimationState.SLEEPING, StoatEntityAnimationState.SITTING_UP, (e, s, age) -> !e.isSleeping());
        animationController.addTransition(StoatEntityAnimationState.SITTING_UP, StoatEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isSat());
        animationController.addTransition(StoatEntityAnimationState.SITTING_UP, StoatEntityAnimationState.LAYING_DOWN, (e, s, age) -> e.isSleeping());
        animationController.addCompletion(StoatEntityAnimationState.SITTING_UP, StoatEntityAnimationState.SITTING, SIT_UP_TICKS);
        animationController.addCompletion(StoatEntityAnimationState.STANDING_UP, StoatEntityAnimationState.IDLING, STAND_UP_TICKS);
        animationController.addTransition(StoatEntityAnimationState.SIT_END, StoatEntityAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addCompletion(StoatEntityAnimationState.SIT_END, StoatEntityAnimationState.IDLING, SIT_END_TICKS);
    }

    @Override
    public EntityAnimationController<StoatEntity, StoatEntityAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        super.tick();

        World world = this.getWorld();

        if (!world.isClient) {
            if (this.isSat() && world.isNight() && !this.isSleeping()) {
                this.setIsSleeping(true);
            }

            if (this.isSleeping() && world.isDay() || !this.isSat()) {
                this.setIsSleeping(false);
            }
        }

        this.animationController.tick();

        if (this.getWorld().isClient) {
            this.idleAnimations.tick(this, this.isAlive());
        }
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f, 0.2f);
    }

    private void doTame(PlayerEntity player) {
        this.setOwner(player);
        ModAdvancements.grant(player, ModAdvancements.STOATALLY_YOURS);
        this.navigation.stop();
        this.setTarget(null);
        this.setSit(true);
        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
    }
}
