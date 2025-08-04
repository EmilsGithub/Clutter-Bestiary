package net.emilsg.clutterbestiary.data;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class BlockLootTableDataGen extends FabricBlockLootTableProvider {

    public BlockLootTableDataGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.EMPEROR_PENGUIN_EGG);
        this.addDrop(ModBlocks.KIWI_BIRD_EGG);
        this.addDrop(ModBlocks.BUTTERFLY_COCOON);
    }
}
