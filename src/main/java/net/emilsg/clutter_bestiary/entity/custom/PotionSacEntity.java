package net.emilsg.clutter_bestiary.entity.custom;

import net.emilsg.clutter_bestiary.entity.variants.PotionWaspVariant;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionSacEntity extends LivingEntity {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(PotionSacEntity.class, TrackedDataHandlerRegistry.STRING);

    public PotionSacEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1D);
    }

    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return List.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
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
    public boolean isCollidable() {
        return false;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isOnGround() || this.horizontalCollision || this.isInsideWall() && this.isAlive()) {
            World world = this.getWorld();
            AreaEffectCloudEntity potionCloud = EntityType.AREA_EFFECT_CLOUD.create(world);
            if (this.getWorld() instanceof ServerWorld serverWorld && potionCloud != null) {
                potionCloud.setPotion(this.getVariant().getPotionEffect());
                potionCloud.setDuration(300);
                potionCloud.setRadius(1.5f);
                potionCloud.setPosition(this.getPos());
                serverWorld.spawnEntity(potionCloud);
                this.kill();
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SLIME_DEATH_SMALL;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, PotionWaspVariant.REGENERATION.getId());
    }
}
