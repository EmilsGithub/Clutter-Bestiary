package net.emilsg.clutterbestiary.fabric.datagen;

import net.minecraft.resources.ResourceKey;
import net.emilsg.clutterbestiary.util.ModBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import java.util.concurrent.CompletableFuture;

public class BiomeTagDataGenerator extends FabricTagsProvider<Biome> {

	public BiomeTagDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        String biomesOPlentyNamespace = "biomesoplenty";
        String regionsUnexploredNamespace = "regions_unexplored";
        String clutterBiomesNamespace = "clutterbiomes";
        String terralithNamespace = "terralith";

	    builder(ModBiomeTags.SPAWNS_BUTTERFLIES).add(
                        Biomes.CRIMSON_FOREST,
                        Biomes.WARPED_FOREST,
                        Biomes.SOUL_SAND_VALLEY,
                        Biomes.FLOWER_FOREST,
                        Biomes.SUNFLOWER_PLAINS,
                        Biomes.MEADOW,
                        Biomes.CHERRY_GROVE
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "cherry_blossom_grove")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "bamboo_grove")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "lavender_field")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "clover_plains")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "flower_fields")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "poppy_fields")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "rocky_meadow")))
        ;

        builder(ModBiomeTags.SPAWNS_EMBER_TORTOISES).add(
                        Biomes.BASALT_DELTAS
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "volcano")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "erupting_inferno")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "infernal_holt")))
        ;

        builder(ModBiomeTags.SPAWNS_ECHOFINS).add(
                Biomes.END_HIGHLANDS
        );

        builder(ModBiomeTags.SPAWNS_JELLYFISHES)
                .add(
                        Biomes.WARM_OCEAN,
                        Biomes.LUKEWARM_OCEAN
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "rocky_reef")))
        ;

        builder(ModBiomeTags.SPAWNS_MANTA_RAYS)
                .add(
                        Biomes.WARM_OCEAN,
                        Biomes.LUKEWARM_OCEAN
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "rocky_reef")))
        ;

        builder(ModBiomeTags.SPAWNS_SEAHORSES).add(
                Biomes.WARM_OCEAN
        );

	    builder(ModBiomeTags.SPAWNS_POTION_WASPS)
                .add(
                        Biomes.JUNGLE
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "tropical_jungle")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "rainforest")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "rainforest")))
        ;

        builder(ModBiomeTags.SPAWNS_CAPYBARAS)
                .add(
                        Biomes.SAVANNA_PLATEAU,
                        Biomes.WINDSWEPT_SAVANNA,
                        Biomes.SAVANNA
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "fractured_savanna")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "savanna_badlands")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "savanna_slopes")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "lush_desert")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "steppe")))
        ;

        builder(ModBiomeTags.SPAWNS_BEAVERS)
                .add(Biomes.RIVER)
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "warm_river")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "bayou")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "wetland")))
        ;

        builder(ModBiomeTags.SPAWNS_EMPEROR_PENGUINS).add(
                        Biomes.ICE_SPIKES,
                        Biomes.SNOWY_PLAINS,
                        Biomes.SNOWY_BEACH
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "ice_marsh")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "muskeg")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "frozen_tundra")))
        ;

        builder(ModBiomeTags.SPAWNS_KIWIS)
                .add(
                        Biomes.BAMBOO_JUNGLE,
                        Biomes.JUNGLE,
                        Biomes.SPARSE_JUNGLE
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "jungle_mountains")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "rocky_jungle")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "tropical_jungle")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "rubble_jungle")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "rainforest")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "rocky_rainforest")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "tropics")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "rainforest")))
        ;

        builder(ModBiomeTags.SPAWNS_CHAMELEONS)
                .add(
                        Biomes.BAMBOO_JUNGLE,
                        Biomes.JUNGLE,
                        Biomes.SPARSE_JUNGLE
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "jungle_mountains")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "rocky_jungle")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "tropical_jungle")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "rubble_jungle")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "rainforest")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "rocky_rainforest")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "tropics")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "rainforest")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "sparse_rainforest")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "tropics")))
        ;

        builder(ModBiomeTags.SPAWNS_COATIS)
                .add(
                        Biomes.JUNGLE,
                        Biomes.BAMBOO_JUNGLE,
                        Biomes.OLD_GROWTH_SPRUCE_TAIGA
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "bamboo_grove")))
        ;

        builder(ModBiomeTags.SPAWNS_RED_PANDAS)
                .add(
                        Biomes.GROVE,
                        Biomes.BAMBOO_JUNGLE
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "bamboo_grove")))
        ;

        builder(ModBiomeTags.SPAWNS_DRAGONFLIES)
                .add(
                        Biomes.SWAMP,
                        Biomes.MANGROVE_SWAMP
                )
        ;

        builder(ModBiomeTags.SPAWNS_MOSSBLOOMS).add(
                Biomes.LUSH_CAVES
        );

        builder(ModBiomeTags.SPAWNS_CRIMSON_NEWTS).add(
                Biomes.CRIMSON_FOREST
        );

        builder(ModBiomeTags.SPAWNS_WARPED_NEWTS).add(
                Biomes.WARPED_FOREST
        );

        builder(ModBiomeTags.SPAWNS_KOI)
                .add(
                        Biomes.MANGROVE_SWAMP
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "hot_springs")))
        ;

        builder(ModBiomeTags.SPAWNS_RIVER_TURTLES)
                .add(
                        Biomes.RIVER,
                        Biomes.MANGROVE_SWAMP
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "warm_river")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "bayou")))
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(biomesOPlentyNamespace, "wetland")))
        ;

        builder(ModBiomeTags.SPAWNS_BOOPLETS)
                .add(
                        Biomes.SNOWY_PLAINS,
                        Biomes.SNOWY_TAIGA
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(terralithNamespace, "muskeg")))

                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(regionsUnexploredNamespace, "frozen_tundra")))
        ;

        builder(ModBiomeTags.SPAWNS_STOATS).add(
                Biomes.TAIGA,
                Biomes.SNOWY_TAIGA,
                Biomes.OLD_GROWTH_PINE_TAIGA,
                Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                Biomes.GROVE
        );

        builder(ModBiomeTags.SPAWNS_CROCODILES).add(
                Biomes.SWAMP,
                Biomes.MANGROVE_SWAMP
        );

        builder(ModBiomeTags.SPAWNS_CHORUS_BEETLES).add(
                Biomes.END_HIGHLANDS
        );

	    builder(ModBiomeTags.SPAWNS_WOODPECKERS)
                .add(
                        Biomes.OLD_GROWTH_PINE_TAIGA,
                        Biomes.OLD_GROWTH_BIRCH_FOREST
                )
                .addOptional(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(clutterBiomesNamespace, "giant_redwood_forest")))
        ;

        builder(ModBiomeTags.SPAWNS_ARROWFISH).add(
                Biomes.SWAMP,
                Biomes.MANGROVE_SWAMP
        );
    }
}
