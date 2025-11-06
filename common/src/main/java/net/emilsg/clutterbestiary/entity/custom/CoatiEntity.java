package net.emilsg.clutterbestiary.entity.custom;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.ModTrackedDataHandler;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.entity.variants.CoatiVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.screen_handler.ModMenuTypes;
import net.emilsg.clutterbestiary.screen_handler.handler.CoatiScreenHandler;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.block.BlockRenderType;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class CoatiEntity extends ParentTameableEntity implements InventoryChangedListener {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(CoatiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<CoatiEntityAnimationState> ANIMATION_STATE = DataTracker.registerData(CoatiEntity.class, ModTrackedDataHandler.COATI_ANIMATION_STATE);
    private static final TrackedData<Boolean> DIGGING = DataTracker.registerData(CoatiEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> UNBURROWING = DataTracker.registerData(CoatiEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_CHEST = DataTracker.registerData(CoatiEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Integer> DAYS_FED_HONEY = DataTracker.registerData(CoatiEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> LAST_DAY_FED_HONEY = DataTracker.registerData(CoatiEntity.class, TrackedDataHandlerRegistry.LONG);
    private static final TrackedData<BlockPos> BURROW_POS = DataTracker.registerData(CoatiEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public final AnimationState diggingAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    public final AnimationState standingUpAnimationState = new AnimationState();
    public final AnimationState sniffingAnimationState = new AnimationState();
    public final AnimationState unBurrowingAnimationState = new AnimationState();
    public final AnimationState pickUpItemAnimationState = new AnimationState();

    private int postDigCooldown;
    private boolean digSessionActive = false;

    protected SimpleInventory coatiInventory;
    protected SimpleInventory wildInventory;

    private final Goal wanderOftenGoal = new WanderAroundFarOftenGoal(this, 1.0f);
    private final Goal wanderFarGoal = new WanderAroundFarGoal(this, 1.0f);

    private int forageGrace = 0;

    public CoatiEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.coatiInventory = new SimpleInventory(this.getInventorySize());
        this.wildInventory = new SimpleInventory(this.getWildInventorySize());
        this.coatiInventory.addListener(this);
        this.wildInventory.addListener(this);
        this.updateWanderingGoal();
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
    }

    public SimpleInventory getWildInventory() {
        return wildInventory;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new TameableEscapeDangerGoal(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.25D, 10.0f, 2.0f));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TamedTemptGoal(this, 1.0f, Ingredient.ofItems(Items.GLISTERING_MELON_SLICE), false));
        this.goalSelector.add(3, new CoatiUnburrowingGoal(this));
        this.goalSelector.add(4, new CoatiDigGoal(this));
        this.goalSelector.add(5, new CoatiFindBurrowGoal(this));
        this.goalSelector.add(6, new ForageItemsGoal(this, 1, 12.0));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 6f));

        //TODO Pick up dropped items.
        // TODO Pick up players items when tamed and equipped with a chest.
    }

    public void updateWanderingGoal() {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            this.goalSelector.remove(this.wanderFarGoal);
            this.goalSelector.remove(this.wanderOftenGoal);
            if (this.isTamed()) {
                this.goalSelector.add(7, this.wanderFarGoal);
            } else {
                this.goalSelector.add(7, this.wanderOftenGoal);
            }
        }
    }

    public boolean canFitInWild(ItemStack incoming) {
        // empty slot
        for (int i = 0; i < this.wildInventory.size(); i++) {
            if (this.wildInventory.getStack(i).isEmpty()) return true;
        }
        // combinable stack of the same item+nbt
        for (int i = 0; i < this.wildInventory.size(); i++) {
            ItemStack cur = this.wildInventory.getStack(i);
            if (!cur.isEmpty()
                    && cur.isOf(incoming.getItem())
                    && ItemStack.areItemsAndComponentsEqual(cur, incoming)
                    && cur.getCount() < cur.getMaxCount()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack insertInto(SimpleInventory inv, ItemStack stack) {
        // combine first
        for (int i = 0; i < inv.size() && !stack.isEmpty(); i++) {
            ItemStack cur = inv.getStack(i);
            if (!cur.isEmpty()
                    && cur.isOf(stack.getItem())
                    && ItemStack.areItemsAndComponentsEqual(cur, stack)
                    && cur.getCount() < cur.getMaxCount()) {
                int move = Math.min(stack.getCount(), cur.getMaxCount() - cur.getCount());
                cur.increment(move);
                stack.decrement(move);
            }
        }
        // empty slot
        for (int i = 0; i < inv.size() && !stack.isEmpty(); i++) {
            if (inv.getStack(i).isEmpty()) {
                inv.setStack(i, stack.copy());
                return ItemStack.EMPTY;
            }
        }
        return stack; // no fit
    }


    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        RegistryEntry<Biome> registryEntry = world.getBiome(this.getBlockPos());
        CoatiVariant variant;
        this.setBurrowPos(this.getBlockPos());

        if (registryEntry.matchesKey(BiomeKeys.JUNGLE) || registryEntry.matchesKey(BiomeKeys.BAMBOO_JUNGLE)) variant = CoatiVariant.JUNGLE;
        else variant = CoatiVariant.TAIGA;

        if (random.nextInt(100) == 0) variant = CoatiVariant.ALBINO;

        this.setVariant(variant);

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.225F);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        CoatiEntity child = ModEntityTypes.COATI.get().create(world);
        if (child != null) child.setVariant(CoatiVariant.getRandom());
        return child;
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        PassiveEntity child = this.createChild(world, other);

        if (child != null) {
            child.setBaby(true);
            child.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);

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

            this.breed(world, other, child);
            world.spawnEntityAndPassengers(child);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.GLISTERING_MELON_SLICE);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (this.isTamed() && this.isOwner(player) && !player.shouldCancelInteraction() && !ModUtil.inEitherHand(player, Items.CHEST)) {
            this.setSit(!this.isSitting());
            return ActionResult.SUCCESS;
        }

        if (!this.isTamed() && stack.isOf(Items.MELON_SLICE) && this.isBaby() && !player.shouldCancelInteraction()) {
            stack.decrementUnlessCreative(1, player);
            this.tryTame(player);
            return ActionResult.SUCCESS;
        }

        if (this.isTamed() && this.isOwner(player) && player.shouldCancelInteraction() && !this.isBaby() && this.hasChest()) {
            this.openInventory(player);
            return ActionResult.SUCCESS;
        }

        if (stack.isOf(Items.CHEST) && !this.isBaby() && !this.hasChest() && this.isTamed() && this.isOwner(player) && !player.shouldCancelInteraction()) {
            this.addChest(player, stack);
            return ActionResult.SUCCESS;
        }

        if (stack.isOf(Items.HONEY_BOTTLE) && !this.isBaby() && !this.isTamed() && !player.shouldCancelInteraction()) {
            int streak;

            long day = this.getWorld().getTimeOfDay() / 24000L;
            long lastDayFedHoney = this.getLastDayFedHoney();
            if (lastDayFedHoney == day) return ActionResult.CONSUME;

            if (this.getWorld().isClient) return ActionResult.SUCCESS;

            if (lastDayFedHoney == day -1) {
                streak = this.getDaysFedHoney() + 1;
            } else {
                streak = 1;
            }

            this.setDaysFedHoney(streak);
            this.setLastDayFedHoney(day);

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
                player.giveItemStack(new ItemStack(Items.GLASS_BOTTLE));
            }
            return ActionResult.CONSUME;
        }

        if (this.isBreedingItem(stack)) {
            int i = this.getBreedingAge();
            if (!this.getWorld().isClient && i == 0 && this.canEat() && this.isTamed()) {
                this.eat(player, hand, stack);
                this.lovePlayer(player);
                return ActionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.eat(player, hand, stack);
                this.growUp(toGrowUpAge(-i), true);
                return ActionResult.success(this.getWorld().isClient);
            }

            if (this.getWorld().isClient) {
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    private void tryTame(PlayerEntity player) {
        if (this.random.nextInt(3) == 0) {
            this.setOwner(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setSit(true);
            this.updateWanderingGoal();
            this.getWorld().sendEntityStatus(this, (byte)7);
        } else {
            this.getWorld().sendEntityStatus(this, (byte)6);
        }
    }

    public void setSit(boolean sitting) {
        this.setSitting(sitting);
        this.startState(sitting ? CoatiEntityAnimationState.SITTING : CoatiEntityAnimationState.STANDING_UP);
    }

    @Override
    protected float getSoundVolume() {
        return 1.5f;
    }

    public ActionResult openInventory(PlayerEntity player) {
        if (this.getWorld().isClient) {
            return ActionResult.SUCCESS;
        } else {
            if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                this.openCoatiMenu(serverPlayerEntity);
            }
            return ActionResult.CONSUME;
        }
    }

    public void openCoatiMenu(ServerPlayerEntity player) {
        CoatiEntity self = this;
        MenuRegistry.openExtendedMenu(player, new ExtendedMenuProvider() {

            public void saveExtraData(PacketByteBuf buf) {
                buf.writeInt(self.getId());
            }

            public Text getDisplayName() {
                return self.getDisplayName();
            }

            public ScreenHandler createMenu(int id, PlayerInventory inv, PlayerEntity p) {
                return new CoatiScreenHandler(id, inv, self);
            }
        });
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_COATI_AMBIENT.get();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANIMATION_STATE, CoatiEntityAnimationState.IDLING);
        builder.add(VARIANT, CoatiVariant.JUNGLE.getId());
        builder.add(DAYS_FED_HONEY, 0);
        builder.add(LAST_DAY_FED_HONEY, -1L);
        builder.add(BURROW_POS, this.getBlockPos().down());
        builder.add(DIGGING, false);
        builder.add(UNBURROWING, false);
        builder.add(HAS_CHEST, false);
    }

    public SimpleInventory getCoatiInventory() {
        return coatiInventory;
    }

    public int getForageGrace() {
        return forageGrace;
    }

    public void setForageGrace(int forageGrace) {
        this.forageGrace = forageGrace;
    }

    public boolean isUnBurrowing() {
        return this.dataTracker.get(UNBURROWING);
    }

    public void setUnBurrowing(boolean burrowing) {
        this.dataTracker.set(UNBURROWING, burrowing);
    }

    public boolean isDigSessionActive() {
        return this.digSessionActive;
    }
    public void setDigSessionActive(boolean digSessionActive) {
        this.digSessionActive = digSessionActive;
    }

    public int getPostDigCooldown() {
        return this.postDigCooldown;
    }
    public void setPostDigCooldown(int postDigCooldown) {
        this.postDigCooldown = postDigCooldown;
    }

    public boolean isDigging() {
        return this.dataTracker.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.dataTracker.set(DIGGING, digging);
    }

    public BlockPos getBurrowPos() {
        return this.dataTracker.get(BURROW_POS);
    }

    public void setBurrowPos(BlockPos burrowPos) {
        this.dataTracker.set(BURROW_POS, burrowPos);
    }

    public int getDaysFedHoney() {
        return this.dataTracker.get(DAYS_FED_HONEY);
    }

    public void setDaysFedHoney(int daysFedHoney) {
        this.dataTracker.set(DAYS_FED_HONEY, daysFedHoney);
    }

    public long getLastDayFedHoney() {
        return this.dataTracker.get(LAST_DAY_FED_HONEY);
    }

    public void setLastDayFedHoney(long lastDayFedHoney) {
        this.dataTracker.set(LAST_DAY_FED_HONEY, lastDayFedHoney);
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public CoatiVariant getVariant() {
        return CoatiVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(CoatiVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public boolean hasChest() {
        return this.dataTracker.get(HAS_CHEST);
    }
    public void setHasChest(boolean hasChest) {
        this.dataTracker.set(HAS_CHEST, hasChest);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
        this.dataTracker.set(BURROW_POS, BlockPos.ofFloored(
                nbt.getDouble("BurrowPosX"),
                nbt.getDouble("BurrowPosY"),
                nbt.getDouble("BurrowPosZ")));
        this.dataTracker.set(DAYS_FED_HONEY, nbt.getInt("DaysFedHoney"));
        this.setLastDayFedHoney(nbt.getLong("LastDayFedHoney"));

        this.setHasChest(nbt.getBoolean("HasChest"));
        this.onChestedStatusChanged();

        if (this.hasChest()) {
            NbtList nbtList = nbt.getList("Items", 10);

            for (int i = 0; i < nbtList.size(); i++) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                int j = nbtCompound.getByte("Slot") & 255;
                if (j < this.coatiInventory.size() - 1) {
                    this.coatiInventory.setStack(j + 1, (ItemStack)ItemStack.fromNbt(this.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY));
                }
            }
        }

        if (!this.isTamed()) {
            NbtList list = nbt.getList("WildItems", 10);
            for (int k = 0; k < list.size(); k++) {
                NbtCompound c = list.getCompound(k);
                int slot = c.getByte("WildSlot") & 255;
                if (slot < this.wildInventory.size()) {
                    this.wildInventory.setStack(slot, ItemStack.fromNbt(this.getRegistryManager(), c).orElse(ItemStack.EMPTY));
                }
            }
        }
    }

    public void clearInventory(SimpleInventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putString("Variant", this.getTypeVariant());
        nbt.putDouble("BurrowPosX", this.getBurrowPos().getX());
        nbt.putDouble("BurrowPosY", this.getBurrowPos().getY());
        nbt.putDouble("BurrowPosZ", this.getBurrowPos().getZ());
        nbt.putInt("DaysFedHoney", this.getDaysFedHoney());
        nbt.putLong("LastDayFedHoney", this.getLastDayFedHoney());

        nbt.putBoolean("HasChest", this.hasChest());

        if (this.hasChest()) {
            NbtList nbtList = new NbtList();

            for (int i = 1; i < this.coatiInventory.size(); i++) {
                ItemStack itemStack = this.coatiInventory.getStack(i);
                if (!itemStack.isEmpty()) {
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.putByte("Slot", (byte)(i - 1));
                    nbtList.add(itemStack.encode(this.getRegistryManager(), nbtCompound));
                }
            }

            nbt.put("Items", nbtList);
        }

        if (!this.isTamed()) {
            NbtList wild = new NbtList();
            for (int i = 0; i < this.wildInventory.size(); i++) {
                ItemStack it = this.wildInventory.getStack(i);
                if (!it.isEmpty()) {
                    NbtCompound c = new NbtCompound();
                    c.putByte("WildSlot", (byte)i);
                    wild.add(it.encode(this.getRegistryManager(), c));
                }
            }
            nbt.put("WildItems", wild);
        }
    }

    protected void onChestedStatusChanged() {
        SimpleInventory simpleInventory = this.coatiInventory;
        this.coatiInventory = new SimpleInventory(this.getInventorySize());
        if (simpleInventory != null) {
            simpleInventory.removeListener(this);
            int i = Math.min(simpleInventory.size(), this.coatiInventory.size());

            for (int j = 0; j < i; j++) {
                ItemStack itemStack = simpleInventory.getStack(j);
                if (!itemStack.isEmpty()) {
                    this.coatiInventory.setStack(j, itemStack.copy());
                }
            }
        }

        this.coatiInventory.addListener(this);
    }

    @Override
    public StackReference getStackReference(int mappedIndex) {
        return mappedIndex == 499 ? new StackReference() {
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
                } else if (stack.isOf(Items.CHEST)) {
                    if (!CoatiEntity.this.hasChest()) {
                        CoatiEntity.this.setHasChest(true);
                        CoatiEntity.this.onChestedStatusChanged();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } : super.getStackReference(mappedIndex);
    }

    private void addChest(PlayerEntity player, ItemStack chest) {
        this.setHasChest(true);
        this.playAddChestSound();
        chest.decrementUnlessCreative(1, player);
        this.onChestedStatusChanged();
    }

    protected void playAddChestSound() {
        this.playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    public final int getInventorySize() {
        return 8;
    }

    public final int getWildInventorySize() {
        return 16;
    }

    @Override
    public void tick() {
        World world = this.getWorld();
        BlockState blockState = world.getBlockState(this.getBurrowPos().down());

        if (!world.isClient && this.getForageGrace() > 0) this.forageGrace--;

        if (postDigCooldown > 0) postDigCooldown--;

        if (world.isClient && blockState.getRenderType() != BlockRenderType.INVISIBLE && this.isDigging() && this.diggingAnimationState.isRunning()) {
            Vec3d particlePos = this.getPos();
            if (this.age % 10 == 0) {
                for (int i = 0; i < 8; i++) {
                    world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), particlePos.getX(), particlePos.getY(), particlePos.getZ(), 0.0, 0, 0.0);
                }
            }
        }

        super.tick();
    }

    //Animation Handling

    private CoatiEntityAnimationState getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    private void setState(CoatiEntityAnimationState state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            CoatiEntityAnimationState state = this.getState();
            this.stopAnimations();
            switch (state.ordinal()) {
                case 1:
                    this.sittingAnimationState.startIfNotRunning(this.age);
                    break;
                case 2:
                    this.standingUpAnimationState.startIfNotRunning(this.age);
                    break;
                case 3:
                    this.sniffingAnimationState.startIfNotRunning(this.age);
                    break;
                case 4:
                    this.diggingAnimationState.startIfNotRunning(this.age);
                    break;
                case 5:
                    this.unBurrowingAnimationState.startIfNotRunning(this.age);
                    break;
                case 6:
                    this.pickUpItemAnimationState.startIfNotRunning(this.age);
                    break;
                default:
                    break;
            }

            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    public void stopAnimations() {
        this.sittingAnimationState.stop();
        this.standingUpAnimationState.stop();
        this.sniffingAnimationState.stop();
        this.diggingAnimationState.stop();
        this.unBurrowingAnimationState.stop();
        this.pickUpItemAnimationState.stop();
    }

    public void startState(CoatiEntityAnimationState state) {
        switch (state.ordinal()) {
            case 0:
                this.setState(CoatiEntityAnimationState.IDLING);
                break;
            case 1:
                this.setState(CoatiEntityAnimationState.SITTING);
                break;
            case 2:
                this.setState(CoatiEntityAnimationState.STANDING_UP);
                break;
            case 3:
                this.setState(CoatiEntityAnimationState.SNIFFING);
                break;
            case 4:
                this.setState(CoatiEntityAnimationState.DIGGING);
                break;
            case 5:
                this.setState(CoatiEntityAnimationState.UNBURROWING);
                break;
            case 6:
                this.setState(CoatiEntityAnimationState.PICKING_UP_ITEM);
                break;
        }
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(CoatiEntityAnimationState.IDLING);
        if (!getWorld().isClient) {
            for (int i = 0; i < coatiInventory.size(); i++) {
                ItemStack s = coatiInventory.getStack(i);
                if (!s.isEmpty()) this.dropStack(s);
            }
            for (int i = 0; i < wildInventory.size(); i++) {
                ItemStack s = wildInventory.getStack(i);
                if (!s.isEmpty()) this.dropStack(s);
            }
            if (hasChest()) this.dropStack(new ItemStack(Items.CHEST));
        }
        super.onDeath(damageSource);
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.1f, 0.8F);
    }

    public static enum CoatiEntityAnimationState {
        IDLING(0),
        SITTING(1),
        STANDING_UP(2),
        SNIFFING(3),
        DIGGING(4),
        UNBURROWING(5),
        PICKING_UP_ITEM(6);

        public static final IntFunction<CoatiEntityAnimationState> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(CoatiEntityAnimationState::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO);
        public static final PacketCodec<ByteBuf, CoatiEntityAnimationState> PACKET_CODEC = PacketCodecs.indexed(INDEX_TO_VALUE, CoatiEntityAnimationState::getIndex);
        private final int index;

        CoatiEntityAnimationState(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }
}