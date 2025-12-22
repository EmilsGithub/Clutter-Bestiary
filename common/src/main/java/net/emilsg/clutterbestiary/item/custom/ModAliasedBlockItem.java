package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.block.Block;
import net.minecraft.item.AliasedBlockItem;

import java.util.function.Supplier;

public class ModAliasedBlockItem extends AliasedBlockItem {

    public ModAliasedBlockItem(Supplier<? extends Block> block, Settings settings) {
        super((Block) ((Supplier) (block)).get(), settings);
    }
}
