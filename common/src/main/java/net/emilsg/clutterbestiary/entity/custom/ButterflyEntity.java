package net.emilsg.clutterbestiary.entity.custom;

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
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class ButterflyEntity extends ParentAnimalEntity {
    private static final int SPORE_BLOSSOM_DUPE_COOLDOWN_TICKS = 20 * 60 * 10;
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.SUGAR);
    private static final TrackedData<Boolean> HAS_COCOON = DataTracker.registerData(ButterflyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<String> VARIANT = DataTracker.registerData(ButterflyEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> FLYING_TYPE_VARIANT = DataTracker.registerData(ButterflyEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final AnimationState flyingAnimState = new AnimationState();
    private int dupeTimer;

    public ButterflyEntity(EntityType<? extends ParentAnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.lookControl = new ButterflyLookControl(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 32.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        RegistryEntry<Biome> registryEntry = world.getBiome(this.getBlockPos());
        ButterflyVariant variant = ButterflyVariant.getRandom(false);

        if (spawnReason.equals(SpawnReason.SPAWN_EGG)) {
            variant = Util.getRandom(ButterflyVariant.values(), this.random);
        }

        if (registryEntry.isIn(BiomeTags.IS_OVERWORLD)) {
            variant = ButterflyVariant.getRandom(true);
        } else if (registryEntry.isIn(BiomeTags.IS_NETHER)) {
            if (registryEntry.matchesKey(BiomeKeys.WARPED_FOREST)) {
                variant = ButterflyVariant.WARPED;
            } else if (registryEntry.matchesKey(BiomeKeys.CRIMSON_FOREST)) {
                variant = ButterflyVariant.CRIMSON;
            } else if (registryEntry.matchesKey(BiomeKeys.SOUL_SAND_VALLEY)) {
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
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HAS_COCOON, false);
        builder.add(VARIANT, ButterflyVariant.WHITE.getId());
        builder.add(FLYING_TYPE_VARIANT, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new ButterflyEscapeFluidGoal(this));
        this.goalSelector.add(1, new ButterflyMateGoal(this, 1.0));
        this.goalSelector.add(2, new ButterflyPlaceCocoonGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.25, BREEDING_INGREDIENT, false));
        this.goalSelector.add(4, new ButterflyDupeSporeBlossomGoal(this, 1, SPORE_BLOSSOM_DUPE_COOLDOWN_TICKS));
        this.goalSelector.add(5, new ButterflyWanderNetherGoal(this));
        this.goalSelector.add(5, new ButterflyWanderOverworldGoal(this));
    }

    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
        if (nbt.contains("FlyingVariant")) {
            this.setFlyingVariant(nbt.getInt("FlyingVariant"));
        }

        if (nbt.contains("DupeTimer")) {
            this.setDupeTimer(nbt.getInt("DupeTimer"));
        }

        if (nbt.contains("HasCocoon")) {
            this.setHasCocoon(nbt.getBoolean("HasCocoon"));
        }

        if (nbt.contains("Variant")) {
            this.setVariant(ButterflyVariant.fromId(nbt.getString("Variant")));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setHasCocoon(nbt.getBoolean("HasCocoon"));
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
        this.setDupeTimer(nbt.getInt("DupeTimer"));
        this.dataTracker.set(FLYING_TYPE_VARIANT, nbt.getInt("FlyingVariant"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HasCocoon", this.hasCocoon());
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putInt("DupeTimer", this.getDupeTimer());
        nbt.putInt("FlyingVariant", this.getFlyingTypeVariant());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    private static boolean isTodayAroundHalloween() {
        LocalDate localDate = LocalDate.now();
        int i = localDate.getDayOfMonth();
        int j = localDate.getMonth().getValue();
        return j == 10 && i >= 20 || j == 11 && i <= 3;
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.BUTTERFLIES_SPAWN_ON);
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        super.breed(world, other);
        ButterflyLarvaEntity larva = ModEntityTypes.BUTTERFLY_LARVA.get().create(world);
        if (larva != null) {
            BlockPos spawnPos = this.findLarvaSpawnPos(world);
            larva.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, this.getYaw(), 0.0F);
            larva.setHomePos(spawnPos);
            larva.setVariant(other instanceof ButterflyEntity mate && this.random.nextBoolean() ? mate.getVariant() : this.getVariant());
            larva.setPersistent();
            world.spawnEntity(larva);
        }

        ServerPlayerEntity serverPlayerEntity = this.getLovingPlayer();
        if (serverPlayerEntity == null && other.getLovingPlayer() != null) {
            serverPlayerEntity = other.getLovingPlayer();
        }

        if (serverPlayerEntity != null) {
            serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
            Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this, other, null);
        }

        this.setBreedingAge(6000);
        other.setBreedingAge(6000);
        this.resetLoveTicks();
        other.resetLoveTicks();
        world.sendEntityStatus(this, (byte) 18);
    }

    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }

    private BlockPos findLarvaSpawnPos(ServerWorld world) {
        BlockPos origin = this.getBlockPos();
        for (int radius = 0; radius <= 2; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    for (int y = 0; y <= 8; y++) {
                        BlockPos pos = origin.add(x, -y, z);
                        if (world.getBlockState(pos).isAir() && world.getBlockState(pos.up()).isAir()
                                && world.getFluidState(pos).isEmpty() && world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                            return pos;
                        }
                    }
                }
            }
        }
        return origin;
    }

    @Override
    public boolean canEat() {
        return super.canEat() && !this.hasCocoon();
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return true;
    }

    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
        NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack, nbtCompound -> {
            nbtCompound.putString("Variant", this.getTypeVariant());
            nbtCompound.putInt("FlyingVariant", this.getFlyingTypeVariant());
            nbtCompound.putInt("DupeTimer", this.getDupeTimer());
            nbtCompound.putBoolean("HasCocoon", this.hasCocoon());
        });
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public int getDupeTimer() {
        return this.dupeTimer;
    }

    public void setDupeTimer(int time) {
        this.dupeTimer = time;
    }

    public int getFlyingTypeVariant() {
        return this.dataTracker.get(FLYING_TYPE_VARIANT);
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return this.isSafeFlightTarget(pos) ? 10.0F : 0.0F;
    }

    public boolean isSafeFlightTarget(BlockPos pos) {
        World world = this.getWorld();
        if (!world.getBlockState(pos).isAir() || !world.getFluidState(pos).isEmpty()) return false;

        for (Direction direction : Direction.values()) {
            if (!world.getFluidState(pos.offset(direction)).isEmpty()) return false;
        }

        return true;
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public ButterflyVariant getVariant() {
        return ButterflyVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(ButterflyVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public boolean hasCocoon() {
        return this.dataTracker.get(HAS_COCOON);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.tryBottle(player, hand, this)) {
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public boolean isExperienceDroppingDisabled() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return this.getVariant().isFireImmune();
    }

    @Override
    public void setOnFireFromLava() {
    }

    public void setFlyingVariant(int flyingVariant) {
        this.dataTracker.set(FLYING_TYPE_VARIANT, flyingVariant);
    }

    public void setHasCocoon(boolean hasCocoon) {
        this.dataTracker.set(HAS_COCOON, hasCocoon);
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            AnimationPlayback.updateLoop(this, this.flyingAnimState, this.isAlive());
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (!this.getWorld().isClient && this.getDupeTimer() < SPORE_BLOSSOM_DUPE_COOLDOWN_TICKS) {
            this.setDupeTimer(this.getDupeTimer() + 1);
        }
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }

        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_WOOL_BREAK;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_WOOL_HIT;
    }

    @Override
    protected void onKilledBy(@Nullable LivingEntity adversary) {
        if (isTodayAroundHalloween() && adversary instanceof PlayerEntity && random.nextInt(10) == 0) {
            adversary.damage(this.getWorld().getDamageSources().magic(), 6.0f);
        }
        super.onKilledBy(adversary);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private boolean tryBottle(PlayerEntity player, Hand hand, ButterflyEntity entity) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == Items.GLASS_BOTTLE && entity.isAlive()) {
            if (entity.getWorld().isClient) return true;
            entity.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 0.75F);
            ItemStack bottleStack = new ItemStack(ModItems.BUTTERFLY_IN_A_BOTTLE.get());
            entity.copyDataToStack(bottleStack);
            ItemStack butterflyBottleStack = ItemUsage.exchangeStack(itemStack, player, bottleStack, false);
            player.swingHand(hand);
            player.setStackInHand(hand, butterflyBottleStack);

            entity.discard();
            return true;
        }
        return false;
    }

    static class ButterflyLookControl extends LookControl {
        ButterflyLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            super.tick();
        }

        protected boolean shouldStayHorizontal() {
            return true;
        }
    }
}
