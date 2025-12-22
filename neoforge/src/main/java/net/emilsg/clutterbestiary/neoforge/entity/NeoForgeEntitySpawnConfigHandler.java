package net.emilsg.clutterbestiary.neoforge.entity;

import net.emilsg.clutterbestiary.config.Configs;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class NeoForgeEntitySpawnConfigHandler {
    public static final Map<EntityType<?>, BooleanSupplier> SPAWN_CONFIG = new HashMap<>();

    public static void register() {
        registerSpawnToggle(ModEntityTypes.BUTTERFLY.get(), () -> ModConfigManager.get(Configs.spawnButterflies, true));
        registerSpawnToggle(ModEntityTypes.CHAMELEON.get(), () -> ModConfigManager.get(Configs.spawnChameleons, true));
        registerSpawnToggle(ModEntityTypes.ECHOFIN.get(), () -> ModConfigManager.get(Configs.spawnEchofins, true));
        registerSpawnToggle(ModEntityTypes.MOSSBLOOM.get(), () -> ModConfigManager.get(Configs.spawnMossblooms, true));
        registerSpawnToggle(ModEntityTypes.KIWI_BIRD.get(), () -> ModConfigManager.get(Configs.spawnKiwis, true));
        registerSpawnToggle(ModEntityTypes.EMPEROR_PENGUIN.get(), () -> ModConfigManager.get(Configs.spawnEmperorPenguins, true));
        registerSpawnToggle(ModEntityTypes.BEAVER.get(), () -> ModConfigManager.get(Configs.spawnBeavers, true));
        registerSpawnToggle(ModEntityTypes.CAPYBARA.get(), () -> ModConfigManager.get(Configs.spawnCapybaras, true));
        registerSpawnToggle(ModEntityTypes.CRIMSON_NEWT.get(), () -> ModConfigManager.get(Configs.spawnCrimsonNewts, true));
        registerSpawnToggle(ModEntityTypes.WARPED_NEWT.get(), () -> ModConfigManager.get(Configs.spawnWarpedNewts, true));
        registerSpawnToggle(ModEntityTypes.EMBER_TORTOISE.get(), () -> ModConfigManager.get(Configs.spawnEmberTortoises, true));
        registerSpawnToggle(ModEntityTypes.JELLYFISH.get(), () -> ModConfigManager.get(Configs.spawnJellyfishes, true));
        registerSpawnToggle(ModEntityTypes.MANTA_RAY.get(), () -> ModConfigManager.get(Configs.spawnMantaRays, true));
        registerSpawnToggle(ModEntityTypes.SEAHORSE.get(), () -> ModConfigManager.get(Configs.spawnSeahorses, true));
        registerSpawnToggle(ModEntityTypes.POTION_WASP.get(), () -> ModConfigManager.get(Configs.spawnPotionWasps, true));
        registerSpawnToggle(ModEntityTypes.DRAGONFLY.get(), () -> ModConfigManager.get(Configs.spawnDragonflies, true));
        registerSpawnToggle(ModEntityTypes.BOOPLET.get(), () -> ModConfigManager.get(Configs.spawnBooplets, true));
        registerSpawnToggle(ModEntityTypes.KOI.get(), () -> ModConfigManager.get(Configs.spawnKoi, true));
        registerSpawnToggle(ModEntityTypes.RIVER_TURTLE.get(), () -> ModConfigManager.get(Configs.spawnRiverTurtles, true));
        registerSpawnToggle(ModEntityTypes.COATI.get(), () -> ModConfigManager.get(Configs.spawnCoatis, true));
        registerSpawnToggle(ModEntityTypes.RED_PANDA.get(), () -> ModConfigManager.get(Configs.spawnRedPandas, true));
    }

    public static void registerSpawnToggle(EntityType<?> type, BooleanSupplier enabled) {
        SPAWN_CONFIG.put(type, enabled);
    }
}
