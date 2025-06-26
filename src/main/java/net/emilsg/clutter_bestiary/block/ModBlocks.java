package net.emilsg.clutter_bestiary.block;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.block.custom.ButterflyCocoonBlock;
import net.emilsg.clutter_bestiary.block.custom.HatchingEggBlock;
import net.emilsg.clutter_bestiary.entity.ModEntityTypes;
import net.emilsg.clutter_bestiary.util.ModBlockTags;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block BUTTERFLY_COCOON = registerBlockWithoutItem("butterfly_cocoon", new ButterflyCocoonBlock(FabricBlockSettings.copy(Blocks.MOSS_BLOCK).breakInstantly().nonOpaque().sounds(BlockSoundGroup.MOSS_BLOCK)));
    public static final Block KIWI_BIRD_EGG = registerBlockWithoutItem("kiwi_bird_egg", new HatchingEggBlock(FabricBlockSettings.copy(Blocks.SNIFFER_EGG).nonOpaque(), ModEntityTypes.KIWI_BIRD, 5, ModBlockTags.KIWI_EGG_HATCH_BOOST, 7.5, 7));
    public static final Block EMPEROR_PENGUIN_EGG = registerBlockWithoutItem("emperor_penguin_egg", new HatchingEggBlock(FabricBlockSettings.copy(Blocks.SNIFFER_EGG).nonOpaque(), ModEntityTypes.EMPEROR_PENGUIN, 8, ModBlockTags.EMPEROR_PENGUIN_EGG_HATCH_BOOST, 6.5, 5));

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(ClutterBestiary.MOD_ID, name), block);
    }

    public static void register() {

    }
}
