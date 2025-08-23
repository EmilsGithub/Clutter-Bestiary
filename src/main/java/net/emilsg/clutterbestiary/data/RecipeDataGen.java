package net.emilsg.clutterbestiary.data;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeDataGen extends FabricRecipeProvider {

    public RecipeDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public static void offerSmoking(RecipeExporter exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        offerMultipleOptions(exporter, RecipeSerializer.SMOKING, SmokingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_smoking");
    }

    public static void offerCampfireCooking(RecipeExporter exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        offerMultipleOptions(exporter, RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_campfire_cooking");
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        offer1ToXRecipes(recipeExporter, RecipeCategory.MISC, Items.GLOWSTONE_DUST, ModItems.MOSSBLOOM_ANTLERS, 2);

        offerCompactingRecipe(recipeExporter, RecipeCategory.MISC, ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE, ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS);

        offerFoodCookingRecipes(recipeExporter, ModItems.RAW_VENISON, ModItems.COOKED_VENISON, 0.4f, "venison");
        offerFoodCookingRecipes(recipeExporter, ModItems.RAW_VENISON_RIBS, ModItems.COOKED_VENISON_RIBS, 0.4f, "venison_ribs");
        offerFoodCookingRecipes(recipeExporter, ModItems.RAW_CHORUS_ECHOFIN, ModItems.COOKED_CHORUS_ECHOFIN, 0.4f, "chorus_echofin");
        offerFoodCookingRecipes(recipeExporter, ModItems.RAW_LEVITATING_ECHOFIN, ModItems.COOKED_LEVITATING_ECHOFIN, 0.4f, "levitating_echofin");

        for (Item elytra : Registries.ITEM) {
            if (elytra instanceof BestiaryElytraItem clutterElytraItem)
                offerButterflyElytraRecipes(recipeExporter, elytra, clutterElytraItem.getComponent());
        }
    }

    public void offer1ToXRecipes(RecipeExporter exporter, RecipeCategory category, Item result, ItemConvertible ingredient, int count) {
        ShapelessRecipeJsonBuilder.create(category, result, count)
                .input(ingredient)
                .criterion("has_" + ingredient.asItem().toString(), conditionsFromItem(ingredient))
                .offerTo(exporter, Identifier.of(ClutterBestiary.MOD_ID, getRecipeName(result) + "_from_" + getRecipeName(ingredient.asItem())));
    }

    public void offerButterflyElytraRecipes(RecipeExporter exporter, Item result, Item addition) {
        SmithingTransformRecipeJsonBuilder
                .create(Ingredient.ofItems(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE), Ingredient.fromTag(ModItemTags.C_ELYTRA), Ingredient.ofItems(addition), RecipeCategory.MISC, result)
                .criterion("has_elytra", conditionsFromItem(ModItems.BUTTERFLY_IN_A_BOTTLE))
                .offerTo(exporter, Identifier.of(ClutterBestiary.MOD_ID, getRecipeName(result)));
    }

    public void offerFoodCookingRecipes(RecipeExporter exporter, ItemConvertible input, ItemConvertible output, float experience, String group) {
        List<ItemConvertible> inputs = List.of(input);
        offerSmoking(exporter, inputs, RecipeCategory.FOOD, output, experience, 100, group);
        offerSmelting(exporter, inputs, RecipeCategory.FOOD, output, experience, 200, group);
        offerCampfireCooking(exporter, inputs, RecipeCategory.FOOD, output, experience, 600, group);
    }
}
