package net.emilsg.clutterbestiary.item.custom;

import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ModAliasedBlockItem extends BlockItem {

    public ModAliasedBlockItem(Supplier<? extends Block> block, Properties settings) {
        // Aliased block items keep their own item.* translation key, as ItemNameBlockItem did.
        super(block.get(), settings.useItemDescriptionPrefix());
    }
}
