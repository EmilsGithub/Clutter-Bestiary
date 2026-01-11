package net.emilsg.clutterbestiary.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.neoforge.compat.curios.CuriosElytraUse;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeFeatureRenderer.class)
public abstract class CapeFeatureRendererMixin {

    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
    )
    private boolean clutterbestiary$hideCapeForElytra(boolean original, MatrixStack matrices,
                                                      VertexConsumerProvider vertexConsumers, int light,
                                                      AbstractClientPlayerEntity player) {
        if (original || player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BestiaryElytraItem) {
            return true;
        }

        return ClutterBestiary.IS_CURIOS_LOADED && !CuriosElytraUse.getVisibleEquippedElytra(player).isEmpty();
    }
}
