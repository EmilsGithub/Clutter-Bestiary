package net.emilsg.clutterbestiary.entity.custom;

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
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
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
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class MossbloomEntity extends ParentTameableEntity implements Mount, JumpingMount, HandledEntityAnimations<MossbloomEntity, MossbloomAnimationState> {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.BIG_DRIPLEAF);
    private static final Item TAMING_ITEM = Items.SPORE_BLOSSOM;
    private static final int IDLE_GESTURE_CHANCE = 50;
    private static final int EAR_GESTURE_TICKS = 5;
    private static final int TAIL_GESTURE_TICKS = 10;

    private static final TrackedData<String> VARIANT = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> HAS_HORNS = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SPRINTING = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SADDLED = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ANIMATION_STATE = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> ANIMATION_REVISION = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> ANIMATION_START = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.LONG);

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

    public MossbloomEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData initializedData = super.initialize(world, difficulty, spawnReason, entityData);
        MossbloomVariant variant = MossbloomVariant.getRandom(this.getRandom());
        this.setVariant(variant);
        this.setHasHorns(variant == MossbloomVariant.HORNED && !this.isBaby());

        return initializedData;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, MossbloomVariant.HORNED.getId());
        builder.add(HAS_HORNS, true);
        builder.add(IS_SPRINTING, false);
        builder.add(IS_SADDLED, false);
        builder.add(ANIMATION_STATE, MossbloomAnimationState.IDLING.getIndex());
        builder.add(ANIMATION_REVISION, 0);
        builder.add(ANIMATION_START, -1L);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new MossbloomDropHornsGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TrackedFleeGoal(this, 2.5f));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1));
        this.goalSelector.add(4, new TemptGoal(this, 1.25, BREEDING_INGREDIENT, false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(6, new WanderAroundFarOftenGoal(this, 1.0f));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
        this.dataTracker.set(HAS_HORNS, nbt.getBoolean("HasHorns"));
        this.setHornDropTimer(nbt.getInt("HornDropTimer"));
        this.setIsShaking(false);
        this.dataTracker.set(IS_SADDLED, nbt.getBoolean("IsSaddled"));
        if (this.getVariant() == MossbloomVariant.FLOWERING) this.setHasHorns(false);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("HasHorns", this.getHasHorns());
        nbt.putInt("HornDropTimer", this.getHornDropTimer());
        nbt.putBoolean("IsShaking", this.getIsShaking());
        nbt.putBoolean("IsSaddled", this.getIsSaddled());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f)
                .add(EntityAttributes.GENERIC_JUMP_STRENGTH, 0.75f)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0f);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.MOSSBLOOMS_SPAWN_ON);
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        MossbloomEntity child = (MossbloomEntity) this.createChild(world, other);
        if (child != null) {
            ServerPlayerEntity serverPlayerEntity = this.getLovingPlayer();
            if (serverPlayerEntity == null && other.getLovingPlayer() != null) {
                serverPlayerEntity = other.getLovingPlayer();
            }

            if (serverPlayerEntity != null) {
                serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
                Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this, other, child);
            }

            this.setBreedingAge(6000);
            other.setBreedingAge(6000);
            this.resetLoveTicks();
            other.resetLoveTicks();
            child.setBaby(true);

            boolean isVariantHorned = random.nextBoolean();
            child.setVariant(isVariantHorned ? MossbloomVariant.HORNED : MossbloomVariant.FLOWERING);

            child.setHasHorns(isVariantHorned);
            if (isVariantHorned) child.setHornDropTimer(-SHOULD_DROP_HORNS_VALUE);

            child.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            world.spawnEntityAndPassengers(child);
            world.sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);
            if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
            }

        }
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
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

    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        BlockPos pos = this.getBlockPos();
        BlockState blockState = world.getBlockState(pos.down());
        return (blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.STONE) || blockState.isOf(Blocks.MOSS_BLOCK) || blockState.isOf(Blocks.CLAY));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        MossbloomEntity child = ModEntityTypes.MOSSBLOOM.get().create(world);
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
        if (firstPassenger instanceof MobEntity mobEntity) {
            return mobEntity;
        } else {
            if (this.getIsSaddled()) {
                firstPassenger = this.getFirstPassenger();
                if (firstPassenger instanceof PlayerEntity) {
                    return (PlayerEntity) firstPassenger;
                }
            }

            return null;
        }
    }

    public boolean getHasHorns() {
        return this.dataTracker.get(HAS_HORNS);
    }

    public void setHasHorns(boolean hasHorns) {
        this.dataTracker.set(HAS_HORNS, hasHorns);
    }

    public int getHornDropTimer() {
        return this.hornDropTimer;
    }

    public void setHornDropTimer(int hornDropTimer) {
        this.hornDropTimer = hornDropTimer;
    }

    public boolean getIsSaddled() {
        return this.dataTracker.get(IS_SADDLED);
    }

    public void setIsSaddled(boolean saddled) {
        this.dataTracker.set(IS_SADDLED, saddled);
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
        return this.getAttributeValue(EntityAttributes.GENERIC_JUMP_STRENGTH);
    }

    @Override
    public void setJumpStrength(int strength) {
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
    public int getLimitPerChunk() {
        return 2;
    }

    public boolean getSprinting() {
        return this.dataTracker.get(IS_SPRINTING);
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public MossbloomVariant getVariant() {
        return MossbloomVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(MossbloomVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (fallDistance > 1.0F) {
            this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.75F);
        }

        int i = this.computeFallDamage(fallDistance, damageMultiplier);
        if (i <= 0) {
            return false;
        } else {
            this.damage(damageSource, (float) i);
            if (this.hasPassengers()) {

                for (Entity entity : this.getPassengersDeep()) {
                    entity.damage(damageSource, (float) i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if (this.isTamed() && hand == Hand.MAIN_HAND) {
            if (!player.isSneaking()) {
                if (!this.getIsSaddled() && itemStack.isOf(Items.SADDLE)) {
                    if (!player.getAbilities().creativeMode) itemStack.decrement(1);
                    this.setIsSaddled(true);
                    this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.NEUTRAL, 0.5f, 1.25f);
                    return ActionResult.SUCCESS;
                }
                if (itemStack.isEmpty()) {
                    this.setRiding(player);
                    return ActionResult.SUCCESS;
                }
            }

            if (item instanceof ShearsItem && this.getIsSaddled()) {
                this.setIsSaddled(false);
                itemStack.damage(1, player, LivingEntity.getSlotForHand(hand));
                this.dropStack(new ItemStack(Items.SADDLE), 0.5F);
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.NEUTRAL, 0.5f, 1.25f);
                return ActionResult.SUCCESS;
            }
        }

        if (!this.isTamed() && item == this.getTamingItem()) {
            if (this.getWorld().isClient()) return ActionResult.CONSUME;

            if (!player.getAbilities().creativeMode) itemStack.decrement(1);
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_HORSE_EAT, SoundCategory.NEUTRAL, 0.5f, 1.75f);

            if (random.nextInt(8) == 0) {
                super.setOwner(player);
                this.navigation.recalculatePath();
                this.setTarget(null);
                this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            } else {
                this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
            }
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isImmobile() {
        return super.isImmobile() && this.hasPassengers() && this.getIsSaddled();
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
        this.dataTracker.set(IS_SPRINTING, sprinting);
    }

    @Override
    public void startJumping(int height) {
        this.jumping = true;
    }

    @Override
    public void stopJumping() {
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world instanceof ServerWorld serverWorld && this.getVariant() == MossbloomVariant.FLOWERING && this.getRandom().nextInt(4800) == 0) {
            this.tickFertilize(serverWorld);
        }

        this.animationController.tick();
        if (world.isClient) this.tickIdleGestures();

        if (!world.isClient && this.getVariant() == MossbloomVariant.HORNED && !this.isBaby()) {
            if (!this.getHasHorns() && this.getHornDropTimer() >= (SHOULD_DROP_HORNS_VALUE / 3)) this.setHasHorns(true);
            this.setHornDropTimer(this.getHornDropTimer() + 1);
        }

        if (!world.isClient && this.getVariant() == MossbloomVariant.HORNED && this.isBaby()) {
            this.setHasHorns(false);
        }

        this.jumping = false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.isSubmergedInWater() && this.getControllingPassenger() != null)
            this.getControllingPassenger().dismountVehicle();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.hasPassengers() && getControllingPassenger() instanceof PlayerEntity) {
            LivingEntity livingentity = this.getControllingPassenger();
            this.setYaw(livingentity.getYaw());
            this.prevYaw = this.getYaw();
            this.setPitch(livingentity.getPitch() * 0.5F);
            this.setRotation(this.getYaw(), this.getPitch());
            this.bodyYaw = this.getYaw();
            this.headYaw = this.bodyYaw;
            float f = livingentity.sidewaysSpeed * 0.5F;
            float f1 = livingentity.forwardSpeed;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            if (this.isLogicalSideForUpdatingMovement()) {
                float newSpeed = (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);

                boolean sprinting = livingentity.isSprinting();

                if (sprinting) newSpeed *= 1.4F;
                this.setIsSprinting(sprinting);

                this.setMovementSpeed(newSpeed);
                super.travel(new Vec3d(f, movementInput.y, f1));
            }
        } else {
            if (!this.getWorld().isClient && this.getSprinting()) this.setIsSprinting(false);
            super.travel(movementInput);
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        }
        int[][] is = Dismounting.getDismountOffsets(direction);
        BlockPos blockPos = this.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (EntityPose entityPose : passenger.getPoses()) {
            Box box = passenger.getBoundingBox(entityPose);
            for (int[] js : is) {
                mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
                double d = this.getWorld().getDismountHeight(mutable);
                if (!Dismounting.canDismountInBlock(d)) continue;
                Vec3d vec3d = Vec3d.ofCenter(mutable, d);
                if (!Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) continue;
                passenger.setPose(entityPose);
                return vec3d;
            }
        }
        return super.updatePassengerForDismount(passenger);
    }

    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return MathHelper.ceil((fallDistance * 0.5F - 3.0F) * damageMultiplier);
    }

    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        if (this.isOnGround() && this.jumpStrength == 0.0F && !this.jumping) {
            return Vec3d.ZERO;
        } else {
            float f = controllingPlayer.sidewaysSpeed * 0.5F;
            float g = controllingPlayer.forwardSpeed;
            if (g <= 0.0F) {
                g *= 0.25F;
            }

            return new Vec3d(f, 0.0, g);
        }
    }

    protected Vec2f getControlledRotation(LivingEntity controllingPassenger) {
        return new Vec2f(controllingPassenger.getPitch() * 0.5F, controllingPassenger.getYaw());
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_MOSSBLOOM_HURT.get();
    }

    protected void jump(float strength, Vec3d movementInput) {
        double d = this.getJumpStrength() * (double) strength * (double) this.getJumpVelocityMultiplier();
        double e = d + (double) this.getJumpBoostVelocityModifier();
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x, e, vec3d.z);
        this.setInAir(true);
        this.velocityDirty = true;
        if (movementInput.z > 0.0) {
            float f = MathHelper.sin(this.getYaw() * 0.017453292F);
            float g = MathHelper.cos(this.getYaw() * 0.017453292F);
            this.setVelocity(this.getVelocity().add((-0.4F * f * strength), 0.0, (0.4F * g * strength)));
        }

    }

    @Override
    protected void onGrowUp() {
        super.onGrowUp();

        if (!this.isBaby() && this.getVariant() == MossbloomVariant.HORNED) {
            this.setHasHorns(true);
        }
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.isLiquid()) {
            BlockState blockState = this.getWorld().getBlockState(pos.up());
            BlockSoundGroup blockSoundGroup = state.getSoundGroup();
            if (blockState.isOf(Blocks.SNOW)) {
                blockSoundGroup = blockState.getSoundGroup();
            }

            if (this.hasPassengers()) {
                ++this.soundTicks;
                if (this.soundTicks > 5 && this.soundTicks % 3 == 0) {
                    this.playWalkSound(blockSoundGroup);
                } else if (this.soundTicks <= 5) {
                    this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch() + 0.5f);
                }
            } else if (this.isWooden(blockSoundGroup)) {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch() + 0.5f);
            } else {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch() + 0.5f);
            }

        }
    }

    protected void playWalkSound(BlockSoundGroup group) {
        this.playSound(SoundEvents.ENTITY_HORSE_GALLOP, group.getVolume() * 0.05F, group.getPitch() + 0.25f);
        if (this.random.nextInt(10) == 0) {
            this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, group.getVolume() * 0.6F, group.getPitch() + 0.5f);
        }
    }

    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        super.tickControlled(controllingPlayer, movementInput);
        Vec2f vec2f = this.getControlledRotation(controllingPlayer);
        this.setRotation(vec2f.y, vec2f.x);
        this.prevYaw = this.bodyYaw = this.headYaw = this.getYaw();
        if (this.isLogicalSideForUpdatingMovement()) {

            if (this.isOnGround()) {
                this.setInAir(false);
                if (this.jumpStrength > 0.0F && !this.isInAir()) {
                    this.jump(this.jumpStrength, movementInput);
                }

                this.jumpStrength = 0.0F;
            }
        }

    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.5f, 0.5F);
    }

    private boolean isWooden(BlockSoundGroup soundGroup) {
        return soundGroup == BlockSoundGroup.WOOD || soundGroup == BlockSoundGroup.NETHER_WOOD || soundGroup == BlockSoundGroup.NETHER_STEM || soundGroup == BlockSoundGroup.CHERRY_WOOD || soundGroup == BlockSoundGroup.BAMBOO_WOOD;
    }

    private void pickRandomIdleAnim(int i) {
        switch (i) {
            case 1 -> this.earTwitchAnimationStateRE.start(this.age);
            case 2 -> this.earTwitchAnimationStateLE.start(this.age);
            case 3 -> this.wagTailAnimationStateBE.start(this.age);
            default -> this.earTwitchAnimationStateBE.start(this.age);
        }
        this.idleGestureTicksRemaining = i == 3 ? TAIL_GESTURE_TICKS : EAR_GESTURE_TICKS;
    }

    private void stopIdleGestureAnimations() {
        this.earTwitchAnimationStateRE.stop();
        this.earTwitchAnimationStateLE.stop();
        this.earTwitchAnimationStateBE.stop();
        this.wagTailAnimationStateBE.stop();
    }

    private void setRiding(PlayerEntity pPlayer) {
        pPlayer.setYaw(this.getYaw());
        pPlayer.setPitch(this.getPitch());
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

    private void tickFertilize(ServerWorld world) {
        BlockPos origin = this.getBlockPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        Random random = this.getRandom();

        for (int i = 0; i < 32; i++) {
            pos.set(origin.getX() + random.nextInt(11) - 5,
                    origin.getY() + random.nextInt(3) - 1,
                    origin.getZ() + random.nextInt(11) - 5);

            BlockState state = world.getBlockState(pos);
            if (state.isAir()) continue;

            Block block = state.getBlock();
            if (state.isIn(BlockTags.BEE_GROWABLES) && block instanceof Fertilizable fertilizable
                    && fertilizable.isFertilizable(world, pos, state) && fertilizable.canGrow(world, random, pos, state)) {
                fertilizable.grow(world, random, pos, state);
                world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        15, 0.5, 0.5, 0.5, 0.0);
                return;
            }
        }
    }
}
