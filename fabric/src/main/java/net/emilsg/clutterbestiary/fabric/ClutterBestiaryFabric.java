package net.emilsg.clutterbestiary.fabric;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.config.Configs;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.fabric.util.FabricEntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public final class ClutterBestiaryFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ClutterBestiary.init();
        FabricEntitySpawns.register();

        // Butterfly Elytras are gliders: chest-slot flight and durability are handled by vanilla, and Trinkets
        // Updated flies any glider worn in its cape slot. The config can still switch the Trinkets slot off.
        if (ClutterBestiary.IS_TRINKETS_LOADED && !ModConfigManager.get(Configs.doTrinketsElytraFlight, true)) {
            EntityElytraEvents.ALLOW.register((LivingEntity livingEntity) -> LivingEntity.canGlideUsing(livingEntity.getItemBySlot(EquipmentSlot.CHEST), EquipmentSlot.CHEST));
        }
    }

}
