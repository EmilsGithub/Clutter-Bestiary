package net.emilsg.clutterbestiary.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;

import net.emilsg.clutterbestiary.fabric.datagen.*;

public class ClutterBestiaryDataGenFabric implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ItemTagDataGenerator::new);
        pack.addProvider(BlockTagDataGenerator::new);
        pack.addProvider(EntityTagDataGenerator::new);
        pack.addProvider(BiomeTagDataGenerator::new);

        pack.addProvider(RecipeDataGenerator::new);

        pack.addProvider(LootTableDataGenerator::new);

        pack.addProvider(ModelDataGenerator::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {

    }
}
