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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WoodpeckerEntity extends ParentAnimalEntity implements HandledEntityAnimations<WoodpeckerEntity, WoodpeckerAnimationState> {
    private static final int LARVA_SPAWN_CHANCE = 3;
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS);
    private static final double HOVER_SPEED_SQUARED = 0.0025;
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(WoodpeckerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(WoodpeckerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(WoodpeckerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(WoodpeckerEntity.class, EntityDataSerializers.LONG);

    private final PathNavigation landNavigation;
    private final PathNavigation flightNavigation;
    private final MoveControl landMoveControl;
    private final MoveControl flightMoveControl;
    private final EntityAnimationController<WoodpeckerEntity, WoodpeckerAnimationState> animationController = new EntityAnimationController<>(
            this, WoodpeckerAnimationState.GROUND_IDLE, WoodpeckerAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private boolean attached;
    private boolean pecking;
    private boolean peckingInterrupted;
    @Nullable private Vec3 attachmentPos;
    @Nullable private Direction attachmentFace;

    public WoodpeckerEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.landNavigation = this.navigation;
        this.flightNavigation = this.createFlightNavigation(world);
        this.landMoveControl = this.moveControl;
        this.flightMoveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLYING, false);
        builder.define(ANIMATION_STATE, WoodpeckerAnimationState.GROUND_IDLE.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(4, new WoodpeckerPeckLogGoal(this, 1.0));
        this.goalSelector.addGoal(5, new WoodpeckerPerchOnLeavesGoal(this, 1.0));
        this.goalSelector.addGoal(6, new WoodpeckerFlyAroundGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.FLYING_SPEED, 0.45)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.WOODPECKERS_SPAWN_ON);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.WOODPECKER.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.2f;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isFlying() {
        return this.entityData.get(FLYING);
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
        this.entityData.set(FLYING, flying);
        this.setNoGravity(flying);
        this.navigation = flying ? this.flightNavigation : this.landNavigation;
        this.moveControl = flying ? this.flightMoveControl : this.landMoveControl;
    }

    public void attachToLog(Vec3 position, Direction face) {
        this.navigation.stop();
        this.attached = true;
        this.pecking = false;
        this.attachmentPos = position;
        this.attachmentFace = face;
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
        this.setPos(position);
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
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        boolean wasPeckingAtLog = this.pecking && this.attached;
        boolean projectileHit = source.is(DamageTypeTags.IS_PROJECTILE);
        boolean damaged = super.hurtServer(serverLevel, source, amount);
        if (wasPeckingAtLog && (damaged || projectileHit)) this.interruptPecking();
        if (!damaged || !wasPeckingAtLog || !projectileHit) return damaged;
        if (!(this.level() instanceof ServerLevel serverWorld)) return true;
        if (this.random.nextInt(LARVA_SPAWN_CHANCE) == 0) this.spawnButterflyLarva(serverWorld);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.attached && this.attachmentPos != null) {
                this.setPos(this.attachmentPos);
                this.setDeltaMovement(Vec3.ZERO);
                this.fallDistance = 0.0f;
                this.faceLog();
            }

            WoodpeckerAnimationState state = !this.isFlying() ? WoodpeckerAnimationState.GROUND_IDLE
                    : this.pecking ? WoodpeckerAnimationState.PECKING
                    : this.attached ? WoodpeckerAnimationState.ATTACHED
                    : this.getDeltaMovement().lengthSqr() <= HOVER_SPEED_SQUARED ? WoodpeckerAnimationState.HOVERING
                    : WoodpeckerAnimationState.FLYING;
            this.animationController.requestState(state);
        }
        this.animationController.tick();
    }

    private void faceLog() {
        if (this.attachmentFace == null) return;
        float yaw = this.attachmentFace.getOpposite().toYRot();
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
    }

    private void spawnButterflyLarva(ServerLevel world) {
        ButterflyLarvaEntity larva = ModEntityTypes.BUTTERFLY_LARVA.get().create(world, EntitySpawnReason.BREEDING);
        if (larva == null) return;
        larva.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0f);
        larva.setHomePos(this.blockPosition());
        larva.setVariant(ButterflyVariant.getRandom(true));
        larva.setPersistenceRequired();
        world.addFreshEntity(larva);
    }

    private PathNavigation createFlightNavigation(Level world) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, world);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public EntityAnimationController<WoodpeckerEntity, WoodpeckerAnimationState> getAnimationController() {
        return this.animationController;
    }
}
