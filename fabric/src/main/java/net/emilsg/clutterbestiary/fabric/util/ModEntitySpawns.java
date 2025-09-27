package net.emilsg.clutterbestiary.fabric.util;

import net.emilsg.clutterbestiary.config.Configs;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.util.ModBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;

public class ModEntitySpawns {

    public static void registerSpawns() {
        if (ModConfigManager.get(Configs.spawnEchofins, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_ECHOFINS), SpawnGroup.AMBIENT,
                    ModEntityTypes.ECHOFIN.get(), 30, 1, 3);
        }

        if (ModConfigManager.get(Configs.spawnCrimsonNewts, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_CRIMSON_NEWTS), SpawnGroup.CREATURE,
                    ModEntityTypes.CRIMSON_NEWT.get(), 60, 2, 3);
        }

        if (ModConfigManager.get(Configs.spawnWarpedNewts, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_WARPED_NEWTS), SpawnGroup.CREATURE,
                    ModEntityTypes.WARPED_NEWT.get(), 60, 2, 3);
        }

        if (ModConfigManager.get(Configs.spawnEmberTortoises, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_EMBER_TORTOISES), SpawnGroup.CREATURE,
                    ModEntityTypes.EMBER_TORTOISE.get(), 60, 1, 2);
        }

        if (ModConfigManager.get(Configs.spawnButterflies, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_BUTTERFLIES), SpawnGroup.CREATURE,
                    ModEntityTypes.BUTTERFLY.get(), 20, 3, 6);
        }

        if (ModConfigManager.get(Configs.spawnChameleons, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_CHAMELEONS), SpawnGroup.CREATURE,
                    ModEntityTypes.CHAMELEON.get(), 15, 1, 2);
        }

        if (ModConfigManager.get(Configs.spawnMossblooms, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_MOSSBLOOMS), SpawnGroup.AMBIENT,
                    ModEntityTypes.MOSSBLOOM.get(), 30, 1, 2);
        }

        if (ModConfigManager.get(Configs.spawnKiwis, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_KIWIS), SpawnGroup.CREATURE,
                    ModEntityTypes.KIWI_BIRD.get(), 30, 2, 3);
        }

        if (ModConfigManager.get(Configs.spawnEmperorPenguins, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_EMPEROR_PENGUINS), SpawnGroup.CREATURE,
                    ModEntityTypes.EMPEROR_PENGUIN.get(), 5, 2, 4);
        }

        if (ModConfigManager.get(Configs.spawnBeavers, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_BEAVERS), SpawnGroup.CREATURE,
                    ModEntityTypes.BEAVER.get(), 5, 2, 3);
        }

        if (ModConfigManager.get(Configs.spawnCapybaras, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_CAPYBARAS), SpawnGroup.CREATURE,
                    ModEntityTypes.CAPYBARA.get(), 5, 3, 5);
        }
        if (ModConfigManager.get(Configs.spawnJellyfishes, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_JELLYFISHES), SpawnGroup.WATER_AMBIENT,
                    ModEntityTypes.JELLYFISH.get(), 6, 5, 9);
        }

        if (ModConfigManager.get(Configs.spawnSeahorses, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_SEAHORSES), SpawnGroup.WATER_AMBIENT,
                    ModEntityTypes.SEAHORSE.get(), 20, 4, 7);
        }

        if (ModConfigManager.get(Configs.spawnMantaRays, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_MANTA_RAYS), SpawnGroup.WATER_CREATURE,
                    ModEntityTypes.MANTA_RAY.get(), 20, 1, 3);
        }

        if (ModConfigManager.get(Configs.spawnDragonflies, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_DRAGONFLIES), SpawnGroup.CREATURE,
                    ModEntityTypes.DRAGONFLY.get(), 20, 2, 4);
        }

        if (ModConfigManager.get(Configs.spawnKoi, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_KOI), SpawnGroup.WATER_AMBIENT,
                    ModEntityTypes.KOI.get(), 20, 4, 7);
        }

        if (ModConfigManager.get(Configs.spawnBooplets, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_BOOPLETS), SpawnGroup.CREATURE,
                    ModEntityTypes.BOOPLET.get(), 20, 4, 7);
        }

        if (ModConfigManager.get(Configs.spawnPotionWasps, true)) {
            BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_POTION_WASPS), SpawnGroup.CREATURE,
                    ModEntityTypes.POTION_WASP.get(), 20, 1, 2);
        }
    }

}
