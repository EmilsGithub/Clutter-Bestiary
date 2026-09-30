package net.emilsg.clutterbestiary.fabric;

import net.emilsg.clutterbestiary.entity.client.BucketEntityVariantProperty;
import net.emilsg.clutterbestiary.fabric.datagen.*;
import net.emilsg.clutterbestiary.fabric.mixin.SelectItemModelPropertiesAccessor;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

public class ClutterBestiaryDataGenFabric implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        // Client item definitions reference this property, so its codec must be known while models are written.
        SelectItemModelPropertiesAccessor.clutterbestiary$getIdMapper().put(BucketEntityVariantProperty.ID, BucketEntityVariantProperty.TYPE);

        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ItemTagDataGenerator::new);
        pack.addProvider(BlockTagDataGenerator::new);
        pack.addProvider(EntityTagDataGenerator::new);
        pack.addProvider(BiomeTagDataGenerator::new);

        pack.addProvider(RecipeDataGenerator::new);

        pack.addProvider(LootTableDataGenerator::new);
        pack.addProvider(EntityLootTableDataGenerator::new);

        pack.addProvider(AdvancementDataGenerator::new);

        pack.addProvider(ModelDataGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {

    }
}
