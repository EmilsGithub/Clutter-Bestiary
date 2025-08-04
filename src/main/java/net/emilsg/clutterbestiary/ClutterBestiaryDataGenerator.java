package net.emilsg.clutterbestiary;

import net.emilsg.clutterbestiary.data.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ClutterBestiaryDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(BlockTagDataGen::new);
        pack.addProvider(ItemTagDataGen::new);
        pack.addProvider(BlockLootTableDataGen::new);
        pack.addProvider(ModelDataGen::new);
        pack.addProvider(BiomeTagDataGen::new);
        pack.addProvider(RecipeDataGen::new);
    }
}
