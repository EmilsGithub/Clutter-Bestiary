package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentFishEntity;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SeahorseEntity extends ParentFishEntity implements Bucketable {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.SEA_PICKLE);
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(SeahorseEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> HAS_CHILDREN = SynchedEntityData.defineId(SeahorseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> HAS_CHILDREN_TIMER = SynchedEntityData.defineId(SeahorseEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> CHILD = SynchedEntityData.defineId(SeahorseEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState swimmingAnimationState = new AnimationState();
    public final AnimationState flopAnimationState = new AnimationState();
    protected int breedingAge;
    protected int forcedAge;
    protected int happyTicksRemaining;
    private int ticker = 0;
    private int loveTicks;
    @Nullable
    private UUID lovingPlayer;
    private int swimmingAnimationTimeout = 0;
    private int flopAnimationTimeout = 0;

    public SeahorseEntity(EntityType<? extends AbstractFish> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        if (entityData == null) {
            entityData = new AgeableMob.AgeableMobGroupData(true);
        }

        AgeableMob.AgeableMobGroupData passiveData = (AgeableMob.AgeableMobGroupData) entityData;
        if (passiveData.isShouldSpawnBaby() && passiveData.getGroupSize() > 0 && world.getRandom().nextFloat() <= passiveData.getBabySpawnChance()) {
            this.setBreedingAge(-24000);
        }

        passiveData.increaseGroupSizeByOne();
        SeahorseVariant variant = SeahorseVariant.getRandom();
        this.setVariant(variant);

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, SeahorseVariant.YELLOW.getID());
        builder.define(HAS_CHILDREN, false);
        builder.define(HAS_CHILDREN_TIMER, 0.0f);
        builder.define(CHILD, false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 0.7D));
        this.goalSelector.addGoal(1, new SeahorseFollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new SeahorseMateGoal(this, 1D, SeahorseEntity.class));
        this.goalSelector.addGoal(3, new SeahorseReleaseChildrenGoal(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(5, new SwimToRandomPlaceGoal(this, 0.5D));
        this.goalSelector.addGoal(6, new SeahorseMoveToCoralGoal(this, 0.5D, 8));
    }

    @Override
    public void loadFromBucketTag(CompoundTag nbt) {
        if (nbt.contains("Variant")) {
            this.setVariant(SeahorseVariant.fromId(nbt.getStringOr("Variant", "")));
        }

        super.loadFromBucketTag(nbt);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
        this.entityData.set(HAS_CHILDREN, nbt.getBooleanOr("HasChildren", false));
        this.entityData.set(HAS_CHILDREN_TIMER, nbt.getFloatOr("HasChildrenTimer", 0.0F));
        this.loveTicks = nbt.getIntOr("InLove", 0);
        this.lovingPlayer = nbt.read("LoveCause", UUIDUtil.CODEC).orElse(null);
        this.setBreedingAge(nbt.getIntOr("Age", 0));
        this.forcedAge = nbt.getIntOr("ForcedAge", 0);
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("HasChildren", this.hasChildren());
        nbt.putFloat("HasChildrenTimer", this.getHasChildrenTimer());
        nbt.putInt("InLove", this.loveTicks);
        if (this.lovingPlayer != null) {
            nbt.store("LoveCause", UUIDUtil.CODEC, this.lovingPlayer);
        }
        nbt.putInt("Age", this.getBreedingAge());
        nbt.putInt("ForcedAge", this.forcedAge);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return SeahorseEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 2D).add(Attributes.TEMPT_RANGE, 10.0);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterAnimal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos).is(ModBlockTags.SEAHORSES_SPAWN_ON);
    }

    public static int toGrowUpAge(int breedingAge) {
        return (int) ((float) (breedingAge / 20) * 0.1F);
    }

    public void breed(ServerLevel world, SeahorseEntity other) {
        this.breed(world, other, null);
    }

    public void breed(ServerLevel world, SeahorseEntity other, @Nullable SeahorseEntity baby) {
        this.setBreedingAge(6000);
        other.setBreedingAge(6000);
        this.setHasChildren(true);
        this.resetLoveTicks();
        other.resetLoveTicks();
        world.broadcastEntityEvent(this, (byte) 18);
        if (world.getGameRules().get(GameRules.MOB_DROPS)) {
            world.addFreshEntity(new ExperienceOrb(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }

    }

    public boolean canBreedWith(SeahorseEntity other) {
        if (other == this) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            return this.isInLove() && other.isInLove();
        }
    }

    public boolean canEat() {
        return this.loveTicks <= 0;
    }

    public boolean removeWhenFarAway(double distanceSquared) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        super.saveToBucketTag(stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, (nbtCompound) -> nbtCompound.putString("Variant", this.getTypeVariant()));
    }

    @Nullable
    public SeahorseEntity createChild(ServerLevel world, SeahorseEntity entity) {
        return ModEntityTypes.SEAHORSE.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (this.isInvulnerableTo(serverLevel, source)) {
            return false;
        } else {
            this.loveTicks = 0;
            return super.hurtServer(serverLevel, source, amount);
        }
    }

    public int getBreedingAge() {
        if (this.level().isClientSide()) {
            return this.entityData.get(CHILD) ? -1 : 1;
        } else {
            return this.breedingAge;
        }
    }

    public void setBreedingAge(int age) {
        int i = this.getBreedingAge();
        this.breedingAge = age;
        if (i < 0 && age >= 0 || i >= 0 && age < 0) {
            this.entityData.set(CHILD, age < 0);
            this.onGrowUp();
        }

    }

    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SEAHORSE_BUCKET.get());
    }

    public float getHasChildrenTimer() {
        return this.entityData.get(HAS_CHILDREN_TIMER);
    }

    public void setHasChildrenTimer(float hasChildrenTimer) {
        this.entityData.set(HAS_CHILDREN_TIMER, hasChildrenTimer);
    }

    public int getLoveTicks() {
        return this.loveTicks;
    }

    public void setLoveTicks(int loveTicks) {
        this.loveTicks = loveTicks;
    }

    @Nullable
    public ServerPlayer getLovingPlayer() {
        if (this.lovingPlayer == null) {
            return null;
        } else {
            Player playerEntity = this.level().getPlayerByUUID(this.lovingPlayer);
            return playerEntity instanceof ServerPlayer ? (ServerPlayer) playerEntity : null;
        }
    }

    public int getMaxChildren() {
        return 3;
    }

    public SeahorseVariant getVariant() {
        return SeahorseVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(SeahorseVariant variant) {
        this.entityData.set(VARIANT, variant.getID());
    }

    public void growUp(int age, boolean overGrow) {
        int i = this.getBreedingAge();
        int j = i;
        i += age * 20;
        if (i > 0) {
            i = 0;
        }

        int k = i - j;
        this.setBreedingAge(i);
        if (overGrow) {
            this.forcedAge += k;
            if (this.happyTicksRemaining == 0) {
                this.happyTicksRemaining = 40;
            }
        }

        if (this.getBreedingAge() == 0) {
            this.setBreedingAge(this.forcedAge);
        }

    }

    public void growUp(int age) {
        this.growUp(age, false);
    }

    public void handleEntityEvent(byte status) {
        if (status == 18) {
            for (int i = 0; i < 7; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d, e, f);
            }
        } else {
            super.handleEntityEvent(status);
        }

    }

    public boolean hasChildren() {
        return this.entityData.get(HAS_CHILDREN);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.isBreedingItem(itemStack)) {
            int i = this.getBreedingAge();
            if (!this.level().isClientSide() && i == 0 && this.canEat()) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                if (this.level().isClientSide()) return InteractionResult.CONSUME;
                this.eat(player, hand, itemStack);
                this.growUp(toGrowUpAge(-i), true);
                return InteractionResult.SUCCESS;
            }

            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            }
        }

        return super.mobInteract(player, hand);
    }

    public boolean isBaby() {
        return this.getBreedingAge() < 0;
    }

    public void setBaby(boolean baby) {
        this.setBreedingAge(baby ? -24000 : 0);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isInLove() {
        return this.getLoveTicks() > 0;
    }

    public boolean isReadyToBreed() {
        return false;
    }

    public void lovePlayer(@Nullable Player player) {
        this.loveTicks = 600;
        if (player != null) {
            this.lovingPlayer = player.getUUID();
        }

        this.level().broadcastEntityEvent(this, (byte) 18);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (CHILD.equals(data)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(data);
    }

    public void resetLoveTicks() {
        this.setLoveTicks(0);
    }

    public void setHasChildren(boolean hasChildren) {
        this.entityData.set(HAS_CHILDREN, hasChildren);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (!world.isClientSide()) {
            ticker++;
            if (this.hasChildren() && ticker == 120) {
                this.setHasChildrenTimer(this.getHasChildrenTimer() + 0.20f);
                ticker = 0;
            }
        }

        if (world.isClientSide()) {
            this.setupAnimationStates();
        }
    }

    public void aiStep() {
        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645, ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.setOnGround(false);
            this.needsSync = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }

        if (this.getBreedingAge() != 0) {
            this.loveTicks = 0;
        }

        if (this.loveTicks > 0) {
            --this.loveTicks;
            if (this.loveTicks % 10 == 0) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d, e, f);
            }
        }

        if (this.level().isClientSide()) {
            if (this.happyTicksRemaining > 0) {
                if (this.happyTicksRemaining % 4 == 0) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                }

                --this.happyTicksRemaining;
            }
        } else if (this.isAlive()) {
            int i = this.getBreedingAge();
            if (i < 0) {
                ++i;
                this.setBreedingAge(i);
            } else if (i > 0) {
                --i;
                this.setBreedingAge(i);
            }
        }

        super.aiStep();
    }

    protected void eat(Player player, InteractionHand hand, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        if (this.getBreedingAge() != 0) {
            this.loveTicks = 0;
        }

        super.customServerAiStep(serverLevel);
    }

    protected void onGrowUp() {
        if (!this.isBaby() && this.isPassenger()) {
            Entity var2 = this.getVehicle();
            if (var2 instanceof Boat boatEntity) {
                if (!boatEntity.hasEnoughSpaceFor(this)) {
                    this.stopRiding();
                }
            }
        }

    }

    private String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    private void setupAnimationStates() {
        if (this.swimmingAnimationTimeout <= 0) {
            this.swimmingAnimationTimeout = 20;
            this.swimmingAnimationState.start(this.tickCount);
        } else {
            --this.swimmingAnimationTimeout;
        }

        if (this.flopAnimationTimeout <= 0) {
            this.flopAnimationTimeout = 20;
            this.flopAnimationState.start(this.tickCount);
        } else {
            --this.flopAnimationTimeout;
        }
    }
}
