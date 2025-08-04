package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.PotionWaspVariant;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class PotionWaspEntity extends ParentAnimalEntity {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(PotionWaspEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> HAS_POTION_SAC = DataTracker.registerData(PotionWaspEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState flyingAnimState = new AnimationState();
    private int animationTimeout = 0;

    public PotionWaspEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.lookControl = new PotionWaspLookControl(this);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effectInstance) {
        List<StatusEffect> potionEffects = PotionWaspVariant.getAllStatusEffects();
        for (StatusEffect effect : potionEffects) {
            if (effectInstance.getEffectType() == effect) return false;
        }
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.hasPotionSac()) {
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                PotionSacEntity potionSacEntity = ModEntityTypes.POTION_SAC.create(serverWorld);

                if (potionSacEntity == null) return super.damage(source, amount);

                potionSacEntity.setVariant(this.getVariant());
                potionSacEntity.setPosition(this.getPos().add(0D, -0.25D, 0D));
                potionSacEntity.setOnGround(false);

                serverWorld.spawnEntity(potionSacEntity);
                this.setHasPotionSac(false);
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public PotionWaspVariant getVariant() {
        return PotionWaspVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(PotionWaspVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public boolean hasPotionSac() {
        return this.dataTracker.get(HAS_POTION_SAC);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        PotionWaspVariant variant = PotionWaspVariant.getRandom();
        this.setVariant(variant);
        this.setHasPotionSac(true);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
        this.dataTracker.set(HAS_POTION_SAC, nbt.getBoolean("HasPotionSac"));
    }

    public void setHasPotionSac(boolean hasPotionSac) {
        this.dataTracker.set(HAS_POTION_SAC, hasPotionSac);
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        super.takeKnockback(this.hasPotionSac() ? 0 : strength, x, z);
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("HasPotionSac", this.hasPotionSac());
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }

        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, PotionWaspVariant.REGENERATION.getId());
        this.dataTracker.startTracking(HAS_POTION_SAC, true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new PotionWaspWanderAroundGoal(this));
    }

    private void setupAnimationStates() {
        if (this.animationTimeout <= 0) {
            this.animationTimeout = 40;
            this.flyingAnimState.start(this.age);
        } else {
            --this.animationTimeout;
        }
    }

    static class PotionWaspLookControl extends LookControl {
        PotionWaspLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            super.tick();
        }

        protected boolean shouldStayHorizontal() {
            return true;
        }
    }

    class PotionWaspWanderAroundGoal extends Goal {

        PotionWaspWanderAroundGoal(PotionWaspEntity potionWasp) {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return PotionWaspEntity.this.navigation.isIdle() && PotionWaspEntity.this.random.nextInt(4) == 0;
        }

        public boolean shouldContinue() {
            return PotionWaspEntity.this.navigation.isFollowingPath();
        }

        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                PotionWaspEntity.this.navigation.startMovingAlong(PotionWaspEntity.this.navigation.findPathTo(BlockPos.ofFloored(vec3d), 1), 1.0F);
            }

        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d2 = PotionWaspEntity.this.getRotationVec(0.0F);

            Vec3d vec3d3 = AboveGroundTargeting.find(PotionWaspEntity.this, 8, 7, vec3d2.x, vec3d2.z, ((float) Math.PI / 2F), 3, 1);
            return vec3d3 != null ? vec3d3 : NoPenaltySolidTargeting.find(PotionWaspEntity.this, 8, 4, -2, vec3d2.x, vec3d2.z, (float) Math.PI / 2F);
        }
    }

}