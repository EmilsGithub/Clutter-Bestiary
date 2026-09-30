package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ArrowfishProjectileEntity extends AbstractArrow {

    public ArrowfishProjectileEntity(EntityType<? extends ArrowfishProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ArrowfishProjectileEntity(Level world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.ARROWFISH_PROJECTILE.get(), owner, world, stack, shotFrom);
    }

    public ArrowfishProjectileEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.ARROWFISH_PROJECTILE.get(), x, y, z, world, stack, null);
    }

    public boolean isStuckInGround() {
        return this.isInGround();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Vec3 velocity = this.getDeltaMovement();
        if (!this.level().isClientSide()) {
            LivingEntity livingEntity = entityHitResult.getEntity() instanceof LivingEntity target ? target : null;
            if (livingEntity != null) {
                livingEntity.knockback(0.8, -velocity.x, -velocity.z, this.damageSources().arrow(this, this.getOwner()), 0.0F);
                if (this.getOwner() instanceof ServerPlayer player && livingEntity != player) {
                    ModAdvancements.grant(player, ModAdvancements.FISH_SLAP);
                }
            } else {
                entityHitResult.getEntity().push(velocity.normalize().scale(0.8));
            }
        }

        this.setDeltaMovement(velocity.scale(-0.1));
        this.setYRot(this.getYRot() + 180.0f);
        this.yRotO += 180.0f;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.ARROWFISH.get());
    }
}
