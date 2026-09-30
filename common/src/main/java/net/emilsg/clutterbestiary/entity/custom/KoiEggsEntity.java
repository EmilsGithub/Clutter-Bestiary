package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.koi.*;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KoiEggsEntity extends Mob {
    private static final EntityDataAccessor<String> BASE_COLOR = SynchedEntityData.defineId(KoiEggsEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PRIMARY_PATTERN_COLOR = SynchedEntityData.defineId(KoiEggsEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PRIMARY_PATTERN_TYPE = SynchedEntityData.defineId(KoiEggsEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SECONDARY_PATTERN_COLOR = SynchedEntityData.defineId(KoiEggsEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SECONDARY_PATTERN_TYPE = SynchedEntityData.defineId(KoiEggsEntity.class, EntityDataSerializers.STRING);

    private static final int MIN_HATCH_TIME = 6000; // 5 minutes
    private static final int MAX_HATCH_TIME = 12000; // 10 minutes

    private int timeToHatch;

    public KoiEggsEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
        this.timeToHatch = MIN_HATCH_TIME + this.random.nextInt(MAX_HATCH_TIME - MIN_HATCH_TIME + 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BASE_COLOR, KoiBaseColorVariant.ORANGE.getID());
        builder.define(PRIMARY_PATTERN_COLOR, KoiPrimaryPatternColorVariant.WHITE.getID());
        builder.define(PRIMARY_PATTERN_TYPE, KoiPrimaryPatternTypeVariant.SPOTTED.getID());
        builder.define(SECONDARY_PATTERN_COLOR, KoiSecondaryPatternColorVariant.BLACK.getID());
        builder.define(SECONDARY_PATTERN_TYPE, KoiSecondaryPatternTypeVariant.SMALL_SPOTS.getID());
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.timeToHatch = nbt.getIntOr("HatchTime", this.timeToHatch);
        this.setBaseColorVariant(KoiBaseColorVariant.fromId(nbt.getStringOr("BaseColor", "")));
        this.setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant.fromId(nbt.getStringOr("PrimaryPatternColor", "")));
        this.setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant.fromId(nbt.getStringOr("PrimaryPatternType", "")));
        this.setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant.fromId(nbt.getStringOr("SecondaryPatternColor", "")));
        this.setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant.fromId(nbt.getStringOr("SecondaryPatternType", "")));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("HatchTime", this.timeToHatch);
        nbt.putString("BaseColor", this.getBaseColorVariant().getID());
        nbt.putString("PrimaryPatternColor", this.getPrimaryPatternColorVariant().getID());
        nbt.putString("PrimaryPatternType", this.getPrimaryPatternTypeVariant().getID());
        nbt.putString("SecondaryPatternColor", this.getSecondaryPatternColorVariant().getID());
        nbt.putString("SecondaryPatternType", this.getSecondaryPatternTypeVariant().getID());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 1D).add(Attributes.FOLLOW_RANGE, 1D);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    public KoiBaseColorVariant getBaseColorVariant() {
        return KoiBaseColorVariant.fromId(this.entityData.get(BASE_COLOR));
    }

    public void setBaseColorVariant(KoiBaseColorVariant baseColorVariant) {
        this.entityData.set(BASE_COLOR, baseColorVariant.getID());
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public KoiPrimaryPatternColorVariant getPrimaryPatternColorVariant() {
        return KoiPrimaryPatternColorVariant.fromId(this.entityData.get(PRIMARY_PATTERN_COLOR));
    }

    public void setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant primaryPatternColorVariant) {
        this.entityData.set(PRIMARY_PATTERN_COLOR, primaryPatternColorVariant.getID());
    }

    public KoiPrimaryPatternTypeVariant getPrimaryPatternTypeVariant() {
        return KoiPrimaryPatternTypeVariant.fromId(this.entityData.get(PRIMARY_PATTERN_TYPE));
    }

    public void setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant primaryPatternTypeVariant) {
        this.entityData.set(PRIMARY_PATTERN_TYPE, primaryPatternTypeVariant.getID());
    }

    public KoiSecondaryPatternColorVariant getSecondaryPatternColorVariant() {
        return KoiSecondaryPatternColorVariant.fromId(this.entityData.get(SECONDARY_PATTERN_COLOR));
    }

    public void setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant secondaryPatternColorVariant) {
        this.entityData.set(SECONDARY_PATTERN_COLOR, secondaryPatternColorVariant.getID());
    }

    public KoiSecondaryPatternTypeVariant getSecondaryPatternTypeVariant() {
        return KoiSecondaryPatternTypeVariant.fromId(this.entityData.get(SECONDARY_PATTERN_TYPE));
    }

    public void setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant secondaryPatternTypeVariant) {
        this.entityData.set(SECONDARY_PATTERN_TYPE, secondaryPatternTypeVariant.getID());
    }

    @Override
    public void tick() {
        this.setNoGravity(this.isUnderWater());
        super.tick();
        if (this.level() instanceof ServerLevel serverWorld) this.tickHatching(serverWorld);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.floatAboveGroundInWater();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH_SMALL;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    private void floatAboveGroundInWater() {
        if (!this.isInWater()) return;

        BlockPos currentPos = this.blockPosition();
        Level world = this.level();

        for (int y = 0; y < 10; y++) {
            BlockPos checkPos = currentPos.below(y);
            if (!world.getBlockState(checkPos).isAir() && world.getBlockState(checkPos).isRedstoneConductor(world, checkPos)) {
                double targetY = checkPos.getY() + 2.0;
                double verticalVelocity = this.getY() < targetY ? 0.01 : 0.0;

                double pushX = 0.0;
                double pushZ = 0.0;

                if (world.getBlockState(currentPos.north()).isRedstoneConductor(world, currentPos.north())) pushZ += 0.001;
                if (world.getBlockState(currentPos.south()).isRedstoneConductor(world, currentPos.south())) pushZ -= 0.001;
                if (world.getBlockState(currentPos.east()).isRedstoneConductor(world, currentPos.east())) pushX -= 0.001;
                if (world.getBlockState(currentPos.west()).isRedstoneConductor(world, currentPos.west())) pushX += 0.001;

                this.setDeltaMovement(this.getDeltaMovement().x + pushX, verticalVelocity, this.getDeltaMovement().z + pushZ);
                break;
            }
        }
    }

    private void hatch(ServerLevel world) {
        int amount = random.nextInt(13) == 0 ? random.nextBoolean() ? 3 : 2 : 1;

        double x = this.getX() + (amount > 1 ? ((random.nextBoolean() ? 1 : -1) * random.nextFloat() / 5) : 0);
        double y = this.getY() + (amount > 1 ? ((random.nextBoolean() ? 1 : -1) * random.nextFloat() / 5) : 0);
        double z = this.getZ() + (amount > 1 ? ((random.nextBoolean() ? 1 : -1) * random.nextFloat() / 5) : 0);

        world.sendParticles(ParticleTypes.BUBBLE, x, y, z, amount, 0.1, 0.1, 0.1, 5.0E-4);

        for (int i = 0; i < amount; i++) {
            KoiEntity koiEntity = ModEntityTypes.KOI.get().create(world, EntitySpawnReason.BREEDING);
            if (koiEntity == null) return;

            koiEntity.setBaby(true);
            koiEntity.setBaseColorVariant(this.getBaseColorVariant());
            koiEntity.setPrimaryPatternColorVariant(this.getPrimaryPatternColorVariant());
            koiEntity.setPrimaryPatternTypeVariant(this.getPrimaryPatternTypeVariant());
            koiEntity.setSecondaryPatternColorVariant(this.getSecondaryPatternColorVariant());
            koiEntity.setSecondaryPatternTypeVariant(this.getSecondaryPatternTypeVariant());
            koiEntity.snapTo(x, y, z, this.getYRot(), this.getXRot());
            world.addFreshEntity(koiEntity);
        }
        this.discard();
    }

    private void tickHatching(ServerLevel world) {
        if (this.timeToHatch <= 0) {
            this.hatch(world);
            return;
        }
        this.timeToHatch--;
        if (this.timeToHatch == 800 || this.timeToHatch == 40) {
            world.broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
            world.playSound(null, this.blockPosition(), SoundEvents.SNIFFER_EGG_CRACK, SoundSource.NEUTRAL, 0.5f, 1.5f);
        }
    }

}
