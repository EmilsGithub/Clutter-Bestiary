package net.emilsg.clutter_bestiary.entity.custom;

import net.emilsg.clutter_bestiary.entity.ModEntityTypes;
import net.emilsg.clutter_bestiary.entity.custom.goal.KoiMateGoal;
import net.emilsg.clutter_bestiary.entity.custom.parent.ParentFishEntity;
import net.emilsg.clutter_bestiary.entity.variants.koi.*;
import net.emilsg.clutter_bestiary.item.ModItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class KoiEntity extends ParentFishEntity {

    private static final TrackedData<String> BASE_COLOR = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PRIMARY_PATTERN_COLOR = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PRIMARY_PATTERN_TYPE = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SECONDARY_PATTERN_COLOR = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SECONDARY_PATTERN_TYPE = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> CHILD = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected int breedingAge;
    protected int forcedAge;
    protected int happyTicksRemaining;
    private int loveTicks;
    private final Item breedingItem = Items.KELP;
    @Nullable
    private UUID lovingPlayer;

    public final AnimationState swimmingAnimationState = new AnimationState();
    private int swimmingAnimationTimeout = 0;

    public KoiEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BASE_COLOR, KoiBaseColorVariant.ORANGE.getID());
        this.dataTracker.startTracking(PRIMARY_PATTERN_COLOR, KoiPrimaryPatternColorVariant.WHITE.getID());
        this.dataTracker.startTracking(PRIMARY_PATTERN_TYPE, KoiPrimaryPatternTypeVariant.SPOTTED.getID());
        this.dataTracker.startTracking(SECONDARY_PATTERN_COLOR, KoiSecondaryPatternColorVariant.BLACK.getID());
        this.dataTracker.startTracking(SECONDARY_PATTERN_TYPE, KoiSecondaryPatternTypeVariant.SMALL_SPOTS.getID());
        this.dataTracker.startTracking(CHILD, false);
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new KoiMateGoal(this, 1D, KoiEntity.class));
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.KELP), false));
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.swimmingAnimationTimeout <= 0) {
            this.swimmingAnimationTimeout = 20;
            this.swimmingAnimationState.start(this.age);
        } else {
            --this.swimmingAnimationTimeout;
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("BaseColor", this.getBaseColorVariant().getID());
        nbt.putString("PrimaryPatternColor", this.getPrimaryPatternColorVariant().getID());
        nbt.putString("PrimaryPatternType", this.getPrimaryPatternTypeVariant().getID());
        nbt.putString("SecondaryPatternColor", this.getSecondaryPatternColorVariant().getID());
        nbt.putString("SecondaryPatternType", this.getSecondaryPatternTypeVariant().getID());
        nbt.putInt("InLove", this.loveTicks);
        if (this.lovingPlayer != null) {
            nbt.putUuid("LoveCause", this.lovingPlayer);
        }
        nbt.putInt("Age", this.getBreedingAge());
        nbt.putInt("ForcedAge", this.forcedAge);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBaseColorVariant(KoiBaseColorVariant.fromId(nbt.getString("BaseColor")));
        this.setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant.fromId(nbt.getString("PrimaryPatternColor")));
        this.setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant.fromId(nbt.getString("PrimaryPatternType")));
        this.setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant.fromId(nbt.getString("SecondaryPatternColor")));
        this.setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant.fromId(nbt.getString("SecondaryPatternType")));
        this.loveTicks = nbt.getInt("InLove");
        this.lovingPlayer = nbt.containsUuid("LoveCause") ? nbt.getUuid("LoveCause") : null;
        this.setBreedingAge(nbt.getInt("Age"));
        this.forcedAge = nbt.getInt("ForcedAge");
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(true);
        }

        PassiveEntity.PassiveData passiveData = (PassiveEntity.PassiveData) entityData;
        if (passiveData.canSpawnBaby() && passiveData.getSpawnedCount() > 0 && world.getRandom().nextFloat() <= passiveData.getBabyChance()) {
            this.setBreedingAge(-24000);
        }

        passiveData.countSpawned();

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

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public KoiBaseColorVariant getBaseColorVariant() {
        return KoiBaseColorVariant.fromId(this.dataTracker.get(BASE_COLOR));
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SALMON_DEATH;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SALMON_HURT;
    }

    public void setBaseColorVariant(KoiBaseColorVariant baseColorVariant) {
        this.dataTracker.set(BASE_COLOR, baseColorVariant.getID());
    }

    public KoiPrimaryPatternColorVariant getPrimaryPatternColorVariant() {
        return KoiPrimaryPatternColorVariant.fromId(this.dataTracker.get(PRIMARY_PATTERN_COLOR));
    }

    public void setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant primaryPatternColorVariant) {
        this.dataTracker.set(PRIMARY_PATTERN_COLOR, primaryPatternColorVariant.getID());
    }

    public KoiPrimaryPatternTypeVariant getPrimaryPatternTypeVariant() {
        return KoiPrimaryPatternTypeVariant.fromId(this.dataTracker.get(PRIMARY_PATTERN_TYPE));
    }

    public void setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant primaryPatternTypeVariant) {
        this.dataTracker.set(PRIMARY_PATTERN_TYPE, primaryPatternTypeVariant.getID());
    }

    public KoiSecondaryPatternColorVariant getSecondaryPatternColorVariant() {
        return KoiSecondaryPatternColorVariant.fromId(this.dataTracker.get(SECONDARY_PATTERN_COLOR));
    }

    public void setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant secondaryPatternColorVariant) {
        this.dataTracker.set(SECONDARY_PATTERN_COLOR, secondaryPatternColorVariant.getID());
    }

    public KoiSecondaryPatternTypeVariant getSecondaryPatternTypeVariant() {
        return KoiSecondaryPatternTypeVariant.fromId(this.dataTracker.get(SECONDARY_PATTERN_TYPE));
    }

    public void setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant secondaryPatternTypeVariant) {
        this.dataTracker.set(SECONDARY_PATTERN_TYPE, secondaryPatternTypeVariant.getID());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentFishEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6D);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ModItems.KOI_BUCKET);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
    }

    public void copyDataToStack(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        if (this.hasCustomName()) {
            stack.setCustomName(this.getCustomName());
        }

        if (this.isAiDisabled()) {
            nbtCompound.putBoolean("NoAI", this.isAiDisabled());
        }

        if (this.isSilent()) {
            nbtCompound.putBoolean("Silent", this.isSilent());
        }

        if (this.hasNoGravity()) {
            nbtCompound.putBoolean("NoGravity", this.hasNoGravity());
        }

        if (this.isGlowingLocal()) {
            nbtCompound.putBoolean("Glowing", this.isGlowingLocal());
        }

        if (this.isInvulnerable()) {
            nbtCompound.putBoolean("Invulnerable", this.isInvulnerable());
        }

        nbtCompound.putFloat("Health", this.getHealth());

        nbtCompound.putString("BaseColor", this.getBaseColorVariant().getID());
        nbtCompound.putString("PrimaryPatternColor", this.getPrimaryPatternColorVariant().getID());
        nbtCompound.putString("PrimaryPatternType", this.getPrimaryPatternTypeVariant().getID());
        nbtCompound.putString("SecondaryPatternColor", this.getSecondaryPatternColorVariant().getID());
        nbtCompound.putString("SecondaryPatternType", this.getSecondaryPatternTypeVariant().getID());
    }

    public void copyDataFromNbt(KoiEntity entity, NbtCompound nbt) {
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

        if (nbt.contains("Health", NbtElement.NUMBER_TYPE)) {
            entity.setHealth(nbt.getFloat("Health"));
        }

        if (nbt.contains("Health", NbtElement.NUMBER_TYPE)) {
            entity.setHealth(nbt.getFloat("Health"));
        }

        if (nbt.contains("BaseColor", NbtElement.STRING_TYPE)) {
            entity.setBaseColorVariant(KoiBaseColorVariant.fromId(nbt.getString("BaseColor")));
        }

        if (nbt.contains("PrimaryPatternColor", NbtElement.STRING_TYPE)) {
            entity.setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant.fromId(nbt.getString("PrimaryPatternColor")));
        }

        if (nbt.contains("PrimaryPatternType", NbtElement.STRING_TYPE)) {
            entity.setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant.fromId(nbt.getString("PrimaryPatternType")));
        }

        if (nbt.contains("SecondaryPatternColor", NbtElement.STRING_TYPE)) {
            entity.setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant.fromId(nbt.getString("SecondaryPatternColor")));
        }

        if (nbt.contains("SecondaryPatternType", NbtElement.STRING_TYPE)) {
            entity.setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant.fromId(nbt.getString("SecondaryPatternType")));
        }
    }

    private ActionResult tryBucket(PlayerEntity player, Hand hand, KoiEntity koiEntity) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == Items.WATER_BUCKET && koiEntity.isAlive()) {
            koiEntity.playSound(koiEntity.getBucketFillSound(), 1.0F, 1.0F);
            ItemStack filledBucketItem = koiEntity.getBucketItem();
            koiEntity.copyDataToStack(filledBucketItem);
            ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, player, filledBucketItem, false);
            player.setStackInHand(hand, itemStack3);
            World world = koiEntity.getWorld();
            if (!world.isClient) {
                Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledBucketItem);
            }

            koiEntity.discard();
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (this.isBreedingItem(itemStack)) {
            int i = this.getBreedingAge();
            if (!this.getWorld().isClient && i == 0 && this.canEat()) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                return ActionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.eat(player, hand, itemStack);
                this.growUp(toGrowUpAge(-i), true);
                return ActionResult.success(this.getWorld().isClient);
            }

            if (this.getWorld().isClient) {
                return ActionResult.CONSUME;
            }
        } else if (itemStack.isOf(Items.BUCKET)) {
            return tryBucket(player, hand, this);
        }

        return super.interactMob(player, hand);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos).getFluidState().isOf(Fluids.WATER);
    }

    public static int toGrowUpAge(int breedingAge) {
        return (int) ((float) (breedingAge / 20) * 0.1F);
    }

    public void breed(ServerWorld world, KoiEntity other) {
        this.breed(world, other, null);
    }

    public void breed(ServerWorld world, KoiEntity other, @Nullable KoiEntity baby) {
        this.setBreedingAge(6000);
        other.setBreedingAge(6000);

        KoiEggsEntity koiEggs = ModEntityTypes.KOI_EGGS.create(world);
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

        koiEggs.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        world.spawnEntity(koiEggs);

        this.resetLoveTicks();
        other.resetLoveTicks();
        world.sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);

        if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            int xp = this.getRandom().nextInt(7) + 1;
            world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), xp));
        }
    }

    public <T> T getWeightedVariant(KoiEntity parent1, KoiEntity parent2, int parent1Weight, int parent2Weight, int randomWeight, Function<KoiEntity, T> getter, Supplier<T> randomSupplier) {
        int total = parent1Weight + parent2Weight + randomWeight;
        int decider = parent1.getRandom().nextInt(total);
        if (decider < parent1Weight) return getter.apply(parent1);
        if (decider < parent1Weight + parent2Weight) return getter.apply(parent2);
        return randomSupplier.get();
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

    @Nullable
    public KoiEntity createChild(ServerWorld world, KoiEntity entity) {
        return ModEntityTypes.KOI.create(world);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            this.loveTicks = 0;
            return super.damage(source, amount);
        }
    }

    public int getBreedingAge() {
        if (this.getWorld().isClient) {
            return this.dataTracker.get(CHILD) ? -1 : 1;
        } else {
            return this.breedingAge;
        }
    }

    public void setBreedingAge(int age) {
        int i = this.getBreedingAge();
        this.breedingAge = age;
        if (i < 0 && age >= 0 || i >= 0 && age < 0) {
            this.dataTracker.set(CHILD, age < 0);
            this.onGrowUp();
        }

    }

    public int getLoveTicks() {
        return this.loveTicks;
    }

    public void setLoveTicks(int loveTicks) {
        this.loveTicks = loveTicks;
    }

    @Nullable
    public ServerPlayerEntity getLovingPlayer() {
        if (this.lovingPlayer == null) {
            return null;
        } else {
            PlayerEntity playerEntity = this.getWorld().getPlayerByUuid(this.lovingPlayer);
            return playerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity) playerEntity : null;
        }
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

    public void handleStatus(byte status) {
        if (status == 18) {
            for (int i = 0; i < 7; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }

    }

    public boolean isBaby() {
        return this.getBreedingAge() < 0;
    }

    public void setBaby(boolean baby) {
        this.setBreedingAge(baby ? -24000 : 0);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(this.breedingItem);
    }

    public boolean isInLove() {
        return this.getLoveTicks() > 0;
    }

    public boolean isReadyToBreed() {
        return false;
    }

    public void lovePlayer(@Nullable PlayerEntity player) {
        this.loveTicks = 600;
        if (player != null) {
            this.lovingPlayer = player.getUuid();
        }

        this.getWorld().sendEntityStatus(this, (byte) 18);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (CHILD.equals(data)) {
            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    public void resetLoveTicks() {
        this.setLoveTicks(0);
    }

    @Override
    public void tickMovement() {
        if (!this.isTouchingWater() && this.isOnGround() && this.verticalCollision) {
            this.setVelocity(this.getVelocity().add(((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645, ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.setOnGround(false);
            this.velocityDirty = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getSoundPitch());
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
                this.getWorld().addParticle(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
            }
        }

        if (this.getWorld().isClient) {
            if (this.happyTicksRemaining > 0) {
                if (this.happyTicksRemaining % 4 == 0) {
                    this.getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), 0.0, 0.0, 0.0);
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

        super.tickMovement();
    }

    protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

    }

    @Override
    protected void mobTick() {
        if (this.getBreedingAge() != 0) {
            this.loveTicks = 0;
        }

        super.mobTick();
    }

    protected void onGrowUp() {
        if (!this.isBaby() && this.hasVehicle()) {
            Entity var2 = this.getVehicle();
            if (var2 instanceof BoatEntity boatEntity) {
                if (!boatEntity.isSmallerThanBoat(this)) {
                    this.stopRiding();
                }
            }
        }

    }


}
