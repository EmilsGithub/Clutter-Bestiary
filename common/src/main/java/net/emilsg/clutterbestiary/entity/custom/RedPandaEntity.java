package net.emilsg.clutterbestiary.entity.custom;
import java.util.function.Predicate;
import net.minecraft.world.entity.animal.feline.CatSoundVariants;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.Optional;
import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.IdleAnimationGroup;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.entity.variants.RedPandaVariant;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class RedPandaEntity extends ParentTameableEntity implements HandledEntityAnimations<RedPandaEntity, RedPandaEntityAnimationState> {
    private static final Predicate<ItemStack> BREEDING_INGREDIENT = stack -> stack.is(ModItemTags.RED_PANDA_BREEDING_FOOD);
    private static final Predicate<ItemStack> TAMING_INGREDIENT = stack -> stack.is(ModItemTags.RED_PANDA_CRAVINGS);
    private static final int LAY_DOWN_TICKS = 10;
    private static final int STAND_UP_TICKS = 10;
    private static final int Y_POSE_START_TICKS = 15;
    private static final int Y_POSE_END_TICKS = 15;
    private static final int SIT_END_TICKS = 10;
    public static final EntityDataAccessor<Integer> PARTNER_ID = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_Y_POSING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> Y_POSE_DURATION = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> Y_POSE_TICKER = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SLEEP_TIMER = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SLEEP_TRACKER = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> CURRENT_CRAVING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> TIMES_FED = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_STAYING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> STAYING_POS = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BLOCK_POS);
    public final AnimationState rightEarTwitchAnimationState = new AnimationState();
    public final AnimationState leftEarTwitchAnimationState = new AnimationState();
    public final AnimationState sniffAnimationState = new AnimationState();
    private final EntityAnimationController<RedPandaEntity, RedPandaEntityAnimationState> animationController = new EntityAnimationController<>(this, RedPandaEntityAnimationState.IDLING, RedPandaEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(3, 3, 100)
            .add(33, leftEarTwitchAnimationState)
            .add(33, rightEarTwitchAnimationState)
            .add(34, sniffAnimationState);

    public RedPandaEntity(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
        this.setupAnimationController();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5f, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0f));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25f, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(5, new RedPandaFollowOwnerGoal(this, 1.0f, 10.0f, 2.0f));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(6, new RedPandaChallengeOtherGoal(this, 0.00025f));
        this.goalSelector.addGoal(6, new RedPandaLookAtPartnerGoal(this));
        this.goalSelector.addGoal(7, new RedPandaSleepGoal(this));
        this.goalSelector.addGoal(8, new RedPandaWanderAroundFarGoal(this, 1.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0f));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_STATE, RedPandaEntityAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
        builder.define(IS_SLEEPING, false);
        builder.define(IS_Y_POSING, false);
        builder.define(Y_POSE_DURATION, 0);
        builder.define(Y_POSE_TICKER, 0);
        builder.define(SLEEP_TIMER, 0);
        builder.define(SLEEP_TRACKER, 0);
        builder.define(PARTNER_ID, -1);
        builder.define(CURRENT_CRAVING, "");
        builder.define(TIMES_FED, 0);
        builder.define(IS_SITTING, false);
        builder.define(IS_STAYING, false);
        builder.define(STAYING_POS, this.blockPosition());
        builder.define(VARIANT, RedPandaVariant.FLUFF.getId());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        this.setRandomCraving();
        this.setStayingPos(this.blockPosition());
        this.setStaying(false);
        this.setVariant(RedPandaVariant.getRandom());

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setIsSleeping(nbt.getBooleanOr("IsSleeping", false));
        this.setSleepTimer(nbt.getIntOr("SleepTimer", 0));
        this.setSleepTracker(nbt.getIntOr("SleepTracker", 0));
        this.setSit(nbt.getBooleanOr("IsSitting", false));
        this.setTimesFed(nbt.getIntOr("TimesFed", 0));
        this.setYPoseDuration(nbt.getIntOr("YPoseDuration", 0));
        this.setCurrentCraving(BuiltInRegistries.ITEM.getValue(Identifier.tryParse(nbt.getStringOr("CurrentCraving", ""))));
        this.setVariant(RedPandaVariant.fromId(nbt.getStringOr("Variant", "")));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putInt("SleepTimer", this.getSleepTimer());
        nbt.putInt("SleepTracker", this.getSleepTracker());
        nbt.putInt("TimesFed", this.getTimesFed());
        nbt.putBoolean("IsSitting", this.isSat());
        nbt.putInt("YPoseDuration", this.getYPoseDuration());
        nbt.putString("CurrentCraving", this.getCurrentCraving().toString());
        nbt.putString("Variant", this.getTypeVariant());
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.RED_PANDAS_SPAWN_ON);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18f)
                .add(Attributes.ATTACK_SPEED, 0.6f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.15f)
                .add(Attributes.ATTACK_DAMAGE, 2.5f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public boolean canBeChallenged() {
        return !this.isBaby() && !this.getSleepState() && this.getPartnerID() <= -1;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        RedPandaEntity redPandaEntity = ModEntityTypes.RED_PANDA.get().create(world, EntitySpawnReason.BREEDING);

        if (redPandaEntity != null) {
            if (this.isTame()) {
                redPandaEntity.setOwnerReference(this.getOwnerReference());
                redPandaEntity.setTame(true, true);
            } else {
                redPandaEntity.setRandomCraving();
            }

            if (entity instanceof RedPandaEntity partner) {
                redPandaEntity.setVariant(random.nextBoolean() ? this.getVariant() : partner.getVariant());
            } else {
                redPandaEntity.setVariant(RedPandaVariant.getRandom());
            }
        }

        return redPandaEntity;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (this.getSleepState()) {
            this.setSleepTimer(0);
            this.setSleepTracker(0);
            this.setIsSleeping(false);
            this.startState(RedPandaEntityAnimationState.IDLING);
        }

        return super.hurtServer(serverLevel, source, amount);
    }

    public Item getCurrentCraving() {
        return BuiltInRegistries.ITEM.getValue(Identifier.tryParse(this.entityData.get(CURRENT_CRAVING)));
    }

    public void setCurrentCraving(Item currentCraving) {
        this.entityData.set(CURRENT_CRAVING, currentCraving.toString());
    }

    public int getPartnerID() {
        return this.entityData.get(PARTNER_ID);
    }

    public void setPartnerID(int partnerID) {
        this.entityData.set(PARTNER_ID, partnerID);
    }

    public boolean getSleepState() {
        return this.getSleepTracker() > 0 || this.getSleepTimer() > 0 || this.isSleeping();
    }

    public int getSleepTimer() {
        return this.entityData.get(SLEEP_TIMER);
    }

    public void setSleepTimer(int sleepTimer) {
        this.entityData.set(SLEEP_TIMER, sleepTimer);
    }

    public int getSleepTracker() {
        return this.entityData.get(SLEEP_TRACKER);
    }

    public void setSleepTracker(int sleepTracker) {
        this.entityData.set(SLEEP_TRACKER, sleepTracker);
    }

    public BlockPos getStayingPos() {
        return this.entityData.get(STAYING_POS);
    }

    public void setStayingPos(BlockPos stayingPos) {
        this.entityData.set(STAYING_POS, stayingPos);
    }

    @Override
    public Item getTamingItem() {
        //N/A as it has special taming.
        return null;
    }

    public int getTimesFed() {
        return this.entityData.get(TIMES_FED);
    }

    public void setTimesFed(int timesFed) {
        this.entityData.set(TIMES_FED, timesFed);
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public RedPandaVariant getVariant() {
        return RedPandaVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(RedPandaVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    public int getYPoseDuration() {
        return this.entityData.get(Y_POSE_DURATION);
    }

    public void setYPoseDuration(int yPoseDuration) {
        this.entityData.set(Y_POSE_DURATION, yPoseDuration);
    }

    public int getYPoseTicker() {
        return this.entityData.get(Y_POSE_TICKER);
    }

    public void setYPoseTicker(int yPoseTicker) {
        this.entityData.set(Y_POSE_TICKER, yPoseTicker);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        Level world = this.level();

        if (this.isSleeping()) return InteractionResult.PASS;

        if (this.isTame() && this.getOwner() == player && player.isSecondaryUseActive()) {
            if (world.isClientSide()) return InteractionResult.CONSUME;
            boolean staying = !this.isStaying();

            this.setStaying(staying);
            if (staying) this.setStayingPos(this.blockPosition());

            player.sendOverlayMessage(
                    Component.translatable("translation.clutterbestiary.red_panda.name", this.getName()).withStyle(ChatFormatting.BLUE)
                            .append(Component.translatable(staying ? "translation.clutterbestiary.red_panda.is_staying" : "translation.clutterbestiary.red_panda.not_staying"))
            );

            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && this.getOwner() == player && !TAMING_INGREDIENT.test(heldItem) && !BREEDING_INGREDIENT.test(heldItem) && !player.isSecondaryUseActive()) {
            if (!world.isClientSide()) {
                this.setSit(!this.isSat());
                if (this.isSat()) {
                    this.startState(RedPandaEntityAnimationState.SIT_START);
                } else {
                    this.startState(RedPandaEntityAnimationState.SIT_END);
                }
            }

            return InteractionResult.SUCCESS;
        }

        if (!TAMING_INGREDIENT.test(heldItem) && !BREEDING_INGREDIENT.test(heldItem) && !player.isSecondaryUseActive()) {
            if (!world.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable("translation.clutterbestiary.red_panda.name", this.getName()).withStyle(ChatFormatting.BLUE)
                                .append(Component.translatable("translation.clutterbestiary.red_panda.craving").withStyle(ChatFormatting.BLUE))
                                .append(Component.translatable(this.getCurrentCraving().getDescriptionId()).withStyle(ChatFormatting.WHITE))
                );
            }
            return InteractionResult.SUCCESS;
        } else if (heldItem.getItem() == this.getCurrentCraving()) {
            if (this.getTimesFed() >= 3 || this.getTimesFed() < 0) {
                return InteractionResult.PASS;
            } else {
                if (world.isClientSide()) return InteractionResult.CONSUME;
                this.setTimesFed(this.getTimesFed() + 1);
                heldItem.consume(1, player);

                if (this.getTimesFed() == 3) {
                    this.doTame(player);
                } else {
                    this.level().broadcastEntityEvent(this, EntityEvent.VILLAGER_HAPPY);
                    this.makeSound(SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().eatSound().value());
                    this.setRandomCraving();
                }
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
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

    public boolean isStaying() {
        return this.entityData.get(IS_STAYING);
    }

    public void setStaying(boolean isStaying) {
        this.entityData.set(IS_STAYING, isStaying);
    }

    public boolean isYPosing() {
        return this.entityData.get(IS_Y_POSING);
    }

    public void die(DamageSource damageSource) {
        this.startState(RedPandaEntityAnimationState.IDLING);
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

    public void setIsYPosing(boolean yPosing) {
        this.entityData.set(IS_Y_POSING, yPosing);
    }

    public void setSit(boolean sitting) {
        super.setOrderedToSit(sitting);
        this.entityData.set(IS_SITTING, sitting);
    }

    private void setupAnimationController() {
        animationController.addTransition(RedPandaEntityAnimationState.IDLING, RedPandaEntityAnimationState.LAYING_DOWN, (e, s, age) -> e.isSleeping());
        animationController.addTransition(RedPandaEntityAnimationState.IDLING, RedPandaEntityAnimationState.STARTING_Y_POSE, (e, s, age) -> e.isYPosing());
        animationController.addTransition(RedPandaEntityAnimationState.IDLING, RedPandaEntityAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addTransition(RedPandaEntityAnimationState.LAYING_DOWN, RedPandaEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isSleeping());
        animationController.addCompletion(RedPandaEntityAnimationState.LAYING_DOWN, RedPandaEntityAnimationState.SLEEPING, LAY_DOWN_TICKS);
        animationController.addTransition(RedPandaEntityAnimationState.SLEEPING, RedPandaEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isSleeping());
        animationController.addCompletion(RedPandaEntityAnimationState.STANDING_UP, RedPandaEntityAnimationState.IDLING, STAND_UP_TICKS);
        animationController.addTransition(RedPandaEntityAnimationState.STARTING_Y_POSE, RedPandaEntityAnimationState.ENDING_Y_POSE, (e, s, age) -> !e.isYPosing());
        animationController.addCompletion(RedPandaEntityAnimationState.STARTING_Y_POSE, RedPandaEntityAnimationState.Y_POSING, Y_POSE_START_TICKS);
        animationController.addTransition(RedPandaEntityAnimationState.Y_POSING, RedPandaEntityAnimationState.ENDING_Y_POSE, (e, s, age) -> !e.isYPosing());
        animationController.addCompletion(RedPandaEntityAnimationState.ENDING_Y_POSE, RedPandaEntityAnimationState.IDLING, Y_POSE_END_TICKS);
        animationController.addTransition(RedPandaEntityAnimationState.SIT_START, RedPandaEntityAnimationState.SIT_END, (e, s, age) -> !e.isSat());
        animationController.addTransition(RedPandaEntityAnimationState.SIT_END, RedPandaEntityAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addCompletion(RedPandaEntityAnimationState.SIT_END, RedPandaEntityAnimationState.IDLING, SIT_END_TICKS);
    }

    public boolean shouldTryTeleportToOwner() {
        if (this.isStaying()) return false;
        LivingEntity livingEntity = this.getOwner();
        return livingEntity != null && this.distanceToSqr(this.getOwner()) >= 216.0;
    }

    @Override
    public EntityAnimationController<RedPandaEntity, RedPandaEntityAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (!world.isClientSide()) {
            if (this.getYPoseDuration() <= 0) this.setIsYPosing(false);

            if (this.getSleepTimer() > 0) this.setSleepTimer(this.getSleepTimer() - 1);
            this.setIsSleeping(this.getSleepTimer() > 0);

            if ((!this.isTame() && !this.isStaying()) && (world.isBrightOutside() ? random.nextInt(3000) == 0 : random.nextInt(9000) == 0) && !this.isSleeping() && this.getSleepTimer() <= 0 && this.getPartnerID() == -1) {
                int sleepDuration = (30 + random.nextInt(18) * 5) * 20;

                this.setSleepTimer(sleepDuration);
            } else if (this.isSleeping() && this.getSleepTimer() > 0) {
                this.setSleepTracker(this.getSleepTracker() + 1);
            }
        }

        this.animationController.tick();

        if (this.level().isClientSide()) {
            this.idleAnimations.tick(this, this.isAlive());
        }
    }

    @Override
    protected int calculateFallDamage(double fallDistance, float damageMultiplier) {
        return (int) (super.calculateFallDamage(fallDistance, damageMultiplier) * 0.5);
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 1.15f, 0.5F, 1.0F);
    }

    private void doTame(Player player) {
        this.tame(player);
        ModAdvancements.grant(player, ModAdvancements.THREE_COURSE_MEAL);
        this.navigation.stop();
        this.setTarget(null);
        this.setSit(true);
        this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
    }

    private void setRandomCraving() {
        var entries = BuiltInRegistries.ITEM.get(ModItemTags.RED_PANDA_CRAVINGS);
        if (entries.isPresent()) {
            var tagList = entries.get();
            int randomFromTag = this.random.nextInt(tagList.size());
            Item craving = tagList.get(randomFromTag).value();
            this.setCurrentCraving(craving);
        }
    }
}
