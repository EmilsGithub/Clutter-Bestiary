package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.custom.goal.TamedEscapeDangerGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractNetherNewtEntity extends ParentTameableEntity implements NeutralMob {
    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(AbstractNetherNewtEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FUNGI = SynchedEntityData.defineId(AbstractNetherNewtEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(AbstractNetherNewtEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();

    int ticker = 6000;
    private long persistentAngerEndTime = NO_ANGER_END_TIME;
    @Nullable
    private EntityReference<LivingEntity> persistentAngerTarget;
    private int idleAnimationTimeout = 0;

    public AbstractNetherNewtEntity(EntityType<? extends ParentTameableEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        float scaledSize;
        switch (random.nextInt(3) + 1) {
            case 2 -> scaledSize = 1f;
            case 3 -> scaledSize = 1.25f;
            default -> scaledSize = 0.85f;
        }
        this.setNewtSize(scaledSize);
        this.reapplyPosition();
        this.refreshDimensions();
        this.setFungiCount(random.nextInt(5) + 1);

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SIZE, 1f);
        builder.define(FUNGI, 1);
        builder.define(SITTING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new TamedEscapeDangerGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.2f));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.2f, this.getBreedingIngredient(), false));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.2, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.2f));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0f));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.readPersistentAngerSaveData(this.level(), nbt);
        this.setNewtSize(nbt.getFloatOr("Size", 0.0F));
        this.setFungiCount(nbt.getIntOr("Fungi", 0));
        this.entityData.set(SITTING, nbt.getBooleanOr("isSitting", false));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        this.addPersistentAngerSaveData(nbt);
        nbt.putFloat("Size", this.getNewtSize());
        nbt.putInt("Fungi", this.getFungiCount());
        nbt.putBoolean("isSitting", this.entityData.get(SITTING));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_DAMAGE, 3.0f);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        AbstractNetherNewtEntity netherNewtEntity = (AbstractNetherNewtEntity) this.getBreedOffspring(world, other);

        if (netherNewtEntity == null) return;

        float scaledSize;
        switch (random.nextInt(3) + 1) {
            case 2 -> scaledSize = 1f;
            case 3 -> scaledSize = 1.25f;
            default -> scaledSize = 0.85f;
        }

        netherNewtEntity.setBaby(true);
        netherNewtEntity.setNewtSize(scaledSize);
        netherNewtEntity.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
        netherNewtEntity.setFungiCount(random.nextInt(5) + 1);
        this.finalizeSpawnChildFromBreeding(world, other, netherNewtEntity);
        world.addFreshEntityWithPassengers(netherNewtEntity);
    }

    @Override
    public boolean canMate(Animal other) {
        if (other == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(other instanceof AbstractNetherNewtEntity netherNewtEntity)) {
            return false;
        } else {
            if (!netherNewtEntity.isTame()) {
                return false;
            } else if (netherNewtEntity.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && netherNewtEntity.isInLove();
            }
        }
    }

    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason spawnReason) {
        return true;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public abstract @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity);

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        this.persistentAngerEndTime = endTime;
    }

    @Nullable
    @Override
    public EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> angryAt) {
        this.persistentAngerTarget = angryAt;
    }

    public abstract Item getBreedingItem();

    public int getFungiCount() {
        return this.entityData.get(FUNGI);
    }

    public void setFungiCount(int count) {
        this.entityData.set(FUNGI, Mth.clamp(count, 0, 5));
    }

    public float getNewtSize() {
        return this.entityData.get(SIZE);
    }

    public void setNewtSize(float size) {
        this.entityData.set(SIZE, size);
        Objects.requireNonNull(getAttribute(Attributes.SCALE)).setBaseValue(size);
        this.reapplyPosition();
        this.refreshDimensions();
    }

    @Nullable
    public abstract Holder<MobEffect> getOnAttackEffect();

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity target) {
        if (!super.doHurtTarget(serverLevel, target)) return false;
        Holder<MobEffect> effect = this.getOnAttackEffect();
        if (effect != null && target instanceof LivingEntity livingEntity && !this.level().isClientSide()) {
            livingEntity.addEffect(new MobEffectInstance(effect, 100), this);
        }
        return true;
    }

    @Override
    public float getVoicePitch() {
        float multiplier = this.getNewtSize() > 1.0f ? 1.05f : 1.2f;
        return this.isBaby() ? ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F) * multiplier : ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * multiplier;
    }

    public abstract Item getTamingItem();

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        Level world = this.level();
        BlockPos pos = this.blockPosition();
        int fungiCount = this.getFungiCount();

        if (stackInHand.getItem() instanceof ShearsItem && !world.isClientSide() && fungiCount != 0) {
            if (!player.getAbilities().instabuild) stackInHand.hurtAndBreak(1, player, hand.asEquipmentSlot());
            this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(this.getFungusItem(), fungiCount));
            world.playSound(null, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.setFungiCount(0);
            return InteractionResult.SUCCESS;
        }

        if (this.isFood(stackInHand) && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().instabuild) {
                stackInHand.consume(1, player);
            }
            FoodProperties foodComponent = stackInHand.get(DataComponents.FOOD);
            float nutrition = foodComponent != null ? (float) foodComponent.nutrition() : 1.0F;
            this.heal(2.0F * nutrition);
            return InteractionResult.SUCCESS;
        }

        if (this.isTamingItem(stackInHand) && !isTame()) {
            this.playSound(SoundEvents.STRIDER_EAT, 1.0F, 1.5F);
            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    stackInHand.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !this.level().isClientSide()) {
                    super.tame(player);
                    this.navigation.recomputePath();
                    this.setHealth(this.getMaxHealth());
                    this.setTame(true, true);
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
                    setSit(true);
                } else {
                    this.level().broadcastEntityEvent(this, EntityEvent.TAMING_FAILED);
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (isTame() && !this.level().isClientSide() && hand == InteractionHand.MAIN_HAND && isOwnedBy(player) && !isFood(stackInHand)) {
            setSit(!isOrderedToSit());
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(this.getBreedingItem());
    }

    public boolean isOrderedToSit() {
        return this.entityData.get(SITTING);
    }

    // AgeableMob#setBaby is final now; babies get their random traits when their age first turns negative.
    @Override
    public void setAge(int age) {
        if (age < 0 && this.getAge() >= 0 && !this.level().isClientSide()) {
            float scaledSize;
            switch (random.nextInt(3) + 1) {
                case 2 -> scaledSize = 1.25f;
                case 3 -> scaledSize = 1.5f;
                default -> scaledSize = 1;
            }
            this.setNewtSize(scaledSize);
            this.setFungiCount(random.nextInt(5) + 1);
        }
        super.setAge(age);
    }

    public void setSit(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        super.setOrderedToSit(sitting);
        this.navigation.stop();
    }

    public boolean isAngryAt(LivingEntity entity) {
        return this.level() instanceof ServerLevel serverLevel && this.isAngryAt(entity, serverLevel);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (!world.isClientSide()) {
            ticker--;

            if (ticker <= 0 && this.getFungiCount() != 5) {
                this.setFungiCount(this.getFungiCount() + 1);
                ticker = 6000;
            }
        }

        if (world.isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_NETHER_NEWT_AMBIENT.get();
    }

    protected abstract Item getFungusItem();

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_NETHER_NEWT_HURT.get();
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        super.playHurtSound(source);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        SoundType blockSoundGroup = state.getSoundType();
        this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.05F, blockSoundGroup.getPitch());
    }

    @Override
    protected void applyTamingSideEffects() {
        Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0D);
        Objects.requireNonNull(getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(6.0f);
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f, 0.2F, 1.0F);
    }

    private Ingredient getBreedingIngredient() {
        return Ingredient.of(getBreedingItem());
    }

    private boolean isTamingItem(ItemStack itemStack) {
        return itemStack.is(this.getTamingItem());
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && !this.isMoving()) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isOrderedToSit() && !this.sittingAnimationState.isStarted()) {
            this.sittingAnimationState.start(this.tickCount);
        } else if (!this.isOrderedToSit()) {
            this.sittingAnimationState.stop();
        }
    }
}
