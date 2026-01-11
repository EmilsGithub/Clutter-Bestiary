package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.util.ModBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

public class BiomeTagDataGenerator extends FabricTagProvider<Biome> {

	public BiomeTagDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        String biomesOPlentyNamespace = "biomesoplenty";
        String regionsUnexploredNamespace = "regions_unexplored";
        String clutterBiomesNamespace = "clutterbiomes";
        String terralithNamespace = "terralith";

	    getOrCreateTagBuilder(ModBiomeTags.SPAWNS_BUTTERFLIES).add(
                        BiomeKeys.CRIMSON_FOREST,
                        BiomeKeys.WARPED_FOREST,
                        BiomeKeys.SOUL_SAND_VALLEY,
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.SUNFLOWER_PLAINS,
                        BiomeKeys.MEADOW,
                        BiomeKeys.CHERRY_GROVE
                )
                .addOptional(Identifier.of(biomesOPlentyNamespace, "cherry_blossom_grove"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "bamboo_grove"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "lavender_field"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "clover_plains"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "flower_fields"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "poppy_fields"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "rocky_meadow"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_EMBER_TORTOISES).add(
                        BiomeKeys.BASALT_DELTAS
                )
                .addOptional(Identifier.of(biomesOPlentyNamespace, "volcano"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "erupting_inferno"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "infernal_holt"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_ECHOFINS).add(
                BiomeKeys.END_HIGHLANDS
        );

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_JELLYFISHES)
                .add(
                        BiomeKeys.WARM_OCEAN,
                        BiomeKeys.LUKEWARM_OCEAN
                )
                .addOptional(Identifier.of(regionsUnexploredNamespace, "rocky_reef"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_MANTA_RAYS)
                .add(
                        BiomeKeys.WARM_OCEAN,
                        BiomeKeys.LUKEWARM_OCEAN
                )
                .addOptional(Identifier.of(regionsUnexploredNamespace, "rocky_reef"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_SEAHORSES).add(
                BiomeKeys.WARM_OCEAN
        );

	    getOrCreateTagBuilder(ModBiomeTags.SPAWNS_POTION_WASPS)
                .add(
                        BiomeKeys.JUNGLE
                )
                .addOptional(Identifier.of(terralithNamespace, "tropical_jungle"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "rainforest"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "rainforest"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_CAPYBARAS)
                .add(
                        BiomeKeys.SAVANNA_PLATEAU,
                        BiomeKeys.WINDSWEPT_SAVANNA,
                        BiomeKeys.SAVANNA
                )
                .addOptional(Identifier.of(terralithNamespace, "fractured_savanna"))
                .addOptional(Identifier.of(terralithNamespace, "savanna_badlands"))
                .addOptional(Identifier.of(terralithNamespace, "savanna_slopes"))
                .addOptional(Identifier.of(terralithNamespace, "lush_desert"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "steppe"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_BEAVERS)
                .add(BiomeKeys.RIVER)
                .addOptional(Identifier.of(terralithNamespace, "warm_river"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "bayou"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "wetland"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_EMPEROR_PENGUINS).add(
                        BiomeKeys.ICE_SPIKES,
                        BiomeKeys.SNOWY_PLAINS,
                        BiomeKeys.SNOWY_BEACH
                )
                .addOptional(Identifier.of(terralithNamespace, "ice_marsh"))
                .addOptional(Identifier.of(terralithNamespace, "muskeg"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "frozen_tundra"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_KIWIS)
                .add(
                        BiomeKeys.BAMBOO_JUNGLE,
                        BiomeKeys.JUNGLE,
                        BiomeKeys.SPARSE_JUNGLE
                )
                .addOptional(Identifier.of(terralithNamespace, "jungle_mountains"))
                .addOptional(Identifier.of(terralithNamespace, "rocky_jungle"))
                .addOptional(Identifier.of(terralithNamespace, "tropical_jungle"))
                .addOptional(Identifier.of(terralithNamespace, "rubble_jungle"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "rainforest"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "rocky_rainforest"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "tropics"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "rainforest"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_CHAMELEONS)
                .add(
                        BiomeKeys.BAMBOO_JUNGLE,
                        BiomeKeys.JUNGLE,
                        BiomeKeys.SPARSE_JUNGLE
                )
                .addOptional(Identifier.of(terralithNamespace, "jungle_mountains"))
                .addOptional(Identifier.of(terralithNamespace, "rocky_jungle"))
                .addOptional(Identifier.of(terralithNamespace, "tropical_jungle"))
                .addOptional(Identifier.of(terralithNamespace, "rubble_jungle"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "rainforest"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "rocky_rainforest"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "tropics"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "rainforest"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "sparse_rainforest"))
                .addOptional(Identifier.of(regionsUnexploredNamespace, "tropics"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_COATIS)
                .add(
                        BiomeKeys.JUNGLE,
                        BiomeKeys.BAMBOO_JUNGLE,
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
                )
                .addOptional(Identifier.of(biomesOPlentyNamespace, "bamboo_grove"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_RED_PANDAS)
                .add(
                        BiomeKeys.GROVE,
                        BiomeKeys.BAMBOO_JUNGLE
                )
                .addOptional(Identifier.of(biomesOPlentyNamespace, "bamboo_grove"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_DRAGONFLIES)
                .add(
                        BiomeKeys.SWAMP,
                        BiomeKeys.MANGROVE_SWAMP
                )
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_MOSSBLOOMS).add(
                BiomeKeys.LUSH_CAVES
        );

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_CRIMSON_NEWTS).add(
                BiomeKeys.CRIMSON_FOREST
        );

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_WARPED_NEWTS).add(
                BiomeKeys.WARPED_FOREST
        );

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_KOI)
                .add(
                        BiomeKeys.MANGROVE_SWAMP
                )
                .addOptional(Identifier.of(biomesOPlentyNamespace, "hot_springs"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_RIVER_TURTLES)
                .add(
                        BiomeKeys.RIVER,
                        BiomeKeys.MANGROVE_SWAMP
                )
                .addOptional(Identifier.of(terralithNamespace, "warm_river"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "bayou"))
                .addOptional(Identifier.of(biomesOPlentyNamespace, "wetland"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_BOOPLETS)
                .add(
                        BiomeKeys.SNOWY_PLAINS,
                        BiomeKeys.SNOWY_TAIGA
                )
                .addOptional(Identifier.of(terralithNamespace, "muskeg"))

                .addOptional(Identifier.of(regionsUnexploredNamespace, "frozen_tundra"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_STOATS).add(
                BiomeKeys.TAIGA,
                BiomeKeys.SNOWY_TAIGA,
                BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                BiomeKeys.GROVE
        );

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_CROCODILES).add(
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP
        );

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_CHORUS_BEETLES).add(
                BiomeKeys.END_HIGHLANDS
        );

	    getOrCreateTagBuilder(ModBiomeTags.SPAWNS_WOODPECKERS)
                .add(
                        BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                        BiomeKeys.OLD_GROWTH_BIRCH_FOREST
                )
                .addOptional(Identifier.of(clutterBiomesNamespace, "giant_redwood_forest"))
        ;

        getOrCreateTagBuilder(ModBiomeTags.SPAWNS_ARROWFISH).add(
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP
        );
    }
}
