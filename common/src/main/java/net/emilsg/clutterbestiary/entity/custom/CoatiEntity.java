package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import dev.architectury.registry.menu.ExtendedMenuDataProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.IdleAnimationGroup;
import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.entity.variants.CoatiVariant;
import net.emilsg.clutterbestiary.menu.handler.CoatiScreenHandler;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CoatiEntity extends ParentTameableEntity implements HandledEntityAnimations<CoatiEntity, CoatiEntityAnimationState> {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.GLISTERING_MELON_SLICE);
    private static final Item TAMING_ITEM = Items.MELON_SLICE;
    // Wild inventory entries keep their legacy "WildSlot" key alongside the inline item fields.
    private static final Codec<ItemStackWithSlot> WILD_ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.optionalAlwaysPresentFieldOf(ExtraCodecs.UNSIGNED_BYTE, "WildSlot", 0).forGetter(ItemStackWithSlot::slot),
            ItemStack.MAP_CODEC.forGetter(ItemStackWithSlot::stack)
    ).apply(instance, ItemStackWithSlot::new));
    public static final int DIG_DURATION_TICKS = 160;
    public static final int UNBURROW_DURATION_TICKS = 180;
    public static final float DIG_ANIMATION_SPEED = 2.0f;
    private static final int STAND_UP_TICKS = 5;
    private static final int PICK_UP_TICKS = 10;
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> UNBURROWING = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_CHEST = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DAYS_FED_HONEY = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> LAST_DAY_FED_HONEY = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<BlockPos> BURROW_POS = SynchedEntityData.defineId(CoatiEntity.class, EntityDataSerializers.BLOCK_POS);
    public final AnimationState leftEarTwitchAnimationState = new AnimationState();
    public final AnimationState rightEarTwitchAnimationState = new AnimationState();
    private final EntityAnimationController<CoatiEntity, CoatiEntityAnimationState> animationController = new EntityAnimationController<>(this, CoatiEntityAnimationState.IDLING, CoatiEntityAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private final IdleAnimationGroup idleAnimations = new IdleAnimationGroup(3, 3, 100)
            .add(1, leftEarTwitchAnimationState)
            .add(1, rightEarTwitchAnimationState)
            .add(1, leftEarTwitchAnimationState, rightEarTwitchAnimationState);
    private final Goal wanderOftenGoal = new WanderAroundFarOftenGoal(this, 1.0f);
    private final Goal wanderFarGoal = new WaterAvoidingRandomStrollGoal(this, 1.0f);
    protected SimpleContainer coatiInventory;
    protected SimpleContainer wildInventory;
    private int postDigCooldown;
    private boolean digSessionActive = false;
    private int forageGrace = 0;

    public CoatiEntity(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
        this.coatiInventory = new SimpleContainer(this.getInventorySize());
        this.wildInventory = new SimpleContainer(this.getWildInventorySize());
        this.updateWanderingGoal();
        this.setupAnimationController();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new TamableAnimalPanicGoal(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.25D, 10.0f, 2.0f));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TamedTemptGoal(this, 1.0f, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(3, new CoatiUnburrowingGoal(this));
        this.goalSelector.addGoal(4, new CoatiDigGoal(this));
        this.goalSelector.addGoal(5, new CoatiFindBurrowGoal(this, this.getDaysFedHoneyNeeded()));
        this.goalSelector.addGoal(6, new ForageItemsGoal(this, 1, 12.0));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6f));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        Holder<Biome> registryEntry = world.getBiome(this.blockPosition());
        CoatiVariant variant;
        this.setBurrowPos(this.blockPosition());

        if (registryEntry.is(Biomes.JUNGLE) || registryEntry.is(Biomes.BAMBOO_JUNGLE)) {
            variant = CoatiVariant.JUNGLE;
        } else if (registryEntry.is(Biomes.OLD_GROWTH_SPRUCE_TAIGA)) {
            variant = CoatiVariant.TAIGA;
        } else {
            variant = CoatiVariant.getRandomAlbinoExcluded();
        }

        if (random.nextInt(100) == 0) variant = CoatiVariant.ALBINO;

        this.setVariant(variant);

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_STATE, CoatiEntityAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
        builder.define(VARIANT, CoatiVariant.JUNGLE.getId());
        builder.define(DAYS_FED_HONEY, 0);
        builder.define(LAST_DAY_FED_HONEY, -1L);
        builder.define(BURROW_POS, this.blockPosition().below());
        builder.define(DIGGING, false);
        builder.define(UNBURROWING, false);
        builder.define(HAS_CHEST, false);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
        this.entityData.set(BURROW_POS, BlockPos.containing(
                nbt.getDoubleOr("BurrowPosX", 0.0),
                nbt.getDoubleOr("BurrowPosY", 0.0),
                nbt.getDoubleOr("BurrowPosZ", 0.0)));
        this.entityData.set(DAYS_FED_HONEY, nbt.getIntOr("DaysFedHoney", 0));
        this.setLastDayFedHoney(nbt.getLongOr("LastDayFedHoney", 0L));

        this.setHasChest(nbt.getBooleanOr("HasChest", false));
        this.onChestedStatusChanged();

        if (this.hasChest()) {
            for (ItemStackWithSlot item : nbt.listOrEmpty("Items", ItemStackWithSlot.CODEC)) {
                if (item.isValidInContainer(this.coatiInventory.getContainerSize())) {
                    this.coatiInventory.setItem(item.slot(), item.stack());
                }
            }
        }

        if (!this.isTame()) {
            for (ItemStackWithSlot item : nbt.listOrEmpty("WildItems", WILD_ITEM_CODEC)) {
                if (item.isValidInContainer(this.wildInventory.getContainerSize())) {
                    this.wildInventory.setItem(item.slot(), item.stack());
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putString("Variant", this.getTypeVariant());
        nbt.putDouble("BurrowPosX", this.getBurrowPos().getX());
        nbt.putDouble("BurrowPosY", this.getBurrowPos().getY());
        nbt.putDouble("BurrowPosZ", this.getBurrowPos().getZ());
        nbt.putInt("DaysFedHoney", this.getDaysFedHoney());
        nbt.putLong("LastDayFedHoney", this.getLastDayFedHoney());

        nbt.putBoolean("HasChest", this.hasChest());

        if (this.hasChest()) {
            ValueOutput.TypedOutputList<ItemStackWithSlot> items = nbt.list("Items", ItemStackWithSlot.CODEC);

            for (int i = 0; i < this.coatiInventory.getContainerSize(); i++) {
                ItemStack itemStack = this.coatiInventory.getItem(i);
                if (!itemStack.isEmpty()) {
                    items.add(new ItemStackWithSlot(i, itemStack));
                }
            }
        }

        if (!this.isTame()) {
            ValueOutput.TypedOutputList<ItemStackWithSlot> wild = nbt.list("WildItems", WILD_ITEM_CODEC);
            for (int i = 0; i < this.wildInventory.getContainerSize(); i++) {
                ItemStack it = this.wildInventory.getItem(i);
                if (!it.isEmpty()) {
                    wild.add(new ItemStackWithSlot(i, it));
                }
            }
        }
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.COATIS_SPAWN_ON);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.MOVEMENT_SPEED, 0.225F);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        AgeableMob child = this.getBreedOffspring(world, other);

        if (child != null) {
            child.setBaby(true);
            child.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);

            if (child instanceof CoatiEntity coatiEntity) {
                CoatiVariant variant = CoatiVariant.getRandom();
                int randomInt = random.nextInt(100);
                if (randomInt == 0) {
                    variant = CoatiVariant.ALBINO;
                } else if (other instanceof CoatiEntity coatiParent) {
                    CoatiVariant otherVariant = coatiParent.getVariant();
                    variant = random.nextBoolean() ? this.getVariant() : otherVariant;
                }

                coatiEntity.setVariant(variant);
            }

            this.finalizeSpawnChildFromBreeding(world, other, child);
            world.addFreshEntityWithPassengers(child);
        }
    }

    public boolean canFitInWild(ItemStack incoming) {
        for (int i = 0; i < this.wildInventory.getContainerSize(); i++) {
            if (this.wildInventory.getItem(i).isEmpty()) return true;
        }
        for (int i = 0; i < this.wildInventory.getContainerSize(); i++) {
            ItemStack cur = this.wildInventory.getItem(i);
            if (!cur.isEmpty()
                    && cur.is(incoming.getItem())
                    && ItemStack.isSameItemSameComponents(cur, incoming)
                    && cur.getCount() < cur.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    public void clearInventory(SimpleContainer inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, ItemStack.EMPTY);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        CoatiEntity child = ModEntityTypes.COATI.get().create(world, EntitySpawnReason.BREEDING);
        if (child != null) child.setVariant(CoatiVariant.getRandom());
        return child;
    }

    public BlockPos getBurrowPos() {
        return this.entityData.get(BURROW_POS);
    }

    public void setBurrowPos(BlockPos burrowPos) {
        this.entityData.set(BURROW_POS, burrowPos);
    }

    public SimpleContainer getCoatiInventory() {
        return coatiInventory;
    }

    public int getDaysFedHoney() {
        return this.entityData.get(DAYS_FED_HONEY);
    }

    public void setDaysFedHoney(int daysFedHoney) {
        this.entityData.set(DAYS_FED_HONEY, daysFedHoney);
    }

    public int getDaysFedHoneyNeeded() {
        return 3;
    }

    public int getForageGrace() {
        return forageGrace;
    }

    public void setForageGrace(int forageGrace) {
        this.forageGrace = forageGrace;
    }

    public final int getInventorySize() {
        return 12;
    }

    public long getLastDayFedHoney() {
        return this.entityData.get(LAST_DAY_FED_HONEY);
    }

    public void setLastDayFedHoney(long lastDayFedHoney) {
        this.entityData.set(LAST_DAY_FED_HONEY, lastDayFedHoney);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    public int getPostDigCooldown() {
        return this.postDigCooldown;
    }

    public void setPostDigCooldown(int postDigCooldown) {
        this.postDigCooldown = postDigCooldown;
    }

    @Override
    public SlotAccess getSlot(int mappedIndex) {
        return mappedIndex == 499 ? new SlotAccess() {
            @Override
            public ItemStack get() {
                return CoatiEntity.this.hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY;
            }

            @Override
            public boolean set(ItemStack stack) {
                if (stack.isEmpty()) {
                    if (CoatiEntity.this.hasChest()) {
                        CoatiEntity.this.setHasChest(false);
                        CoatiEntity.this.onChestedStatusChanged();
                    }

                    return true;
                } else if (stack.is(Items.CHEST)) {
                    if (!CoatiEntity.this.hasChest()) {
                        CoatiEntity.this.setHasChest(true);
                        CoatiEntity.this.onChestedStatusChanged();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } : super.getSlot(mappedIndex);
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public CoatiVariant getVariant() {
        return CoatiVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(CoatiVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    public SimpleContainer getWildInventory() {
        return wildInventory;
    }

    public final int getWildInventorySize() {
        return 16;
    }

    public boolean hasChest() {
        return this.entityData.get(HAS_CHEST);
    }

    public ItemStack insertInto(SimpleContainer inv, ItemStack stack) {
        for (int i = 0; i < inv.getContainerSize() && !stack.isEmpty(); i++) {
            ItemStack cur = inv.getItem(i);
            if (!cur.isEmpty()
                    && cur.is(stack.getItem())
                    && ItemStack.isSameItemSameComponents(cur, stack)
                    && cur.getCount() < cur.getMaxStackSize()) {
                int move = Math.min(stack.getCount(), cur.getMaxStackSize() - cur.getCount());
                cur.grow(move);
                stack.shrink(move);
            }
        }
        for (int i = 0; i < inv.getContainerSize() && !stack.isEmpty(); i++) {
            if (inv.getItem(i).isEmpty()) {
                inv.setItem(i, stack.copy());
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.isTame() && this.isOwnedBy(player) && !player.isSecondaryUseActive() && !ModUtil.inEitherHand(player, Items.CHEST) && !ModUtil.inEitherHand(player, Items.HONEY_BOTTLE)) {
            this.setSit(!this.isOrderedToSit());
            return InteractionResult.SUCCESS;
        }

        if (!this.isTame() && stack.getItem() == this.getTamingItem() && this.isBaby() && !player.isSecondaryUseActive()) {
            stack.consume(1, player);
            this.tryTame(player);
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && this.isOwnedBy(player) && player.isSecondaryUseActive() && !this.isBaby() && this.hasChest()) {
            return this.openInventory(player);
        }

        if (stack.is(Items.CHEST) && !this.isBaby() && !this.hasChest() && this.isTame() && this.isOwnedBy(player) && !player.isSecondaryUseActive()) {
            this.addChest(player, stack);
            return InteractionResult.SUCCESS;
        }

        if (stack.is(Items.HONEY_BOTTLE) && !this.isBaby() && !this.isTame() && !player.isSecondaryUseActive()) {
            int streak;

            long day = this.level().getOverworldClockTime() / 24000L;
            long lastDayFedHoney = this.getLastDayFedHoney();
            if (lastDayFedHoney == day) return InteractionResult.CONSUME;

            this.makeSound(SoundEvents.HONEY_DRINK.value());

            if (this.level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            if (lastDayFedHoney == day - 1) {
                streak = this.getDaysFedHoney() + 1;
            } else {
                streak = 1;
            }

            this.setDaysFedHoney(streak);
            this.setLastDayFedHoney(day);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                player.addItem(new ItemStack(Items.GLASS_BOTTLE));
            }
            return InteractionResult.CONSUME;
        }

        if (this.isFood(stack)) {
            int i = this.getAge();
            if (!this.level().isClientSide() && i == 0 && this.canFallInLove() && this.isTame()) {
                this.usePlayerItem(player, hand, stack);
                this.setInLove(player);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                if (this.level().isClientSide()) return InteractionResult.CONSUME;
                this.usePlayerItem(player, hand, stack);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                return InteractionResult.SUCCESS;
            }

            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isDigSessionActive() {
        return this.digSessionActive;
    }

    public void setDigSessionActive(boolean digSessionActive) {
        this.digSessionActive = digSessionActive;
    }

    public boolean isDigging() {
        return this.entityData.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.entityData.set(DIGGING, digging);
    }

    public boolean isSniffing() {
        return this.animationController.getState() == CoatiEntityAnimationState.SNIFFING;
    }

    public boolean isUnBurrowing() {
        return this.entityData.get(UNBURROWING);
    }

    public void setUnBurrowing(boolean burrowing) {
        this.entityData.set(UNBURROWING, burrowing);
    }

    public void die(DamageSource damageSource) {
        this.startState(CoatiEntityAnimationState.IDLING);
        if (!level().isClientSide()) {
            for (int i = 0; i < coatiInventory.getContainerSize(); i++) {
                ItemStack s = coatiInventory.getItem(i);
                if (!s.isEmpty()) this.spawnAtLocation((ServerLevel) this.level(), s);
            }
            for (int i = 0; i < wildInventory.getContainerSize(); i++) {
                ItemStack s = wildInventory.getItem(i);
                if (!s.isEmpty()) this.spawnAtLocation((ServerLevel) this.level(), s);
            }
            if (hasChest()) this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(Items.CHEST));
        }
        super.die(damageSource);
    }

    public void openCoatiMenu(ServerPlayer player) {
        CoatiEntity self = this;
        MenuRegistry.openExtendedMenu(player, new ExtendedMenuDataProvider<Integer>() {

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
                return new CoatiScreenHandler(id, inv, self);
            }

            @Override
            public Component getDisplayName() {
                return self.getDisplayName();
            }

            @Override
            public Integer getExtraData(ServerPlayer serverPlayer) {
                return self.getId();
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, Integer> getExtraDataCodec() {
                return CoatiScreenHandler.ENTITY_ID_CODEC;
            }
        });
    }

    public InteractionResult openInventory(Player player) {
        if (this.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            if (player instanceof ServerPlayer serverPlayerEntity) {
                this.openCoatiMenu(serverPlayerEntity);
                ModAdvancements.grant(serverPlayerEntity, ModAdvancements.PACK_RAT);
            }
            return InteractionResult.CONSUME;
        }
    }

    public void setHasChest(boolean hasChest) {
        this.entityData.set(HAS_CHEST, hasChest);
    }

    public void setSit(boolean sitting) {
        this.setOrderedToSit(sitting);

        if (!this.level().isClientSide()) {
            if (sitting) {
                startState(CoatiEntityAnimationState.SITTING);
            } else {
                startState(CoatiEntityAnimationState.STANDING_UP);
            }
        }
    }

    private void setupAnimationController() {
        animationController.addTransition(CoatiEntityAnimationState.IDLING, CoatiEntityAnimationState.SITTING, (e, s, age) -> e.isTame() && e.isOrderedToSit());
        animationController.addTransition(CoatiEntityAnimationState.SITTING, CoatiEntityAnimationState.STANDING_UP, (e, s, age) -> !e.isOrderedToSit());
        animationController.addTransition(CoatiEntityAnimationState.STANDING_UP, CoatiEntityAnimationState.SITTING, (e, s, age) -> e.isOrderedToSit());
        animationController.addCompletion(CoatiEntityAnimationState.STANDING_UP, CoatiEntityAnimationState.IDLING, STAND_UP_TICKS);
        animationController.addCompletion(CoatiEntityAnimationState.PICKING_UP_ITEM, CoatiEntityAnimationState.IDLING, PICK_UP_TICKS);
    }

    @Override
    public EntityAnimationController<CoatiEntity, CoatiEntityAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        super.tick();
        this.animationController.tick();

        if (this.level().isClientSide()) {
            this.idleAnimations.tick(this, this.isAlive());
        }

        if (!this.level().isClientSide()) {
            if (this.getForageGrace() > 0) this.forageGrace--;
            if (postDigCooldown > 0) postDigCooldown--;

            return;
        }

        Level world = this.level();
        BlockState blockState = world.getBlockState(this.getBurrowPos().below());

        if (blockState.getRenderShape() != RenderShape.INVISIBLE
                && this.isDigging()
                && this.animationController.getAnimationState(CoatiEntityAnimationState.DIGGING).isStarted()) {

            Vec3 particlePos = this.position();
            if (this.tickCount % 10 == 0) {
                for (int i = 0; i < 8; i++) {
                    world.addParticle(
                            new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                            particlePos.x(),
                            particlePos.y(),
                            particlePos.z(),
                            0.0, 0, 0.0
                    );
                }
            }
        }
    }

    public void updateWanderingGoal() {
        if (this.level() != null && !this.level().isClientSide()) {
            this.goalSelector.removeGoal(this.wanderFarGoal);
            this.goalSelector.removeGoal(this.wanderOftenGoal);
            if (this.isTame()) {
                this.goalSelector.addGoal(7, this.wanderFarGoal);
            } else {
                this.goalSelector.addGoal(7, this.wanderOftenGoal);
            }
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_COATI_AMBIENT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1.5f;
    }

    protected void onChestedStatusChanged() {
        SimpleContainer simpleInventory = this.coatiInventory;
        this.coatiInventory = new SimpleContainer(this.getInventorySize());
        if (simpleInventory != null) {
            int i = Math.min(simpleInventory.getContainerSize(), this.coatiInventory.getContainerSize());

            for (int j = 0; j < i; j++) {
                ItemStack itemStack = simpleInventory.getItem(j);
                if (!itemStack.isEmpty()) {
                    this.coatiInventory.setItem(j, itemStack.copy());
                }
            }
        }
    }

    protected void playAddChestSound() {
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 1.1f, 0.65F, 1.0F);
    }

    private void addChest(Player player, ItemStack chest) {
        this.setHasChest(true);
        this.playAddChestSound();
        chest.consume(1, player);
        if (player instanceof ServerPlayer serverPlayer) {
            ModAdvancements.grant(serverPlayer, ModAdvancements.PACK_RAT);
        }
        this.onChestedStatusChanged();
    }

    private void tryTame(Player player) {
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setSit(true);
            this.updateWanderingGoal();
            this.level().broadcastEntityEvent(this, (byte) 7);
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6);
        }
    }
}
