package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmberTortoiseEntity extends ParentAnimalEntity {
    private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(EmberTortoiseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(EmberTortoiseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHIELDING = SynchedEntityData.defineId(EmberTortoiseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHIELDING_DURATION = SynchedEntityData.defineId(EmberTortoiseEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHIELDING_COOLDOWN = SynchedEntityData.defineId(EmberTortoiseEntity.class, EntityDataSerializers.INT);
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.of(Items.FIRE_CHARGE);
    public static Map<Block, Block> smeltableBlocksConversionMap = new HashMap<>();

    static {
        smeltableBlocksConversionMap.put(Blocks.GRASS_BLOCK, Blocks.DIRT);
        smeltableBlocksConversionMap.put(Blocks.PODZOL, Blocks.DIRT);
        smeltableBlocksConversionMap.put(Blocks.MYCELIUM, Blocks.DIRT);
        smeltableBlocksConversionMap.put(Blocks.ICE, Blocks.WATER);
        smeltableBlocksConversionMap.put(Blocks.CRIMSON_NYLIUM, Blocks.NETHERRACK);
        smeltableBlocksConversionMap.put(Blocks.WARPED_NYLIUM, Blocks.NETHERRACK);
        smeltableBlocksConversionMap.put(Blocks.RAW_IRON_BLOCK, Blocks.IRON_BLOCK);
        smeltableBlocksConversionMap.put(Blocks.RAW_COPPER_BLOCK, Blocks.COPPER_BLOCK.weathering().unaffected());
        smeltableBlocksConversionMap.put(Blocks.RAW_GOLD_BLOCK, Blocks.GOLD_BLOCK);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shieldingAnimationState = new AnimationState();
    public final AnimationState shieldingTubeAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public int idleAnimationTimeout = 0;
    public int shieldingAnimationTimeout = 0;
    public int attackAnimationTimeout = 0;
    private int fireSoundTicker = 0;
    private int fireChargeSoundTicker = 0;


    public EmberTortoiseEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOVING, false);
        builder.define(ATTACKING, false);
        builder.define(SHIELDING, false);
        builder.define(SHIELDING_COOLDOWN, 0);
        builder.define(SHIELDING_DURATION, 400);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EmberTortoiseMeleeGoal(this, 1.0f, true));
        this.goalSelector.addGoal(3, new EmberTortoiseMateGoal(this, 1.2f));
        this.goalSelector.addGoal(4, new EmberTortoiseTemptGoal(this, 1.2f, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(5, new EmberTortoiseFollowParentGoal(this, 1.2f));
        this.goalSelector.addGoal(6, new EmberTortoiseWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.addGoal(7, new EmberTortoiseLookAtEntityGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new EmberTortoiseLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Hoglin.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zoglin.class, true));

    }

    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setShielding(nbt.getBooleanOr("Shielding", false));
        this.setShieldingDuration(nbt.getIntOr("ShieldingDuration", 0));
        this.setShieldingCooldown(nbt.getIntOr("ShieldingCooldown", 0));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Shielding", this.isShielding());
        nbt.putInt("ShieldingDuration", this.getShieldingDuration());
        nbt.putInt("ShieldingCooldown", this.getShieldingCooldown());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.175f)
                .add(Attributes.ATTACK_DAMAGE, 8.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.EMBER_TORTOISES_SPAWN_ON);
    }

    public boolean canShield() {
        return this.getShieldingCooldown() <= 0;
    }

    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason spawnReason) {
        return true;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.EMBER_TORTOISE.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (this.isShielding()) {
            if (source.getEntity() instanceof LivingEntity livingEntity) {
                if (livingEntity.getMainHandItem().is(ItemTags.PICKAXES)) {
                    return super.hurtServer(serverLevel, source, amount * 2);
                }
            }

            if (source.getDirectEntity() instanceof Projectile projectile) {
                projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-1));
                return false;
            }

            return super.hurtServer(serverLevel, source, amount / 16);
        }
        return super.hurtServer(serverLevel, source, amount);
    }

    public int getShieldingCooldown() {
        return this.entityData.get(SHIELDING_COOLDOWN);
    }

    public void setShieldingCooldown(int shieldingCooldown) {
        this.entityData.set(SHIELDING_COOLDOWN, shieldingCooldown);
    }

    public int getShieldingDuration() {
        return this.entityData.get(SHIELDING_DURATION);
    }

    public void setShieldingDuration(int shieldingDuration) {
        this.entityData.set(SHIELDING_DURATION, shieldingDuration);
    }

    public Item getShieldingRechargeItem() {
        return Items.BLAZE_POWDER;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        if (stackInHand.is(this.getShieldingRechargeItem())) {
            if (this.canShield()) {
                this.startShielding();
                player.playSound(SoundEvents.FIRECHARGE_USE, 0.5F + random.nextFloat(), 0.75F);
                player.swing(hand, SwingAnimation.DEFAULT, false);
                if (!player.getAbilities().instabuild) stackInHand.shrink(1);
            } else {
                int cooldown = this.getShieldingCooldown();
                this.setShieldingCooldown(cooldown - 100);
            }
        }

        return super.mobInteract(player, hand);
    }

    public boolean isAggressive() {
        return this.entityData.get(ATTACKING);
    }

    public void setAggressive(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return this.isShielding();
    }

    public boolean isMoving() {
        return this.entityData.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(MOVING, moving);
    }

    @Override
    public boolean isPushable() {
        return !this.isShielding();
    }

    public boolean isShielding() {
        return this.entityData.get(SHIELDING);
    }

    public void setShielding(boolean shielding) {
        this.entityData.set(SHIELDING, shielding);
    }

    @Override
    public void knockback(double strength, double x, double z, DamageSource source, float damage, boolean comesFromEffect) {
        super.knockback(0, x, z, source, damage, comesFromEffect);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (this.isAlive() && this.getHealth() <= (this.getMaxHealth() / 4) && this.canShield() && !world.isClientSide()) {
            this.startShielding();
        } else if (this.isAlive() && this.getShieldingDuration() <= 0 && !world.isClientSide()) {
            this.setShielding(false);
        }

        if (this.isShielding() && world.isClientSide() && !this.isDeadOrDying()) {
            Vec3 entityPos = this.position();
            int numberOfParticles = 10;

            fireSoundTicker++;
            fireChargeSoundTicker++;

            if (fireSoundTicker >= 20) {
                world.playLocalSound(entityPos.x() + 0.5F, entityPos.y() + 0.5F, entityPos.z() + 0.5F, SoundEvents.FIRE_AMBIENT, SoundSource.NEUTRAL, 0.5F + this.random.nextFloat(), 0.0125F, false);
                fireSoundTicker = 0;
            }

            if (fireChargeSoundTicker >= 20) {
                world.playLocalSound(entityPos.x() + 0.5F, entityPos.y() + 0.5F, entityPos.z() + 0.5F, SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F + this.random.nextFloat(), 0.25F, false);
                fireChargeSoundTicker = 0;
            }

            for (int i = 0; i < numberOfParticles; i++) {
                double velocityX = (this.random.nextDouble() - 0.5) * 0.4;
                double velocityY = (this.random.nextDouble() - 0.5) * 0.4;
                double velocityZ = (this.random.nextDouble() - 0.5) * 0.4;

                world.addParticle(this.random.nextBoolean() ? ParticleTypes.FLAME : ParticleTypes.SMALL_FLAME,
                        entityPos.x, entityPos.y + 1, entityPos.z,
                        velocityX, velocityY, velocityZ);
            }
        }

        if (this.isShielding() && !world.isClientSide() && !this.isDeadOrDying()) {
            if (this.isInWaterOrRain()) this.setShielding(false);

            this.setShieldingDuration(this.getShieldingDuration() - 1);

            if (this.tickCount % 5 == 0) {
                AABB area = new AABB(this.blockPosition()).inflate(3, 1, 3);
                List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(LivingEntity.class, area, e -> true);
                for (LivingEntity entity : nearbyEntities) {
                    entity.setSharedFlagOnFire(true);
                    entity.setRemainingFireTicks(100);
                    if (entity instanceof Player player && world instanceof ServerLevel) {
                        ModUtil.grantImpossibleAdvancement("bestiary/hot_hot_hot", player);
                    }
                }
                this.meltNearbyBlocks(world);
            }
        }

        if (!world.isClientSide() && this.getHealth() < this.getMaxHealth() && random.nextInt(200) == 0 && this.level().dimensionTypeRegistration().is(BuiltinDimensionTypes.NETHER) && this.isAlive()) {
            this.setHealth((float) (int) (this.getHealth() + 1));
        }

        if (world.isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_EMBER_TORTOISE_HURT.get();
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 3f, 0.3F, 1.0F);
    }

    private void meltNearbyBlocks(Level level) {
        if (!(level instanceof ServerLevel world)) return;
        BlockPos center = this.blockPosition();
        int radius = 3;
        Map<Block, Block> smeltResults = new HashMap<>();

        BlockPos.betweenClosedStream(center.offset(-radius, -radius, -radius), center.offset(radius + 1, radius, radius + 1))
                .filter(pos -> pos.closerThan(center, radius + 0.5))
                .forEach(pos -> {
                    BlockState state = world.getBlockState(pos);
                    if (state.isAir()) return;
                    Block inputBlock = state.getBlock();
                    Block resultBlock = smeltResults.computeIfAbsent(inputBlock, block -> {
                        ItemStack inputStack = new ItemStack(block.asItem());
                        SingleRecipeInput input = new SingleRecipeInput(inputStack);
                        var match = world.recipeAccess().getRecipeFor(
                                RecipeType.SMELTING,
                                input,
                                world
                        );
                        if (match.isPresent()) {
                            ItemStack result = match.get().value().assemble(input);
                            return Block.byItem(result.getItem());
                        }
                        return smeltableBlocksConversionMap.getOrDefault(block, Blocks.AIR);
                    });

                    if (resultBlock != Blocks.AIR && random.nextInt(150) == 0) {
                        world.setBlockAndUpdate(pos, resultBlock.defaultBlockState());
                    }

                    if (random.nextInt(400) == 0 && world.getBlockState(pos.above()).isAir() && state.isRedstoneConductor(world, pos)) {
                        world.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
                    }
                });
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && !this.isMoving() && !this.isShielding()) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAggressive() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 40;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAggressive()) {
            attackAnimationState.stop();
        }

        if (this.isShielding() && shieldingAnimationTimeout <= 0) {
            this.shieldingAnimationState.start(this.tickCount);
            this.shieldingTubeAnimationState.startIfStopped(this.tickCount);
            this.shieldingAnimationTimeout = 1;
        } else if (!this.isShielding()) {
            this.shieldingAnimationTimeout = 0;
        }
    }

    private void startShielding() {
        this.setShielding(true);
        this.setShieldingDuration((random.nextIntBetweenInclusive(4, 8) + 1) * 100);
        this.setShieldingCooldown(2400);
    }
}
