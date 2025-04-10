package net.emilsg.clutter_bestiary.entity.custom;

import net.emilsg.clutter_bestiary.entity.custom.goal.ButterflyWanderOverworldGoal;
import net.emilsg.clutter_bestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutter_bestiary.entity.variants.ButterflyVariant;
import net.emilsg.clutter_bestiary.entity.variants.PotionWaspVariant;
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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class PotionWaspEntity extends ParentAnimalEntity {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(PotionWaspEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> HAS_POTION_SAC = DataTracker.registerData(PotionWaspEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_LARGE = DataTracker.registerData(PotionWaspEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int potionSacDelayTicks = -1;

    public final AnimationState flyingAnimState = new AnimationState();
    private int animationTimeout = 0;

    public final AnimationState popSacAnimState = new AnimationState();


    public PotionWaspEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.lookControl = new PotionWaspLookControl(this);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, PotionWaspVariant.REGENERATION.getId());
        this.dataTracker.startTracking(HAS_POTION_SAC, true);
        this.dataTracker.startTracking(IS_LARGE, false);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
        nbt.putBoolean("HasPotionSac", this.hasPotionSac());
        nbt.putBoolean("IsLarge", this.isLarge());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
        this.dataTracker.set(HAS_POTION_SAC, nbt.getBoolean("HasPotionSac"));
        this.dataTracker.set(IS_LARGE, nbt.getBoolean("IsLarge"));
    }

    public PotionWaspVariant getVariant() {
        return PotionWaspVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(PotionWaspVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setHasPotionSac(boolean hasPotionSac) {
        this.dataTracker.set(HAS_POTION_SAC, hasPotionSac);
    }

    public boolean hasPotionSac() {
        return this.dataTracker.get(HAS_POTION_SAC);
    }

    public void setIsLarge(boolean isLarge) {
        this.dataTracker.set(IS_LARGE, isLarge);
    }

    public boolean isLarge() {
        return this.dataTracker.get(IS_LARGE);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        PotionWaspVariant variant = PotionWaspVariant.getRandom();
        this.setVariant(variant);
        this.setHasPotionSac(true);
        this.setIsLarge(random.nextInt(20) == 0);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (hasPotionSac() && amount >= 1f) {
            if (!this.getWorld().isClient) {
                potionSacDelayTicks = 20;
            }
            if (this.getWorld().isClient) {
                this.popSacAnimState.start(this.age);
            }
        }
        return super.damage(source, amount);
    }

    private void setupAnimationStates() {
        if (this.animationTimeout <= 0) {
            this.animationTimeout = 40;
            this.flyingAnimState.start(this.age);
        } else {
            --this.animationTimeout;
        }
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (potionSacDelayTicks == 0) {
            world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 2f, 0.25f);
        }

        if (world.isClient) {
            this.setupAnimationStates();
        } else {
            this.spawnEffectClound();
        }
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effectInstance) {
        List<StatusEffect> potionEffects = PotionWaspVariant.getAllStatusEffects();
        for (StatusEffect effect : potionEffects) {
            if (effectInstance.getEffectType() == effect) return false;
        }
        return true;
    }

    protected Box calculateBoundingBox() {
        return this.getBoxAt(getX(), getY(), getZ());
    }

    public Box getBoxAt(double x, double y, double z) {
        float width = this.getWidth() / 2.0F;
        float height = this.getHeight();

        width = width * (isLarge() ? 1.3f : 1);
        height = height * (isLarge() ? 1.5f : 1);

        return new Box(x - (double)width, y, z - (double)width, x + (double)width, y + (double)height, z + (double)width);
    }

    private void spawnEffectClound() {
        if (!this.getWorld().isClient && potionSacDelayTicks >= 0) {
            potionSacDelayTicks--;
            if (potionSacDelayTicks == 0) {
                setHasPotionSac(false);
                AreaEffectCloudEntity potionCloud = EntityType.AREA_EFFECT_CLOUD.create((ServerWorld) this.getWorld());
                if (potionCloud != null) {
                    potionCloud.setPotion(this.getVariant().getPotionEffect());
                    potionCloud.setDuration(isLarge() ? 200 : 100);
                    potionCloud.setRadius(isLarge() ? 2.5f : 1.5f);
                    potionCloud.setPosition(this.getPos());
                    this.getWorld().spawnEntity(potionCloud);
                }
            }
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new PotionWaspWanderAroundGoal(this));
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
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

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
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
                PotionWaspEntity.this.navigation.startMovingAlong(PotionWaspEntity.this.navigation.findPathTo(BlockPos.ofFloored(vec3d), 1), (double)1.0F);
            }

        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d2 = PotionWaspEntity.this.getRotationVec(0.0F);

            Vec3d vec3d3 = AboveGroundTargeting.find(PotionWaspEntity.this, 8, 7, vec3d2.x, vec3d2.z, ((float)Math.PI / 2F), 3, 1);
            return vec3d3 != null ? vec3d3 : NoPenaltySolidTargeting.find(PotionWaspEntity.this, 8, 4, -2, vec3d2.x, vec3d2.z, (double)((float)Math.PI / 2F));
        }
    }

}
