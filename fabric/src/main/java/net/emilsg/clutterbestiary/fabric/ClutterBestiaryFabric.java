package net.emilsg.clutterbestiary.fabric;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.config.Configs;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.fabric.compat.trinkets.TrinketsElytraUse;
import net.emilsg.clutterbestiary.fabric.util.ModEntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;

public final class ClutterBestiaryFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ClutterBestiary.init();

        EntityElytraEvents.CUSTOM.register((LivingEntity livingEntity, boolean tick) -> {
            ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);

            if (itemStack.getItem() instanceof ElytraItem && ElytraItem.isUsable(itemStack)) {
                 doElytraTick(livingEntity, itemStack);
                return true;
            }

            return false;
        });

        if (ClutterBestiary.IS_TRINKETS_LOADED && ModConfigManager.get(Configs.doTrinketsElytraFlight, true)) TrinketsElytraUse.doFlight();
        ModEntitySpawns.registerSpawns();
    }

    private static void doElytraTick(LivingEntity entity, ItemStack itemStack) {
        int nextRoll = entity.getFallFlyingTicks() + 1;

        if (!entity.getWorld().isClient && nextRoll % 10 == 0) {
            if (nextRoll / 10 % 2 == 0) {
                itemStack.damage(1, entity, EquipmentSlot.CHEST);
            }

            entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
        }
    }

}
