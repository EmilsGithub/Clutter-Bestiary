package net.emilsg.clutterbestiary.neoforge.mixin;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.neoforge.compat.curios.CuriosElytraUse;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Chest-slot gliders are handled by vanilla; this adds gliders worn in the Curios back slot.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    protected LivingEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract boolean canGlide();

    @Inject(method = "canGlide", at = @At("RETURN"), cancellable = true)
    private void clutterbestiary$canGlideWithCuriosElytra(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (callbackInfo.getReturnValueZ() || !ClutterBestiary.IS_CURIOS_LOADED) return;

        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.onGround() && !livingEntity.isPassenger() && !livingEntity.hasEffect(MobEffects.LEVITATION) && CuriosElytraUse.canGlide(livingEntity)) {
            callbackInfo.setReturnValue(true);
        }
    }

    // Vanilla damages a random equipment-slot glider, which would fail when only a back-slot glider is worn.
    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void clutterbestiary$tickCuriosElytra(CallbackInfo callbackInfo) {
        if (!ClutterBestiary.IS_CURIOS_LOADED || this.level().isClientSide()) return;

        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (clutterbestiary$hasEquipmentGlider(livingEntity) || !this.canGlide()) return;

        this.checkFallDistanceAccumulation();
        int checkFallFlyTicks = livingEntity.getFallFlyingTicks() + 1;
        if (checkFallFlyTicks % 10 == 0) {
            if (checkFallFlyTicks / 10 % 2 == 0) {
                CuriosElytraUse.damageGlider(livingEntity);
            }

            this.gameEvent(GameEvent.ELYTRA_GLIDE);
        }

        callbackInfo.cancel();
    }

    @Unique
    private static boolean clutterbestiary$hasEquipmentGlider(LivingEntity livingEntity) {
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            if (LivingEntity.canGlideUsing(livingEntity.getItemBySlot(slot), slot)) return true;
        }
        return false;
    }
}
