package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class EntityTagDataGenerator extends FabricTagProvider.EntityTypeTagProvider {

    public EntityTagDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        /** Vanilla **/
        this.getOrCreateTagBuilder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
                ModEntityTypes.EMPEROR_PENGUIN.get(),
                ModEntityTypes.BOOPLET.get(),
                ModEntityTypes.RED_PANDA.get()
        );

        this.getOrCreateTagBuilder(EntityTypeTags.AQUATIC)
                .add(ModEntityTypes.JELLYFISH.get(),
                ModEntityTypes.MANTA_RAY.get(),
                ModEntityTypes.KOI.get(),
                ModEntityTypes.SEAHORSE.get(),
                ModEntityTypes.KOI_EGGS.get()
                );

        this.getOrCreateTagBuilder(EntityTypeTags.ARTHROPOD).add(
                ModEntityTypes.POTION_WASP.get(),
                ModEntityTypes.DRAGONFLY.get()
        );

        /** Modded **/


        /** Common **/
    }
}
