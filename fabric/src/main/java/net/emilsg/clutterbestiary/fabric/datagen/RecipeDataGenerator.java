package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeDataGenerator extends FabricRecipeProvider {

    public RecipeDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        offerAllCookingRecipes(exporter, ModItems.RAW_CHORUS_ECHOFIN.get(), ModItems.COOKED_CHORUS_ECHOFIN.get(), 0.35f, "chorus_echofin");
        offerAllCookingRecipes(exporter, ModItems.RAW_LEVITATING_ECHOFIN.get(), ModItems.COOKED_LEVITATING_ECHOFIN.get(), 0.35f, "levitating_echofin");
        offerAllCookingRecipes(exporter, ModItems.RAW_VENISON.get(), ModItems.COOKED_VENISON.get(), 0.35f, "venison");
        offerAllCookingRecipes(exporter, ModItems.RAW_VENISON_RIBS.get(), ModItems.COOKED_VENISON_RIBS.get(), 0.35f, "venison_ribs");

        offerCompactingRecipe(exporter, RecipeCategory.MISC, ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE.get(), ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS.get());
        offerOTORecipe(exporter, RecipeCategory.MISC, Items.GLOWSTONE_DUST, ModItems.MOSSBLOOM_ANTLERS.get());

        for (Item elytra : Registries.ITEM) {
            if (elytra instanceof BestiaryElytraItem bestiaryElytraItem) offerDecoratedElytraRecipes(exporter, elytra, bestiaryElytraItem.getComponent());
        }
    }

    public static void offerOTORecipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapelessRecipeJsonBuilder
                .create(category, output)
                .input(input, 1)
                .criterion("from_item", conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(ClutterBestiary.MOD_ID, getRecipeName(output)));
    }

    public void offerDecoratedElytraRecipes(RecipeExporter exporter, Item result, Item addition) {
        SmithingTransformRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE.get()),
                        Ingredient.fromTag(ModItemTags.C_ELYTRA), Ingredient.ofItems(addition),
                        RecipeCategory.MISC, result)
                .criterion("has_elytra_component", conditionsFromItem(addition))
                .criterion("has_elytra", conditionsFromItem(Items.ELYTRA))
                .offerTo(exporter, Identifier.of(ClutterBestiary.MOD_ID, getRecipeName(result)));
    }

    public void offerAllCookingRecipes(RecipeExporter exporter, Item component, Item result, float experience, String group) {
        List<ItemConvertible> COOKING_LIST = List.of(component);
        offerSmoking(exporter, COOKING_LIST, RecipeCategory.FOOD, result, experience, 100, group);
        offerSmelting(exporter, COOKING_LIST, RecipeCategory.FOOD, result, experience, 200, group);
        offerCampfireCooking(exporter, COOKING_LIST, RecipeCategory.FOOD, result, experience, 600, group);
    }

    public void offerAllSmeltingRecipes(RecipeExporter exporter, Item component, Item result, float experience, String group) {
        List<ItemConvertible> SMELTING_LIST = List.of(component);
        offerBlasting(exporter, SMELTING_LIST, RecipeCategory.MISC, result, experience, 100, group);
        offerSmelting(exporter, SMELTING_LIST, RecipeCategory.MISC, result, experience, 200, group);
    }

    public static void offerCampfireCooking(RecipeExporter exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        offerMultipleOptions(exporter, RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_campfire_cooking");
    }

    public static void offerSmoking(RecipeExporter exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        offerMultipleOptions(exporter, RecipeSerializer.SMOKING, SmokingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_smoking");
    }
}
