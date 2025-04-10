package net.emilsg.clutter_bestiary.data;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.item.ModItems;
import net.emilsg.clutter_bestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutter_bestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class RecipeDataGen extends FabricRecipeProvider {

    public RecipeDataGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

        offer1ToXRecipes(exporter, RecipeCategory.MISC, Items.GLOWSTONE_DUST, ModItems.MOSSBLOOM_ANTLERS, 2);

        offerCompactingRecipe(exporter, RecipeCategory.MISC, ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE, ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS);

        offerFoodCookingRecipes(exporter, ModItems.RAW_VENISON, ModItems.COOKED_VENISON, 0.4f, "venison");
        offerFoodCookingRecipes(exporter, ModItems.RAW_VENISON_RIBS, ModItems.COOKED_VENISON_RIBS, 0.4f, "venison_ribs");
        offerFoodCookingRecipes(exporter, ModItems.RAW_CHORUS_ECHOFIN, ModItems.COOKED_CHORUS_ECHOFIN, 0.4f, "chorus_echofin");
        offerFoodCookingRecipes(exporter, ModItems.RAW_LEVITATING_ECHOFIN, ModItems.COOKED_LEVITATING_ECHOFIN, 0.4f, "levitating_echofin");

        for (Item elytra : Registries.ITEM) {
            if (elytra instanceof BestiaryElytraItem clutterElytraItem) offerButterflyElytraRecipes(exporter, elytra, clutterElytraItem.getComponent());
        }
    }

    public void offerButterflyElytraRecipes(Consumer<RecipeJsonProvider> exporter, Item result, Item addition) {
        SmithingTransformRecipeJsonBuilder
                .create(Ingredient.ofItems(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE), Ingredient.fromTag(ModItemTags.C_ELYTRA), Ingredient.ofItems(addition), RecipeCategory.MISC, result)
                .criterion("has_elytra", conditionsFromItem(ModItems.BUTTERFLY_IN_A_BOTTLE))
                .offerTo(exporter, Identifier.of(ClutterBestiary.MOD_ID, getRecipeName(result)));
    }

    public void offer1ToXRecipes(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, Item result, ItemConvertible ingredient, int count) {
        ShapelessRecipeJsonBuilder.create(category, result, count)
                .input(ingredient)
                .criterion("has_" + ingredient.asItem().toString(), conditionsFromItem(ingredient))
                .offerTo(exporter, Identifier.of(ClutterBestiary.MOD_ID, getRecipeName(result) + "_from_" + ingredient.asItem().toString()));
    }

    public void offerFoodCookingRecipes(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output, float experience, String group) {
        List<ItemConvertible> inputs = List.of(input);
        offerSmoking(exporter, inputs, RecipeCategory.FOOD, output, experience, 100, group);
        offerSmelting(exporter, inputs, RecipeCategory.FOOD, output, experience, 200, group);
        offerCampfireCooking(exporter, inputs, RecipeCategory.FOOD, output, experience, 600, group);
    }

    public static void offerSmelting(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        offerMultipleOptions(exporter, RecipeSerializer.SMELTING, inputs, category, output, experience, cookingTime, group, "_from_smelting_" + inputs.get(0).asItem().toString());
    }

    public static void offerSmoking(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        offerMultipleOptions(exporter, RecipeSerializer.SMOKING, inputs, category, output, experience, cookingTime, group, "_from_smoking_" + inputs.get(0).asItem().toString());
    }

    public static void offerCampfireCooking(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        offerMultipleOptions(exporter, RecipeSerializer.CAMPFIRE_COOKING, inputs, category, output, experience, cookingTime, group, "_from_campfire_cooking_" + inputs.get(0).asItem().toString());
    }
}
