package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.custom.goal.EchofinConditionalActiveTargetGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.EchofinWanderAroundGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EchofinEntity extends ParentAnimalEntity {

    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(EchofinEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(EchofinEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> ABILITY_ACTIVE = SynchedEntityData.defineId(EchofinEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState movingAnimState = new AnimationState();
    private int animationTimeout = 0;
    private int abilityTimer;


    public EchofinEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new EchofinLookControl(this);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        setVariant(EchofinVariant.getRandom());
        this.setHomePos(this.blockPosition());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HOME_POS, BlockPos.ZERO);
        builder.define(VARIANT, EchofinVariant.CHORUS.getId());
        builder.define(ABILITY_ACTIVE, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 3.0, true));
        this.goalSelector.addGoal(2, new EchofinWanderAroundGoal(this));
        this.targetSelector.addGoal(1, new EchofinConditionalActiveTargetGoal(this, Player.class, false));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        int i = nbt.getIntOr("HomePosX", 0);
        int j = nbt.getIntOr("HomePosY", 0);
        int k = nbt.getIntOr("HomePosZ", 0);
        this.setHomePos(new BlockPos(i, j, k));
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
        this.setEntityAbilityTimer(nbt.getIntOr("AbilityTimer", 0));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("HomePosX", this.getHomePos().getX());
        nbt.putInt("HomePosY", this.getHomePos().getY());
        nbt.putInt("HomePosZ", this.getHomePos().getZ());
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putInt("AbilityTimer", this.getAbilityTimerEntitiesTimer());
    }

    public void copyDataFromNbt(CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag(this, nbt);
        if (nbt.contains("Variant")) {
            this.setVariant(EchofinVariant.fromId(nbt.getStringOr("Variant", "")));
        }
        if (nbt.contains("AbilityTimer")) {
            this.setEntityAbilityTimer(nbt.getIntOr("AbilityTimer", 0));
        }
    }

    public void copyDataToStack(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbt -> {
            nbt.putString("Variant", this.getTypeVariant());
            nbt.putInt("AbilityTimer", this.getAbilityTimerEntitiesTimer());
        });
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.ATTACK_DAMAGE, 1D)
                .add(Attributes.FLYING_SPEED, 0.75f)
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.ECHOFINS_SPAWN_ON);
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return true;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    public int getAbilityTimerEntitiesTimer() {
        return this.abilityTimer;
    }

    public BlockPos getHomePos() {
        return this.entityData.get(HOME_POS);
    }

    public void setHomePos(BlockPos pos) {
        this.entityData.set(HOME_POS, pos);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 3;
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public EchofinVariant getVariant() {
        return EchofinVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(EchofinVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public boolean hasAbility() {
        return this.entityData.get(ABILITY_ACTIVE);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.is(Items.BUCKET) && this.isAlive()) {
            EchofinVariant variant = this.getVariant();
            Item bucketItem = variant == EchofinVariant.CHORUS
                    ? ModItems.CHORUS_ECHOFIN_BUCKET.get()
                    : ModItems.LEVITATING_ECHOFIN_BUCKET.get();
            ItemStack bucketStack = new ItemStack(bucketItem);
            this.copyDataToStack(bucketStack);
            player.setItemInHand(hand, ItemUtils.createFilledResult(heldItem, player, bucketStack, false));
            this.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0f, 1.5f);

            Level world = this.level();
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, bucketStack);
            }

            this.discard();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean wasExperienceConsumed() {
        return true;
    }

    @Override
    public void handleDamageEvent(DamageSource damageSource) {
        super.handleDamageEvent(damageSource);
        this.setEntityAbilityTimer(0);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);

        Level world = player.level();

        if (world.isClientSide() || player.isCreative()) return;

        if (shouldTeleportPlayers()) teleportPlayer(player, world);
        else if (shouldLevitatePlayers()) levitatePlayer(player);
    }

    @Override
    public void snapTo(BlockPos pos, float yaw, float pitch) {
        this.setHomePos(pos);
        super.snapTo(pos, yaw, pitch);
    }

    public void setEntityAbilityTimer(int timer) {
        this.abilityTimer = Math.max(0, Math.min(2400, timer));
        this.entityData.set(ABILITY_ACTIVE, this.abilityTimer >= 2400);
    }

    public boolean shouldLevitatePlayers() {
        return hasAbility() && this.getVariant() == EchofinVariant.LEVITATING;
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    public boolean shouldTeleportPlayers() {
        return hasAbility() && this.getVariant() == EchofinVariant.CHORUS;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
        if (world instanceof ServerLevel) {
            setEntityAbilityTimer(getAbilityTimerEntitiesTimer() + random.nextInt(3));

            if (hasAbility() && random.nextInt(1000) == 0) {
                setEntityAbilityTimer(0);
            }
        }

        if (world.isClientSide()) {
            this.setupAnimationStates();

            if (this.getVariant() == EchofinVariant.CHORUS && random.nextBoolean()) {
                this.level().addParticle(ParticleTypes.PORTAL, true, false, this.getX() + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1), this.getY() + random.nextDouble() / 16.0 * (double) (random.nextBoolean() ? 1 : -1), this.getZ() + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1), (random.nextBoolean() ? 0.1 : -0.1), (random.nextBoolean() ? 0.1 : -0.1), (random.nextBoolean() ? 0.1 : -0.1));
            }
        }
    }

    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }

        };
        birdNavigation.setCanOpenDoors(false);
        birdNavigation.setCanFloat(false);
        return birdNavigation;
    }

    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private void levitatePlayer(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 2), this);
        if (random.nextBoolean()) this.setEntityAbilityTimer(0);
    }

    private void setupAnimationStates() {
        if (this.animationTimeout <= 0) {
            this.animationTimeout = 20;
            this.movingAnimState.start(this.tickCount);
        } else {
            --this.animationTimeout;
        }
    }

    private void teleportPlayer(Player player, Level world) {
        for (int i = 0; i < 16; ++i) {
            double x = player.getX() + (player.level().getRandom().nextDouble() - 0.5) * 512.0;
            double y = Mth.clamp(player.getY() + (double) (player.level().getRandom().nextInt(16) - 8), world.getMinY(), world.getMinY() + ((ServerLevel) world).getLogicalHeight() - 1);
            double z = player.getZ() + (player.level().getRandom().nextDouble() - 0.5) * 512.0;
            if (player.isPassenger()) {
                player.stopRiding();
            }

            Vec3 vec3d = player.position();
            if (player.randomTeleport(x, y, z, true, state -> false)) {
                world.gameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Context.of(player));
                this.setEntityAbilityTimer(0);
                break;
            }
        }
    }

    private static class EchofinLookControl extends LookControl {
        EchofinLookControl(Mob entity) {
            super(entity);
        }

        public void tick() {
            super.tick();
        }

        protected boolean resetXRotOnTick() {
            return true;
        }
    }

}
