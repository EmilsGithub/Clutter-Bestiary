package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.custom.goal.MantaRayJumpGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentWaterEntity;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MantaRayEntity extends ParentWaterEntity {
    private static final TrackedData<Float> SIZE = DataTracker.registerData(MantaRayEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public final AnimationState flopAnimationState = new AnimationState();
    private int flopAnimationTimeout = 0;

    public MantaRayEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new AquaticMoveControl(this, 65, 10, 0.025F, 0.1F, true);
        this.lookControl = new YawAdjustingLookControl(this, 10);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentWaterEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new MoveIntoWaterGoal(this));
        this.goalSelector.add(1, new SwimAroundGoal(this, 1.0, 10));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(4, new MantaRayJumpGoal(this, 10));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2000000476837158, true));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, JellyfishEntity.class, true));
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos).isIn(ModBlockTags.MANTA_RAYS_SPAWN_ON);
    }

    @Override
    public Vec3d getVehicleAttachmentPos(Entity vehicle) {
        return super.getVehicleAttachmentPos(vehicle).add(0, -2.5f / 16f, 0);
    }

    public float getSize() {
        return this.dataTracker.get(SIZE);
    }

    public void setSize(float size) {
        this.dataTracker.set(SIZE, size);
        Objects.requireNonNull(getAttributeInstance(EntityAttributes.GENERIC_SCALE)).setBaseValue(size);
        this.refreshPosition();
        this.calculateDimensions();
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setSize(nbt.getFloat("Size"));
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void tickMovement() {
        if (!this.isTouchingWater() && this.isOnGround() && this.verticalCollision) {
            this.setVelocity(this.getVelocity().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4000000059604645, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
            this.setOnGround(false);
            this.velocityDirty = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getSoundPitch());
        }
        super.tickMovement();
    }

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(this.getMovementSpeed(), movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Size", this.getSize());
    }

    protected EntityNavigation createNavigation(World world) {
        return new SwimNavigation(this, world);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setPitch(0.0F);
        float scaledSize;
        float chance = random.nextFloat();

        if (chance < 0.31) scaledSize = 0.75f;
        else if (chance < 0.62) scaledSize = 0.9f;
        else if (chance < 0.95) scaledSize = 1f;
        else scaledSize = 1.25f;

        this.setSize(scaledSize);
        this.refreshPosition();
        this.calculateDimensions();

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SIZE, 0f);
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.5f, 0.7F);
    }

    private SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    private void setupAnimationStates() {
        if (this.flopAnimationTimeout <= 0) {
            this.flopAnimationTimeout = 10;
            this.flopAnimationState.startIfNotRunning(this.age);
        } else {
            --this.flopAnimationTimeout;
        }
    }

}
