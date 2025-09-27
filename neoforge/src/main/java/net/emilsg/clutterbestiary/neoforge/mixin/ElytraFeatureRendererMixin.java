package net.emilsg.clutterbestiary.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.item.custom.ButterflyElytraItem;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ElytraFeatureRenderer.class, priority = 1500)
public class ElytraFeatureRendererMixin {

    @ModifyReturnValue(
            method = "shouldRender(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("RETURN")
    )
    private boolean cb$shouldRender(boolean original, ItemStack stack, LivingEntity entity) {
        return original || clutterbestiary$getEquippedElytra(entity).getItem() instanceof BestiaryElytraItem;
    }

    @ModifyReturnValue(
            method = "getElytraTexture(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("RETURN")
    )
    private Identifier cb$elytraTex(Identifier original, ItemStack stack, LivingEntity entity) {
        var item = stack.getItem();
        if (item instanceof ButterflyElytraItem b) {
            return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/elytra/" + b.getType() + ".png");
        }
        return original;
    }

    @Unique
    private ItemStack clutterbestiary$getEquippedElytra(LivingEntity livingEntity) {
        return livingEntity.getEquippedStack(EquipmentSlot.CHEST);
    }

}
