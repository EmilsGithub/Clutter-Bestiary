package net.emilsg.clutterbestiary.fabric.util;

import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.config.SpawnConfig;
import net.emilsg.clutterbestiary.util.ModEntitySpawns;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

public final class FabricEntitySpawns {

    public static void register() {
        for (ModEntitySpawns.SpawnDefinition definition : ModEntitySpawns.DEFINITIONS) {
            SpawnConfig spawnConfig = ModConfigManager.getSpawnConfig(definition.configName());
            if (!spawnConfig.isEnabled()) continue;

            BiomeModifications.addSpawn(BiomeSelectors.tag(definition.biomeTag()), definition.spawnGroup(),
                    definition.entityType().get(), spawnConfig.getSpawnWeight(), spawnConfig.getMinGroupSize(), spawnConfig.getMaxGroupSize());
        }
    }
}
