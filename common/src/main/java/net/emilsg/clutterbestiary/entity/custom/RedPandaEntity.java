package net.emilsg.clutterbestiary.entity.custom;

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
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class RedPandaEntity extends ParentTameableEntity implements HandledEntityAnimations<RedPandaEntity, RedPandaEntityAnimationState> {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.fromTag(ModItemTags.RED_PANDA_BREEDING_FOOD);
    private static final Ingredient TAMING_INGREDIENT = Ingredient.fromTag(ModItemTags.RED_PANDA_CRAVINGS);
    private static final int LAY_DOWN_TICKS = 10;
    private static final int STAND_UP_TICKS = 10;
    private static final int Y_POSE_START_TICKS = 15;
    private static final int Y_POSE_END_TICKS = 15;
    private static final int SIT_END_TICKS = 10;
    public static final TrackedData<Integer> PARTNER_ID = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> VARIANT = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.LONG);
    private static final TrackedData<Boolean> IS_SLEEPING = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_Y_POSING = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> Y_POSE_DURATION = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> Y_POSE_TICKER = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SLEEP_TIMER = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SLEEP_TRACKER = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> CURRENT_CRAVING = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> TIMES_FED = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_SITTING = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_STAYING = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> STAYING_POS = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    public final AnimationState rightEarTwitchAnimationState = new AnimationState();
    public final AnimationState leftEarTwitchAnimationState = new AnimationState();
    public final AnimationState sniffAnimationState = new AnimationState();
    private final EntityAnimationController<RedPandaEntity, RedPandaEntityAnimationState> animationController = new EntityAnimationController<>(this, RedPandaEntityAnimationState.IDLING, RedPandaEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(3, 3, 100)
            .add(33, leftEarTwitchAnimationState)
            .add(33, rightEarTwitchAnimationState)
            .add(34, sniffAnimationState);

    public RedPandaEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setupAnimationController();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5f, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1.0f));
        this.goalSelector.add(4, new TemptGoal(this, 1.25f, BREEDING_INGREDIENT, false));
        this.goalSelector.add(5, new RedPandaFollowOwnerGoal(this, 1.0f, 10.0f, 2.0f));
        this.goalSelector.add(6, new FollowParentGoal(this, 1.25));
        this.goalSelector.add(6, new RedPandaChallengeOtherGoal(this, 0.00025f));
        this.goalSelector.add(6, new RedPandaLookAtPartnerGoal(this));
        this.goalSelector.add(7, new RedPandaSleepGoal(this));
        this.goalSelector.add(8, new RedPandaWanderAroundFarGoal(this, 1.0f));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANIMATION_STATE, RedPandaEntityAnimationState.IDLING.getIndex());
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, -1L);
        builder.add(IS_SLEEPING, false);
        builder.add(IS_Y_POSING, false);
        builder.add(Y_POSE_DURATION, 0);
        builder.add(Y_POSE_TICKER, 0);
        builder.add(SLEEP_TIMER, 0);
        builder.add(SLEEP_TRACKER, 0);
        builder.add(PARTNER_ID, -1);
        builder.add(CURRENT_CRAVING, "");
        builder.add(TIMES_FED, 0);
        builder.add(IS_SITTING, false);
        builder.add(IS_STAYING, false);
        builder.add(STAYING_POS, this.getBlockPos());
        builder.add(VARIANT, RedPandaVariant.FLUFF.getId());
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setRandomCraving();
        this.setStayingPos(this.getBlockPos());
        this.setStaying(false);
        this.setVariant(RedPandaVariant.getRandom());

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setIsSleeping(nbt.getBoolean("IsSleeping"));
        this.setSleepTimer(nbt.getInt("SleepTimer"));
        this.setSleepTracker(nbt.getInt("SleepTracker"));
        this.setSit(nbt.getBoolean("IsSitting"));
        this.setTimesFed(nbt.getInt("TimesFed"));
        this.setYPoseDuration(nbt.getInt("YPoseDuration"));
        this.setCurrentCraving(Registries.ITEM.get(Identifier.tryParse(nbt.getString("CurrentCraving"))));
        this.setVariant(RedPandaVariant.fromId(nbt.getString("Variant")));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putInt("SleepTimer", this.getSleepTimer());
        nbt.putInt("SleepTracker", this.getSleepTracker());
        nbt.putInt("TimesFed", this.getTimesFed());
        nbt.putBoolean("IsSitting", this.isSat());
        nbt.putInt("YPoseDuration", this.getYPoseDuration());
        nbt.putString("CurrentCraving", this.getCurrentCraving().toString());
        nbt.putString("Variant", this.getTypeVariant());
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.RED_PANDAS_SPAWN_ON);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.18f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.6f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.15f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.5f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public boolean canBeChallenged() {
        return !this.isBaby() && !this.getSleepState() && this.getPartnerID() <= -1;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        RedPandaEntity redPandaEntity = ModEntityTypes.RED_PANDA.get().create(world);

        if (redPandaEntity != null) {
            if (this.isTamed()) {
                redPandaEntity.setOwnerUuid(this.getOwnerUuid());
                redPandaEntity.setTamed(true, true);
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
    public boolean damage(DamageSource source, float amount) {
        if (this.getSleepState()) {
            this.setSleepTimer(0);
            this.setSleepTracker(0);
            this.setIsSleeping(false);
            this.startState(RedPandaEntityAnimationState.IDLING);
        }

        return super.damage(source, amount);
    }

    public Item getCurrentCraving() {
        return Registries.ITEM.get(Identifier.tryParse(this.dataTracker.get(CURRENT_CRAVING)));
    }

    public void setCurrentCraving(Item currentCraving) {
        this.dataTracker.set(CURRENT_CRAVING, currentCraving.toString());
    }

    public int getPartnerID() {
        return this.dataTracker.get(PARTNER_ID);
    }

    public void setPartnerID(int partnerID) {
        this.dataTracker.set(PARTNER_ID, partnerID);
    }

    public boolean getSleepState() {
        return this.getSleepTracker() > 0 || this.getSleepTimer() > 0 || this.isSleeping();
    }

    public int getSleepTimer() {
        return this.dataTracker.get(SLEEP_TIMER);
    }

    public void setSleepTimer(int sleepTimer) {
        this.dataTracker.set(SLEEP_TIMER, sleepTimer);
    }

    public int getSleepTracker() {
        return this.dataTracker.get(SLEEP_TRACKER);
    }

    public void setSleepTracker(int sleepTracker) {
        this.dataTracker.set(SLEEP_TRACKER, sleepTracker);
    }

    public BlockPos getStayingPos() {
        return this.dataTracker.get(STAYING_POS);
    }

    public void setStayingPos(BlockPos stayingPos) {
        this.dataTracker.set(STAYING_POS, stayingPos);
    }

    @Override
    public Item getTamingItem() {
        //N/A as it has special taming.
        return null;
    }

    public int getTimesFed() {
        return this.dataTracker.get(TIMES_FED);
    }

    public void setTimesFed(int timesFed) {
        this.dataTracker.set(TIMES_FED, timesFed);
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public RedPandaVariant getVariant() {
        return RedPandaVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(RedPandaVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public int getYPoseDuration() {
        return this.dataTracker.get(Y_POSE_DURATION);
    }

    public void setYPoseDuration(int yPoseDuration) {
        this.dataTracker.set(Y_POSE_DURATION, yPoseDuration);
    }

    public int getYPoseTicker() {
        return this.dataTracker.get(Y_POSE_TICKER);
    }

    public void setYPoseTicker(int yPoseTicker) {
        this.dataTracker.set(Y_POSE_TICKER, yPoseTicker);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);
        World world = this.getWorld();

        if (this.isSleeping()) return ActionResult.PASS;

        if (this.isTamed() && this.getOwner() == player && player.shouldCancelInteraction()) {
            if (world.isClient) return ActionResult.CONSUME;
            boolean staying = !this.isStaying();

            this.setStaying(staying);
            if (staying) this.setStayingPos(this.getBlockPos());

            player.sendMessage(
                    Text.translatable("translation.clutterbestiary.red_panda.name", this.getName()).formatted(Formatting.BLUE)
                            .append(Text.translatable(staying ? "translation.clutterbestiary.red_panda.is_staying" : "translation.clutterbestiary.red_panda.not_staying")),
                    true
            );

            return ActionResult.SUCCESS;
        }

        if (this.isTamed() && this.getOwner() == player && !TAMING_INGREDIENT.test(heldItem) && !BREEDING_INGREDIENT.test(heldItem) && !player.shouldCancelInteraction()) {
            if (!world.isClient) {
                this.setSit(!this.isSat());
                if (this.isSat()) {
                    this.startState(RedPandaEntityAnimationState.SIT_START);
                } else {
                    this.startState(RedPandaEntityAnimationState.SIT_END);
                }
            }

            return ActionResult.SUCCESS;
        }

        if (!TAMING_INGREDIENT.test(heldItem) && !BREEDING_INGREDIENT.test(heldItem) && !player.shouldCancelInteraction()) {
            if (!world.isClient) {
                player.sendMessage(
                        Text.translatable("translation.clutterbestiary.red_panda.name", this.getName()).formatted(Formatting.BLUE)
                                .append(Text.translatable("translation.clutterbestiary.red_panda.craving").formatted(Formatting.BLUE))
                                .append(Text.translatable(this.getCurrentCraving().getTranslationKey()).formatted(Formatting.WHITE)), true
                );
            }
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == this.getCurrentCraving()) {
            if (this.getTimesFed() >= 3 || this.getTimesFed() < 0) {
                return ActionResult.PASS;
            } else {
                if (world.isClient) return ActionResult.CONSUME;
                this.setTimesFed(this.getTimesFed() + 1);
                heldItem.decrementUnlessCreative(1, player);

                if (this.getTimesFed() == 3) {
                    this.doTame(player);
                } else {
                    this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                    this.playSound(SoundEvents.ENTITY_CAT_EAT);
                    this.setRandomCraving();
                }
                return ActionResult.SUCCESS;
            }
        }

        return super.interactMob(player, hand);
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

    public boolean isStaying() {
        return this.dataTracker.get(IS_STAYING);
    }

    public void setStaying(boolean isStaying) {
        this.dataTracker.set(IS_STAYING, isStaying);
    }

    public boolean isYPosing() {
        return this.dataTracker.get(IS_Y_POSING);
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(RedPandaEntityAnimationState.IDLING);
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

    public void setIsYPosing(boolean yPosing) {
        this.dataTracker.set(IS_Y_POSING, yPosing);
    }

    public void setSit(boolean sitting) {
        super.setSitting(sitting);
        this.dataTracker.set(IS_SITTING, sitting);
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
        return livingEntity != null && this.squaredDistanceTo(this.getOwner()) >= 216.0;
    }

    @Override
    public EntityAnimationController<RedPandaEntity, RedPandaEntityAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (!world.isClient) {
            if (this.getYPoseDuration() <= 0) this.setIsYPosing(false);

            if (this.getSleepTimer() > 0) this.setSleepTimer(this.getSleepTimer() - 1);
            this.setIsSleeping(this.getSleepTimer() > 0);

            if ((!this.isTamed() && !this.isStaying()) && (world.isDay() ? random.nextInt(3000) == 0 : random.nextInt(9000) == 0) && !this.isSleeping() && this.getSleepTimer() <= 0 && this.getPartnerID() == -1) {
                int sleepDuration = (30 + random.nextInt(18) * 5) * 20;

                this.setSleepTimer(sleepDuration);
            } else if (this.isSleeping() && this.getSleepTimer() > 0) {
                this.setSleepTracker(this.getSleepTracker() + 1);
            }
        }

        this.animationController.tick();

        if (this.getWorld().isClient) {
            this.idleAnimations.tick(this, this.isAlive());
        }
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return (int) (super.computeFallDamage(fallDistance, damageMultiplier) * 0.5);
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.15f, 0.5F);
    }

    private void doTame(PlayerEntity player) {
        this.setOwner(player);
        ModAdvancements.grant(player, ModAdvancements.THREE_COURSE_MEAL);
        this.navigation.stop();
        this.setTarget(null);
        this.setSit(true);
        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
    }

    private void setRandomCraving() {
        var entries = Registries.ITEM.getEntryList(ModItemTags.RED_PANDA_CRAVINGS);
        if (entries.isPresent()) {
            var tagList = entries.get();
            int randomFromTag = this.random.nextInt(tagList.size());
            Item craving = tagList.get(randomFromTag).value();
            this.setCurrentCraving(craving);
        }
    }
}
