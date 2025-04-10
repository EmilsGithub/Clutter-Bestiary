package net.emilsg.clutter_bestiary.data;

import net.emilsg.clutter_bestiary.util.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BlockTagDataGen extends FabricTagProvider<Block> {

    public BlockTagDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        getOrCreateTagBuilder(ModBlockTags.ECHOFINS_SPAWN_ON).add(
                Blocks.CHORUS_FLOWER,
                Blocks.CHORUS_PLANT,
                Blocks.END_STONE
        );

        getOrCreateTagBuilder(ModBlockTags.BUTTERFLIES_SPAWN_ON).add(
                Blocks.GRASS_BLOCK,
                Blocks.SOUL_SAND,
                Blocks.SOUL_SOIL,
                Blocks.WARPED_NYLIUM,
                Blocks.CRIMSON_NYLIUM,
                Blocks.NETHERRACK
        );

        getOrCreateTagBuilder(ModBlockTags.EMBER_TORTOISES_SPAWN_ON).add(
                Blocks.NETHERRACK,
                Blocks.BASALT,
                Blocks.BLACKSTONE
        );

        getOrCreateTagBuilder(ModBlockTags.CRIMSON_NEWTS_SPAWN_ON).add(
                Blocks.NETHERRACK,
                Blocks.CRIMSON_NYLIUM
        );

        getOrCreateTagBuilder(ModBlockTags.WARPED_NEWTS_SPAWN_ON).add(
                Blocks.NETHERRACK,
                Blocks.WARPED_NYLIUM
        );

        getOrCreateTagBuilder(ModBlockTags.SEAHORSES_SPAWN_ON).add(
                Blocks.WATER
        );

        getOrCreateTagBuilder(ModBlockTags.MANTA_RAYS_SPAWN_ON).add(
                Blocks.WATER
        );

        getOrCreateTagBuilder(ModBlockTags.JELLYFISHES_SPAWN_ON).add(
                Blocks.WATER
        );

        getOrCreateTagBuilder(ModBlockTags.CAPYBARAS_SPAWN_ON).add(
                Blocks.GRASS_BLOCK,
                Blocks.SAND
        );

        getOrCreateTagBuilder(ModBlockTags.BEAVERS_SPAWN_ON).add(
                Blocks.GRASS_BLOCK,
                Blocks.SAND,
                Blocks.WATER
        );

        getOrCreateTagBuilder(ModBlockTags.EMPEROR_PENGUINS_SPAWN_ON).add(
                Blocks.SNOW_BLOCK,
                Blocks.GRASS_BLOCK,
                Blocks.ICE
        );

        getOrCreateTagBuilder(ModBlockTags.KIWIS_SPAWN_ON).add(
                Blocks.GRASS_BLOCK
        );

        getOrCreateTagBuilder(ModBlockTags.CHAMELEONS_SPAWN_ON).add(
                Blocks.GRASS_BLOCK
        );

        getOrCreateTagBuilder(ModBlockTags.MOSSBLOOMS_SPAWN_ON).add(
                Blocks.GRASS_BLOCK,
                Blocks.MOSS_BLOCK,
                Blocks.STONE,
                Blocks.DEEPSLATE
        );


    }
}
