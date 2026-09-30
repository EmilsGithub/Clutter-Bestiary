package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.HandledEntityAnimations;
import net.emilsg.clutterbestiary.animation_handling.animation_states.MossbloomAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.MossbloomDropHornsGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.TrackedFleeGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WanderAroundFarOftenGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.entity.variants.MossbloomVariant;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MossbloomEntity extends ParentTameableEntity implements PlayerRideable, PlayerRideableJumping, HandledEntityAnimations<MossbloomEntity, MossbloomAnimationState> {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.BIG_DRIPLEAF);
    private static final Item TAMING_ITEM = Items.SPORE_BLOSSOM;
    private static final int IDLE_GESTURE_CHANCE = 50;
    private static final int EAR_GESTURE_TICKS = 5;
    private static final int TAIL_GESTURE_TICKS = 10;

    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(MossbloomEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> HAS_HORNS = SynchedEntityData.defineId(MossbloomEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SPRINTING = SynchedEntityData.defineId(MossbloomEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SADDLED = SynchedEntityData.defineId(MossbloomEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(MossbloomEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_REVISION = SynchedEntityData.defineId(MossbloomEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANIMATION_START = SynchedEntityData.defineId(MossbloomEntity.class, EntityDataSerializers.LONG);

    public static int SHOULD_DROP_HORNS_VALUE = 12000;
    public final AnimationState earTwitchAnimationStateLE = new AnimationState();
    public final AnimationState earTwitchAnimationStateRE = new AnimationState();
    public final AnimationState earTwitchAnimationStateBE = new AnimationState();
    public final AnimationState wagTailAnimationStateBE = new AnimationState();
    private final EntityAnimationController<MossbloomEntity, MossbloomAnimationState> animationController = new EntityAnimationController<>(this, MossbloomAnimationState.IDLING, MossbloomAnimationState.class, ANIMATION_STATE, ANIMATION_REVISION, ANIMATION_START);
    private int idleGestureTicksRemaining;
    protected int soundTicks;
    protected boolean inAir;
    protected float jumpStrength;
    private int hornDropTimer;

    public MossbloomEntity(EntityType<? extends ParentTameableEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        SpawnGroupData initializedData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        MossbloomVariant variant = MossbloomVariant.getRandom(this.getRandom());
        this.setVariant(variant);
        this.setHasHorns(variant == MossbloomVariant.HORNED && !this.isBaby());

        return initializedData;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, MossbloomVariant.HORNED.getId());
        builder.define(HAS_HORNS, true);
        builder.define(IS_SPRINTING, false);
        builder.define(IS_SADDLED, false);
        builder.define(ANIMATION_STATE, MossbloomAnimationState.IDLING.getIndex());
        builder.define(ANIMATION_REVISION, 0);
        builder.define(ANIMATION_START, -1L);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MossbloomDropHornsGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new TrackedFleeGoal(this, 2.5f));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(6, new WanderAroundFarOftenGoal(this, 1.0f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
        this.entityData.set(HAS_HORNS, nbt.getBooleanOr("HasHorns", false));
        this.setHornDropTimer(nbt.getIntOr("HornDropTimer", 0));
        this.setIsShaking(false);
        this.entityData.set(IS_SADDLED, nbt.getBooleanOr("IsSaddled", false));
        if (this.getVariant() == MossbloomVariant.FLOWERING) this.setHasHorns(false);
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("HasHorns", this.getHasHorns());
        nbt.putInt("HornDropTimer", this.getHornDropTimer());
        nbt.putBoolean("IsShaking", this.getIsShaking());
        nbt.putBoolean("IsSaddled", this.getIsSaddled());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.ATTACK_DAMAGE, 6.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f)
                .add(Attributes.JUMP_STRENGTH, 0.75f)
                .add(Attributes.STEP_HEIGHT, 1.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.MOSSBLOOMS_SPAWN_ON);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        MossbloomEntity child = (MossbloomEntity) this.getBreedOffspring(world, other);
        if (child != null) {
            ServerPlayer serverPlayerEntity = this.getLoveCause();
            if (serverPlayerEntity == null && other.getLoveCause() != null) {
                serverPlayerEntity = other.getLoveCause();
            }

            if (serverPlayerEntity != null) {
                serverPlayerEntity.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayerEntity, this, other, child);
            }

            this.setAge(6000);
            other.setAge(6000);
            this.resetLove();
            other.resetLove();
            child.setBaby(true);

            boolean isVariantHorned = random.nextBoolean();
            child.setVariant(isVariantHorned ? MossbloomVariant.HORNED : MossbloomVariant.FLOWERING);

            child.setHasHorns(isVariantHorned);
            if (isVariantHorned) child.setHornDropTimer(-SHOULD_DROP_HORNS_VALUE);

            child.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            world.addFreshEntityWithPassengers(child);
            world.broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
            if (world.getGameRules().get(GameRules.MOB_DROPS)) {
                world.addFreshEntity(new ExperienceOrb(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
            }

        }
    }

    @Override
    public boolean canMate(Animal other) {
        if (other == this) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else if (((MossbloomEntity) other).getVariant().getId().equals(this.getVariant().getId())) {
            return false;
        } else {
            return this.isInLove() && other.isInLove();
        }
    }

    @Override
    public boolean canJump() {
        return this.getIsSaddled();
    }

    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason spawnReason) {
        BlockPos pos = this.blockPosition();
        BlockState blockState = world.getBlockState(pos.below());
        return (blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.STONE) || blockState.is(Blocks.MOSS_BLOCK) || blockState.is(Blocks.CLAY));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        MossbloomEntity child = ModEntityTypes.MOSSBLOOM.get().create(world, EntitySpawnReason.BREEDING);
        if (child != null) {
            MossbloomVariant variant = MossbloomVariant.getRandom(child.getRandom());
            child.setVariant(variant);
            child.setHasHorns(variant == MossbloomVariant.HORNED && !child.isBaby());
        }
        return child;
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity firstPassenger = this.getFirstPassenger();
        if (firstPassenger instanceof Mob mobEntity) {
            return mobEntity;
        } else {
            if (this.getIsSaddled()) {
                firstPassenger = this.getFirstPassenger();
                if (firstPassenger instanceof Player) {
                    return (Player) firstPassenger;
                }
            }

            return null;
        }
    }

    public boolean getHasHorns() {
        return this.entityData.get(HAS_HORNS);
    }

    public void setHasHorns(boolean hasHorns) {
        this.entityData.set(HAS_HORNS, hasHorns);
    }

    public int getHornDropTimer() {
        return this.hornDropTimer;
    }

    public void setHornDropTimer(int hornDropTimer) {
        this.hornDropTimer = hornDropTimer;
    }

    public boolean getIsSaddled() {
        return this.entityData.get(IS_SADDLED);
    }

    public void setIsSaddled(boolean saddled) {
        this.entityData.set(IS_SADDLED, saddled);
    }

    public boolean getIsShaking() {
        return this.animationController.getState() == MossbloomAnimationState.SHAKING;
    }

    public void setIsShaking(boolean isShaking) {
        this.animationController.requestState(isShaking && this.getVariant() == MossbloomVariant.HORNED
                ? MossbloomAnimationState.SHAKING : MossbloomAnimationState.IDLING);
    }

    @Override
    public EntityAnimationController<MossbloomEntity, MossbloomAnimationState> getAnimationController() {
        return this.animationController;
    }

    public double getJumpStrength() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    @Override
    public void onPlayerJump(int strength) {
        if (this.getIsSaddled()) {
            if (strength < 0) {
                strength = 0;
            } else {
                this.jumping = true;
            }

            if (strength >= 90) {
                this.jumpStrength = 1.0F;
            } else {
                this.jumpStrength = 0.4F + 0.4F * (float) strength / 90.0F;
            }

        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    public boolean getSprinting() {
        return this.entityData.get(IS_SPRINTING);
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public MossbloomVariant getVariant() {
        return MossbloomVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(MossbloomVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (fallDistance > 1.0F) {
            this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.75F);
        }

        int i = this.calculateFallDamage(fallDistance, damageMultiplier);
        if (i <= 0) {
            return false;
        } else {
            this.hurt(damageSource, (float) i);
            if (this.isVehicle()) {

                for (Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(damageSource, (float) i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();

        if (this.isTame() && hand == InteractionHand.MAIN_HAND) {
            if (!player.isShiftKeyDown()) {
                if (!this.getIsSaddled() && itemStack.is(Items.SADDLE)) {
                    if (!player.getAbilities().instabuild) itemStack.shrink(1);
                    this.setIsSaddled(true);
                    this.level().playSound(null, this.blockPosition(), SoundEvents.PIG_SADDLE.value(), SoundSource.NEUTRAL, 0.5f, 1.25f);
                    return InteractionResult.SUCCESS;
                }
                if (itemStack.isEmpty()) {
                    this.setRiding(player);
                    return InteractionResult.SUCCESS;
                }
            }

            if (item instanceof ShearsItem && this.getIsSaddled()) {
                this.setIsSaddled(false);
                itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(Items.SADDLE), 0.5F);
                this.level().playSound(null, this.blockPosition(), SoundEvents.MOOSHROOM_SHEAR, SoundSource.NEUTRAL, 0.5f, 1.25f);
                return InteractionResult.SUCCESS;
            }
        }

        if (!this.isTame() && item == this.getTamingItem()) {
            if (this.level().isClientSide()) return InteractionResult.CONSUME;

            if (!player.getAbilities().instabuild) itemStack.shrink(1);
            this.level().playSound(null, this.blockPosition(), SoundEvents.HORSE_EAT, SoundSource.NEUTRAL, 0.5f, 1.75f);

            if (random.nextInt(8) == 0) {
                super.tame(player);
                this.navigation.recomputePath();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
            } else {
                this.level().broadcastEntityEvent(this, EntityEvent.TAMING_FAILED);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.getIsSaddled();
    }

    public boolean isInAir() {
        return this.inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public boolean isVariantOf(MossbloomVariant variant) {
        return this.getVariant() == variant;
    }

    public void setIsSprinting(boolean sprinting) {
        this.entityData.set(IS_SPRINTING, sprinting);
    }

    @Override
    public void handleStartJump(int height) {
        this.jumping = true;
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (world instanceof ServerLevel serverWorld && this.getVariant() == MossbloomVariant.FLOWERING && this.getRandom().nextInt(4800) == 0) {
            this.tickFertilize(serverWorld);
        }

        this.animationController.tick();
        if (world.isClientSide()) this.tickIdleGestures();

        if (!world.isClientSide() && this.getVariant() == MossbloomVariant.HORNED && !this.isBaby()) {
            if (!this.getHasHorns() && this.getHornDropTimer() >= (SHOULD_DROP_HORNS_VALUE / 3)) this.setHasHorns(true);
            this.setHornDropTimer(this.getHornDropTimer() + 1);
        }

        if (!world.isClientSide() && this.getVariant() == MossbloomVariant.HORNED && this.isBaby()) {
            this.setHasHorns(false);
        }

        this.jumping = false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isUnderWater() && this.getControllingPassenger() != null)
            this.getControllingPassenger().removeVehicle();
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isVehicle() && getControllingPassenger() instanceof Player) {
            LivingEntity livingentity = this.getControllingPassenger();
            this.setYRot(livingentity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(livingentity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
            float f = livingentity.xxa * 0.5F;
            float f1 = livingentity.zza;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            if (this.isLocalInstanceAuthoritative()) {
                float newSpeed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);

                boolean sprinting = livingentity.isSprinting();

                if (sprinting) newSpeed *= 1.4F;
                this.setIsSprinting(sprinting);

                this.setSpeed(newSpeed);
                super.travel(new Vec3(f, movementInput.y, f1));
            }
        } else {
            if (!this.level().isClientSide() && this.getSprinting()) this.setIsSprinting(false);
            super.travel(movementInput);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(passenger);
        }
        int[][] is = DismountHelper.offsetsForDirection(direction);
        BlockPos blockPos = this.blockPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Pose entityPose : passenger.getDismountPoses()) {
            AABB box = passenger.getLocalBoundsForPose(entityPose);
            for (int[] js : is) {
                mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
                double d = this.level().getBlockFloorHeight(mutable);
                if (!DismountHelper.isBlockFloorValid(d)) continue;
                Vec3 vec3d = Vec3.upFromBottomCenterOf(mutable, d);
                if (!DismountHelper.canDismountTo(this.level(), passenger, box.move(vec3d))) continue;
                passenger.setPose(entityPose);
                return vec3d;
            }
        }
        return super.getDismountLocationForPassenger(passenger);
    }

    protected int calculateFallDamage(double fallDistance, float damageMultiplier) {
        return Mth.ceil((fallDistance * 0.5F - 3.0F) * damageMultiplier);
    }

    protected Vec3 getRiddenInput(Player controllingPlayer, Vec3 movementInput) {
        if (this.onGround() && this.jumpStrength == 0.0F && !this.jumping) {
            return Vec3.ZERO;
        } else {
            float f = controllingPlayer.xxa * 0.5F;
            float g = controllingPlayer.zza;
            if (g <= 0.0F) {
                g *= 0.25F;
            }

            return new Vec3(f, 0.0, g);
        }
    }

    protected Vec2 getControlledRotation(LivingEntity controllingPassenger) {
        return new Vec2(controllingPassenger.getXRot() * 0.5F, controllingPassenger.getYRot());
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_MOSSBLOOM_HURT.get();
    }

    protected void jump(float strength, Vec3 movementInput) {
        double d = this.getJumpStrength() * (double) strength * (double) this.getBlockJumpFactor();
        double e = d + (double) this.getJumpBoostPower();
        Vec3 vec3d = this.getDeltaMovement();
        this.setDeltaMovement(vec3d.x, e, vec3d.z);
        this.setInAir(true);
        this.needsSync = true;
        if (movementInput.z > 0.0) {
            float f = Mth.sin(this.getYRot() * 0.017453292F);
            float g = Mth.cos(this.getYRot() * 0.017453292F);
            this.setDeltaMovement(this.getDeltaMovement().add((-0.4F * f * strength), 0.0, (0.4F * g * strength)));
        }

    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();

        if (!this.isBaby() && this.getVariant() == MossbloomVariant.HORNED) {
            this.setHasHorns(true);
        }
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.liquid()) {
            BlockState blockState = this.level().getBlockState(pos.above());
            SoundType blockSoundGroup = state.getSoundType();
            if (blockState.is(Blocks.SNOW)) {
                blockSoundGroup = blockState.getSoundType();
            }

            if (this.isVehicle()) {
                ++this.soundTicks;
                if (this.soundTicks > 5 && this.soundTicks % 3 == 0) {
                    this.playWalkSound(blockSoundGroup);
                } else if (this.soundTicks <= 5) {
                    this.playSound(SoundEvents.HORSE_STEP_WOOD, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch() + 0.5f);
                }
            } else if (this.isWooden(blockSoundGroup)) {
                this.playSound(SoundEvents.HORSE_STEP_WOOD, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch() + 0.5f);
            } else {
                this.playSound(SoundEvents.HORSE_STEP, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch() + 0.5f);
            }

        }
    }

    protected void playWalkSound(SoundType group) {
        this.playSound(SoundEvents.HORSE_GALLOP, group.getVolume() * 0.05F, group.getPitch() + 0.25f);
        if (this.random.nextInt(10) == 0) {
            this.playSound(SoundEvents.HORSE_BREATHE, group.getVolume() * 0.6F, group.getPitch() + 0.5f);
        }
    }

    protected void tickRidden(Player controllingPlayer, Vec3 movementInput) {
        super.tickRidden(controllingPlayer, movementInput);
        Vec2 vec2f = this.getControlledRotation(controllingPlayer);
        this.setRot(vec2f.y, vec2f.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isLocalInstanceAuthoritative()) {

            if (this.onGround()) {
                this.setInAir(false);
                if (this.jumpStrength > 0.0F && !this.isInAir()) {
                    this.jump(this.jumpStrength, movementInput);
                }

                this.jumpStrength = 0.0F;
            }
        }

    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 1.5f, 0.5F, 1.0F);
    }

    private boolean isWooden(SoundType soundGroup) {
        return soundGroup == SoundType.WOOD || soundGroup == SoundType.NETHER_WOOD || soundGroup == SoundType.STEM || soundGroup == SoundType.CHERRY_WOOD || soundGroup == SoundType.BAMBOO_WOOD;
    }

    private void pickRandomIdleAnim(int i) {
        switch (i) {
            case 1 -> this.earTwitchAnimationStateRE.start(this.tickCount);
            case 2 -> this.earTwitchAnimationStateLE.start(this.tickCount);
            case 3 -> this.wagTailAnimationStateBE.start(this.tickCount);
            default -> this.earTwitchAnimationStateBE.start(this.tickCount);
        }
        this.idleGestureTicksRemaining = i == 3 ? TAIL_GESTURE_TICKS : EAR_GESTURE_TICKS;
    }

    private void stopIdleGestureAnimations() {
        this.earTwitchAnimationStateRE.stop();
        this.earTwitchAnimationStateLE.stop();
        this.earTwitchAnimationStateBE.stop();
        this.wagTailAnimationStateBE.stop();
    }

    private void setRiding(Player pPlayer) {
        pPlayer.setYRot(this.getYRot());
        pPlayer.setXRot(this.getXRot());
        pPlayer.startRiding(this);
    }

    private void tickIdleGestures() {
        if (!this.isAlive() || this.isMoving() || this.isFleeing() || this.getSprinting() || this.getIsShaking()) {
            if (this.idleGestureTicksRemaining > 0) this.stopIdleGestureAnimations();
            this.idleGestureTicksRemaining = 0;
            return;
        }

        if (this.idleGestureTicksRemaining > 0) {
            if (--this.idleGestureTicksRemaining == 0) this.stopIdleGestureAnimations();
            return;
        }

        if (this.random.nextInt(IDLE_GESTURE_CHANCE) == 0) this.pickRandomIdleAnim(this.random.nextInt(4));
    }

    private void tickFertilize(ServerLevel world) {
        BlockPos origin = this.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        RandomSource random = this.getRandom();

        for (int i = 0; i < 32; i++) {
            pos.set(origin.getX() + random.nextInt(11) - 5,
                    origin.getY() + random.nextInt(3) - 1,
                    origin.getZ() + random.nextInt(11) - 5);

            BlockState state = world.getBlockState(pos);
            if (state.isAir()) continue;

            Block block = state.getBlock();
            if (state.is(BlockTags.BEE_GROWABLES) && block instanceof BonemealableBlock fertilizable
                    && fertilizable.isValidBonemealTarget(world, pos, state, BonemealSource.MOB) && fertilizable.isBonemealSuccess(world, random, pos, state, BonemealSource.MOB)) {
                fertilizable.performBonemeal(world, random, pos, state, BonemealSource.MOB);
                world.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        15, 0.5, 0.5, 0.5, 0.0);
                return;
            }
        }
    }
}
