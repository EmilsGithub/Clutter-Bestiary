package net.emilsg.clutterbestiary.entity.client.player;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.compat.trinkets.TrinketsElytraUse;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;


public class RendererRegistration {

    public static void register() {
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((AbstractClientPlayerEntity player) -> !(player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BestiaryElytraItem));
        if (ClutterBestiary.IS_TRINKETS_LOADED) LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((AbstractClientPlayerEntity player) -> TrinketsElytraUse.getEquippedElytra(player).isEmpty());
    }

}
