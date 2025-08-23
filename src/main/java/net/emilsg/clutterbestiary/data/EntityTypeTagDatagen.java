package net.emilsg.clutterbestiary.data;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagDatagen extends FabricTagProvider.EntityTypeTagProvider {

    public EntityTypeTagDatagen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EntityTypeTags.ARTHROPOD).add(
                ModEntityTypes.POTION_WASP
        );

        getOrCreateTagBuilder(EntityTypeTags.AQUATIC).add(
                ModEntityTypes.MANTA_RAY,
                ModEntityTypes.SEAHORSE,
                ModEntityTypes.KOI,
                ModEntityTypes.KOI_EGGS
        );
    }
}
