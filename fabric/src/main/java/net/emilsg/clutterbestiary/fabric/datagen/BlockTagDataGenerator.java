package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

public class BlockTagDataGenerator extends FabricTagsProvider.BlockTagsProvider {

    public BlockTagDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        builder(ModBlockTags.KIWI_EGG_HATCH_BOOST)
                .add(Blocks.HAY_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.EMPEROR_PENGUIN_EGG_HATCH_BOOST)
                .add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.CROCODILE_EGG_HATCH_BOOST)
                .add(Blocks.SAND.builtInRegistryHolder().key())
                .add(Blocks.RED_SAND.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.BEAVERS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.SAND.builtInRegistryHolder().key())
                .add(Blocks.WATER.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.COATIS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.PODZOL.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.RED_PANDAS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.PODZOL.builtInRegistryHolder().key())
                .add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.RIVER_TURTLES_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.SAND.builtInRegistryHolder().key())
                .add(Blocks.WATER.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.BOOPLETS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.BUTTERFLIES_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.SOUL_SAND.builtInRegistryHolder().key())
                .add(Blocks.SOUL_SOIL.builtInRegistryHolder().key())
                .add(Blocks.WARPED_NYLIUM.builtInRegistryHolder().key())
                .add(Blocks.CRIMSON_NYLIUM.builtInRegistryHolder().key())
                .add(Blocks.NETHERRACK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.CAPYBARAS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.CHAMELEONS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.CRIMSON_NEWTS_SPAWN_ON)
                .add(Blocks.NETHERRACK.builtInRegistryHolder().key())
                .add(Blocks.CRIMSON_NYLIUM.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.DRAGONFLIES_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.SAND.builtInRegistryHolder().key())
                .add(Blocks.LILY_PAD.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.ECHOFINS_SPAWN_ON)
                .add(Blocks.CHORUS_FLOWER.builtInRegistryHolder().key())
                .add(Blocks.CHORUS_PLANT.builtInRegistryHolder().key())
                .add(Blocks.END_STONE.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.EMBER_TORTOISES_SPAWN_ON)
                .add(Blocks.NETHERRACK.builtInRegistryHolder().key())
                .add(Blocks.BASALT.builtInRegistryHolder().key())
                .add(Blocks.BLACKSTONE.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.EMPEROR_PENGUINS_SPAWN_ON)
                .add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.JELLYFISHES_SPAWN_ON)
                .add(Blocks.WATER.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.KIWIS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.KOI_SPAWN_ON)
                .add(Blocks.WATER.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.MANTA_RAYS_SPAWN_ON)
                .add(Blocks.WATER.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.MOSSBLOOMS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.MOSS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.CLAY.builtInRegistryHolder().key())
                .add(Blocks.STONE.builtInRegistryHolder().key())
                .add(Blocks.DEEPSLATE.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.POTION_WASPS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.SAND.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.SEAHORSES_SPAWN_ON)
                .add(Blocks.WATER.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.WARPED_NEWTS_SPAWN_ON)
                .add(Blocks.NETHERRACK.builtInRegistryHolder().key())
                .add(Blocks.WARPED_NYLIUM.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.STOATS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.PODZOL.builtInRegistryHolder().key())
                .add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.CROCODILES_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.MUD.builtInRegistryHolder().key())
                .add(Blocks.SAND.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.CHORUS_BEETLES_SPAWN_ON)
                .add(Blocks.END_STONE.builtInRegistryHolder().key())
                .add(Blocks.CHORUS_PLANT.builtInRegistryHolder().key())
                .add(Blocks.CHORUS_FLOWER.builtInRegistryHolder().key())
        ;

        builder(ModBlockTags.WOODPECKERS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key())
                .add(Blocks.PODZOL.builtInRegistryHolder().key());

        builder(ModBlockTags.ARROWFISH_SPAWN_ON)
                .add(Blocks.WATER.builtInRegistryHolder().key())
                .add(Blocks.SAND.builtInRegistryHolder().key())
        ;

    }

}
