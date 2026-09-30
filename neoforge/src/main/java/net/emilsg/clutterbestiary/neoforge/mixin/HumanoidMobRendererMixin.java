package net.emilsg.clutterbestiary.neoforge.mixin;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.neoforge.compat.curios.CuriosElytraUse;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Shows a visible Curios back-slot glider through the vanilla wings layer, which also hides the cape as it does for a
 * chest-slot Elytra.
 */
@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin {

    @Inject(method = "extractHumanoidRenderState", at = @At("TAIL"))
    private static void clutterbestiary$showCuriosElytra(LivingEntity entity, HumanoidRenderState state, float partialTicks, ItemModelResolver itemModelResolver, CallbackInfo callbackInfo) {
        if (!ClutterBestiary.IS_CURIOS_LOADED || state.chestEquipment.has(DataComponents.GLIDER)) return;

        ItemStack curiosElytra = CuriosElytraUse.getVisibleEquippedElytra(entity);
        if (!curiosElytra.isEmpty()) {
            state.chestEquipment = curiosElytra;
        }
    }
}
