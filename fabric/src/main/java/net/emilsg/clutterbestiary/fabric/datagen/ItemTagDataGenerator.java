package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItemTagDataGenerator extends FabricTagProvider.ItemTagProvider {

    public ItemTagDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        /** Vanilla **/

        /** Common **/

        getOrCreateTagBuilder(ModItemTags.C_ENTITY_WATER_BUCKETS).add(
                ModItems.SEAHORSE_BUCKET.get(),
                ModItems.KOI_BUCKET.get()
        );

        getOrCreateTagBuilder(ModItemTags.C_EGGS).add(
                ModItems.EMPEROR_PENGUIN_EGG.get(),
                ModItems.KIWI_BIRD_EGG.get()
        );

        getOrCreateTagBuilder(ModItemTags.C_ELYTRA).add(
                ModItems.WHITE_BUTTERFLY_ELYTRA.get(),
                ModItems.LIGHT_GRAY_BUTTERFLY_ELYTRA.get(),
                ModItems.GRAY_BUTTERFLY_ELYTRA.get(),
                ModItems.BLACK_BUTTERFLY_ELYTRA.get(),
                ModItems.BROWN_BUTTERFLY_ELYTRA.get(),
                ModItems.RED_BUTTERFLY_ELYTRA.get(),
                ModItems.ORANGE_BUTTERFLY_ELYTRA.get(),
                ModItems.YELLOW_BUTTERFLY_ELYTRA.get(),
                ModItems.LIME_BUTTERFLY_ELYTRA.get(),
                ModItems.GREEN_BUTTERFLY_ELYTRA.get(),
                ModItems.CYAN_BUTTERFLY_ELYTRA.get(),
                ModItems.LIGHT_BLUE_BUTTERFLY_ELYTRA.get(),
                ModItems.BLUE_BUTTERFLY_ELYTRA.get(),
                ModItems.PURPLE_BUTTERFLY_ELYTRA.get(),
                ModItems.MAGENTA_BUTTERFLY_ELYTRA.get(),
                ModItems.PINK_BUTTERFLY_ELYTRA.get(),
                ModItems.CRIMSON_BUTTERFLY_ELYTRA.get(),
                ModItems.WARPED_BUTTERFLY_ELYTRA.get(),
                ModItems.SOUL_BUTTERFLY_ELYTRA.get(),
                Items.ELYTRA
        );

        getOrCreateTagBuilder(ModItemTags.C_SEEDS).add(
                Items.BEETROOT_SEEDS,
                Items.PUMPKIN_SEEDS,
                Items.WHEAT_SEEDS,
                Items.TORCHFLOWER_SEEDS,
                Items.PITCHER_POD
        );

        /** Clutter Bestiary **/

        getOrCreateTagBuilder(ModItemTags.RED_PANDA_BREEDING_FOOD).add(
                Items.BAMBOO
        );

        getOrCreateTagBuilder(ModItemTags.RED_PANDA_CRAVINGS).add(
                Items.APPLE,
                Items.CHICKEN,
                Items.BEETROOT,
                Items.CARROT,
                Items.SWEET_BERRIES,
                Items.GLOW_BERRIES,
                Items.EGG,
                Items.COD,
                Items.SALMON
        );
    }
}
