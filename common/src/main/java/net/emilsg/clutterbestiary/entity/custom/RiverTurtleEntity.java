package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationConditions;
import net.emilsg.clutterbestiary.animation_handling.AnimationStateMachine;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RiverTurtleAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class RiverTurtleEntity extends ParentAnimalEntity {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> BASKING_DURATION = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> HIDING = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SITTING = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState hidingAnimationState = new AnimationState();
    public final AnimationState unhidingAnimationState = new AnimationState();
    public final AnimationState sitStartAnimationState = new AnimationState();
    public final AnimationState sitEndAnimationState = new AnimationState();

    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;
    private RiverTurtleAnimationState lastSyncedAnimState = RiverTurtleAnimationState.IDLING;

    private final AnimationStateMachine<RiverTurtleEntity, RiverTurtleAnimationState> animMachine = new AnimationStateMachine<>(this, RiverTurtleAnimationState.IDLING, RiverTurtleAnimationState.class);


    private int healthTicker = 0;
    private boolean canHealPassively = false;

    public RiverTurtleEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);

        this.moveControl = new RiverTurtleMoveControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
        this.setupAnimationStateMachine();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new RiverTurtleHideGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.add(2, new BaskGoal(this, 0.0001f));
        this.goalSelector.add(3, new HarvestKelpGoal(this, 1, 12, 0.05f));
        this.goalSelector.add(3, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.add(4, new LeaveWaterGoal(this, 1.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6f));

        this.targetSelector.add(1, new ConditionalActiveTargetGoal<>(this, CodEntity.class, true, 0.001f));
        this.targetSelector.add(2, new ConditionalActiveTargetGoal<>(this, SalmonEntity.class, true, 0.001f));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setVariant(RiverTurtleVariant.getRandom());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANIMATION_STATE, RiverTurtleAnimationState.IDLING.getIndex());
        builder.add(BASKING_DURATION, 0);
        builder.add(HIDING, false);
        builder.add(VARIANT, RiverTurtleVariant.SANDY.getId());
        builder.add(IS_SITTING, false);
    }

    public void copyDataFromNbt(RiverTurtleEntity entity, NbtCompound nbt) {
        if (nbt.contains("NoAI")) {
            entity.setAiDisabled(nbt.getBoolean("NoAI"));
        }

        if (nbt.contains("Silent")) {
            entity.setSilent(nbt.getBoolean("Silent"));
        }

        if (nbt.contains("NoGravity")) {
            entity.setNoGravity(nbt.getBoolean("NoGravity"));
        }

        if (nbt.contains("Glowing")) {
            entity.setGlowing(nbt.getBoolean("Glowing"));
        }

        if (nbt.contains("Invulnerable")) {
            entity.setInvulnerable(nbt.getBoolean("Invulnerable"));
        }

        if (nbt.contains("Variant")) {
            entity.setVariant(RiverTurtleVariant.fromId(nbt.getString("Variant")));
        }

        if (nbt.contains("Health", 99)) {
            entity.setHealth(nbt.getFloat("Health"));
        }

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBaskingDuration(nbt.getInt("BaskingDuration"));
        this.setVariant(RiverTurtleVariant.fromId(nbt.getString("Variant")));
        this.setSit(nbt.getBoolean("IsSitting"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("BaskingDuration", this.getBaskingDuration());
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("IsSitting", this.isSat());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.175F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2F);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.RIVER_TURTLES_SPAWN_ON);
    }

    public void copyDataToStack(RiverTurtleEntity entity, ItemStack stack) {
        stack.set(DataComponentTypes.CUSTOM_NAME, entity.getCustomName());
        NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack, (nbtCompound) -> {
            if (entity.isAiDisabled()) {
                nbtCompound.putBoolean("NoAI", entity.isAiDisabled());
            }

            if (entity.isSilent()) {
                nbtCompound.putBoolean("Silent", entity.isSilent());
            }

            if (entity.hasNoGravity()) {
                nbtCompound.putBoolean("NoGravity", entity.hasNoGravity());
            }

            if (entity.isGlowing()) {
                nbtCompound.putBoolean("Glowing", entity.isGlowing());
            }

            if (entity.isInvulnerable()) {
                nbtCompound.putBoolean("Invulnerable", entity.isInvulnerable());
            }

            nbtCompound.putFloat("Health", entity.getHealth());
            nbtCompound.putString("Variant", entity.getTypeVariant());
        });
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        RiverTurtleEntity child = ModEntityTypes.RIVER_TURTLE.get().create(world);
        if (child != null) child.setVariant(RiverTurtleVariant.getRandom());
        return child;
    }

    public int getBaskingDuration() {
        return this.dataTracker.get(BASKING_DURATION);
    }

    public void setBaskingDuration(int baskingDuration) {
        this.dataTracker.set(BASKING_DURATION, baskingDuration);
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public RiverTurtleVariant getVariant() {
        return RiverTurtleVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(RiverTurtleVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.tryBucket(player, hand, this)) {
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.APPLE);
    }

    public boolean isHiding() {
        return this.dataTracker.get(HIDING);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(RiverTurtleAnimationState.IDLING);
        super.onDeath(damageSource);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            RiverTurtleAnimationState animationState = RiverTurtleAnimationState.fromIndex(this.getState());
            this.stopAnimations();
            switch (animationState) {
                case HIDING -> this.hidingAnimationState.startIfNotRunning(this.age);
                case UNHIDING -> this.unhidingAnimationState.startIfNotRunning(this.age);
                case SIT_START -> this.sitStartAnimationState.startIfNotRunning(this.age);
                case SIT_END -> this.sitEndAnimationState.startIfNotRunning(this.age);
                default -> {
                }
            }

            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_RIVER_TURTLE_AMBIENT.get();
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 240;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_RIVER_TURTLE_HURT.get();
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 1.25f;
    }

    public void setIsHiding(boolean isHiding) {
        this.dataTracker.set(HIDING, isHiding);
    }

    public void startState(RiverTurtleAnimationState state) {
        switch (state) {
            case IDLING -> this.setState(RiverTurtleAnimationState.IDLING.getIndex());
            case HIDING -> this.setState(RiverTurtleAnimationState.HIDING.getIndex());
            case UNHIDING -> this.setState(RiverTurtleAnimationState.UNHIDING.getIndex());
            case SIT_START -> this.setState(RiverTurtleAnimationState.SIT_START.getIndex());
            case SIT_END -> this.setState(RiverTurtleAnimationState.SIT_END.getIndex());
        }
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient()) {
            boolean changed = animMachine.tick();
            RiverTurtleAnimationState current = animMachine.getCurrent();

            if (!changed) {
                RiverTurtleAnimationState tracked = RiverTurtleAnimationState.fromIndex(this.getState());
                if (tracked != current) {
                    animMachine.forceState(tracked);
                    current = tracked;
                }
            }

            if (current != lastSyncedAnimState) {
                this.setState(current.getIndex());
                lastSyncedAnimState = current;
            }

            if (this.getBaskingDuration() > 0) this.setBaskingDuration(this.getBaskingDuration() - 1);

            if (this.getHealth() < this.getMaxHealth()) {
                if (this.getHealth() <= (this.getMaxHealth() / 2)) {
                    this.setIsHiding(true);
                    canHealPassively = true;
                }

                if (canHealPassively) {
                    healthTicker++;

                    if (healthTicker >= 150 + random.nextInt(100)) {
                        this.heal(1);
                        healthTicker = 0;
                    }
                }
            } else {
                canHealPassively = false;
                this.setIsHiding(false);
            }
        }

        super.tick();
    }

    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.8, 0.95, 0.8));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    protected EntityNavigation createNavigation(World world) {
        return new RiverTurtleSwimNavigation(this, world);
    }

    @Override
    protected int getNextAirUnderwater(int air) {
        return air;
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.15f, 0.7F);
    }

    private int getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    private void setState(int state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    private void stopAnimations() {
        this.hidingAnimationState.stop();
        this.unhidingAnimationState.stop();
        this.sitStartAnimationState.stop();
        this.sitEndAnimationState.stop();
    }

    private boolean tryBucket(PlayerEntity player, Hand hand, RiverTurtleEntity entity) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == Items.WATER_BUCKET && entity.isAlive()) {
            entity.playSound(SoundEvents.ITEM_BUCKET_FILL_AXOLOTL, 1.0F, 0.75F);
            ItemStack bucketStack = new ItemStack(ModItems.RIVER_TURTLE_BUCKET.get());
            copyDataToStack(entity, bucketStack);
            ItemStack riverTurtleBucketStack = ItemUsage.exchangeStack(itemStack, player, bucketStack, false);
            player.swingHand(hand);
            player.setStackInHand(hand, riverTurtleBucketStack);

            entity.discard();
            return true;
        }
        return false;
    }

    private static class RiverTurtleMoveControl extends MoveControl {
        private final RiverTurtleEntity riverTurtle;

        public RiverTurtleMoveControl(RiverTurtleEntity riverTurtle) {
            super(riverTurtle);
            this.riverTurtle = riverTurtle;
        }

        public void tick() {
            if (this.riverTurtle.isTouchingWater()) {
                this.riverTurtle.setVelocity(this.riverTurtle.getVelocity().add(0.0, 0.005, 0.0));

                if (this.state != State.MOVE_TO || this.riverTurtle.getNavigation().isIdle()) {
                    this.riverTurtle.setMovementSpeed(0.0F);
                    return;
                }

                double d = this.targetX - this.riverTurtle.getX();
                double e = this.targetY - this.riverTurtle.getY();
                double f = this.targetZ - this.riverTurtle.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.riverTurtle.setYaw(this.wrapDegrees(this.riverTurtle.getYaw(), h, 90.0F));
                this.riverTurtle.bodyYaw = this.riverTurtle.getYaw();
                float i = (float) (this.speed * this.riverTurtle.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125F, this.riverTurtle.getMovementSpeed(), i);
                this.riverTurtle.setMovementSpeed(j);
                this.riverTurtle.setVelocity(this.riverTurtle.getVelocity().add(0, this.riverTurtle.getMovementSpeed() * e * 0.1, 0));
            } else {
                this.riverTurtle.setVelocity(this.riverTurtle.getVelocity().add(0.0, 0, 0.0));
                super.tick();
            }
        }
    }

    public boolean isSat() {
        return this.dataTracker.get(IS_SITTING);
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(IS_SITTING, sitting);
    }

    public void setupAnimationStateMachine() {
        animMachine.addTransition(
                RiverTurtleAnimationState.IDLING,
                RiverTurtleAnimationState.SIT_START,
                (e, s, age) -> e.isSat()
        );

        animMachine.addTransition(
                RiverTurtleAnimationState.SIT_START,
                RiverTurtleAnimationState.SIT_END,
                (e, s, age) -> !e.isSat()
        );

        animMachine.addTransition(
                RiverTurtleAnimationState.SIT_END,
                RiverTurtleAnimationState.IDLING,
                AnimationConditions.and(
                        (e, s, age) -> !e.isSat(),
                        AnimationConditions.timeAtLeast(5)
                )
        );
    }

    private static class RiverTurtleSwimNavigation extends AmphibiousSwimNavigation {
        RiverTurtleSwimNavigation(RiverTurtleEntity riverTurtleEntity, World world) {
            super(riverTurtleEntity, world);
        }

        public boolean isValidPosition(BlockPos pos) {
            if (this.entity instanceof RiverTurtleEntity) {
                return this.world.getBlockState(pos).isOf(Blocks.WATER) || (this.world.getBlockState(pos).isReplaceable() && this.world.getBlockState(pos.down()).isSolidBlock(this.world, pos.down()));
            }

            return !this.world.getBlockState(pos.down()).isAir();
        }
    }
}
