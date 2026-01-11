package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationConditions;
import net.emilsg.clutterbestiary.animation_handling.AnimationStateMachine;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.entity.variants.RedPandaVariant;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class RedPandaEntity extends ParentTameableEntity implements HandledEntityAnimations {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.STRING);
    public static final TrackedData<Integer> PARTNER_ID = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(RedPandaEntity.class, TrackedDataHandlerRegistry.INTEGER);
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
    public final AnimationState layingDownAnimationState = new AnimationState();
    public final AnimationState sleepingAnimationState = new AnimationState();
    public final AnimationState standingUpAnimationState = new AnimationState();
    public final AnimationState startingYPoseAnimationState = new AnimationState();
    public final AnimationState yPosingAnimationState = new AnimationState();
    public final AnimationState endingYPoseAnimationState = new AnimationState();
    public final AnimationState rightEarTwitchAnimationState = new AnimationState();
    public final AnimationState leftEarTwitchAnimationState = new AnimationState();
    public final AnimationState sitStartAnimationState = new AnimationState();
    public final AnimationState sitEndAnimationState = new AnimationState();
    public final AnimationState sniffAnimationState = new AnimationState();
    private final AnimationStateMachine<RedPandaEntity, RedPandaEntityAnimationState> animMachine = new AnimationStateMachine<>(this, RedPandaEntityAnimationState.IDLING, RedPandaEntityAnimationState.class);
    public int idleAnimationTimeout = 0;
    private boolean clientAnimSynced;
    private RedPandaEntityAnimationState lastSyncedAnimState = RedPandaEntityAnimationState.IDLING;

    public RedPandaEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setupAnimationStateMachine();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5f, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1.0f));
        this.goalSelector.add(4, new TemptGoal(this, 1.25f, stack -> stack.isIn(ModItemTags.RED_PANDA_BREEDING_FOOD), false));
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
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return (int) (super.computeFallDamage(fallDistance, damageMultiplier) * 0.5);
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

    public int getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    public void setState(int state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    public BlockPos getStayingPos() {
        return this.dataTracker.get(STAYING_POS);
    }

    public void setStayingPos(BlockPos stayingPos) {
        this.dataTracker.set(STAYING_POS, stayingPos);
    }

    public int getTimesFed() {
        return this.dataTracker.get(TIMES_FED);
    }

    public void setTimesFed(int timesFed) {
        this.dataTracker.set(TIMES_FED, timesFed);
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

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public RedPandaVariant getVariant() {
        return RedPandaVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(RedPandaVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);
        World world = this.getWorld();

        if (this.isSleeping()) return ActionResult.PASS;

        if (this.isTamed() && this.getOwner() == player && player.shouldCancelInteraction()) {
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

        if (this.isTamed() && this.getOwner() == player && !heldItem.isIn(ModItemTags.RED_PANDA_CRAVINGS) && !heldItem.isIn(ModItemTags.RED_PANDA_BREEDING_FOOD) && !player.shouldCancelInteraction()) {
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

        if (!heldItem.isIn(ModItemTags.RED_PANDA_CRAVINGS) && !heldItem.isIn(ModItemTags.RED_PANDA_BREEDING_FOOD) && !player.shouldCancelInteraction()) {
            player.sendMessage(
                    Text.translatable("translation.clutterbestiary.red_panda.name", this.getName()).formatted(Formatting.BLUE)
                            .append(Text.translatable("translation.clutterbestiary.red_panda.craving").formatted(Formatting.BLUE))
                            .append(Text.translatable(this.getCurrentCraving().getTranslationKey()).formatted(Formatting.WHITE)), true
            );
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == this.getCurrentCraving()) {
            if (this.getTimesFed() >= 3 || this.getTimesFed() < 0) {
                return ActionResult.PASS;
            } else {
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
        return stack.isIn(ModItemTags.RED_PANDA_BREEDING_FOOD);
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

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            RedPandaEntityAnimationState animationState = RedPandaEntityAnimationState.fromIndex(this.getState());
            this.stopAnimations();
            switch (animationState) {
                case LAYING_DOWN -> this.layingDownAnimationState.startIfNotRunning(this.age);
                case SLEEPING -> this.sleepingAnimationState.startIfNotRunning(this.age);
                case STANDING_UP -> this.standingUpAnimationState.startIfNotRunning(this.age);
                case STARTING_Y_POSE -> this.startingYPoseAnimationState.startIfNotRunning(this.age);
                case Y_POSING -> this.yPosingAnimationState.startIfNotRunning(this.age);
                case ENDING_Y_POSE -> this.endingYPoseAnimationState.startIfNotRunning(this.age);
                case SIT_START -> this.sitStartAnimationState.startIfNotRunning(this.age);
                case SIT_END -> this.sitEndAnimationState.startIfNotRunning(this.age);
                default -> {
                }
            }

            this.calculateDimensions();
        }

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

    public void setupAnimationStateMachine() {
        animMachine.addTransition(
                RedPandaEntityAnimationState.IDLING,
                RedPandaEntityAnimationState.LAYING_DOWN,
                (e, s, age) -> e.isSleeping()
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.LAYING_DOWN,
                RedPandaEntityAnimationState.SLEEPING,
                AnimationConditions.and(
                        (e, s, age) -> e.isSleeping(),
                        AnimationConditions.timeAtLeast(10)
                )
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.SLEEPING,
                RedPandaEntityAnimationState.STANDING_UP,
                (e, s, age) -> !e.isSleeping()

        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.STANDING_UP,
                RedPandaEntityAnimationState.IDLING,
                AnimationConditions.and(
                        (e, s, age) -> !e.isSleeping(),
                        AnimationConditions.timeAtLeast(10)
                )
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.IDLING,
                RedPandaEntityAnimationState.STARTING_Y_POSE,
                (e, s, age) -> e.isYPosing()
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.STARTING_Y_POSE,
                RedPandaEntityAnimationState.Y_POSING,
                AnimationConditions.and(
                        (e, s, age) -> e.isYPosing(),
                        AnimationConditions.timeAtLeast(15)
                )
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.Y_POSING,
                RedPandaEntityAnimationState.ENDING_Y_POSE,
                (e, s, age) -> !e.isYPosing()

        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.ENDING_Y_POSE,
                RedPandaEntityAnimationState.IDLING,
                AnimationConditions.and(
                        (e, s, age) -> !e.isYPosing(),
                        AnimationConditions.timeAtLeast(15)
                )
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.IDLING,
                RedPandaEntityAnimationState.SIT_START,
                (e, s, age) -> e.isSat()
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.SIT_START,
                RedPandaEntityAnimationState.SIT_END,
                (e, s, age) -> !e.isSat()
        );

        animMachine.addTransition(
                RedPandaEntityAnimationState.SIT_END,
                RedPandaEntityAnimationState.IDLING,
                AnimationConditions.and(
                        (e, s, age) -> !e.isSat(),
                        AnimationConditions.timeAtLeast(10)
                )
        );
    }

    public boolean shouldTryTeleportToOwner() {
        if (this.isStaying()) return false;
        LivingEntity livingEntity = this.getOwner();
        return livingEntity != null && this.squaredDistanceTo(this.getOwner()) >= 216.0;
    }

    public void startState(RedPandaEntityAnimationState state) {
        switch (state) {
            case IDLING -> this.setState(RedPandaEntityAnimationState.IDLING.getIndex());
            case LAYING_DOWN -> this.setState(RedPandaEntityAnimationState.LAYING_DOWN.getIndex());
            case SLEEPING -> this.setState(RedPandaEntityAnimationState.SLEEPING.getIndex());
            case STANDING_UP -> this.setState(RedPandaEntityAnimationState.STANDING_UP.getIndex());
            case STARTING_Y_POSE -> this.setState(RedPandaEntityAnimationState.STARTING_Y_POSE.getIndex());
            case Y_POSING -> this.setState(RedPandaEntityAnimationState.Y_POSING.getIndex());
            case ENDING_Y_POSE -> this.setState(RedPandaEntityAnimationState.ENDING_Y_POSE.getIndex());
            case SIT_START -> this.setState(RedPandaEntityAnimationState.SIT_START.getIndex());
            case SIT_END -> this.setState(RedPandaEntityAnimationState.SIT_END.getIndex());
        }
    }

    public void stopAnimations() {
        this.layingDownAnimationState.stop();
        this.sleepingAnimationState.stop();
        this.standingUpAnimationState.stop();
        this.startingYPoseAnimationState.stop();
        this.yPosingAnimationState.stop();
        this.endingYPoseAnimationState.stop();
        this.sitStartAnimationState.stop();
        this.sitEndAnimationState.stop();
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (!clientAnimSynced) {
            clientAnimSynced = true;

            if (this.isSat()) startState(RedPandaEntityAnimationState.SIT_START);
            if (this.isSleeping()) startState(RedPandaEntityAnimationState.LAYING_DOWN);
            if (this.isYPosing()) startState(RedPandaEntityAnimationState.STARTING_Y_POSE);
        }

        if (!world.isClient) {
            if (this.getYPoseDuration() <= 0) this.setIsYPosing(false);

            boolean changed = animMachine.tick();
            RedPandaEntityAnimationState current = animMachine.getCurrent();

            if (!changed) {
                RedPandaEntityAnimationState tracked = RedPandaEntityAnimationState.fromIndex(this.getState());
                if (tracked != current) {
                    animMachine.forceState(tracked);
                    current = tracked;
                }
            }

            if (current != lastSyncedAnimState) {
                this.setState(current.getIndex());
                lastSyncedAnimState = current;
            }

            if (this.getSleepTimer() > 0) this.setSleepTimer(this.getSleepTimer() - 1);
            this.setIsSleeping(this.getSleepTimer() > 0);

            if ((!this.isTamed() && !this.isStaying()) && (world.isDay() ? random.nextInt(3000) == 0 : random.nextInt(9000) == 0) && !this.isSleeping() && this.getSleepTimer() <= 0 && this.getPartnerID() == -1) {
                int sleepDuration = (30 + random.nextInt(18) * 5) * 20;

                this.setSleepTimer(sleepDuration);
            } else if (this.isSleeping() && this.getSleepTimer() > 0) {
                this.setSleepTracker(this.getSleepTracker() + 1);
                if (this.getSleepTracker() > 10 && this.getSleepTracker() < this.getSleepTimer() - 10) {
                    this.startState(RedPandaEntityAnimationState.SLEEPING);
                }
            }

            if (this.getYPoseTicker() > 15 && this.getYPoseTicker() < this.getYPoseDuration() - 15) {
                this.startState(RedPandaEntityAnimationState.Y_POSING);
            }
        }

        if (this.getWorld().isClient) {
            this.setupAnimationStates();
        }
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

    private void pickRandomIdleAnim(float chance) {
        if (chance < 0.33) {
            this.leftEarTwitchAnimationState.start(this.age);
        } else if (chance < 0.66) {
            this.rightEarTwitchAnimationState.start(this.age);
        } else {
            this.sniffAnimationState.start(this.age);
        }
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

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && random.nextInt(100) == 0) {
            this.idleAnimationTimeout = 3;
            this.pickRandomIdleAnim(random.nextFloat());
        } else {
            --this.idleAnimationTimeout;
        }
    }

    private void doTame(PlayerEntity player) {
        this.setOwner(player);
        this.navigation.stop();
        this.setTarget(null);
        this.setSit(true);
        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
    }

}
