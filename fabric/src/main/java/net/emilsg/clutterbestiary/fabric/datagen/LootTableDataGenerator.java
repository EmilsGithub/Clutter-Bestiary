package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class LootTableDataGenerator extends FabricBlockLootSubProvider {

    public LootTableDataGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.dropSelf(ModBlocks.BUTTERFLY_COCOON.get());
        this.dropSelf(ModBlocks.KIWI_BIRD_EGG.get());
        this.dropSelf(ModBlocks.EMPEROR_PENGUIN_EGG.get());
        this.dropSelf(ModBlocks.CROCODILE_EGG.get());
    }
}
