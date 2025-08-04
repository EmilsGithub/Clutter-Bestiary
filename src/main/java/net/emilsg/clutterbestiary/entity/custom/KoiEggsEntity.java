package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.koi.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KoiEggsEntity extends MobEntity {
    private static final TrackedData<String> BASE_COLOR = DataTracker.registerData(KoiEggsEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PRIMARY_PATTERN_COLOR = DataTracker.registerData(KoiEggsEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PRIMARY_PATTERN_TYPE = DataTracker.registerData(KoiEggsEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SECONDARY_PATTERN_COLOR = DataTracker.registerData(KoiEggsEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SECONDARY_PATTERN_TYPE = DataTracker.registerData(KoiEggsEntity.class, TrackedDataHandlerRegistry.STRING);

    private int timeToHatch;

    public KoiEggsEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        this.timeToHatch = 120;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 1D);
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return List.of();
    }

    public KoiBaseColorVariant getBaseColorVariant() {
        return KoiBaseColorVariant.fromId(this.dataTracker.get(BASE_COLOR));
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SALMON_HURT;
    }

    public void setBaseColorVariant(KoiBaseColorVariant baseColorVariant) {
        this.dataTracker.set(BASE_COLOR, baseColorVariant.getID());
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.AQUATIC;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    public KoiPrimaryPatternColorVariant getPrimaryPatternColorVariant() {
        return KoiPrimaryPatternColorVariant.fromId(this.dataTracker.get(PRIMARY_PATTERN_COLOR));
    }

    public void setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant primaryPatternColorVariant) {
        this.dataTracker.set(PRIMARY_PATTERN_COLOR, primaryPatternColorVariant.getID());
    }

    public KoiPrimaryPatternTypeVariant getPrimaryPatternTypeVariant() {
        return KoiPrimaryPatternTypeVariant.fromId(this.dataTracker.get(PRIMARY_PATTERN_TYPE));
    }

    public void setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant primaryPatternTypeVariant) {
        this.dataTracker.set(PRIMARY_PATTERN_TYPE, primaryPatternTypeVariant.getID());
    }

    public KoiSecondaryPatternColorVariant getSecondaryPatternColorVariant() {
        return KoiSecondaryPatternColorVariant.fromId(this.dataTracker.get(SECONDARY_PATTERN_COLOR));
    }

    public void setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant secondaryPatternColorVariant) {
        this.dataTracker.set(SECONDARY_PATTERN_COLOR, secondaryPatternColorVariant.getID());
    }

    public KoiSecondaryPatternTypeVariant getSecondaryPatternTypeVariant() {
        return KoiSecondaryPatternTypeVariant.fromId(this.dataTracker.get(SECONDARY_PATTERN_TYPE));
    }

    public void setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant secondaryPatternTypeVariant) {
        this.dataTracker.set(SECONDARY_PATTERN_TYPE, secondaryPatternTypeVariant.getID());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBaseColorVariant(KoiBaseColorVariant.fromId(nbt.getString("BaseColor")));
        this.setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant.fromId(nbt.getString("PrimaryPatternColor")));
        this.setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant.fromId(nbt.getString("PrimaryPatternType")));
        this.setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant.fromId(nbt.getString("SecondaryPatternColor")));
        this.setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant.fromId(nbt.getString("SecondaryPatternType")));
    }

    @Override
    public void tick() {
        this.setNoGravity(this.isSubmergedInWater());


        this.tickHatching(this.getWorld());

        super.tick();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("BaseColor", this.getBaseColorVariant().getID());
        nbt.putString("PrimaryPatternColor", this.getPrimaryPatternColorVariant().getID());
        nbt.putString("PrimaryPatternType", this.getPrimaryPatternTypeVariant().getID());
        nbt.putString("SecondaryPatternColor", this.getSecondaryPatternColorVariant().getID());
        nbt.putString("SecondaryPatternType", this.getSecondaryPatternTypeVariant().getID());
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SLIME_DEATH_SMALL;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BASE_COLOR, KoiBaseColorVariant.ORANGE.getID());
        this.dataTracker.startTracking(PRIMARY_PATTERN_COLOR, KoiPrimaryPatternColorVariant.WHITE.getID());
        this.dataTracker.startTracking(PRIMARY_PATTERN_TYPE, KoiPrimaryPatternTypeVariant.SPOTTED.getID());
        this.dataTracker.startTracking(SECONDARY_PATTERN_COLOR, KoiSecondaryPatternColorVariant.BLACK.getID());
        this.dataTracker.startTracking(SECONDARY_PATTERN_TYPE, KoiSecondaryPatternTypeVariant.SMALL_SPOTS.getID());
    }

    private void hatch(World world) {
        int amount = random.nextInt(13) == 0 ? random.nextBoolean() ? 3 : 2 : 1;

        double x = this.getX() + (amount > 1 ? ((random.nextBoolean() ? 1 : - 1) * random.nextFloat() / 5) : 0);
        double y = this.getY() + (amount > 1 ? ((random.nextBoolean() ? 1 : - 1) * random.nextFloat() / 5) : 0);
        double z = this.getZ() + (amount > 1 ? ((random.nextBoolean() ? 1 : - 1) * random.nextFloat() / 5) : 0);

        for (int i = 0; i < amount; i++) {
            if (world.isClient) world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0.0, 5.0E-4, 0.0);
        }

        for (int i = 0; i < amount; i++) {
            if (world instanceof ServerWorld serverWorld) {
                KoiEntity koiEntity = ModEntityTypes.KOI.create(serverWorld);
                if (koiEntity == null) return;

                koiEntity.setBaby(true);

                koiEntity.setBaseColorVariant(this.getBaseColorVariant());
                koiEntity.setPrimaryPatternColorVariant(this.getPrimaryPatternColorVariant());
                koiEntity.setPrimaryPatternTypeVariant(this.getPrimaryPatternTypeVariant());
                koiEntity.setSecondaryPatternColorVariant(this.getSecondaryPatternColorVariant());
                koiEntity.setSecondaryPatternTypeVariant(this.getSecondaryPatternTypeVariant());

                koiEntity.refreshPositionAndAngles(x, y ,z, this.getYaw(), this.getPitch());

                serverWorld.spawnEntity(koiEntity);
            }
        }
        this.kill();
        this.discard();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.floatAboveGroundInWater();
    }

    private void tickHatching(World world) {
        if (this.timeToHatch <= 0) this.hatch(world);
        this.timeToHatch--;

        if (world.isClient) {
            if (this.timeToHatch % 1200 == 0 && this.timeToHatch != 0) {
                world.sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_SNIFFER_EGG_CRACK, SoundCategory.NEUTRAL, 0.5f, 1.5f);
            }
        }

    }

    private void floatAboveGroundInWater() {
        if (!this.isTouchingWater()) return;

        BlockPos currentPos = this.getBlockPos();
        World world = this.getWorld();

        for (int y = 0; y < 10; y++) {
            BlockPos checkPos = currentPos.down(y);
            if (!world.getBlockState(checkPos).isAir() && world.getBlockState(checkPos).isSolidBlock(world, checkPos)) {
                double targetY = checkPos.getY() + 2.0;
                double verticalVelocity = this.getY() < targetY ? 0.01 : 0.0;

                double pushX = 0.0;
                double pushZ = 0.0;

                if (world.getBlockState(currentPos.north()).isSolidBlock(world, currentPos.north())) pushZ += 0.001;
                if (world.getBlockState(currentPos.south()).isSolidBlock(world, currentPos.south())) pushZ -= 0.001;
                if (world.getBlockState(currentPos.east()).isSolidBlock(world, currentPos.east())) pushX -= 0.001;
                if (world.getBlockState(currentPos.west()).isSolidBlock(world, currentPos.west())) pushX += 0.001;

                this.setVelocity(this.getVelocity().x + pushX, verticalVelocity, this.getVelocity().z + pushZ);
                break;
            }
        }
    }

}
