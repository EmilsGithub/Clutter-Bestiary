package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class StoatEntity extends ParentTameableEntity implements HandledEntityAnimations<StoatEntity, StoatEntityAnimationState> {
    private static final int SIT_START_TICKS = 10;
    private static final int SIT_END_TICKS = 5;
    private static final int LAY_DOWN_TICKS = 10;
    private static final int SIT_UP_TICKS = 10;
    private static final int STAND_UP_TICKS = 10;
    private static final Item TAMING_ITEM = Items.EGG;
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.RABBIT, Items.CHICKEN);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(StoatEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(StoatEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(StoatEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(StoatEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(StoatEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(StoatEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState rightEarTwitchAnimationState = new AnimationState();
    public final AnimationState leftEarTwitchAnimationState = new AnimationState();

    private final EntityAnimationController<StoatEntity, StoatEntityAnimationState> animationController = new EntityAnimationController<>(this, StoatEntityAnimationState.IDLING, StoatEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(20, 119, 1)
            .add(33, leftEarTwitchAnimationState)
            .add(67, rightEarTwitchAnimationState);

    public StoatEntity(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
        this.setupAnimationController();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_STATE, StoatEntityAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
        builder.define(VARIANT, StoatVariant.SUMMER.getId());
        builder.define(IS_SITTING, false);
        builder.define(IS_SLEEPING, false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        this.setVariant(StoatVariant.getRandom());

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TrackedFleeGoal(this, 2.0f));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0f, 10.0f, 2.0f));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WanderAroundFarOftenGoal(this, 1.0f));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(StoatVariant.fromId(nbt.getStringOr("Variant", "")));
        this.setIsSleeping(nbt.getBooleanOr("IsSleeping", false));
        this.setSit(nbt.getBooleanOr("IsSitting", false));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putBoolean("IsSitting", this.isSat());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.21f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.STOATS_SPAWN_ON);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        StoatEntity child = ModEntityTypes.STOAT.get().create(world, EntitySpawnReason.BREEDING);
        if (child != null && this.isTame() && this.getOwnerReference() != null) {
            child.setOwnerReference(this.getOwnerReference());
            child.setTame(true, true);
        }
        return child;
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public StoatVariant getVariant() {
        return StoatVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(StoatVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        Level world = this.level();

        if (this.isSleeping() && heldItem.getItem() == this.getTamingItem()) return InteractionResult.PASS;

        if (this.isTame() && this.getOwner() == player && !player.isSecondaryUseActive() && !this.isFood(heldItem)) {
            if (!world.isClientSide()) {
                this.setSit(!this.isSat());
                if (this.isSat()) {
                    this.startState(StoatEntityAnimationState.SIT_START);
                } else if (!this.isSleeping()) {
                    this.startState(StoatEntityAnimationState.SIT_END);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (heldItem.getItem() == this.getTamingItem() && !this.isBaby() && !this.isTame()) {
            if (!world.isClientSide()) {
                heldItem.shrink(1);
                if ((this.random.nextInt(3) == 0)) {
                    this.doTame(player);
                } else {
                    this.level().broadcastEntityEvent(this, EntityEvent.TAMING_FAILED);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.isSleeping() ? SoundEvents.FOX_SLEEP : SoundEvents.FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.35f;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isSat() {
        return this.entityData.get(IS_SITTING);
    }

    public boolean isSleeping() {
        return this.entityData.get(IS_SLEEPING);
    }

    public void die(DamageSource damageSource) {
        this.startState(StoatEntityAnimationState.IDLING);
        super.die(damageSource);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (ANIMATION_STATE.equals(data)) this.refreshDimensions();
        super.onSyncedDataUpdated(data);
    }

    public void setIsSleeping(boolean isSleeping) {
        this.entityData.set(IS_SLEEPING, isSleeping);
    }

    public void setSit(boolean sitting) {
        super.setOrderedToSit(sitting);
        this.entityData.set(IS_SITTING, sitting);
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

        Level world = this.level();

        if (!world.isClientSide()) {
            if (this.isSat() && world.isDarkOutside() && !this.isSleeping()) {
                this.setIsSleeping(true);
            }

            if (this.isSleeping() && world.isBrightOutside() || !this.isSat()) {
                this.setIsSleeping(false);
            }
        }

        this.animationController.tick();

        if (this.level().isClientSide()) {
            this.idleAnimations.tick(this, this.isAlive());
        }
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f, 0.2f, 1.0F);
    }

    private void doTame(Player player) {
        this.tame(player);
        ModAdvancements.grant(player, ModAdvancements.STOATALLY_YOURS);
        this.navigation.stop();
        this.setTarget(null);
        this.setSit(true);
        this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
    }
}
