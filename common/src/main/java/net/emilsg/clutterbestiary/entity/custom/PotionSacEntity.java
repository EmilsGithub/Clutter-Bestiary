package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.variants.PotionWaspVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionSacEntity extends Mob {
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(PotionSacEntity.class, EntityDataSerializers.STRING);

    public PotionSacEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, PotionWaspVariant.REGENERATION.getId());
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getStringOr("Variant", ""));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", this.getTypeVariant());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1D);
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return false;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
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
    public void tick() {
        super.tick();
        if (!(this.level() instanceof ServerLevel serverWorld) || !this.isAlive()) return;
        if (!this.onGround() && !this.horizontalCollision && !this.isInWall()) return;

        AreaEffectCloud potionCloud = EntityTypes.AREA_EFFECT_CLOUD.create(serverWorld, EntitySpawnReason.TRIGGERED);
        if (potionCloud == null) return;

        potionCloud.setPotionContents(new PotionContents(this.getVariant().getPotionEffect()));
        potionCloud.setDuration(300);
        potionCloud.setRadius(1.5f);
        potionCloud.setPos(this.position());
        if (serverWorld.addFreshEntity(potionCloud)) this.discard();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH_SMALL;
    }
}
