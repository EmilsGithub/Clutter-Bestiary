package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.custom.goal.TamedEscapeDangerGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractNetherNewtEntity extends ParentTameableEntity implements Angerable {
    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(AbstractNetherNewtEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    private static final TrackedData<Boolean> MOVING = DataTracker.registerData(AbstractNetherNewtEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> SIZE = DataTracker.registerData(AbstractNetherNewtEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> FUNGI = DataTracker.registerData(AbstractNetherNewtEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(AbstractNetherNewtEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();

    int ticker = 6000;
    @Nullable
    private UUID angryAt;
    private int idleAnimationTimeout = 0;

    public AbstractNetherNewtEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f);
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        AbstractNetherNewtEntity netherNewtEntity = (AbstractNetherNewtEntity) this.createChild(world, other);

        if (netherNewtEntity == null) return;

        float scaledSize;
        switch (random.nextInt(3) + 1) {
            case 2 -> scaledSize = 1.25f;
            case 3 -> scaledSize = 1.5f;
            default -> scaledSize = 1;
        }

        netherNewtEntity.setBaby(true);
        netherNewtEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
        netherNewtEntity.setNewtSize(scaledSize);
        netherNewtEntity.setFungiCount(random.nextInt(5) + 1);
        this.breed(world, other, netherNewtEntity);
        world.spawnEntityAndPassengers(netherNewtEntity);
    }

    public boolean canBeLeashedBy(PlayerEntity player) {
        return !this.isSitting() && this.isOwner(player);
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        super.playHurtSound(source);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        if (other == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(other instanceof AbstractNetherNewtEntity netherNewtEntity)) {
            return false;
        } else {
            if (!netherNewtEntity.isTamed()) {
                return false;
            } else if (netherNewtEntity.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && netherNewtEntity.isInLove();
            }
        }
    }

    @Override
    public abstract @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity);

    @Override
    public float getSoundPitch() {
        float multiplier = this.getNewtSize() > 1.0f ? 1.05f : 1.2f;
        return this.isBaby() ? ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F) * multiplier : ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * multiplier;
    }

    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return true;
    }

    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public double getEyeY() {
        return super.getEyeY() * 0.4f;
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.getWorld(), nbt);
        this.setNewtSize(nbt.getFloat("Size"));
        this.setFungiCount(nbt.getInt("Fungi"));
        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
        this.navigation.stop();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity attacker && attacker.getWorld() instanceof ServerWorld && !this.isBaby() && this.getFungiCount() >= 1 && getOnAttackEffect() != null) {
            if (random.nextInt(8) == 0) {
                attacker.addStatusEffect(new StatusEffectInstance(getOnAttackEffect(), getOnAttackEffect() == StatusEffects.POISON ? 100 : 200, 1), this);
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public int getAngerTime() {
        return this.dataTracker.get(ANGER_TIME);
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.dataTracker.set(ANGER_TIME, angerTime);
    }

    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    public abstract Item getBreedingItem();

    @Nullable
    public abstract RegistryEntry<StatusEffect> getOnAttackEffect();

    public int getFungiCount() {
        return this.dataTracker.get(FUNGI);
    }

    public void setFungiCount(int count) {
        this.dataTracker.set(FUNGI, MathHelper.clamp(count, 0, 5));
    }

    public float getNewtSize() {
        return this.dataTracker.get(SIZE);
    }

    public void setNewtSize(float size) {
        this.dataTracker.set(SIZE, MathHelper.clamp(size, 0f, 1.5f));
        this.calculateDimensions();
    }

    @Override
    public float getScale() {
        AttributeContainer attributeContainer = this.getAttributes();
        return attributeContainer == null ? 0.65f * this.getNewtSize() : this.clampScale((float)attributeContainer.getValue(EntityAttributes.GENERIC_SCALE) * (0.65f * this.getNewtSize()));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
        nbt.putFloat("Size", this.getNewtSize());
        nbt.putInt("Fungi", this.getFungiCount());
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        float scaledSize;
        switch (random.nextInt(3) + 1) {
            case 2 -> scaledSize = 1.25f;
            case 3 -> scaledSize = 1.5f;
            default -> scaledSize = 1;
        }
        this.setNewtSize(scaledSize);
        this.setFungiCount(random.nextInt(5) + 1);

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        World world = this.getWorld();
        BlockPos pos = this.getBlockPos();
        int fungiCount = this.getFungiCount();

        if (stackInHand.getItem() instanceof ShearsItem && !world.isClient && fungiCount != 0) {
            if (!player.getAbilities().creativeMode) stackInHand.damage(1, player, LivingEntity.getSlotForHand(hand));
            this.dropStack(new ItemStack(this.getFungusItem(), fungiCount));
            world.playSound(null, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.setFungiCount(0);
            return ActionResult.SUCCESS;
        }

        if (this.isBreedingItem(stackInHand) && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().creativeMode) {
                stackInHand.decrementUnlessCreative(1, player);
            }
            FoodComponent foodComponent = stackInHand.get(DataComponentTypes.FOOD);
            float nutrition = foodComponent != null ? (float)foodComponent.nutrition() : 1.0F;
            this.heal(2.0F * nutrition);
            return ActionResult.SUCCESS;
        }

        if (this.isTamingItem(stackInHand) && !isTamed()) {
            this.playSound(SoundEvents.ENTITY_STRIDER_EAT, 1.0F, 1.5F);
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
                    this.setTamed(true, true);
                    this.setTarget(null);
                    this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                    setSit(true);
                } else {
                    this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
                }

                return ActionResult.SUCCESS;
            }
        }

        if (isTamed() && !this.getWorld().isClient() && hand == Hand.MAIN_HAND && isOwner(player) && !isBreedingItem(stackInHand)) {
            setSit(!isSitting());
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_NETHER_NEWT_AMBIENT;
    }

    protected abstract Item getTamingItem();


    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(this.getBreedingItem());
    }

    public boolean isMoving() {
        return this.dataTracker.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.dataTracker.set(MOVING, moving);
    }

    public boolean isUniversallyAngry(World world) {
        return world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) && this.hasAngerTime() && this.getAngryAt() == null;
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (SIZE.equals(data)) {
            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANGER_TIME, 0);
        builder.add(SIZE, 0f);
        builder.add(MOVING, false);
        builder.add(FUNGI, 1);
        builder.add(SITTING, false);
    }

    @Override
    public void setBaby(boolean baby) {
        float scaledSize;
        switch (random.nextInt(3) + 1) {
            case 2 -> scaledSize = 1.25f;
            case 3 -> scaledSize = 1.5f;
            default -> scaledSize = 1;
        }
        this.setNewtSize(scaledSize);
        this.setFungiCount(random.nextInt(5) + 1);
        super.setBaby(baby);
    }

    public boolean shouldAngerAt(LivingEntity entity) {
        if (!this.canTarget(entity)) {
            return false;
        } else {
            return entity.getType() == EntityType.PLAYER && this.isUniversallyAngry(entity.getWorld()) || entity.getUuid().equals(this.getAngryAt());
        }
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (!world.isClient) {
            ticker--;

            if (ticker <= 0 && this.getFungiCount() != 5) {
                this.setFungiCount(this.getFungiCount() + 1);
                ticker = 6000;
            }
        }

        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient) {
            BlockPos oldPos = this.getBlockPos();
            BlockPos newPos = this.getBlockPos();
            this.setMoving(oldPos != newPos);
            this.tickAngerLogic((ServerWorld) this.getWorld(), true);
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TamedEscapeDangerGoal(this, 1.5D));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(2, new PounceAtTargetGoal(this, 0.4f));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.add(4, new AnimalMateGoal(this, 1.2f));
        this.goalSelector.add(5, new TemptGoal(this, 1.2f, this.getBreedingIngredient(), false));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.2, 10.0F, 2.0F));
        this.goalSelector.add(7, new FollowParentGoal(this, 1.2f));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0f));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new UniversalAngerGoal<>(this, true));
    }

    @Override
    protected void updateAttributesForTamed() {
        Objects.requireNonNull(getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(20.0D);
        Objects.requireNonNull(getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).setBaseValue(6.0f);
    }

    protected abstract Item getFungusItem();

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_NETHER_NEWT_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        BlockSoundGroup blockSoundGroup = state.getSoundGroup();
        this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.05F, blockSoundGroup.getPitch());
    }

    private boolean isTamingItem(ItemStack itemStack) {
        return itemStack.isOf(this.getTamingItem());
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f, 0.2F);
    }

    private Ingredient getBreedingIngredient() {
        return Ingredient.ofItems(getBreedingItem());
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && !this.isMoving()) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isSitting() && !this.sittingAnimationState.isRunning()) {
            this.sittingAnimationState.start(this.age);
        } else if (!this.isSitting()) {
            this.sittingAnimationState.stop();
        }
    }
}
