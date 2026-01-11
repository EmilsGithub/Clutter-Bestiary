package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RiverTurtleAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class RiverTurtleEntity extends ParentAnimalEntity implements Bucketable, HandledEntityAnimations<RiverTurtleEntity, RiverTurtleAnimationState> {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.APPLE);
    private static final int SIT_END_TICKS = 5;
    private static final int UNHIDE_TICKS = 50;
    private static final TrackedData<String> VARIANT = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.LONG);
    private static final TrackedData<Integer> BASKING_DURATION = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> HIDING = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SITTING = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;
    private final EntityAnimationController<RiverTurtleEntity, RiverTurtleAnimationState> animationController = new EntityAnimationController<>(this, RiverTurtleAnimationState.IDLING, RiverTurtleAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private int healthTicker = 0;
    private int healCooldown;
    private boolean canHealPassively = false;

    public RiverTurtleEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);

        this.moveControl = new RiverTurtleMoveControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
        this.setupAnimationController();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new RiverTurtleHideGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.add(2, new BaskGoal(this, 0.0001f));
        this.goalSelector.add(3, new HarvestKelpGoal(this, 1, 12, 0.05f));
        this.goalSelector.add(3, new HighWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.add(4, new LeaveWaterGoal(this, 1.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6f));

        this.targetSelector.add(1, new ConditionalActiveTargetGoal<>(this, CodEntity.class, true, 0.001f));
        this.targetSelector.add(2, new ConditionalActiveTargetGoal<>(this, SalmonEntity.class, true, 0.001f));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setVariant(RiverTurtleVariant.getRandom());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANIMATION_STATE, RiverTurtleAnimationState.IDLING.getIndex());
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, -1L);
        builder.add(BASKING_DURATION, 0);
        builder.add(HIDING, false);
        builder.add(VARIANT, RiverTurtleVariant.SANDY.getID());
        builder.add(IS_SITTING, false);
        builder.add(FROM_BUCKET, false);
    }

    @Override
    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
        if (nbt.contains("Variant")) {
            this.setVariant(RiverTurtleVariant.fromId(nbt.getString("Variant")));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBaskingDuration(nbt.getInt("BaskingDuration"));
        this.setVariant(RiverTurtleVariant.fromId(nbt.getString("Variant")));
        this.setSit(nbt.getBoolean("IsSitting"));
        this.setFromBucket(nbt.getBoolean("FromBucket"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("BaskingDuration", this.getBaskingDuration());
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("IsSitting", this.isSat());
        nbt.putBoolean("FromBucket", this.isFromBucket());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.175F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2F);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.RIVER_TURTLES_SPAWN_ON);
    }

    @Override
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
        NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack, nbt -> nbt.putString("Variant", this.getTypeVariant()));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        RiverTurtleEntity child = ModEntityTypes.RIVER_TURTLE.get().create(world);
        if (child != null) child.setVariant(RiverTurtleVariant.getRandom());
        return child;
    }

    public int getBaskingDuration() {
        return this.dataTracker.get(BASKING_DURATION);
    }

    public void setBaskingDuration(int baskingDuration) {
        this.dataTracker.set(BASKING_DURATION, baskingDuration);
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 240;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 1.25f;
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public RiverTurtleVariant getVariant() {
        return RiverTurtleVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(RiverTurtleVariant variant) {
        this.dataTracker.set(VARIANT, variant.getID());
    }

    @Override
    public boolean isFromBucket() {
        return this.dataTracker.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.dataTracker.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ModItems.RIVER_TURTLE_BUCKET.get());
    }

    @Override
    public SoundEvent getBucketFillSound() {
        return SoundEvents.ITEM_BUCKET_FILL_AXOLOTL;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return Bucketable.tryBucket(player, hand, this).orElseGet(() -> super.interactMob(player, hand));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isHiding() {
        return this.dataTracker.get(HIDING);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    public boolean isSat() {
        return this.dataTracker.get(IS_SITTING);
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(RiverTurtleAnimationState.IDLING);
        super.onDeath(damageSource);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) this.calculateDimensions();
        super.onTrackedDataSet(data);
    }

    public void setIsHiding(boolean isHiding) {
        this.dataTracker.set(HIDING, isHiding);
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(IS_SITTING, sitting);
    }

    private void setupAnimationController() {
        animationController.addTransition(RiverTurtleAnimationState.IDLING, RiverTurtleAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addTransition(RiverTurtleAnimationState.SIT_START, RiverTurtleAnimationState.SIT_END, (e, s, age) -> !e.isSat());
        animationController.addTransition(RiverTurtleAnimationState.SIT_END, RiverTurtleAnimationState.SIT_START, (e, s, age) -> e.isSat());
        animationController.addCompletion(RiverTurtleAnimationState.SIT_END, RiverTurtleAnimationState.IDLING, SIT_END_TICKS);
        animationController.addCompletion(RiverTurtleAnimationState.UNHIDING, RiverTurtleAnimationState.IDLING, UNHIDE_TICKS);
    }

    @Override
    public EntityAnimationController<RiverTurtleEntity, RiverTurtleAnimationState> getAnimationController() {
        return animationController;
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient()) {
            if (this.getBaskingDuration() > 0) this.setBaskingDuration(this.getBaskingDuration() - 1);

            if (this.getHealth() < this.getMaxHealth()) {
                if (this.getHealth() <= (this.getMaxHealth() / 2)) {
                    this.setIsHiding(true);
                    canHealPassively = true;
                }

                if (canHealPassively) {
                    if (this.healCooldown == 0) this.healCooldown = 150 + random.nextInt(100);
                    healthTicker++;

                    if (healthTicker >= this.healCooldown) {
                        this.heal(1);
                        healthTicker = 0;
                        this.healCooldown = 0;
                    }
                }
            } else {
                canHealPassively = false;
                healthTicker = 0;
                this.healCooldown = 0;
                this.setIsHiding(false);
            }
        }

        super.tick();
        this.animationController.tick();
    }

    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.8, 0.95, 0.8));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    protected EntityNavigation createNavigation(World world) {
        return new RiverTurtleSwimNavigation(this, world);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_RIVER_TURTLE_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_RIVER_TURTLE_HURT.get();
    }

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

        this.limbAnimator.updateLimbs(f * 1.15f, 0.7F);
    }

    private static class RiverTurtleMoveControl extends MoveControl {
        private final RiverTurtleEntity riverTurtle;

        public RiverTurtleMoveControl(RiverTurtleEntity riverTurtle) {
            super(riverTurtle);
            this.riverTurtle = riverTurtle;
        }

        public void tick() {
            if (this.riverTurtle.isTouchingWater()) {
                this.riverTurtle.setVelocity(this.riverTurtle.getVelocity().add(0.0, 0.005, 0.0));

                if (this.state != State.MOVE_TO || this.riverTurtle.getNavigation().isIdle()) {
                    this.riverTurtle.setMovementSpeed(0.0F);
                    return;
                }

                double d = this.targetX - this.riverTurtle.getX();
                double e = this.targetY - this.riverTurtle.getY();
                double f = this.targetZ - this.riverTurtle.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.riverTurtle.setYaw(this.wrapDegrees(this.riverTurtle.getYaw(), h, 90.0F));
                this.riverTurtle.bodyYaw = this.riverTurtle.getYaw();
                float i = (float) (this.speed * this.riverTurtle.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125F, this.riverTurtle.getMovementSpeed(), i);
                this.riverTurtle.setMovementSpeed(j);
                this.riverTurtle.setVelocity(this.riverTurtle.getVelocity().add(0, this.riverTurtle.getMovementSpeed() * e * 0.1, 0));
            } else {
                this.riverTurtle.setVelocity(this.riverTurtle.getVelocity().add(0.0, 0, 0.0));
                super.tick();
            }
        }
    }

    private static class RiverTurtleSwimNavigation extends AmphibiousSwimNavigation {
        RiverTurtleSwimNavigation(RiverTurtleEntity riverTurtleEntity, World world) {
            super(riverTurtleEntity, world);
        }

        public boolean isValidPosition(BlockPos pos) {
            if (this.entity instanceof RiverTurtleEntity) {
                return this.world.getBlockState(pos).isOf(Blocks.WATER) || (this.world.getBlockState(pos).isReplaceable() && this.world.getBlockState(pos.down()).isSolidBlock(this.world, pos.down()));
            }

            return !this.world.getBlockState(pos.down()).isAir();
        }
    }
}
