package net.emilsg.clutterbestiary.data;

import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItemTagDataGen extends FabricTagProvider<Item> {

    public ItemTagDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        getOrCreateTagBuilder(ModItemTags.C_ELYTRA).add(
                Items.ELYTRA,
                ModItems.WHITE_BUTTERFLY_ELYTRA,
                ModItems.LIGHT_GRAY_BUTTERFLY_ELYTRA,
                ModItems.GRAY_BUTTERFLY_ELYTRA,
                ModItems.BLACK_BUTTERFLY_ELYTRA,
                ModItems.BROWN_BUTTERFLY_ELYTRA,
                ModItems.RED_BUTTERFLY_ELYTRA,
                ModItems.ORANGE_BUTTERFLY_ELYTRA,
                ModItems.YELLOW_BUTTERFLY_ELYTRA,
                ModItems.LIME_BUTTERFLY_ELYTRA,
                ModItems.GREEN_BUTTERFLY_ELYTRA,
                ModItems.CYAN_BUTTERFLY_ELYTRA,
                ModItems.LIGHT_BLUE_BUTTERFLY_ELYTRA,
                ModItems.BLUE_BUTTERFLY_ELYTRA,
                ModItems.PURPLE_BUTTERFLY_ELYTRA,
                ModItems.MAGENTA_BUTTERFLY_ELYTRA,
                ModItems.PINK_BUTTERFLY_ELYTRA,
                ModItems.CRIMSON_BUTTERFLY_ELYTRA,
                ModItems.WARPED_BUTTERFLY_ELYTRA,
                ModItems.SOUL_BUTTERFLY_ELYTRA
        );

        getOrCreateTagBuilder(ModItemTags.C_ENTITY_WATER_BUCKETS).add(
                ModItems.SEAHORSE_BUCKET,
                ModItems.KOI_BUCKET
        );

        getOrCreateTagBuilder(ModItemTags.C_SEEDS).add(
                Items.BEETROOT_SEEDS,
                Items.MELON_SEEDS,
                Items.PUMPKIN_SEEDS,
                Items.WHEAT_SEEDS,
                Items.TORCHFLOWER_SEEDS,
                Items.PITCHER_POD
        );

    }
}
