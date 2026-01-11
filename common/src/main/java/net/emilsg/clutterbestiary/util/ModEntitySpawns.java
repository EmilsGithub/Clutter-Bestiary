package net.emilsg.clutterbestiary.util;

import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

import java.util.List;

public final class ModEntitySpawns {
    public static final List<SpawnDefinition> DEFINITIONS = List.of(
            new SpawnDefinition("echofin", ModBiomeTags.SPAWNS_ECHOFINS, SpawnGroup.AMBIENT, ModEntityTypes.ECHOFIN),
            new SpawnDefinition("crimson_newt", ModBiomeTags.SPAWNS_CRIMSON_NEWTS, SpawnGroup.CREATURE, ModEntityTypes.CRIMSON_NEWT),
            new SpawnDefinition("warped_newt", ModBiomeTags.SPAWNS_WARPED_NEWTS, SpawnGroup.CREATURE, ModEntityTypes.WARPED_NEWT),
            new SpawnDefinition("ember_tortoise", ModBiomeTags.SPAWNS_EMBER_TORTOISES, SpawnGroup.CREATURE, ModEntityTypes.EMBER_TORTOISE),
            new SpawnDefinition("butterfly", ModBiomeTags.SPAWNS_BUTTERFLIES, SpawnGroup.CREATURE, ModEntityTypes.BUTTERFLY),
            new SpawnDefinition("chameleon", ModBiomeTags.SPAWNS_CHAMELEONS, SpawnGroup.CREATURE, ModEntityTypes.CHAMELEON),
            new SpawnDefinition("mossbloom", ModBiomeTags.SPAWNS_MOSSBLOOMS, SpawnGroup.AMBIENT, ModEntityTypes.MOSSBLOOM),
            new SpawnDefinition("kiwi", ModBiomeTags.SPAWNS_KIWIS, SpawnGroup.CREATURE, ModEntityTypes.KIWI_BIRD),
            new SpawnDefinition("emperor_penguin", ModBiomeTags.SPAWNS_EMPEROR_PENGUINS, SpawnGroup.CREATURE, ModEntityTypes.EMPEROR_PENGUIN),
            new SpawnDefinition("beaver", ModBiomeTags.SPAWNS_BEAVERS, SpawnGroup.CREATURE, ModEntityTypes.BEAVER),
            new SpawnDefinition("capybara", ModBiomeTags.SPAWNS_CAPYBARAS, SpawnGroup.CREATURE, ModEntityTypes.CAPYBARA),
            new SpawnDefinition("jellyfish", ModBiomeTags.SPAWNS_JELLYFISHES, SpawnGroup.WATER_AMBIENT, ModEntityTypes.JELLYFISH),
            new SpawnDefinition("seahorse", ModBiomeTags.SPAWNS_SEAHORSES, SpawnGroup.WATER_AMBIENT, ModEntityTypes.SEAHORSE),
            new SpawnDefinition("manta_ray", ModBiomeTags.SPAWNS_MANTA_RAYS, SpawnGroup.WATER_CREATURE, ModEntityTypes.MANTA_RAY),
            new SpawnDefinition("dragonfly", ModBiomeTags.SPAWNS_DRAGONFLIES, SpawnGroup.CREATURE, ModEntityTypes.DRAGONFLY),
            new SpawnDefinition("koi", ModBiomeTags.SPAWNS_KOI, SpawnGroup.WATER_AMBIENT, ModEntityTypes.KOI),
            new SpawnDefinition("booplet", ModBiomeTags.SPAWNS_BOOPLETS, SpawnGroup.CREATURE, ModEntityTypes.BOOPLET),
            new SpawnDefinition("potion_wasp", ModBiomeTags.SPAWNS_POTION_WASPS, SpawnGroup.CREATURE, ModEntityTypes.POTION_WASP),
            new SpawnDefinition("river_turtle", ModBiomeTags.SPAWNS_RIVER_TURTLES, SpawnGroup.CREATURE, ModEntityTypes.RIVER_TURTLE),
            new SpawnDefinition("coati", ModBiomeTags.SPAWNS_COATIS, SpawnGroup.CREATURE, ModEntityTypes.COATI),
            new SpawnDefinition("red_panda", ModBiomeTags.SPAWNS_RED_PANDAS, SpawnGroup.CREATURE, ModEntityTypes.RED_PANDA),
            new SpawnDefinition("stoat", ModBiomeTags.SPAWNS_STOATS, SpawnGroup.CREATURE, ModEntityTypes.STOAT),
            new SpawnDefinition("crocodile", ModBiomeTags.SPAWNS_CROCODILES, SpawnGroup.CREATURE, ModEntityTypes.CROCODILE),
            new SpawnDefinition("chorus_beetle", ModBiomeTags.SPAWNS_CHORUS_BEETLES, SpawnGroup.CREATURE, ModEntityTypes.CHORUS_BEETLE),
            new SpawnDefinition("woodpecker", ModBiomeTags.SPAWNS_WOODPECKERS, SpawnGroup.CREATURE, ModEntityTypes.WOODPECKER),
            new SpawnDefinition("arrowfish", ModBiomeTags.SPAWNS_ARROWFISH, SpawnGroup.WATER_AMBIENT, ModEntityTypes.ARROWFISH)
    );

    private ModEntitySpawns() {
    }

    public record SpawnDefinition(String configName, TagKey<Biome> biomeTag, SpawnGroup spawnGroup,
                                  RegistrySupplier<? extends EntityType<?>> entityType) {
        public boolean matches(RegistryEntry<Biome> biome) {
            return biome.isIn(this.biomeTag);
        }
    }
}
