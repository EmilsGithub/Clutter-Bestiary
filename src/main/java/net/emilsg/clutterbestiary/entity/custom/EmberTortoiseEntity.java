package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.*;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EmberTortoiseEntity extends ParentAnimalEntity {
    private static final TrackedData<Boolean> MOVING = DataTracker.registerData(EmberTortoiseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(EmberTortoiseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SHIELDING = DataTracker.registerData(EmberTortoiseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SHIELDING_DURATION = DataTracker.registerData(EmberTortoiseEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SHIELDING_COOLDOWN = DataTracker.registerData(EmberTortoiseEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.FIRE_CHARGE);
    public static Map<Block, Block> smeltableBlocksConversionMap = new HashMap<>();

    static {
        smeltableBlocksConversionMap.put(Blocks.GRASS_BLOCK, Blocks.DIRT);
        smeltableBlocksConversionMap.put(Blocks.PODZOL, Blocks.DIRT);
        smeltableBlocksConversionMap.put(Blocks.MYCELIUM, Blocks.DIRT);
        smeltableBlocksConversionMap.put(Blocks.ICE, Blocks.WATER);
        smeltableBlocksConversionMap.put(Blocks.CRIMSON_NYLIUM, Blocks.NETHERRACK);
        smeltableBlocksConversionMap.put(Blocks.WARPED_NYLIUM, Blocks.NETHERRACK);
        smeltableBlocksConversionMap.put(Blocks.RAW_IRON_BLOCK, Blocks.IRON_BLOCK);
        smeltableBlocksConversionMap.put(Blocks.RAW_COPPER_BLOCK, Blocks.COPPER_BLOCK);
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


    public EmberTortoiseEntity(EntityType<? extends ParentAnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.175f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0f);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        if (stackInHand.isOf(this.getShieldingRechargeItem())) {
            if (this.canShield()) {
                this.startShielding();
                player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.5F + random.nextFloat(), 0.75F);
                player.swingHand(hand);
                if (!player.getAbilities().creativeMode) stackInHand.decrement(1);
            } else {
                int cooldown = this.getShieldingCooldown();
                this.setShieldingCooldown(cooldown - 100);
            }
        }

        return super.interactMob(player, hand);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, net.minecraft.util.math.random.Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.EMBER_TORTOISES_SPAWN_ON);
    }

    public boolean canShield() {
        return this.getShieldingCooldown() <= 0;
    }

    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return true;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.EMBER_TORTOISE.create(world);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isShielding()) {
            LivingEntity livingEntity = (LivingEntity) source.getAttacker();

            if (source.getSource() instanceof ProjectileEntity projectile) {
                projectile.setVelocity(projectile.getVelocity().multiply(-1));
                return false;
            }

            if (livingEntity != null && livingEntity.getMainHandStack().getItem() instanceof PickaxeItem) {
                return super.damage(source, amount * 2);
            }

            return super.damage(source, amount / 16);
        }
        return super.damage(source, amount);
    }

    public int getShieldingCooldown() {
        return this.dataTracker.get(SHIELDING_COOLDOWN);
    }

    public void setShieldingCooldown(int shieldingCooldown) {
        this.dataTracker.set(SHIELDING_COOLDOWN, shieldingCooldown);
    }

    public int getShieldingDuration() {
        return this.dataTracker.get(SHIELDING_DURATION);
    }

    public void setShieldingDuration(int shieldingDuration) {
        this.dataTracker.set(SHIELDING_DURATION, shieldingDuration);
    }

    public Item getShieldingRechargeItem() {
        return Items.BLAZE_POWDER;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (this.isAlive() && this.getHealth() <= (this.getMaxHealth() / 4) && this.canShield() && !world.isClient()) {
            this.startShielding();
        } else if (this.isAlive() && this.getShieldingDuration() <= 0 && !world.isClient()) {
            this.setShielding(false);
        }

        if (this.isShielding() && world.isClient && !this.isDead()) {
            Vec3d entityPos = this.getPos();
            Random random = new Random();
            int numberOfParticles = 10;

            fireSoundTicker++;
            fireChargeSoundTicker++;

            if (fireSoundTicker >= 20) {
                world.playSound(entityPos.getX() + 0.5F, entityPos.getY() + 0.5F, entityPos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.NEUTRAL, 0.5F + random.nextFloat(), 0.0125F, false);
                fireSoundTicker = 0;
            }

            if (fireChargeSoundTicker >= 20) {
                world.playSound(entityPos.getX() + 0.5F, entityPos.getY() + 0.5F, entityPos.getZ() + 0.5F, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 0.5F + random.nextFloat(), 0.25F, false);
                fireChargeSoundTicker = 0;
            }

            for (int i = 0; i < numberOfParticles; i++) {
                double velocityX = (random.nextDouble() - 0.5) * 0.4;
                double velocityY = (random.nextDouble() - 0.5) * 0.4;
                double velocityZ = (random.nextDouble() - 0.5) * 0.4;

                world.addParticle(random.nextBoolean() ? ParticleTypes.FLAME : ParticleTypes.SMALL_FLAME,
                        entityPos.x, entityPos.y + 1, entityPos.z,
                        velocityX, velocityY, velocityZ);
            }
        }

        if (this.isShielding() && !world.isClient() && !this.isDead()) {
            if (this.isTouchingWaterOrRain()) this.setShielding(false);

            this.setShieldingDuration(this.getShieldingDuration() - 1);

            Box area = new Box(this.getBlockPos()).expand(3, 1, 3);

            List<LivingEntity> nearbyEntities = world.getEntitiesByClass(LivingEntity.class, area, e -> true);

            if (nearbyEntities != null) {
                for (LivingEntity entity : nearbyEntities) {
                    entity.setOnFire(true);
                    entity.setFireTicks(100);
                    if (entity instanceof PlayerEntity player && world instanceof ServerWorld serverWorld) {
                        ModUtil.grantImpossibleAdvancement("bestiary/hot_hot_hot", serverWorld, player);
                    }
                }
            }

            this.meltNearbyBlocks(world);
        }

        if (!world.isClient() && this.getHealth() < this.getMaxHealth() && random.nextInt(200) == 0 && this.getWorld().getDimensionEntry().matchesKey(DimensionTypes.THE_NETHER) && this.isAlive()) {
            this.setHealth((float) (int) (this.getHealth() + 1));
        }

        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    public boolean isAttacking() {
        return this.dataTracker.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.dataTracker.set(ATTACKING, attacking);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.FIRE_CHARGE);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return this.isShielding();
    }

    public boolean isMoving() {
        return this.dataTracker.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.dataTracker.set(MOVING, moving);
    }

    @Override
    public boolean isPushable() {
        return !this.isShielding();
    }

    public boolean isShielding() {
        return this.dataTracker.get(SHIELDING);
    }

    public void setShielding(boolean shielding) {
        this.dataTracker.set(SHIELDING, shielding);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setShielding(nbt.getBoolean("Shielding"));
        this.setShieldingDuration(nbt.getInt("ShieldingDuration"));
        this.setShieldingCooldown(nbt.getInt("ShieldingCooldown"));
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        super.takeKnockback(0, x, z);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_EMBER_TORTOISE_HURT;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient) {
            BlockPos oldPos = this.getBlockPos();
            BlockPos newPos = this.getBlockPos();
            this.setMoving(oldPos != newPos);
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Shielding", this.isShielding());
        nbt.putInt("ShieldingDuration", this.getShieldingDuration());
        nbt.putInt("ShieldingCooldown", this.getShieldingCooldown());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(MOVING, false);
        builder.add(ATTACKING, false);
        builder.add(SHIELDING, false);
        builder.add(SHIELDING_COOLDOWN, 0);
        builder.add(SHIELDING_DURATION, 400);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new EmberTortoiseMeleeGoal(this, 1.0f, true));
        this.goalSelector.add(3, new EmberTortoiseMateGoal(this, 1.2f));
        this.goalSelector.add(4, new EmberTortoiseTemptGoal(this, 1.2f, BREEDING_INGREDIENT, false));
        this.goalSelector.add(5, new EmberTortoiseFollowParentGoal(this, 1.2f));
        this.goalSelector.add(6, new EmberTortoiseWanderAroundFarGoal(this, 1.0f, 0.001f));
        this.goalSelector.add(7, new EmberTortoiseLookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new EmberTortoiseLookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, HoglinEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZoglinEntity.class, true));

    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 3f, 0.3F);
    }

    private void meltNearbyBlocks(World world) {
        BlockPos center = this.getBlockPos();
        int radius = 3;

        BlockPos.stream(center.add(-radius, -radius, -radius), center.add(radius + 1, radius, radius + 1))
                .filter(pos -> pos.isWithinDistance(center, radius + 0.5))
                .forEach(pos -> {
                    BlockState state = world.getBlockState(pos);
                    Block inputBlock = state.getBlock();
                    ItemStack inputStack = new ItemStack(inputBlock.asItem());

                    var match = world.getRecipeManager().getFirstMatch(
                            RecipeType.SMELTING,
                            new net.minecraft.recipe.input.SingleStackRecipeInput(inputStack),
                            world
                    );

                    if (match.isPresent()) {
                        ItemStack result = match.get().value().getResult(world.getRegistryManager());
                        if (Block.getBlockFromItem(result.getItem()) != Blocks.AIR) {
                            Block resultBlock = Block.getBlockFromItem(result.getItem());
                            if (random.nextInt(750) == 0) world.setBlockState(pos, resultBlock.getDefaultState());
                        }
                    } else if (smeltableBlocksConversionMap.containsKey(inputBlock)) {
                        if (random.nextInt(750) == 0)
                            world.setBlockState(pos, smeltableBlocksConversionMap.get(inputBlock).getDefaultState());
                    }

                    if (random.nextInt(2000) == 0 && world.getBlockState(pos.up()).isAir() && state.isSolidBlock(world, pos)) {
                        world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                    }
                });
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && !this.isMoving() && !this.isShielding()) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 40;
            attackAnimationState.start(this.age);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            attackAnimationState.stop();
        }

        if (this.isShielding() && shieldingAnimationTimeout <= 0) {
            this.shieldingAnimationState.start(this.age);
            this.shieldingTubeAnimationState.startIfNotRunning(this.age);
            this.shieldingAnimationTimeout = 1;
        } else if (!this.isShielding()) {
            this.shieldingAnimationTimeout = 0;
        }
    }

    private void startShielding() {
        this.setShielding(true);
        this.setShieldingDuration((random.nextBetween(4, 8) + 1) * 100);
        this.setShieldingCooldown(2400);
    }
}
