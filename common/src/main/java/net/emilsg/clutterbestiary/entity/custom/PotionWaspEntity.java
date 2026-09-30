package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.PotionWaspWanderAroundGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.PotionWaspVariant;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionWaspEntity extends ParentAnimalEntity {
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(PotionWaspEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> HAS_POTION_SAC = SynchedEntityData.defineId(PotionWaspEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState flyingAnimState = new AnimationState();
    private int animationTimeout = 0;
    private int regrowthTicker = 0;

    public PotionWaspEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new LookControl(this);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        PotionWaspVariant variant = PotionWaspVariant.getRandom();
        this.setVariant(variant);
        this.setHasPotionSac(true);

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, PotionWaspVariant.REGENERATION.getId());
        builder.define(HAS_POTION_SAC, true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PotionWaspWanderAroundGoal(this));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
        this.entityData.set(HAS_POTION_SAC, nbt.getBooleanOr("HasPotionSac", false));
        this.regrowthTicker = Math.max(0, nbt.getIntOr("RegrowthTicker", 0));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("HasPotionSac", this.hasPotionSac());
        nbt.putInt("RegrowthTicker", this.regrowthTicker);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.FLYING_SPEED, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.POTION_WASPS_SPAWN_ON);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        List<Holder<MobEffect>> potionEffects = PotionWaspVariant.getAllStatusEffects();
        for (Holder<MobEffect> effect : potionEffects) {
            if (effectInstance.getEffect() == effect) return false;
        }
        return true;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (!super.hurtServer(serverLevel, source, amount)) return false;
        if (this.hasPotionSac()) {
            if (this.level() instanceof ServerLevel serverWorld) {
                PotionSacEntity potionSacEntity = ModEntityTypes.POTION_SAC.get().create(serverWorld, EntitySpawnReason.BREEDING);

                if (potionSacEntity == null) return true;

                potionSacEntity.setVariant(this.getVariant());
                potionSacEntity.setPos(this.position().add(0D, -0.25D, 0D));
                potionSacEntity.setOnGround(false);

                serverWorld.addFreshEntity(potionSacEntity);
                this.setHasPotionSac(false);
            }
        }
        return true;
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    public String getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public PotionWaspVariant getVariant() {
        return PotionWaspVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(PotionWaspVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public boolean hasPotionSac() {
        return this.entityData.get(HAS_POTION_SAC);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    public void setHasPotionSac(boolean hasPotionSac) {
        this.entityData.set(HAS_POTION_SAC, hasPotionSac);
    }

    @Override
    public void knockback(double strength, double x, double z, DamageSource source, float damage, boolean comesFromEffect) {
        super.knockback(this.hasPotionSac() ? 0 : strength, x, z, source, damage, comesFromEffect);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (!world.isClientSide() && !this.hasPotionSac()) {
            if (this.regrowthTicker >= 1200) {
                if (random.nextFloat() <= 0.0125) {
                    this.setHasPotionSac(true);
                    this.regrowthTicker = 0;
                }
            }
            this.regrowthTicker++;
        }

        if (world.isClientSide()) {
            this.setupAnimationStates();
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

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    private void setupAnimationStates() {
        if (this.animationTimeout <= 0) {
            this.animationTimeout = 40;
            this.flyingAnimState.start(this.tickCount);
        } else {
            --this.animationTimeout;
        }
    }

}
