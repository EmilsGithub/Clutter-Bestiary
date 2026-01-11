package net.emilsg.clutterbestiary.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.item.custom.ButterflyElytraItem;
import net.emilsg.clutterbestiary.neoforge.compat.curios.CuriosElytraUse;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ElytraFeatureRenderer.class, priority = 1500)
public abstract class ElytraFeatureRendererMixin {

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack clutterbestiary$useRenderedElytra(ItemStack chestStack, MatrixStack matrices,
                                                        VertexConsumerProvider vertexConsumers, int light,
                                                        LivingEntity livingEntity) {
        return this.clutterbestiary$selectRenderedElytra(livingEntity, chestStack);
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V")
    )
    private Identifier clutterbestiary$getButterflyElytraTexture(Identifier original, MatrixStack matrices,
                                                                 VertexConsumerProvider vertexConsumers, int light,
                                                                 LivingEntity livingEntity) {
        ItemStack chestStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack itemStack = this.clutterbestiary$selectRenderedElytra(livingEntity, chestStack);
        if (itemStack.getItem() instanceof ButterflyElytraItem butterflyElytraItem) {
            return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/elytra/" + butterflyElytraItem.getType() + ".png");
        }

        return original;
    }

    @ModifyReturnValue(
            method = "shouldRender(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("RETURN")
    )
    private boolean clutterbestiary$shouldRender(boolean original, ItemStack stack, LivingEntity livingEntity) {
        return original || stack.getItem() instanceof BestiaryElytraItem;
    }

    @Unique
    private ItemStack clutterbestiary$selectRenderedElytra(LivingEntity livingEntity, ItemStack chestStack) {
        if (chestStack.getItem() instanceof ElytraItem || !ClutterBestiary.IS_CURIOS_LOADED) return chestStack;

        ItemStack curiosElytra = CuriosElytraUse.getVisibleEquippedElytra(livingEntity);
        return curiosElytra.isEmpty() ? chestStack : curiosElytra;
    }

}
