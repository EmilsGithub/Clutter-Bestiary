package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.BeaverStripBottomLogGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.BeaverStripItemsGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.HighWanderAroundFarGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.LeaveWaterGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class BeaverEntity extends ParentAnimalEntity implements InventoryCarrier {
    private static final Predicate<ItemStack> BREEDING_INGREDIENT = stack -> ModUtil.SAPLING_ITEM_MAP.contains(stack.getItem()) || stack.is(ItemTags.SAPLINGS);
    private static final EntityDataAccessor<Boolean> IS_STRIPPING_ITEMS = SynchedEntityData.defineId(BeaverEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(BeaverEntity.class, EntityDataSerializers.INT);

    private static final String NBT_STRIPPING = "StrippingItems";
    private static final String NBT_STRIP_TIMER = "StripTimer";
    private static final String NBT_LOGS_STRIPPED = "LogsStripped";
    private static final String NBT_STRIPPING_PLAYER = "StrippingPlayer";

    public final AnimationState waterAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idlingAnimationState = new AnimationState();
    public final AnimationState strippingItemsAnimationState = new AnimationState();
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation landNavigation;
    private final SimpleContainer inventory = new SimpleContainer(1);
    private int waterAnimationTimeout = 0;
    private int idleAnimationTimeout = 0;
    private int strippingAnimationTimer = 0;
    private int logsStripped = 0;
    @Nullable
    private UUID strippingPlayerUuid;
    private boolean shouldSpawnStrippingParticles = false;

    public BeaverEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.moveControl = new BeaverMoveControl(this);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, world);
        this.landNavigation = new GroundPathNavigation(this, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreedGoal(this, 1.0f));
        this.goalSelector.addGoal(1, new BeaverStripItemsGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1f, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0f));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2f, true));
        this.goalSelector.addGoal(5, new BeaverStripBottomLogGoal(this, 1.0f));
        this.goalSelector.addGoal(6, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.addGoal(6, new LeaveWaterGoal(this, 1.0f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_STRIPPING_ITEMS, false);
        builder.define(ANIMATION_STATE, BeaverEntityAnimationState.IDLING.getIndex());
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.readInventoryFromTag(nbt);
        boolean restoreStripping = nbt.getBooleanOr(NBT_STRIPPING, false);
        this.strippingAnimationTimer = nbt.getIntOr(NBT_STRIP_TIMER, 0);
        this.logsStripped = nbt.getIntOr(NBT_LOGS_STRIPPED, 0);
        this.strippingPlayerUuid = nbt.read(NBT_STRIPPING_PLAYER, UUIDUtil.CODEC).orElse(null);

        this.setStrippingItems(restoreStripping);

        if (restoreStripping && !this.inventory.isEmpty()) {
            ItemStack s = this.inventory.getItem(0);
            this.setItemInHand(InteractionHand.MAIN_HAND, s.copy());
            this.startState(BeaverEntityAnimationState.STRIPPING_ITEMS);
        } else {
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.startState(BeaverEntityAnimationState.IDLING);
            if (!restoreStripping) this.strippingAnimationTimer = 0;
        }
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        this.writeInventoryToTag(nbt);
        nbt.putBoolean(NBT_STRIPPING, this.isStrippingItems());
        nbt.putInt(NBT_STRIP_TIMER, this.strippingAnimationTimer);
        nbt.putInt(NBT_LOGS_STRIPPED, this.logsStripped);
        if (this.strippingPlayerUuid != null) {
            nbt.store(NBT_STRIPPING_PLAYER, UUIDUtil.CODEC, this.strippingPlayerUuid);
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 2.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.BEAVERS_SPAWN_ON);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.BEAVER.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public SimpleContainer getInventory() {
        return inventory;
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        var strippedItem = ModUtil.getStrippedItem(stackInHand);

        if (strippedItem != null) {
            if (!this.inventory.isEmpty() || this.isInWater()) return InteractionResult.PASS;

            if (stackInHand.getCount() >= 16) {
                this.inventory.setItem(0, stackInHand.copyWithCount(16));
                this.setItemInHand(InteractionHand.MAIN_HAND, stackInHand.copyWithCount(16));
                stackInHand.consume(16, player);
            } else {
                this.inventory.setItem(0, stackInHand.copy());
                this.setItemInHand(InteractionHand.MAIN_HAND, stackInHand.copy());
                stackInHand.setCount(0);
            }

            this.strippingAnimationTimer = 0;
            if (!this.level().isClientSide()) {
                this.strippingPlayerUuid = player.getUUID();
            }
            this.setStrippingItems(true);
            if (!this.level().isClientSide()) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL);
                if (this.shouldPlayChainsawSound())
                    this.level().playSound(null, this.blockPosition(), ModSoundEvents.ENTITY_BEAVER_CHAINSAW.get(), SoundSource.NEUTRAL);
            }

            this.startState(BeaverEntityAnimationState.STRIPPING_ITEMS);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public boolean isStrippingItems() {
        return this.entityData.get(IS_STRIPPING_ITEMS);
    }

    public void setStrippingItems(boolean strippingItems) {
        this.entityData.set(IS_STRIPPING_ITEMS, strippingItems);
    }

    public void die(DamageSource damageSource) {
        this.startState(BeaverEntityAnimationState.IDLING);
        this.spawnAtLocation((ServerLevel) this.level(), this.inventory.getItem(0));
        super.die(damageSource);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            int state = this.getState();
            this.stopAnimations();
            switch (state) {
                case 1:
                    this.strippingItemsAnimationState.startIfStopped(this.tickCount);
                    break;
                default:
                    this.idlingAnimationState.startIfStopped(this.tickCount);
                    break;
            }

            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(data);
    }

    public void setShouldSpawnStrippingParticles(boolean spawnStrippingParticles) {
        this.shouldSpawnStrippingParticles = spawnStrippingParticles;
    }

    public boolean shouldPlayChainsawSound() {
        return this.getDisplayName() != null && (this.getDisplayName().toString().toLowerCase().contains("chainsaw") || this.getDisplayName().toString().toLowerCase().contains("bubba"));
    }

    public boolean shouldSpawnStrippingParticles() {
        return this.shouldSpawnStrippingParticles;
    }

    public void startState(BeaverEntityAnimationState state) {
        switch (state.ordinal()) {
            case 0:
                this.setState(BeaverEntityAnimationState.IDLING.getIndex());
                break;
            case 1:
                this.setState(BeaverEntityAnimationState.STRIPPING_ITEMS.getIndex());
                break;
        }
    }

    public void stopAnimations() {
        this.idlingAnimationState.stop();
        this.strippingItemsAnimationState.stop();
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (!this.getInventory().isEmpty() && this.isStrippingItems() && this.isAlive()) {
            this.strippingAnimationTimer++;
            if ((this.strippingAnimationTimer <= 70 || (this.strippingAnimationTimer >= 100 && this.strippingAnimationTimer < 140))) {
                if ((this.tickCount % 5) == 0 && !world.isClientSide() && !this.shouldPlayChainsawSound())
                    world.playSound(null, this.blockPosition(), SoundEvents.AXE_STRIP.value(), SoundSource.NEUTRAL);
                this.setShouldSpawnStrippingParticles(true);
            } else {
                this.setShouldSpawnStrippingParticles(false);
            }
        }

        if (!world.isClientSide() && strippingAnimationTimer >= 160 && this.isAlive()) {
            this.setStrippingItems(false);
            this.startState(BeaverEntityAnimationState.IDLING);
            ItemStack inventoryStack = this.inventory.getItem(0);
            Item strippedItem = ModUtil.getStrippedItem(inventoryStack);
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

            if (strippedItem == null) {
                this.spawnAtLocation((ServerLevel) this.level(), inventoryStack);
                this.inventory.setItem(0, ItemStack.EMPTY);
                this.strippingAnimationTimer = 0;
                this.strippingPlayerUuid = null;
                return;
            }

            this.finishStrippingHandedLogs(inventoryStack.getCount());

            ItemStack strippedStack = new ItemStack(strippedItem, inventoryStack.getCount());
            this.inventory.setItem(0, strippedStack);

            var vec = this.getLookAngle().normalize().scale(0.5);
            double x = this.getX() + vec.x;
            double y = this.getY() + 0.2;
            double z = this.getZ() + vec.z;

            ItemEntity item = new ItemEntity(this.level(), x, y, z, this.inventory.getItem(0));
            item.setDeltaMovement(vec.x * 0.1, 0.1, vec.z * 0.1);
            this.level().addFreshEntity(item);

            if (!this.level().isClientSide())
                this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL);

            this.startState(BeaverEntityAnimationState.IDLING);
            this.strippingAnimationTimer = 0;
            this.strippingPlayerUuid = null;
            this.inventory.setItem(0, ItemStack.EMPTY);
        }

        if (!this.isStrippingItems()) this.strippingAnimationTimer = 0;

        if (this.isStrippingItems()) {
            if (this.getMainHandItem().isEmpty() && !this.inventory.isEmpty()) {
                this.setItemInHand(InteractionHand.MAIN_HAND, this.inventory.getItem(0).copy());
            }
        }

        this.setupAnimationStates();

    }

    public void onWorldLogStripped() {
        if (!(this.level() instanceof ServerLevel serverWorld)) return;

        serverWorld.getPlayers(player -> player.distanceToSqr(this) <= 256.0)
                .forEach(player -> ModAdvancements.grant(player, ModAdvancements.DAM_GOOD_WORK));
    }

    private void finishStrippingHandedLogs(int amount) {
        if (!(this.level() instanceof ServerLevel serverWorld)) return;

        this.logsStripped = Math.min(25, this.logsStripped + amount);
        if (this.logsStripped < 25 || this.strippingPlayerUuid == null) return;

        ServerPlayer player = serverWorld.getServer().getPlayerList().getPlayer(this.strippingPlayerUuid);
        if (player != null) {
            ModAdvancements.grant(player, ModAdvancements.BUSY_BEAVER);
        }
    }

    public void travel(Vec3 movementInput) {
        if (this.isLocalInstanceAuthoritative() && this.isInWater()) {
            this.moveRelative(0.1F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    protected PathNavigation createNavigation(Level world) {
        return new BeaverSwimNavigation(this, world);
    }


    //Animation

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 1.15f, 0.5F, 1.0F);
    }

    private int getState() {
        return this.entityData.get(ANIMATION_STATE);
    }

    private void setState(int state) {
        this.entityData.set(ANIMATION_STATE, state);
    }

    private void setupAnimationStates() {
        if (this.waterAnimationTimeout <= 0) {
            this.waterAnimationTimeout = 20;
            this.waterAnimationState.startIfStopped(this.tickCount);
        } else {
            --this.waterAnimationTimeout;
        }

        if (this.idleAnimationTimeout <= 0 && random.nextInt(200) == 0 && !this.isStrippingItems()) {
            this.idleAnimationTimeout = 20;
            this.idleAnimationState.startIfStopped(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    public enum BeaverEntityAnimationState {
        IDLING(0),
        STRIPPING_ITEMS(1);

        private final int index;

        BeaverEntityAnimationState(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

    private static class BeaverMoveControl extends MoveControl {
        private final BeaverEntity beaver;

        public BeaverMoveControl(BeaverEntity beaver) {
            super(beaver);
            this.beaver = beaver;
        }

        public void tick() {
            if (this.beaver.isInWater()) {
                this.beaver.setDeltaMovement(this.beaver.getDeltaMovement().add(0.0, 0.005, 0.0));

                if (this.operation != Operation.MOVE_TO || this.beaver.getNavigation().isDone()) {
                    this.beaver.setSpeed(0.0F);
                    return;
                }

                double d = this.wantedX - this.beaver.getX();
                double e = this.wantedY - this.beaver.getY();
                double f = this.wantedZ - this.beaver.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float) (Mth.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.beaver.setYRot(this.rotlerp(this.beaver.getYRot(), h, 90.0F));
                this.beaver.yBodyRot = this.beaver.getYRot();
                float i = (float) (this.speedModifier * this.beaver.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float j = Mth.lerp(0.125F, this.beaver.getSpeed(), i);
                this.beaver.setSpeed(j);
                this.beaver.setDeltaMovement(this.beaver.getDeltaMovement().add(0, this.beaver.getSpeed() * e * 0.1, 0));
            } else {
                this.beaver.setDeltaMovement(this.beaver.getDeltaMovement().add(0.0, 0, 0.0));
                super.tick();
            }
        }
    }

    private static class BeaverSwimNavigation extends AmphibiousPathNavigation {
        BeaverSwimNavigation(BeaverEntity owner, Level world) {
            super(owner, world);
        }

        public boolean isStableDestination(BlockPos pos) {
            if (this.mob instanceof BeaverEntity) {
                return this.level.getBlockState(pos).is(Blocks.WATER) || (this.level.getBlockState(pos).canBeReplaced() && this.level.getBlockState(pos.below()).isRedstoneConductor(this.level, pos.below()));
            }

            return !this.level.getBlockState(pos.below()).isAir();
        }
    }
}
