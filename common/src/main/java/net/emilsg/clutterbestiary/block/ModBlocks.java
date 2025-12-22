package net.emilsg.clutterbestiary.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.block.custom.ButterflyBottleBlock;
import net.emilsg.clutterbestiary.block.custom.ButterflyCocoonBlock;
import net.emilsg.clutterbestiary.block.custom.HatchingEggBlock;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ClutterBestiary.MOD_ID, RegistryKeys.BLOCK);

    public static final RegistrySupplier<Block> BUTTERFLY_COCOON = registerBlockWithoutItem("butterfly_cocoon", () -> new ButterflyCocoonBlock(AbstractBlock.Settings.copy(Blocks.MOSS_BLOCK).nonOpaque().breakInstantly().sounds(BlockSoundGroup.MOSS_BLOCK)));
    public static final RegistrySupplier<Block> KIWI_BIRD_EGG = registerBlockWithoutItem("kiwi_bird_egg", () -> new HatchingEggBlock(AbstractBlock.Settings.copy(Blocks.SNIFFER_EGG).nonOpaque(), ModEntityTypes.KIWI_BIRD, 5, ModBlockTags.KIWI_EGG_HATCH_BOOST, 7.5, 7));
    public static final RegistrySupplier<Block> EMPEROR_PENGUIN_EGG = registerBlockWithoutItem("emperor_penguin_egg", () -> new HatchingEggBlock(AbstractBlock.Settings.copy(Blocks.SNIFFER_EGG).nonOpaque(), ModEntityTypes.EMPEROR_PENGUIN, 8, ModBlockTags.EMPEROR_PENGUIN_EGG_HATCH_BOOST, 6.5, 5));

    public static final RegistrySupplier<Block> BUTTERFLY_IN_A_BOTTLE = registerBlockWithoutItem("butterfly_in_a_bottle", () -> new ButterflyBottleBlock(AbstractBlock.Settings.copy(Blocks.GLASS).nonOpaque()));

    public static RegistrySupplier<Block> registerBlockWithoutItem(String name, Supplier<Block> block) {
        return BLOCKS.register(Identifier.of(ClutterBestiary.MOD_ID, name), block);
    }

    public static void register() {
        BLOCKS.register();
    }
}
