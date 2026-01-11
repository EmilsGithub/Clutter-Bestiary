package net.emilsg.clutterbestiary.neoforge.spawns;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.config.SpawnConfig;
import net.emilsg.clutterbestiary.util.ModEntitySpawns;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.MobSpawnSettingsBuilder;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public class ModBiomeModifier implements BiomeModifier {

    public static final ModBiomeModifier INSTANCE = new ModBiomeModifier();

    public static final MapCodec<ModBiomeModifier> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }

    @Override
    public void modify(RegistryEntry<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) return;

        MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();

        for (ModEntitySpawns.SpawnDefinition definition : ModEntitySpawns.DEFINITIONS) {
            addSpawn(spawns, biome, definition);
        }
    }

    private void addSpawn(MobSpawnSettingsBuilder spawns, RegistryEntry<Biome> biome, ModEntitySpawns.SpawnDefinition definition) {
        SpawnConfig spawnConfig = ModConfigManager.getSpawnConfig(definition.configName());
        if (spawnConfig == null || !spawnConfig.isEnabled()) return;
        if (!definition.matches(biome)) return;

        spawns.spawn(definition.spawnGroup(), new SpawnSettings.SpawnEntry(
                definition.entityType().get(),
                spawnConfig.getSpawnWeight(),
                spawnConfig.getMinGroupSize(),
                spawnConfig.getMaxGroupSize()
        ));
    }
}
