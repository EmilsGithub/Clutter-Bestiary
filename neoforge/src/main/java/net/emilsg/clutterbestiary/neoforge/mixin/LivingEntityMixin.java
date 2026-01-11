package net.emilsg.clutterbestiary.neoforge.mixin;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.neoforge.compat.curios.CuriosElytraUse;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    protected LivingEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickFallFlying", at = @At("HEAD"), cancellable = true)
    private void clutterbestiary$tickCuriosElytra(CallbackInfo callbackInfo) {
        if (!ClutterBestiary.IS_CURIOS_LOADED) return;

        LivingEntity livingEntity = (LivingEntity) (Object) this;
        ItemStack chestStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (chestStack.canElytraFly(livingEntity)) return;

        ItemStack curiosElytra = CuriosElytraUse.getFlightElytra(livingEntity);
        if (curiosElytra.isEmpty()) return;

        boolean isFallFlying = this.getFlag(7);
        if (isFallFlying && !livingEntity.isOnGround() && !livingEntity.hasVehicle() && !livingEntity.hasStatusEffect(StatusEffects.LEVITATION)) {
            isFallFlying = CuriosElytraUse.tickFlight(livingEntity, curiosElytra);
        } else {
            isFallFlying = false;
        }

        if (!livingEntity.getWorld().isClient) {
            this.setFlag(7, isFallFlying);
        }

        callbackInfo.cancel();
    }
}
