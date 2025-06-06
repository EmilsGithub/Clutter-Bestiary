package net.emilsg.clutter_bestiary.entity.custom;

import net.emilsg.clutter_bestiary.entity.custom.parent.ParentFishEntity;
import net.emilsg.clutter_bestiary.entity.variants.koi.*;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class KoiEntity extends ParentFishEntity {

    private static final TrackedData<String> BASE_COLOR = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PRIMARY_PATTERN_COLOR = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PRIMARY_PATTERN_TYPE = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SECONDARY_PATTERN_COLOR = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SECONDARY_PATTERN_TYPE = DataTracker.registerData(KoiEntity.class, TrackedDataHandlerRegistry.STRING);

    public final AnimationState swimmingAnimationState = new AnimationState();
    private int swimmingAnimationTimeout = 0;

    public KoiEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BASE_COLOR, KoiBaseColorVariant.ORANGE.getId());
        this.dataTracker.startTracking(PRIMARY_PATTERN_COLOR, KoiPrimaryPatternColorVariant.WHITE.getId());
        this.dataTracker.startTracking(PRIMARY_PATTERN_TYPE, KoiPrimaryPatternTypeVariant.SPOTTED.getId());
        this.dataTracker.startTracking(SECONDARY_PATTERN_COLOR, KoiSecondaryPatternColorVariant.BLACK.getId());
        this.dataTracker.startTracking(SECONDARY_PATTERN_TYPE, KoiSecondaryPatternTypeVariant.SMALL.getId());
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.swimmingAnimationTimeout <= 0) {
            this.swimmingAnimationTimeout = 20;
            this.swimmingAnimationState.start(this.age);
        } else {
            --this.swimmingAnimationTimeout;
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("BaseColor", this.getBaseColorVariant().getId());
        nbt.putString("PrimaryPatternColor", this.getPrimaryPatternColorVariant().getId());
        nbt.putString("PrimaryPatternType", this.getPrimaryPatternTypeVariant().getId());
        nbt.putString("SecondaryPatternColor", this.getSecondaryPatternColorVariant().getId());
        nbt.putString("SecondaryPatternType", this.getSecondaryPatternTypeVariant().getId());
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

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        KoiBaseColorVariant baseColorVariant = KoiBaseColorVariant.getRandom();
        KoiPrimaryPatternColorVariant primaryPatternColorVariant = KoiPrimaryPatternColorVariant.getRandom();
        KoiPrimaryPatternTypeVariant primaryPatternTypeVariant = KoiPrimaryPatternTypeVariant.getRandom();
        KoiSecondaryPatternColorVariant secondaryPatternColorVariant = KoiSecondaryPatternColorVariant.getRandom();
        KoiSecondaryPatternTypeVariant secondaryPatternTypeVariant = KoiSecondaryPatternTypeVariant.getRandom();

        if (baseColorVariant.getId().equals(primaryPatternColorVariant.getId())) {
            int nextOrdinal = (primaryPatternColorVariant.ordinal() + 1) % KoiPrimaryPatternColorVariant.values().length;
            primaryPatternColorVariant = KoiPrimaryPatternColorVariant.values()[nextOrdinal];
        }

        if (primaryPatternColorVariant.getId().equals(secondaryPatternColorVariant.getId())) {
            int nextOrdinal = (secondaryPatternColorVariant.ordinal() + 1) % KoiSecondaryPatternColorVariant.values().length;
            secondaryPatternColorVariant = KoiSecondaryPatternColorVariant.values()[nextOrdinal];
        }

        this.setBaseColorVariant(baseColorVariant);
        this.setPrimaryPatternColorVariant(primaryPatternColorVariant);
        this.setPrimaryPatternTypeVariant(primaryPatternTypeVariant);
        this.setSecondaryPatternColorVariant(secondaryPatternColorVariant);
        this.setSecondaryPatternTypeVariant(secondaryPatternTypeVariant);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public KoiBaseColorVariant getBaseColorVariant() {
        return KoiBaseColorVariant.fromId(this.dataTracker.get(BASE_COLOR));
    }

    public void setBaseColorVariant(KoiBaseColorVariant baseColorVariant) {
        this.dataTracker.set(BASE_COLOR, baseColorVariant.getId());
    }

    public KoiPrimaryPatternColorVariant getPrimaryPatternColorVariant() {
        return KoiPrimaryPatternColorVariant.fromId(this.dataTracker.get(PRIMARY_PATTERN_COLOR));
    }

    public void setPrimaryPatternColorVariant(KoiPrimaryPatternColorVariant primaryPatternColorVariant) {
        this.dataTracker.set(PRIMARY_PATTERN_COLOR, primaryPatternColorVariant.getId());
    }

    public KoiPrimaryPatternTypeVariant getPrimaryPatternTypeVariant() {
        return KoiPrimaryPatternTypeVariant.fromId(this.dataTracker.get(PRIMARY_PATTERN_TYPE));
    }

    public void setPrimaryPatternTypeVariant(KoiPrimaryPatternTypeVariant primaryPatternTypeVariant) {
        this.dataTracker.set(PRIMARY_PATTERN_TYPE, primaryPatternTypeVariant.getId());
    }

    public KoiSecondaryPatternColorVariant getSecondaryPatternColorVariant() {
        return KoiSecondaryPatternColorVariant.fromId(this.dataTracker.get(SECONDARY_PATTERN_COLOR));
    }

    public void setSecondaryPatternColorVariant(KoiSecondaryPatternColorVariant secondaryPatternColorVariant) {
        this.dataTracker.set(SECONDARY_PATTERN_COLOR, secondaryPatternColorVariant.getId());
    }

    public KoiSecondaryPatternTypeVariant getSecondaryPatternTypeVariant() {
        return KoiSecondaryPatternTypeVariant.fromId(this.dataTracker.get(SECONDARY_PATTERN_TYPE));
    }

    public void setSecondaryPatternTypeVariant(KoiSecondaryPatternTypeVariant secondaryPatternTypeVariant) {
        this.dataTracker.set(SECONDARY_PATTERN_TYPE, secondaryPatternTypeVariant.getId());
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentFishEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6D);
    }

    @Override
    public ItemStack getBucketItem() {
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
    }
}
