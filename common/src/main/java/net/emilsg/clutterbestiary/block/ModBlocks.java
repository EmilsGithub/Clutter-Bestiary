package net.emilsg.clutterbestiary.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.block.custom.ButterflyBottleBlock;
import net.emilsg.clutterbestiary.block.custom.ButterflyCocoonBlock;
import net.emilsg.clutterbestiary.block.custom.CrocodileEggBlock;
import net.emilsg.clutterbestiary.block.custom.HatchingEggBlock;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Function;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ClutterBestiary.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> BUTTERFLY_COCOON = registerBlockWithoutItem("butterfly_cocoon", Blocks.MOSS_BLOCK, properties -> new ButterflyCocoonBlock(properties.noOcclusion().instabreak().sound(SoundType.MOSS)));
    public static final RegistrySupplier<Block> KIWI_BIRD_EGG = registerBlockWithoutItem("kiwi_bird_egg", Blocks.SNIFFER_EGG, properties -> new HatchingEggBlock(properties.noOcclusion(), ModEntityTypes.KIWI_BIRD, 5, ModBlockTags.KIWI_EGG_HATCH_BOOST, 7.5, 7));
    public static final RegistrySupplier<Block> EMPEROR_PENGUIN_EGG = registerBlockWithoutItem("emperor_penguin_egg", Blocks.SNIFFER_EGG, properties -> new HatchingEggBlock(properties.noOcclusion(), ModEntityTypes.EMPEROR_PENGUIN, 8, ModBlockTags.EMPEROR_PENGUIN_EGG_HATCH_BOOST, 6.5, 5));
    public static final RegistrySupplier<Block> CROCODILE_EGG = registerBlockWithoutItem("crocodile_egg", Blocks.SNIFFER_EGG, properties -> new CrocodileEggBlock(properties.noOcclusion(), ModEntityTypes.CROCODILE, 8, ModBlockTags.CROCODILE_EGG_HATCH_BOOST, 6, 12));

    public static final RegistrySupplier<Block> BUTTERFLY_IN_A_BOTTLE = registerBlockWithoutItem("butterfly_in_a_bottle", Blocks.GLASS, properties -> new ButterflyBottleBlock(properties.noOcclusion()));

    public static RegistrySupplier<Block> registerBlockWithoutItem(String name, Block copyFrom, Function<BlockBehaviour.Properties, Block> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, name);
        // Block properties must carry their registry key before the block is constructed.
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        return BLOCKS.register(id, () -> factory.apply(BlockBehaviour.Properties.ofFullCopy(copyFrom).setId(key)));
    }

    public static void register() {
        BLOCKS.register();
    }
}
