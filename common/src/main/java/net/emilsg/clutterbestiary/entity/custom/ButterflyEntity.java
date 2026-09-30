package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyDupeSporeBlossomGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyEscapeFluidGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyMateGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyPlaceCocoonGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyWanderNetherGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.ButterflyWanderOverworldGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.util.Util;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class ButterflyEntity extends ParentAnimalEntity {
    private static final int SPORE_BLOSSOM_DUPE_COOLDOWN_TICKS = 20 * 60 * 10;
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.SUGAR);
    private static final EntityDataAccessor<Boolean> HAS_COCOON = SynchedEntityData.defineId(ButterflyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(ButterflyEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FLYING_TYPE_VARIANT = SynchedEntityData.defineId(ButterflyEntity.class, EntityDataSerializers.INT);

    public final AnimationState flyingAnimState = new AnimationState();
    private int dupeTimer;

    public ButterflyEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new ButterflyLookControl(this);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.LAVA, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 32.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        Holder<Biome> registryEntry = world.getBiome(this.blockPosition());
        ButterflyVariant variant = ButterflyVariant.getRandom(false);

        if (spawnReason.equals(EntitySpawnReason.SPAWN_ITEM_USE)) {
            variant = Util.getRandom(ButterflyVariant.values(), this.random);
        }

        if (registryEntry.is(BiomeTags.IS_OVERWORLD)) {
            variant = ButterflyVariant.getRandom(true);
        } else if (registryEntry.is(BiomeTags.IS_NETHER)) {
            if (registryEntry.is(Biomes.WARPED_FOREST)) {
                variant = ButterflyVariant.WARPED;
            } else if (registryEntry.is(Biomes.CRIMSON_FOREST)) {
                variant = ButterflyVariant.CRIMSON;
            } else if (registryEntry.is(Biomes.SOUL_SAND_VALLEY)) {
                variant = ButterflyVariant.SOUL;
            } else {
                if (random.nextBoolean()) {
                    variant = ButterflyVariant.CRIMSON;
                } else if (random.nextBoolean()) {
                    variant = ButterflyVariant.WARPED;
                } else {
                    variant = ButterflyVariant.SOUL;
                }
            }
        }

        this.setVariant(variant);
        this.setFlyingVariant(random.nextInt(3));
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_COCOON, false);
        builder.define(VARIANT, ButterflyVariant.WHITE.getId());
        builder.define(FLYING_TYPE_VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new ButterflyEscapeFluidGoal(this));
        this.goalSelector.addGoal(1, new ButterflyMateGoal(this, 1.0));
        this.goalSelector.addGoal(2, new ButterflyPlaceCocoonGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(4, new ButterflyDupeSporeBlossomGoal(this, 1, SPORE_BLOSSOM_DUPE_COOLDOWN_TICKS));
        this.goalSelector.addGoal(5, new ButterflyWanderNetherGoal(this));
        this.goalSelector.addGoal(5, new ButterflyWanderOverworldGoal(this));
    }

    public void copyDataFromNbt(CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag(this, nbt);
        if (nbt.contains("FlyingVariant")) {
            this.setFlyingVariant(nbt.getIntOr("FlyingVariant", 0));
        }

        if (nbt.contains("DupeTimer")) {
            this.setDupeTimer(nbt.getIntOr("DupeTimer", 0));
        }

        if (nbt.contains("HasCocoon")) {
            this.setHasCocoon(nbt.getBooleanOr("HasCocoon", false));
        }

        if (nbt.contains("Variant")) {
            this.setVariant(ButterflyVariant.fromId(nbt.getStringOr("Variant", "")));
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setHasCocoon(nbt.getBooleanOr("HasCocoon", false));
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
        this.setDupeTimer(nbt.getIntOr("DupeTimer", 0));
        this.entityData.set(FLYING_TYPE_VARIANT, nbt.getIntOr("FlyingVariant", 0));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("HasCocoon", this.hasCocoon());
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putInt("DupeTimer", this.getDupeTimer());
        nbt.putInt("FlyingVariant", this.getFlyingTypeVariant());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 1D)
                .add(Attributes.FLYING_SPEED, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    private static boolean isTodayAroundHalloween() {
        LocalDate localDate = LocalDate.now();
        int i = localDate.getDayOfMonth();
        int j = localDate.getMonth().getValue();
        return j == 10 && i >= 20 || j == 11 && i <= 3;
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.BUTTERFLIES_SPAWN_ON);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        super.spawnChildFromBreeding(world, other);
        ButterflyLarvaEntity larva = ModEntityTypes.BUTTERFLY_LARVA.get().create(world, EntitySpawnReason.BREEDING);
        if (larva != null) {
            BlockPos spawnPos = this.findLarvaSpawnPos(world);
            larva.snapTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, this.getYRot(), 0.0F);
            larva.setHomePos(spawnPos);
            larva.setVariant(other instanceof ButterflyEntity mate && this.random.nextBoolean() ? mate.getVariant() : this.getVariant());
            larva.setPersistenceRequired();
            world.addFreshEntity(larva);
        }

        ServerPlayer serverPlayerEntity = this.getLoveCause();
        if (serverPlayerEntity == null && other.getLoveCause() != null) {
            serverPlayerEntity = other.getLoveCause();
        }

        if (serverPlayerEntity != null) {
            serverPlayerEntity.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayerEntity, this, other, null);
        }

        this.setAge(6000);
        other.setAge(6000);
        this.resetLove();
        other.resetLove();
        world.broadcastEntityEvent(this, (byte) 18);
    }

    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    private BlockPos findLarvaSpawnPos(ServerLevel world) {
        BlockPos origin = this.blockPosition();
        for (int radius = 0; radius <= 2; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    for (int y = 0; y <= 8; y++) {
                        BlockPos pos = origin.offset(x, -y, z);
                        if (world.getBlockState(pos).isAir() && world.getBlockState(pos.above()).isAir()
                                && world.getFluidState(pos).isEmpty() && world.getBlockState(pos.below()).isRedstoneConductor(world, pos.below())) {
                            return pos;
                        }
                    }
                }
            }
        }
        return origin;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasCocoon();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason spawnReason) {
        return true;
    }

    public void copyDataToStack(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbtCompound -> {
            nbtCompound.putString("Variant", this.getTypeVariant());
            nbtCompound.putInt("FlyingVariant", this.getFlyingTypeVariant());
            nbtCompound.putInt("DupeTimer", this.getDupeTimer());
            nbtCompound.putBoolean("HasCocoon", this.hasCocoon());
        });
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    public int getDupeTimer() {
        return this.dupeTimer;
    }

    public void setDupeTimer(int time) {
        this.dupeTimer = time;
    }

    public int getFlyingTypeVariant() {
        return this.entityData.get(FLYING_TYPE_VARIANT);
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return this.isSafeFlightTarget(pos) ? 10.0F : 0.0F;
    }

    public boolean isSafeFlightTarget(BlockPos pos) {
        Level world = this.level();
        if (!world.getBlockState(pos).isAir() || !world.getFluidState(pos).isEmpty()) return false;

        for (Direction direction : Direction.values()) {
            if (!world.getFluidState(pos.relative(direction)).isEmpty()) return false;
        }

        return true;
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public ButterflyVariant getVariant() {
        return ButterflyVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(ButterflyVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public boolean hasCocoon() {
        return this.entityData.get(HAS_COCOON);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.tryBottle(player, hand, this)) {
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public boolean wasExperienceConsumed() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return this.getVariant().isFireImmune();
    }

    @Override
    public void lavaHurt() {
    }

    public void setFlyingVariant(int flyingVariant) {
        this.entityData.set(FLYING_TYPE_VARIANT, flyingVariant);
    }

    public void setHasCocoon(boolean hasCocoon) {
        this.entityData.set(HAS_COCOON, hasCocoon);
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (world.isClientSide()) {
            AnimationPlayback.updateLoop(this, this.flyingAnimState, this.isAlive());
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide() && this.getDupeTimer() < SPORE_BLOSSOM_DUPE_COOLDOWN_TICKS) {
            this.setDupeTimer(this.getDupeTimer() + 1);
        }
    }

    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }

        };
        birdNavigation.setCanOpenDoors(false);
        birdNavigation.setCanFloat(false);
        return birdNavigation;
    }

    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOOL_BREAK;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WOOL_HIT;
    }

    @Override
    protected void createWitherRose(@Nullable LivingEntity adversary) {
        if (isTodayAroundHalloween() && adversary instanceof Player && random.nextInt(10) == 0) {
            adversary.hurt(this.level().damageSources().magic(), 6.0f);
        }
        super.createWitherRose(adversary);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private boolean tryBottle(Player player, InteractionHand hand, ButterflyEntity entity) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == Items.GLASS_BOTTLE && entity.isAlive()) {
            if (entity.level().isClientSide()) return true;
            entity.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH, 1.0F, 0.75F);
            ItemStack bottleStack = new ItemStack(ModItems.BUTTERFLY_IN_A_BOTTLE.get());
            entity.copyDataToStack(bottleStack);
            ItemStack butterflyBottleStack = ItemUtils.createFilledResult(itemStack, player, bottleStack, false);
            player.swing(hand, SwingAnimation.DEFAULT, false);
            player.setItemInHand(hand, butterflyBottleStack);

            entity.discard();
            return true;
        }
        return false;
    }

    static class ButterflyLookControl extends LookControl {
        ButterflyLookControl(Mob entity) {
            super(entity);
        }

        public void tick() {
            super.tick();
        }

        protected boolean resetXRotOnTick() {
            return true;
        }
    }
}
