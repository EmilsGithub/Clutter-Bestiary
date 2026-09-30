package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class ItemTagDataGenerator extends FabricTagsProvider.ItemTagsProvider {

    public ItemTagDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {

        /** Vanilla **/

        builder(ItemTags.MEAT).add(
                ModItems.RAW_VENISON.getKey(),
                ModItems.COOKED_VENISON.getKey(),
                ModItems.RAW_VENISON_RIBS.getKey(),
                ModItems.COOKED_VENISON_RIBS.getKey()
        );

        /** Common **/

        builder(ModItemTags.C_ENTITY_WATER_BUCKETS).add(
                ModItems.SEAHORSE_BUCKET.getKey(),
                ModItems.KOI_BUCKET.getKey(),
                ModItems.RIVER_TURTLE_BUCKET.getKey(),
                ModItems.JELLYFISH_BUCKET.getKey(),
                ModItems.ARROWFISH_BUCKET.getKey()
        );

        builder(ModItemTags.C_EGGS).add(
                ModItems.EMPEROR_PENGUIN_EGG.getKey(),
                ModItems.KIWI_BIRD_EGG.getKey(),
                ModItems.CROCODILE_EGG.getKey()
        );

        builder(ModItemTags.C_ELYTRA).add(
                ModItems.WHITE_BUTTERFLY_ELYTRA.getKey(),
                ModItems.LIGHT_GRAY_BUTTERFLY_ELYTRA.getKey(),
                ModItems.GRAY_BUTTERFLY_ELYTRA.getKey(),
                ModItems.BLACK_BUTTERFLY_ELYTRA.getKey(),
                ModItems.BROWN_BUTTERFLY_ELYTRA.getKey(),
                ModItems.RED_BUTTERFLY_ELYTRA.getKey(),
                ModItems.ORANGE_BUTTERFLY_ELYTRA.getKey(),
                ModItems.YELLOW_BUTTERFLY_ELYTRA.getKey(),
                ModItems.LIME_BUTTERFLY_ELYTRA.getKey(),
                ModItems.GREEN_BUTTERFLY_ELYTRA.getKey(),
                ModItems.CYAN_BUTTERFLY_ELYTRA.getKey(),
                ModItems.LIGHT_BLUE_BUTTERFLY_ELYTRA.getKey(),
                ModItems.BLUE_BUTTERFLY_ELYTRA.getKey(),
                ModItems.PURPLE_BUTTERFLY_ELYTRA.getKey(),
                ModItems.MAGENTA_BUTTERFLY_ELYTRA.getKey(),
                ModItems.PINK_BUTTERFLY_ELYTRA.getKey(),
                ModItems.CRIMSON_BUTTERFLY_ELYTRA.getKey(),
                ModItems.WARPED_BUTTERFLY_ELYTRA.getKey(),
                ModItems.SOUL_BUTTERFLY_ELYTRA.getKey(),
                Items.ELYTRA.builtInRegistryHolder().key()
        );

        builder(ModItemTags.C_SEEDS).add(
                Items.BEETROOT_SEEDS.builtInRegistryHolder().key(),
                Items.PUMPKIN_SEEDS.builtInRegistryHolder().key(),
                Items.WHEAT_SEEDS.builtInRegistryHolder().key(),
                Items.TORCHFLOWER_SEEDS.builtInRegistryHolder().key(),
                Items.PITCHER_POD.builtInRegistryHolder().key()
        );

        /** Clutter Bestiary **/

        builder(ModItemTags.RED_PANDA_BREEDING_FOOD).add(
                Items.BAMBOO.builtInRegistryHolder().key()
        );

        builder(ModItemTags.RED_PANDA_CRAVINGS).add(
                Items.APPLE.builtInRegistryHolder().key(),
                Items.CHICKEN.builtInRegistryHolder().key(),
                Items.BEETROOT.builtInRegistryHolder().key(),
                Items.CARROT.builtInRegistryHolder().key(),
                Items.SWEET_BERRIES.builtInRegistryHolder().key(),
                Items.GLOW_BERRIES.builtInRegistryHolder().key(),
                Items.EGG.builtInRegistryHolder().key(),
                Items.COD.builtInRegistryHolder().key(),
                Items.SALMON.builtInRegistryHolder().key()
        );
    }
}
