package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModAdvancements;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ArrowfishProjectileEntity extends PersistentProjectileEntity {

    public ArrowfishProjectileEntity(EntityType<? extends ArrowfishProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowfishProjectileEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.ARROWFISH_PROJECTILE.get(), owner, world, stack, shotFrom);
    }

    public ArrowfishProjectileEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.ARROWFISH_PROJECTILE.get(), x, y, z, world, stack, null);
    }

    public boolean isStuckInGround() {
        return this.inGround;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Vec3d velocity = this.getVelocity();
        if (!this.getWorld().isClient) {
            LivingEntity livingEntity = entityHitResult.getEntity() instanceof LivingEntity target ? target : null;
            if (livingEntity != null) {
                livingEntity.takeKnockback(0.8, -velocity.x, -velocity.z);
                if (this.getOwner() instanceof ServerPlayerEntity player && livingEntity != player) {
                    ModAdvancements.grant(player, ModAdvancements.FISH_SLAP);
                }
            } else {
                entityHitResult.getEntity().addVelocity(velocity.normalize().multiply(0.8));
            }
        }

        this.setVelocity(velocity.multiply(-0.1));
        this.setYaw(this.getYaw() + 180.0f);
        this.prevYaw += 180.0f;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.ARROWFISH.get());
    }
}
