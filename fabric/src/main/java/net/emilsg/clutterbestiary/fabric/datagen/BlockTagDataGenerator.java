package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BlockTagDataGenerator extends FabricTagProvider.BlockTagProvider {

    public BlockTagDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ModBlockTags.BEAVERS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.SAND)
                .add(Blocks.WATER)
        ;

        getOrCreateTagBuilder(ModBlockTags.COATIS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
        ;

        getOrCreateTagBuilder(ModBlockTags.RED_PANDAS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.PODZOL)
                .add(Blocks.SNOW_BLOCK)
        ;

        getOrCreateTagBuilder(ModBlockTags.RIVER_TURTLES_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.SAND)
                .add(Blocks.WATER)
        ;

        getOrCreateTagBuilder(ModBlockTags.BOOPLETS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.SNOW_BLOCK)
        ;

        getOrCreateTagBuilder(ModBlockTags.BUTTERFLIES_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.SOUL_SAND)
                .add(Blocks.SOUL_SOIL)
                .add(Blocks.WARPED_NYLIUM)
                .add(Blocks.CRIMSON_NYLIUM)
                .add(Blocks.NETHERRACK)
        ;

        getOrCreateTagBuilder(ModBlockTags.CAPYBARAS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.SNOW_BLOCK)
        ;

        getOrCreateTagBuilder(ModBlockTags.CHAMELEONS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
        ;

        getOrCreateTagBuilder(ModBlockTags.CRIMSON_NEWTS_SPAWN_ON)
                .add(Blocks.NETHERRACK)
                .add(Blocks.CRIMSON_NYLIUM)
        ;

        getOrCreateTagBuilder(ModBlockTags.DRAGONFLIES_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.SAND)
                .add(Blocks.LILY_PAD)
        ;

        getOrCreateTagBuilder(ModBlockTags.ECHOFINS_SPAWN_ON)
                .add(Blocks.CHORUS_FLOWER)
                .add(Blocks.CHORUS_PLANT)
                .add(Blocks.END_STONE)
        ;

        getOrCreateTagBuilder(ModBlockTags.EMBER_TORTOISES_SPAWN_ON)
                .add(Blocks.NETHERRACK)
                .add(Blocks.BASALT)
                .add(Blocks.BLACKSTONE)
        ;

        getOrCreateTagBuilder(ModBlockTags.EMPEROR_PENGUINS_SPAWN_ON)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.GRASS_BLOCK)
        ;

        getOrCreateTagBuilder(ModBlockTags.JELLYFISHES_SPAWN_ON)
                .add(Blocks.WATER)
        ;

        getOrCreateTagBuilder(ModBlockTags.KIWIS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
        ;

        getOrCreateTagBuilder(ModBlockTags.KOI_SPAWN_ON)
                .add(Blocks.WATER)
        ;

        getOrCreateTagBuilder(ModBlockTags.MANTA_RAYS_SPAWN_ON)
                .add(Blocks.WATER)
        ;

        getOrCreateTagBuilder(ModBlockTags.MOSSBLOOMS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.CLAY)
                .add(Blocks.STONE)
                .add(Blocks.DEEPSLATE)
        ;

        getOrCreateTagBuilder(ModBlockTags.POTION_WASPS_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.SAND)
        ;

        getOrCreateTagBuilder(ModBlockTags.SEAHORSES_SPAWN_ON)
                .add(Blocks.WATER)
        ;

        getOrCreateTagBuilder(ModBlockTags.WARPED_NEWTS_SPAWN_ON)
                .add(Blocks.NETHERRACK)
                .add(Blocks.WARPED_NYLIUM)
        ;

    }

}