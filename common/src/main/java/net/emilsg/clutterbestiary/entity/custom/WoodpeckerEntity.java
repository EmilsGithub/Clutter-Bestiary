package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.animation_states.WoodpeckerAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.WoodpeckerFlyAroundGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WoodpeckerPeckLogGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WoodpeckerPerchOnLeavesGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WoodpeckerEntity extends ParentAnimalEntity implements HandledEntityAnimations<WoodpeckerEntity, WoodpeckerAnimationState> {
    private static final int LARVA_SPAWN_CHANCE = 3;
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS);
    private static final double HOVER_SPEED_SQUARED = 0.0025;
    private static final TrackedData<Boolean> FLYING = DataTracker.registerData(WoodpeckerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(WoodpeckerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(WoodpeckerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(WoodpeckerEntity.class, TrackedDataHandlerRegistry.LONG);

    private final EntityNavigation landNavigation;
    private final EntityNavigation flightNavigation;
    private final MoveControl landMoveControl;
    private final MoveControl flightMoveControl;
    private final EntityAnimationController<WoodpeckerEntity, WoodpeckerAnimationState> animationController = new EntityAnimationController<>(
            this, WoodpeckerAnimationState.GROUND_IDLE, WoodpeckerAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private boolean attached;
    private boolean pecking;
    private boolean peckingInterrupted;
    @Nullable private Vec3d attachmentPos;
    @Nullable private Direction attachmentFace;

    public WoodpeckerEntity(EntityType<? extends ParentAnimalEntity> entityType, World world) {
        super(entityType, world);
        this.landNavigation = this.navigation;
        this.flightNavigation = this.createFlightNavigation(world);
        this.landMoveControl = this.moveControl;
        this.flightMoveControl = new FlightMoveControl(this, 20, true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FLYING, false);
        builder.add(ANIMATION_STATE, WoodpeckerAnimationState.GROUND_IDLE.getIndex());
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, -1L);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(4, new WoodpeckerPeckLogGoal(this, 1.0));
        this.goalSelector.add(5, new WoodpeckerPerchOnLeavesGoal(this, 1.0));
        this.goalSelector.add(6, new WoodpeckerFlyAroundGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.45)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.WOODPECKERS_SPAWN_ON);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.WOODPECKER.get().create(world);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 200;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 1.2f;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isFlying() {
        return this.dataTracker.get(FLYING);
    }

    public boolean isAttached() {
        return this.attached;
    }

    public boolean isPeckingInterrupted() {
        return this.peckingInterrupted;
    }

    public void setFlying(boolean flying) {
        if (this.isFlying() == flying) return;
        this.navigation.stop();
        this.dataTracker.set(FLYING, flying);
        this.setNoGravity(flying);
        this.navigation = flying ? this.flightNavigation : this.landNavigation;
        this.moveControl = flying ? this.flightMoveControl : this.landMoveControl;
    }

    public void attachToLog(Vec3d position, Direction face) {
        this.navigation.stop();
        this.attached = true;
        this.pecking = false;
        this.attachmentPos = position;
        this.attachmentFace = face;
        this.setNoGravity(true);
        this.setVelocity(Vec3d.ZERO);
        this.setPosition(position);
        this.faceLog();
    }

    public void setPecking(boolean pecking) {
        this.pecking = pecking;
    }

    public void interruptPecking() {
        this.peckingInterrupted = true;
        this.clearAttachment();
    }

    public void clearPeckingInterruption() {
        this.peckingInterrupted = false;
    }

    public void clearAttachment() {
        this.attached = false;
        this.pecking = false;
        this.attachmentPos = null;
        this.attachmentFace = null;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean wasPeckingAtLog = this.pecking && this.attached;
        boolean projectileHit = source.isIn(DamageTypeTags.IS_PROJECTILE);
        boolean damaged = super.damage(source, amount);
        if (wasPeckingAtLog && (damaged || projectileHit)) this.interruptPecking();
        if (!damaged || !wasPeckingAtLog || !projectileHit) return damaged;
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) return true;
        if (this.random.nextInt(LARVA_SPAWN_CHANCE) == 0) this.spawnButterflyLarva(serverWorld);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            if (this.attached && this.attachmentPos != null) {
                this.setPosition(this.attachmentPos);
                this.setVelocity(Vec3d.ZERO);
                this.fallDistance = 0.0f;
                this.faceLog();
            }

            WoodpeckerAnimationState state = !this.isFlying() ? WoodpeckerAnimationState.GROUND_IDLE
                    : this.pecking ? WoodpeckerAnimationState.PECKING
                    : this.attached ? WoodpeckerAnimationState.ATTACHED
                    : this.getVelocity().lengthSquared() <= HOVER_SPEED_SQUARED ? WoodpeckerAnimationState.HOVERING
                    : WoodpeckerAnimationState.FLYING;
            this.animationController.requestState(state);
        }
        this.animationController.tick();
    }

    private void faceLog() {
        if (this.attachmentFace == null) return;
        float yaw = this.attachmentFace.getOpposite().asRotation();
        this.setYaw(yaw);
        this.bodyYaw = yaw;
        this.headYaw = yaw;
    }

    private void spawnButterflyLarva(ServerWorld world) {
        ButterflyLarvaEntity larva = ModEntityTypes.BUTTERFLY_LARVA.get().create(world);
        if (larva == null) return;
        larva.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
        larva.setHomePos(this.getBlockPos());
        larva.setVariant(ButterflyVariant.getRandom(true));
        larva.setPersistent();
        world.spawnEntity(larva);
    }

    private EntityNavigation createFlightNavigation(World world) {
        BirdNavigation navigation = new BirdNavigation(this, world);
        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(false);
        return navigation;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public EntityAnimationController<WoodpeckerEntity, WoodpeckerAnimationState> getAnimationController() {
        return this.animationController;
    }
}
