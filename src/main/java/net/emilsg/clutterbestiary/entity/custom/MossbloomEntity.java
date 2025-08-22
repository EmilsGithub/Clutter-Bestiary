package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.MossbloomDropHornsGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.TrackedFleeGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WanderAroundFarOftenGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.entity.variants.MossbloomVariant;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class MossbloomEntity extends ParentTameableEntity implements Mount, JumpingMount{

    private static final TrackedData<String> VARIANT = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> HAS_HORNS = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> HORN_DROP_TIMER = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_TILL_DROP = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_SHAKING = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SPRINTING = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SADDLED = DataTracker.registerData(MossbloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static int SHOULD_DROP_HORNS_VALUE = 12000;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shakingAnimationState = new AnimationState();
    public final AnimationState earTwitchAnimationStateLE = new AnimationState();
    public final AnimationState earTwitchAnimationStateRE = new AnimationState();
    public final AnimationState earTwitchAnimationStateBE = new AnimationState();
    public final AnimationState wagTailAnimationStateBE = new AnimationState();
    public int idleAnimationTimeout = 0;
    public int shakingAnimationTimeout = 0;
    public int earTwitchAnimationTimeout = 0;

    protected int soundTicks;
    protected boolean inAir;
    protected float jumpStrength;

    private final Item tamingItem = Items.SPORE_BLOSSOM;

    public MossbloomEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f)
                .add(EntityAttributes.HORSE_JUMP_STRENGTH, 0.75f);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return isValidLushSpawn(type, world, spawnReason, pos, random) && isLightLevelValidForNaturalSpawn(world, pos);
    }

    public static boolean isValidLushSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isOf(Blocks.MOSS_BLOCK) ||
                world.getBlockState(pos.down()).isOf(Blocks.MOSS_CARPET) ||
                world.getBlockState(pos.down()).isOf(Blocks.CLAY);
    }

    protected static boolean isLightLevelValidForNaturalSpawn(BlockRenderView world, BlockPos pos) {
        return true;
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
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
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

    private boolean isWooden(BlockSoundGroup soundGroup) {
        return soundGroup == BlockSoundGroup.WOOD || soundGroup == BlockSoundGroup.NETHER_WOOD || soundGroup == BlockSoundGroup.NETHER_STEM || soundGroup == BlockSoundGroup.CHERRY_WOOD || soundGroup == BlockSoundGroup.BAMBOO_WOOD;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (fallDistance > 1.0F) {
            this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.75F);
        }

        int i = this.computeFallDamage(fallDistance, damageMultiplier);
        if (i <= 0) {
            return false;
        } else {
            this.damage(damageSource, (float)i);
            if (this.hasPassengers()) {

                for (Entity entity : this.getPassengersDeep()) {
                    entity.damage(damageSource, (float) i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return MathHelper.ceil((fallDistance * 0.5F - 3.0F) * damageMultiplier);
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

    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        BlockPos pos = this.getBlockPos();
        BlockState blockState = world.getBlockState(pos.down());
        return (blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.STONE) || blockState.isOf(Blocks.MOSS_BLOCK) || blockState.isOf(Blocks.CLAY));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        MossbloomEntity child = ModEntityTypes.MOSSBLOOM.create(world);
        if (child != null) child.setVariant(MossbloomVariant.getRandom());
        return child;
    }

    public boolean getHasHorns() {
        return this.dataTracker.get(HAS_HORNS);
    }

    public void setHasHorns(boolean hasHorns) {
        this.dataTracker.set(HAS_HORNS, hasHorns);
    }

    public int getHornDropTimer() {
        return this.dataTracker.get(HORN_DROP_TIMER);
    }

    public void setHornDropTimer(int hornDropTimer) {
        this.dataTracker.set(HORN_DROP_TIMER, hornDropTimer);
    }

    public boolean getIsShaking() {
        return this.dataTracker.get(IS_SHAKING);
    }

    public void setIsShaking(boolean isShaking) {
        this.dataTracker.set(IS_SHAKING, isShaking);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_MOSSBLOOM_HURT;
    }

    @Override
    public int getLimitPerChunk() {
        return 2;
    }

    public int getTimeTillDrop() {
        return this.dataTracker.get(TIME_TILL_DROP);
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
                this.jumpStrength = 0.4F + 0.4F * (float)strength / 90.0F;
            }

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

    protected void jump(float strength, Vec3d movementInput) {
        double d = this.getJumpStrength() * (double)strength * (double)this.getJumpVelocityMultiplier();
        double e = d + (double)this.getJumpBoostVelocityModifier();
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

    public double getJumpStrength() {
        return this.getAttributeValue(EntityAttributes.HORSE_JUMP_STRENGTH);
    }

    public boolean isInAir() {
        return this.inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    protected Vec2f getControlledRotation(LivingEntity controllingPassenger) {
        return new Vec2f(controllingPassenger.getPitch() * 0.5F, controllingPassenger.getYaw());
    }

    @Override
    public boolean canJump() {
        return this.getIsSaddled();
    }

    @Override
    public void startJumping(int height) {
        this.jumping = true;
    }

    @Override
    public void stopJumping() {
    }

    public void setTimeTillDrop(int timeTillDrop) {
        this.dataTracker.set(TIME_TILL_DROP, timeTillDrop);
    }

    public boolean getSprinting() {
        return this.dataTracker.get(IS_SPRINTING);
    }

    public void setIsSprinting(boolean sprinting) {
        this.dataTracker.set(IS_SPRINTING, sprinting);
    }

    public boolean getIsSaddled() {
        return this.dataTracker.get(IS_SADDLED);
    }

    public void setIsSaddled(boolean saddled) {
        this.dataTracker.set(IS_SADDLED, saddled);
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

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        MossbloomVariant variant = Util.getRandom(MossbloomVariant.values(), this.random);
        this.setVariant(variant);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.BIG_DRIPLEAF);
    }

    public boolean isVariantOf(MossbloomVariant variant) {
        return this.getVariant() == variant;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
        this.dataTracker.set(HAS_HORNS, nbt.getBoolean("HasHorns"));
        this.dataTracker.set(HORN_DROP_TIMER, nbt.getInt("HornDropTimer"));
        this.dataTracker.set(IS_SHAKING, nbt.getBoolean("IsShaking"));
        this.dataTracker.set(IS_SADDLED, nbt.getBoolean("IsSaddled"));
    }

    public boolean isImmobile() {
        return super.isImmobile() && this.hasPassengers() && this.getIsSaddled();
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (this.getVariant() == MossbloomVariant.FLOWERING && this.getRandom().nextInt(4800) == 0) {
            this.tickFertilize(world);
        }

        if (world.isClient) {
            this.setupAnimationStates();
        }

        if (this.getVariant() == MossbloomVariant.HORNED && !this.isBaby()) {
            if (this.getHornDropTimer() >= (SHOULD_DROP_HORNS_VALUE / 3)) this.setHasHorns(true);
            this.setHornDropTimer(this.getHornDropTimer() + random.nextInt(1) + 1);
        }

        if (this.getVariant() == MossbloomVariant.HORNED && this.isBaby()) {
            this.setHasHorns(false);
        }

        this.jumping = false;
    }

    private void tickFertilize(World world) {
        BlockPos origin = this.getBlockPos();
        int ox = origin.getX(), oy = origin.getY(), oz = origin.getZ();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        Random rand = this.getRandom();

        int dyStart = rand.nextInt(3);
        int dyStep  = rand.nextBoolean() ? 1 : 2;
        int dxStart = rand.nextInt(11);
        int dxStep  = rand.nextInt(10) + 1;
        int dzStart = rand.nextInt(11);
        int dzStep  = rand.nextInt(10) + 1;

        int maxChecks = 32;

        outer:
        for (int yi = 0; yi < 3; yi++) {
            int dy = ((dyStart + yi * dyStep) % 3) - 1;

            for (int xi = 0; xi < 11; xi++) {
                int dx = ((dxStart + xi * dxStep) % 11) - 5;

                for (int zi = 0; zi < 11; zi++) {
                    if (--maxChecks < 0) break outer;

                    int dz = ((dzStart + zi * dzStep) % 11) - 5;
                    pos.set(ox + dx, oy + dy, oz + dz);

                    BlockState state = world.getBlockState(pos);
                    if (state.isAir()) continue;

                    Block block = state.getBlock();
                    if (state.isIn(BlockTags.BEE_GROWABLES) && block instanceof Fertilizable fertilizable && fertilizable.isFertilizable(world, pos, state, false) && fertilizable.canGrow(world, rand, pos, state)) {

                        if (world instanceof ServerWorld serverWorld) {
                            fertilizable.grow(serverWorld, rand, pos, state);
                        } else {
                            BoneMealItem.createParticles(world, pos, 15);
                        }
                        break outer;
                    }
                }
            }
        }
    }



    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.isSubmergedInWater() && this.getControllingPassenger() != null) this.getControllingPassenger().dismountVehicle();
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("HasHorns", this.getHasHorns());
        nbt.putInt("HornDropTimer", this.getHornDropTimer());
        nbt.putBoolean("IsShaking", this.getIsShaking());
        nbt.putBoolean("IsSaddled", this.getIsSaddled());
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, MossbloomVariant.HORNED.getId());
        this.dataTracker.startTracking(HAS_HORNS, true);
        this.dataTracker.startTracking(HORN_DROP_TIMER, 0);
        this.dataTracker.startTracking(IS_SHAKING, false);
        this.dataTracker.startTracking(TIME_TILL_DROP, 0);
        this.dataTracker.startTracking(IS_SPRINTING, false);
        this.dataTracker.startTracking(IS_SADDLED, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new MossbloomDropHornsGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TrackedFleeGoal(this, 2.5f));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1));
        this.goalSelector.add(4, new TemptGoal(this, 1.25, Ingredient.ofItems(Items.BIG_DRIPLEAF), false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(6, new WanderAroundFarOftenGoal(this, 1.0f));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected void onGrowUp() {
        super.onGrowUp();

        if (!this.isBaby() && this.getVariant() == MossbloomVariant.HORNED) {
            this.setHasHorns(true);
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

    private void pickRandomIdleAnim(int i) {
        switch (i) {
            case 1 -> this.earTwitchAnimationStateRE.startIfNotRunning(this.age);
            case 2 -> this.earTwitchAnimationStateLE.startIfNotRunning(this.age);
            case 3 -> this.wagTailAnimationStateBE.startIfNotRunning(this.age);
            default -> this.earTwitchAnimationStateBE.startIfNotRunning(this.age);
        }
    }

    private void setupAnimationStates() {
        if (this.shakingAnimationTimeout <= 0 && this.getIsShaking()) {
            this.shakingAnimationTimeout = 60;
            this.shakingAnimationState.start(this.age);
        } else {
            --this.shakingAnimationTimeout;
        }

        if (this.idleAnimationTimeout <= 0 && !this.isMoving()) {
            this.idleAnimationTimeout = 10;
            this.idleAnimationState.startIfNotRunning(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.earTwitchAnimationTimeout <= 0 && random.nextInt(50) == 0) {
            this.earTwitchAnimationTimeout = 5;
            this.pickRandomIdleAnim(random.nextInt(4));
        } else {
            --this.earTwitchAnimationTimeout;
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

            if (item instanceof ShearsItem) {
                this.setIsSaddled(false);
                itemStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                this.dropStack(new ItemStack(Items.SADDLE), 0.5F);
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.NEUTRAL, 0.5f, 1.25f);
                return ActionResult.SUCCESS;
            }
        }

        if (!this.isTamed() && item == tamingItem) {
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
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity firstPassenger = this.getFirstPassenger();
        if (firstPassenger instanceof MobEntity mobEntity) {
            return mobEntity;
        } else {
            if (this.getIsSaddled()) {
                firstPassenger = this.getFirstPassenger();
                if (firstPassenger instanceof PlayerEntity) {
                    return (PlayerEntity)firstPassenger;
                }
            }

            return null;
        }
    }

    private void setRiding(PlayerEntity pPlayer) {
        pPlayer.setYaw(this.getYaw());
        pPlayer.setPitch(this.getPitch());
        pPlayer.startRiding(this);
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
                float newSpeed = (float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);

                boolean sprinting = MinecraftClient.getInstance().options.sprintKey.isPressed();

                if (sprinting) newSpeed *= 1.4F;
                this.setIsSprinting(sprinting);

                this.setMovementSpeed(newSpeed);
                super.travel(new Vec3d(f, movementInput.y, f1));
            }
        } else {
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
}
