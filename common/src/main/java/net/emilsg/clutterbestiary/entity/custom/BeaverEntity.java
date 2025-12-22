package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.BeaverStripBottomLogGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.BeaverStripItemsGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.HighWanderAroundFarGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.LeaveWaterGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.emilsg.clutterbestiary.util.ModUtil;
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
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BeaverEntity extends ParentAnimalEntity implements InventoryOwner {
    private static final TrackedData<Boolean> IS_STRIPPING_ITEMS = DataTracker.registerData(BeaverEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(BeaverEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final String NBT_STRIPPING = "StrippingItems";
    private static final String NBT_STRIP_TIMER = "StripTimer";

    public final AnimationState waterAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idlingAnimationState = new AnimationState();
    public final AnimationState strippingItemsAnimationState = new AnimationState();
    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;
    private final SimpleInventory inventory = new SimpleInventory(1);
    private int waterAnimationTimeout = 0;
    private int idleAnimationTimeout = 0;
    private int strippingAnimationTimer = 0;
    private boolean shouldSpawnStrippingParticles = false;

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

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new AnimalMateGoal(this, 1.0f));
        this.goalSelector.add(1, new BeaverStripItemsGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 1.1f, stack -> ModUtil.SAPLING_ITEM_MAP.contains(stack.getItem()) || stack.isIn(ItemTags.SAPLINGS), false));
        this.goalSelector.add(3, new FollowParentGoal(this, 1.0f));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.2f, true));
        this.goalSelector.add(5, new BeaverStripBottomLogGoal(this, 1.0f));
        this.goalSelector.add(6, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.add(6, new LeaveWaterGoal(this, 1.0f));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(3, new RevengeGoal(this));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_STRIPPING_ITEMS, false);
        builder.add(ANIMATION_STATE, BeaverEntityAnimationState.IDLING.getIndex());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readInventory(nbt, this.getRegistryManager());
        boolean restoreStripping = nbt.getBoolean(NBT_STRIPPING);
        this.strippingAnimationTimer = nbt.getInt(NBT_STRIP_TIMER);

        this.setStrippingItems(restoreStripping);

        if (restoreStripping && !this.inventory.isEmpty()) {
            ItemStack s = this.inventory.getStack(0);
            this.setStackInHand(Hand.MAIN_HAND, s.copy());
            this.startState(BeaverEntityAnimationState.STRIPPING_ITEMS);
        } else {
            this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            this.startState(BeaverEntityAnimationState.IDLING);
            if (!restoreStripping) this.strippingAnimationTimer = 0;
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeInventory(nbt, this.getRegistryManager());
        nbt.putBoolean(NBT_STRIPPING, this.isStrippingItems());
        nbt.putInt(NBT_STRIP_TIMER, this.strippingAnimationTimer);
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

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.BEAVER.get().create(world);
    }

    @Override
    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        var strippedItem = ModUtil.getStrippedItem(stackInHand);

        if (strippedItem != null) {
            if (!this.inventory.isEmpty() || this.isInsideWaterOrBubbleColumn()) return ActionResult.PASS;

            if (stackInHand.getCount() >= 16) {
                this.inventory.setStack(0, stackInHand.copyWithCount(16));
                this.setStackInHand(Hand.MAIN_HAND, stackInHand.copyWithCount(16));
                stackInHand.decrementUnlessCreative(16, player);
            } else {
                this.inventory.setStack(0, stackInHand.copy());
                this.setStackInHand(Hand.MAIN_HAND, stackInHand.copy());
                stackInHand.setCount(0);
            }

            this.strippingAnimationTimer = 0;
            this.setStrippingItems(true);
            if (!this.getWorld().isClient) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL);
                if (this.shouldPlayChainsawSound())
                    this.getWorld().playSound(null, this.getBlockPos(), ModSoundEvents.ENTITY_BEAVER_CHAINSAW.get(), SoundCategory.NEUTRAL);
            }

            this.startState(BeaverEntityAnimationState.STRIPPING_ITEMS);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return ModUtil.SAPLING_ITEM_MAP.contains(stack.getItem()) || stack.isIn(ItemTags.SAPLINGS);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    public boolean isStrippingItems() {
        return this.dataTracker.get(IS_STRIPPING_ITEMS);
    }

    public void setStrippingItems(boolean strippingItems) {
        this.dataTracker.set(IS_STRIPPING_ITEMS, strippingItems);
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(BeaverEntityAnimationState.IDLING);
        this.dropStack(this.inventory.getStack(0));
        super.onDeath(damageSource);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            int state = this.getState();
            this.stopAnimations();
            switch (state) {
                case 1:
                    this.strippingItemsAnimationState.startIfNotRunning(this.age);
                    break;
                default:
                    this.idlingAnimationState.startIfNotRunning(this.age);
                    break;
            }

            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
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
        World world = this.getWorld();

        if (!this.getInventory().isEmpty() && this.isStrippingItems() && this.isAlive()) {
            this.strippingAnimationTimer++;
            if ((this.strippingAnimationTimer <= 70 || (this.strippingAnimationTimer >= 100 && this.strippingAnimationTimer < 140))) {
                if ((this.age % 5) == 0 && !world.isClient && !this.shouldPlayChainsawSound())
                    world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_AXE_STRIP, SoundCategory.NEUTRAL);
                this.setShouldSpawnStrippingParticles(true);
            } else {
                this.setShouldSpawnStrippingParticles(false);
            }
        }

        if (strippingAnimationTimer >= 160 && this.isAlive()) {
            this.setStrippingItems(false);
            this.startState(BeaverEntityAnimationState.IDLING);
            ItemStack inventoryStack = this.inventory.getStack(0);
            Item strippedItem = ModUtil.getStrippedItem(inventoryStack);
            this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

            if (strippedItem == null) {
                this.dropStack(inventoryStack);
                this.inventory.setStack(0, ItemStack.EMPTY);
                this.strippingAnimationTimer = 0;
                return;
            }

            ItemStack strippedStack = new ItemStack(strippedItem, inventoryStack.getCount());
            this.inventory.setStack(0, strippedStack);

            var vec = this.getRotationVector().normalize().multiply(0.5);
            double x = this.getX() + vec.x;
            double y = this.getY() + 0.2;
            double z = this.getZ() + vec.z;

            ItemEntity item = new ItemEntity(this.getWorld(), x, y, z, this.inventory.getStack(0));
            item.setVelocity(vec.x * 0.1, 0.1, vec.z * 0.1);
            this.getWorld().spawnEntity(item);

            if (!this.getWorld().isClient)
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL);

            this.startState(BeaverEntityAnimationState.IDLING);
            this.strippingAnimationTimer = 0;
            this.inventory.setStack(0, ItemStack.EMPTY);
        }

        if (this.isStrippingItems()) {
            if (this.getMainHandStack().isEmpty() && !this.inventory.isEmpty()) {
                this.setStackInHand(Hand.MAIN_HAND, this.inventory.getStack(0).copy());
            }
        }

        this.setupAnimationStates();

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

    protected EntityNavigation createNavigation(World world) {
        return new BeaverSwimNavigation(this, world);
    }


    //Animation

    @Override
    protected int getNextAirUnderwater(int air) {
        return air;
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.15f, 0.5F);
    }

    private int getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    private void setState(int state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    private void setupAnimationStates() {
        if (this.waterAnimationTimeout <= 0) {
            this.waterAnimationTimeout = 20;
            this.waterAnimationState.startIfNotRunning(this.age);
        } else {
            --this.waterAnimationTimeout;
        }

        if (this.idleAnimationTimeout <= 0 && random.nextInt(200) == 0 && !this.isStrippingItems()) {
            this.idleAnimationTimeout = 20;
            this.idleAnimationState.startIfNotRunning(this.age);
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
}