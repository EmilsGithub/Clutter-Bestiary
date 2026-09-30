package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import java.util.concurrent.CompletableFuture;

public class EntityTagDataGenerator extends FabricTagsProvider.EntityTypeTagsProvider {

    public EntityTagDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {

        /** Vanilla **/
        this.builder(EntityTypeTags.ARROWS).add(ModEntityTypes.ARROWFISH_PROJECTILE.get().builtInRegistryHolder().key());

        this.builder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
                ModEntityTypes.EMPEROR_PENGUIN.get().builtInRegistryHolder().key(),
                ModEntityTypes.BOOPLET.get().builtInRegistryHolder().key(),
                ModEntityTypes.RED_PANDA.get().builtInRegistryHolder().key(),
                ModEntityTypes.CROCODILE.get().builtInRegistryHolder().key()
        );

        this.builder(EntityTypeTags.AQUATIC)
                .add(ModEntityTypes.JELLYFISH.get().builtInRegistryHolder().key(),
                        ModEntityTypes.MANTA_RAY.get().builtInRegistryHolder().key(),
                        ModEntityTypes.KOI.get().builtInRegistryHolder().key(),
                        ModEntityTypes.SEAHORSE.get().builtInRegistryHolder().key(),
                        ModEntityTypes.KOI_EGGS.get().builtInRegistryHolder().key()
                );

        this.builder(EntityTypeTags.ARTHROPOD).add(
                ModEntityTypes.POTION_WASP.get().builtInRegistryHolder().key(),
                ModEntityTypes.DRAGONFLY.get().builtInRegistryHolder().key()
        );

        /** Modded **/


        /** Common **/
    }
}
