package net.emilsg.clutterbestiary.entity.custom;

import io.netty.buffer.ByteBuf;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.ModTrackedDataHandler;
import net.emilsg.clutterbestiary.entity.custom.goal.BeaverStripBottomLogGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.HighWanderAroundFarGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class BeaverEntity extends ParentAnimalEntity implements InventoryOwner {
    private static final TrackedData<BlockPos> HOME_POS = DataTracker.registerData(BeaverEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Boolean> IS_STRIPPING_ITEMS = DataTracker.registerData(BeaverEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BeaverEntityAnimationState> ANIMATION_STATE = DataTracker.registerData(BeaverEntity.class, ModTrackedDataHandler.BEAVER_ANIMATION_STATE);

    private static final Ingredient BREEDING_INGREDIENT = getIngredientWithName("sapling");

    public final AnimationState waterAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState strippingItemsAnimationState = new AnimationState();
    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;
    private int waterAnimationTimeout = 0;
    private int idleAnimationTimeout = 0;
    private final SimpleInventory inventory = new SimpleInventory(1);
    private int strippingAnimationTimer = 0;

    public BeaverEntity(EntityType<? extends ParentAnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.moveControl = new BeaverMoveControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
    }

    private static Ingredient getIngredientWithName(String name) {
        List<Item> items = new ArrayList<>();

        Registries.ITEM.forEach(item -> {
            Identifier id = Registries.ITEM.getId(item);
            if (id.getPath().contains(name)) {
                items.add(item);
            }
        });

        return Ingredient.ofItems(items.toArray(new Item[0]));
    }

    @Override
    public float getStepHeight() {
        return 1.0f;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.BEAVERS_SPAWN_ON);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setHomePos(this.getBlockPos());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.BEAVER.get().create(world);
    }

    @Override
    public void detachLeash(boolean sendPacket, boolean dropItem) {
        this.setHomePos(this.getBlockPos());
        super.detachLeash(sendPacket, dropItem);
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (!this.getInventory().isEmpty() && this.isStrippingItems()) {
            this.strippingAnimationTimer++;
        }

        if (strippingAnimationTimer >= 160) {
            this.setStrippingItems(false);
            this.startState(BeaverEntityAnimationState.IDLING);

            ItemStack inventoryStack = this.inventory.getStack(0);
            Item strippedItem = this.getStrippedItem(inventoryStack);
            if (strippedItem == null) {
                this.dropStack(inventoryStack);
                this.inventory.setStack(0, ItemStack.EMPTY);
                this.strippingAnimationTimer = 0;
                this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                return;
            }
            ItemStack strippedStack = new ItemStack(strippedItem, inventoryStack.getCount());
            this.inventory.setStack(0, strippedStack);
            this.dropStack(this.inventory.getStack(0));
            this.startState(BeaverEntityAnimationState.IDLING);
            this.strippingAnimationTimer = 0;
            this.inventory.setStack(0, ItemStack.EMPTY);
            this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

        }

    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);

        var strippedItem = getStrippedItem(stackInHand);
        if (strippedItem == null || !this.inventory.isEmpty() || this.isInsideWaterOrBubbleColumn()) return ActionResult.PASS;

        this.setStackInHand(Hand.MAIN_HAND, stackInHand);
        stackInHand.setCount(0);
        this.strippingAnimationTimer = 0;
        this.setStrippingItems(true);
        this.startState(BeaverEntityAnimationState.STRIPPING_ITEMS);

        return ActionResult.SUCCESS;
    }

    @Nullable
    private Item getStrippedItem(ItemStack itemStack) {
        String itemID = Registries.ITEM.getId(itemStack.getItem()).toString();

        String[] parts = itemID.split(":", 2);
        String namespace = parts[0];
        String path = parts[1];

        String strippedPath = "stripped_" + path;
        Identifier strippedID = Identifier.of(namespace, strippedPath);
        if (Registries.ITEM.containsId(strippedID)) {
            return Registries.ITEM.get(strippedID);
        }
        return null;
    }



    @Override
    protected int getNextAirUnderwater(int air) {
        return air;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        int i = nbt.getInt("HomePosX");
        int j = nbt.getInt("HomePosY");
        int k = nbt.getInt("HomePosZ");
        this.setHomePos(new BlockPos(i, j, k));
        this.readInventory(nbt, this.getRegistryManager());
    }

    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("HomePosX", this.getHomePos().getX());
        nbt.putInt("HomePosY", this.getHomePos().getY());
        nbt.putInt("HomePosZ", this.getHomePos().getZ());
        this.writeInventory(nbt, this.getRegistryManager());
    }

    public boolean isStrippingItems() {
        return this.dataTracker.get(IS_STRIPPING_ITEMS);
    }

    public void setStrippingItems(boolean strippingItems) {
        this.dataTracker.set(IS_STRIPPING_ITEMS, strippingItems);
    }

    public BlockPos getHomePos() {
        return this.dataTracker.get(HOME_POS);
    }

    public void setHomePos(BlockPos pos) {
        this.dataTracker.set(HOME_POS, pos);
    }

    protected EntityNavigation createNavigation(World world) {
        return new BeaverSwimNavigation(this, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HOME_POS, BlockPos.ORIGIN);
        builder.add(IS_STRIPPING_ITEMS, false);
        builder.add(ANIMATION_STATE, BeaverEntityAnimationState.IDLING);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new AnimalMateGoal(this, 1.0f));
        this.goalSelector.add(1, new BeaverTemptGoal(this, 1.1f, BREEDING_INGREDIENT, false));
        this.goalSelector.add(2, new FollowParentGoal(this, 1.0f));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2f, true));
        this.goalSelector.add(4, new BeaverStripBottomLogGoal(this, 1.0f));
        this.goalSelector.add(5, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(3, new RevengeGoal(this));
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f, 0.5F);
    }

    @Override
    public SimpleInventory getInventory() {
        return inventory;
    }

    private static class BeaverMoveControl extends MoveControl {
        private final BeaverEntity beaver;

        public BeaverMoveControl(BeaverEntity beaver) {
            super(beaver);
            this.beaver = beaver;
        }

        public void tick() {
            if (this.beaver.isTouchingWater()) {
                this.beaver.setVelocity(this.beaver.getVelocity().add(0.0, 0.005, 0.0));

                if (this.state != State.MOVE_TO || this.beaver.getNavigation().isIdle()) {
                    this.beaver.setMovementSpeed(0.0F);
                    return;
                }

                double d = this.targetX - this.beaver.getX();
                double e = this.targetY - this.beaver.getY();
                double f = this.targetZ - this.beaver.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.beaver.setYaw(this.wrapDegrees(this.beaver.getYaw(), h, 90.0F));
                this.beaver.bodyYaw = this.beaver.getYaw();
                float i = (float) (this.speed * this.beaver.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125F, this.beaver.getMovementSpeed(), i);
                this.beaver.setMovementSpeed(j);
                this.beaver.setVelocity(this.beaver.getVelocity().add(0, this.beaver.getMovementSpeed() * e * 0.1, 0));
            } else {
                this.beaver.setVelocity(this.beaver.getVelocity().add(0.0, 0, 0.0));
                super.tick();
            }
        }
    }

    private static class BeaverTemptGoal extends TemptGoal {
        private final BeaverEntity beaver;

        public BeaverTemptGoal(PathAwareEntity entity, double speed, Ingredient food, boolean canBeScared) {
            super(entity, speed, food, canBeScared);
            this.beaver = (BeaverEntity) entity;
        }

        @Override
        public void stop() {
            super.stop();
            this.beaver.setHomePos(this.beaver.getBlockPos());
        }
    }

    private static class BeaverSwimNavigation extends AmphibiousSwimNavigation {
        BeaverSwimNavigation(BeaverEntity owner, World world) {
            super(owner, world);
        }

        public boolean isValidPosition(BlockPos pos) {
            if (this.entity instanceof BeaverEntity) {
                return this.world.getBlockState(pos).isOf(Blocks.WATER) || (this.world.getBlockState(pos).isReplaceable() && this.world.getBlockState(pos.down()).isSolidBlock(this.world, pos.down()));
            }

            return !this.world.getBlockState(pos.down()).isAir();
        }
    }


    //Animation

    private BeaverEntityAnimationState getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    private void setState(BeaverEntityAnimationState state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            BeaverEntityAnimationState state = this.getState();
            this.stopAnimations();
            switch (state.ordinal()) {
                case 1:
                    this.strippingItemsAnimationState.startIfNotRunning(this.age);
                    break;
                default:
                    this.idleAnimationState.startIfNotRunning(this.age);
                    break;
            }

            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    public void stopAnimations() {
        this.idleAnimationState.stop();
        this.strippingItemsAnimationState.stop();
    }

    public void startState(BeaverEntityAnimationState state) {
        switch (state.ordinal()) {
            case 0:
                this.setState(BeaverEntityAnimationState.IDLING);
                break;
            case 1:
                this.setState(BeaverEntityAnimationState.STRIPPING_ITEMS);
                break;
        }
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(BeaverEntityAnimationState.IDLING);
        super.onDeath(damageSource);
    }

    public static enum BeaverEntityAnimationState {
        IDLING(0),
        STRIPPING_ITEMS(1);

        public static final IntFunction<BeaverEntityAnimationState> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(BeaverEntityAnimationState::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO);
        public static final PacketCodec<ByteBuf, BeaverEntityAnimationState> PACKET_CODEC = PacketCodecs.indexed(INDEX_TO_VALUE, BeaverEntityAnimationState::getIndex);
        private final int index;

        BeaverEntityAnimationState(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }
}
