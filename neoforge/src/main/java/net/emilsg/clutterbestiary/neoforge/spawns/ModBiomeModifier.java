package net.emilsg.clutterbestiary.neoforge.spawns;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.core.RegistryAccess;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.config.SpawnConfig;
import net.emilsg.clutterbestiary.util.ModEntitySpawns;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
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
    public void modify(RegistryAccess registries, Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) return;

        MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();

        for (ModEntitySpawns.SpawnDefinition definition : ModEntitySpawns.DEFINITIONS) {
            addSpawn(spawns, biome, definition);
        }
    }

    private void addSpawn(MobSpawnSettingsBuilder spawns, Holder<Biome> biome, ModEntitySpawns.SpawnDefinition definition) {
        SpawnConfig spawnConfig = ModConfigManager.getSpawnConfig(definition.configName());
        if (spawnConfig == null || !spawnConfig.isEnabled()) return;
        if (!definition.matches(biome)) return;

        int minGroupSize = spawnConfig.getMinGroupSize();
        int maxGroupSize = spawnConfig.getMaxGroupSize();
        IntProvider groupSize = minGroupSize == maxGroupSize ? ConstantInt.of(minGroupSize) : UniformInt.of(minGroupSize, maxGroupSize);
        spawns.addSpawn(definition.entityType().get(), spawnConfig.getSpawnWeight(), groupSize);
    }
}
