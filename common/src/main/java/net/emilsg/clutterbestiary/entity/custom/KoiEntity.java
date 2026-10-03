package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.KoiMateGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentFishEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.*;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.fish.AbstractFish;
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
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class KoiEntity extends ParentFishEntity {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.KELP);
    private static final float MIN_ADULT_SIZE = 0.8F;
    private static final float MAX_ADULT_SIZE = 1.2F;
    private static final float MIN_BRED_SIZE = 0.5F;
    private static final float MAX_BRED_SIZE = 1.5F;
    private static final float BRED_SIZE_VARIATION = 0.05F;

    private static final EntityDataAccessor<String> BASE_COLOR = SynchedEntityData.defineId(KoiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PRIMARY_PATTERN_COLOR = SynchedEntityData.defineId(KoiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PRIMARY_PATTERN_TYPE = SynchedEntityData.defineId(KoiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SECONDARY_PATTERN_COLOR = SynchedEntityData.defineId(KoiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SECONDARY_PATTERN_TYPE = SynchedEntityData.defineId(KoiEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> CHILD = SynchedEntityData.defineId(KoiEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState swimmingAnimationState = new AnimationState();
    protected int breedingAge;
    protected int forcedAge;
    protected int happyTicksRemaining;
    private int loveTicks;
    @Nullable
    private UUID lovingPlayer;
    private int swimmingAnimationTimeout = 0;
    // Size this koi grows into when inherited from bred parents; 0 means it rolls a wild size instead.
    private float inheritedAdultSize = 0.0F;

    public KoiEntity(EntityType<? extends AbstractFish> entityType, Level world) {
        super(entityType, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new KoiMateGoal(this, 1D, KoiEntity.class));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, BREEDING_INGREDIENT, false));
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

        if (!this.isBaby()) {
            this.randomizeAdultSize();
        }

        KoiBaseColorVariant base;
        KoiPrimaryPatternColorVariant primaryColor;
        KoiPrimaryPatternTypeVariant primaryType;
        KoiSecondaryPatternColorVariant secondaryColor;
        KoiSecondaryPatternTypeVariant secondaryType;

        do {
            base = KoiBaseColorVariant.getRandom();
            primaryColor = KoiPrimaryPatternColorVariant.getRandom();
            primaryType = KoiPrimaryPatternTypeVariant.getRandom();
            secondaryColor = KoiSecondaryPatternColorVariant.getRandom();
            secondaryType = KoiSecondaryPatternTypeVariant.getRandom();
        }
        while (!KoiVariantCompatibility.isValid(base, primaryColor, primaryType, secondaryColor, secondaryType));

        this.setBaseColorVariant(base);

        this.setPrimaryPatternColorVariant(primaryColor);
        this.setPrimaryPatternTypeVariant(primaryType);
        this.setSecondaryPatternColorVariant(secondaryColor);
        this.setSecondaryPatternTypeVariant(secondaryType);

        if (base.hasSeparateTexture()) {
            this.setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant.NONE);
            this.setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant.NONE);
        }

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BASE_COLOR, KoiBaseColorVariant.ORANGE.getID());
        builder.define(PRIMARY_PATTERN_COLOR, KoiPrimaryPatternColorVariant.WHITE.getID());
        builder.define(PRIMARY_PATTERN_TYPE, KoiPrimaryPatternTypeVariant.SPOTTED.getID());
        builder.define(SECONDARY_PATTERN_COLOR, KoiSecondaryPatternColorVariant.BLACK.getID());
        builder.define(SECONDARY_PATTERN_TYPE, KoiSecondaryPatternTypeVariant.SMALL_SPOTS.getID());
        builder.define(CHILD, false);
    }

    @Override
    public void loadFromBucketTag(CompoundTag nbt) {
        super.loadFromBucketTag(nbt);

        if (nbt.getFloat("Size").isPresent()) {
            this.setKoiSize(nbt.getFloatOr("Size", 1.0F));
        }

        this.inheritedAdultSize = nbt.getFloatOr("InheritedAdultSize", this.inheritedAdultSize);

        if (nbt.getString("BaseColor").isPresent()) {
            this.setBaseColorVariant(KoiBaseColorVariant.fromId(nbt.getStringOr("BaseColor", "")));
        }

        if (nbt.getString("PrimaryPatternColor").isPresent()) {
            this.setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant.fromId(nbt.getStringOr("PrimaryPatternColor", "")));
        }

        if (nbt.getString("PrimaryPatternType").isPresent()) {
            this.setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant.fromId(nbt.getStringOr("PrimaryPatternType", "")));
        }

        if (nbt.getString("SecondaryPatternColor").isPresent()) {
            this.setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant.fromId(nbt.getStringOr("SecondaryPatternColor", "")));
        }

        if (nbt.getString("SecondaryPatternType").isPresent()) {
            this.setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant.fromId(nbt.getStringOr("SecondaryPatternType", "")));
        }
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("BaseColor", this.getBaseColorVariant().getID());
        nbt.putString("PrimaryPatternColor", this.getPrimaryPatternColorVariant().getID());
        nbt.putString("PrimaryPatternType", this.getPrimaryPatternTypeVariant().getID());
        nbt.putString("SecondaryPatternColor", this.getSecondaryPatternColorVariant().getID());
        nbt.putString("SecondaryPatternType", this.getSecondaryPatternTypeVariant().getID());
        nbt.putInt("InLove", this.loveTicks);
        if (this.lovingPlayer != null) {
            nbt.store("LoveCause", UUIDUtil.CODEC, this.lovingPlayer);
        }
        nbt.putInt("Age", this.getBreedingAge());
        nbt.putInt("ForcedAge", this.forcedAge);
        nbt.putFloat("InheritedAdultSize", this.inheritedAdultSize);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setBaseColorVariant(KoiBaseColorVariant.fromId(nbt.getStringOr("BaseColor", "")));
        this.setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant.fromId(nbt.getStringOr("PrimaryPatternColor", "")));
        this.setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant.fromId(nbt.getStringOr("PrimaryPatternType", "")));
        this.setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant.fromId(nbt.getStringOr("SecondaryPatternColor", "")));
        this.setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant.fromId(nbt.getStringOr("SecondaryPatternType", "")));
        this.loveTicks = nbt.getIntOr("InLove", 0);
        this.lovingPlayer = nbt.read("LoveCause", UUIDUtil.CODEC).orElse(null);
        this.inheritedAdultSize = nbt.getFloatOr("InheritedAdultSize", 0.0F);
        this.setBreedingAge(nbt.getIntOr("Age", 0));
        this.forcedAge = nbt.getIntOr("ForcedAge", 0);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentFishEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 6D).add(Attributes.TEMPT_RANGE, 10.0);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterAnimal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.above()).is(ModBlockTags.KOI_SPAWN_ON);
    }

    public static int toGrowUpAge(int breedingAge) {
        return (int) ((float) (breedingAge / 20) * 0.1F);
    }

    public void breed(ServerLevel world, KoiEntity other) {
        this.breed(world, other, null);
    }

    public void breed(ServerLevel world, KoiEntity other, @Nullable KoiEntity baby) {
        this.setBreedingAge(6000);
        other.setBreedingAge(6000);

        KoiEggsEntity koiEggs = ModEntityTypes.KOI_EGGS.get().create(world, EntitySpawnReason.BREEDING);
        if (koiEggs == null) return;

        KoiBaseColorVariant base;
        KoiPrimaryPatternColorVariant primaryColor;
        KoiPrimaryPatternTypeVariant primaryType;
        KoiSecondaryPatternColorVariant secondaryColor;
        KoiSecondaryPatternTypeVariant secondaryType;

        do {
            base = getWeightedVariant(this, other, 40, 40, 20, KoiEntity::getBaseColorVariant, KoiBaseColorVariant::getRandom);
            primaryColor = getWeightedVariant(this, other, 40, 40, 20, KoiEntity::getPrimaryPatternColorVariant, KoiPrimaryPatternColorVariant::getRandom);
            primaryType = getWeightedVariant(this, other, 40, 40, 20, KoiEntity::getPrimaryPatternTypeVariant, KoiPrimaryPatternTypeVariant::getRandom);
            secondaryColor = getWeightedVariant(this, other, 40, 40, 20, KoiEntity::getSecondaryPatternColorVariant, KoiSecondaryPatternColorVariant::getRandom);
            secondaryType = getWeightedVariant(this, other, 40, 40, 20, KoiEntity::getSecondaryPatternTypeVariant, KoiSecondaryPatternTypeVariant::getRandom);
        } while (!KoiVariantCompatibility.isValid(base, primaryColor, primaryType, secondaryColor, secondaryType));

        koiEggs.setBaseColorVariant(base);
        koiEggs.setPrimaryPatternColorVariant(primaryColor);
        koiEggs.setPrimaryPatternTypeVariant(primaryType);
        koiEggs.setSecondaryPatternColorVariant(secondaryColor);
        koiEggs.setSecondaryPatternTypeVariant(secondaryType);
        koiEggs.setParentAverageSize((this.getKoiSize() + other.getKoiSize()) / 2.0F);

        koiEggs.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        world.addFreshEntity(koiEggs);

        this.resetLoveTicks();
        other.resetLoveTicks();
        world.broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);

        if (world.getGameRules().get(GameRules.MOB_DROPS)) {
            int xp = this.getRandom().nextInt(7) + 1;
            world.addFreshEntity(new ExperienceOrb(world, this.getX(), this.getY(), this.getZ(), xp));
        }
    }

    public boolean canBreedWith(KoiEntity other) {
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

    @Override
    public void saveToBucketTag(ItemStack stack) {
        super.saveToBucketTag(stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbt -> {
            nbt.putString("BaseColor", this.getBaseColorVariant().getID());
            nbt.putString("PrimaryPatternType", this.getPrimaryPatternTypeVariant().getID());
            nbt.putString("PrimaryPatternColor", this.getPrimaryPatternColorVariant().getID());
            nbt.putString("SecondaryPatternType", this.getSecondaryPatternTypeVariant().getID());
            nbt.putString("SecondaryPatternColor", this.getSecondaryPatternColorVariant().getID());
            nbt.putFloat("Size", this.getKoiSize());
            if (this.inheritedAdultSize > 0.0F) nbt.putFloat("InheritedAdultSize", this.inheritedAdultSize);
        });
    }

    @Nullable
    public KoiEntity createChild(ServerLevel world, KoiEntity entity) {
        return ModEntityTypes.KOI.get().create(world, EntitySpawnReason.BREEDING);
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

    public KoiBaseColorVariant getBaseColorVariant() {
        return KoiBaseColorVariant.fromId(this.entityData.get(BASE_COLOR));
    }

    public void setBaseColorVariant(KoiBaseColorVariant baseColorVariant) {
        this.entityData.set(BASE_COLOR, baseColorVariant.getID());
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

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.KOI_BUCKET.get());
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

    public KoiPrimaryPatternColorVariant getPrimaryPatternColorVariant() {
        return KoiPrimaryPatternColorVariant.fromId(this.entityData.get(PRIMARY_PATTERN_COLOR));
    }

    public void setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant primaryPatternColorVariant) {
        this.entityData.set(PRIMARY_PATTERN_COLOR, primaryPatternColorVariant.getID());
    }

    public KoiPrimaryPatternTypeVariant getPrimaryPatternTypeVariant() {
        return KoiPrimaryPatternTypeVariant.fromId(this.entityData.get(PRIMARY_PATTERN_TYPE));
    }

    public void setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant primaryPatternTypeVariant) {
        this.entityData.set(PRIMARY_PATTERN_TYPE, primaryPatternTypeVariant.getID());
    }

    public KoiSecondaryPatternColorVariant getSecondaryPatternColorVariant() {
        return KoiSecondaryPatternColorVariant.fromId(this.entityData.get(SECONDARY_PATTERN_COLOR));
    }

    public void setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant secondaryPatternColorVariant) {
        this.entityData.set(SECONDARY_PATTERN_COLOR, secondaryPatternColorVariant.getID());
    }

    public KoiSecondaryPatternTypeVariant getSecondaryPatternTypeVariant() {
        return KoiSecondaryPatternTypeVariant.fromId(this.entityData.get(SECONDARY_PATTERN_TYPE));
    }

    public void setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant secondaryPatternTypeVariant) {
        this.entityData.set(SECONDARY_PATTERN_TYPE, secondaryPatternTypeVariant.getID());
    }

    public <T> T getWeightedVariant(KoiEntity parent1, KoiEntity parent2, int parent1Weight, int parent2Weight, int randomWeight, Function<KoiEntity, T> getter, Supplier<T> randomSupplier) {
        int total = parent1Weight + parent2Weight + randomWeight;
        int decider = parent1.getRandom().nextInt(total);
        if (decider < parent1Weight) return getter.apply(parent1);
        if (decider < parent1Weight + parent2Weight) return getter.apply(parent2);
        return randomSupplier.get();
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

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (world.isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
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
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
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

        boolean isCapturingIridescentWhite = itemStack.is(Items.WATER_BUCKET)
                && this.getBaseColorVariant() == KoiBaseColorVariant.IRIDESCENT_WHITE;
        InteractionResult result = super.mobInteract(player, hand);

        if (isCapturingIridescentWhite && result.consumesAction() && player instanceof ServerPlayer serverPlayer) {
            ModAdvancements.grant(serverPlayer, ModAdvancements.PEARL_OF_THE_POND);
        }

        return result;
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        if (this.getBreedingAge() != 0) {
            this.loveTicks = 0;
        }

        super.customServerAiStep(serverLevel);
    }

    protected void onGrowUp() {
        if (!this.level().isClientSide()) {
            if (this.isBaby()) {
                this.setKoiSize(1.0F);
            } else if (this.inheritedAdultSize > 0.0F) {
                this.setKoiSize(this.inheritedAdultSize);
                this.inheritedAdultSize = 0.0F;
            } else {
                this.randomizeAdultSize();
            }
        }

        if (!this.isBaby() && this.isPassenger()) {
            Entity var2 = this.getVehicle();
            if (var2 instanceof Boat boatEntity) {
                if (!boatEntity.hasEnoughSpaceFor(this)) {
                    this.stopRiding();
                }
            }
        }

    }

    /**
     * Adult koi vary in size; the size is stored in the SCALE attribute, which is saved and synced to the client.
     */
    private void randomizeAdultSize() {
        this.setKoiSize(MIN_ADULT_SIZE + this.random.nextFloat() * (MAX_ADULT_SIZE - MIN_ADULT_SIZE));
    }

    /**
     * Rolls the adult size of a bred koi: the parents' average size, plus or minus a small variation, so selective
     * breeding can push koi beyond the wild size range.
     */
    public void inheritAdultSize(float parentAverageSize) {
        float variation = (this.random.nextFloat() * 2.0F - 1.0F) * BRED_SIZE_VARIATION;
        this.inheritedAdultSize = Mth.clamp(parentAverageSize + variation, MIN_BRED_SIZE, MAX_BRED_SIZE);
    }

    public float getKoiSize() {
        AttributeInstance scale = this.getAttribute(Attributes.SCALE);
        return scale != null ? (float) scale.getBaseValue() : 1.0F;
    }

    public void setKoiSize(float size) {
        AttributeInstance scale = this.getAttribute(Attributes.SCALE);
        if (scale == null) return;
        scale.setBaseValue(size);
        this.refreshDimensions();
    }

    private void setupAnimationStates() {
        if (this.swimmingAnimationTimeout <= 0) {
            this.swimmingAnimationTimeout = 20;
            this.swimmingAnimationState.start(this.tickCount);
        } else {
            --this.swimmingAnimationTimeout;
        }
    }

}
