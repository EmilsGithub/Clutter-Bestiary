package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LootTableDataGenerator extends FabricBlockLootTableProvider {

    public LootTableDataGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.BUTTERFLY_COCOON.get());
        this.addDrop(ModBlocks.KIWI_BIRD_EGG.get());
        this.addDrop(ModBlocks.EMPEROR_PENGUIN_EGG.get());
        this.addDrop(ModBlocks.CROCODILE_EGG.get());
    }
}
