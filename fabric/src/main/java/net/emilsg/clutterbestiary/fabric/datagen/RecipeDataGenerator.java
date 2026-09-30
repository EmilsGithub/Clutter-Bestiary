package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeDataGenerator extends FabricRecipeProvider {

    public RecipeDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                offerAllCookingRecipes(ModItems.RAW_CHORUS_ECHOFIN.get(), ModItems.COOKED_CHORUS_ECHOFIN.get(), 0.35f, "chorus_echofin");
                offerAllCookingRecipes(ModItems.RAW_LEVITATING_ECHOFIN.get(), ModItems.COOKED_LEVITATING_ECHOFIN.get(), 0.35f, "levitating_echofin");
                offerAllCookingRecipes(ModItems.RAW_VENISON.get(), ModItems.COOKED_VENISON.get(), 0.35f, "venison");
                offerAllCookingRecipes(ModItems.RAW_VENISON_RIBS.get(), ModItems.COOKED_VENISON_RIBS.get(), 0.35f, "venison_ribs");

                this.threeByThreePacker(RecipeCategory.MISC, ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE.get(), ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS.get());
                offerOTORecipe(RecipeCategory.MISC, Items.GLOWSTONE_DUST, ModItems.MOSSBLOOM_ANTLERS.get());

                for (Item elytra : BuiltInRegistries.ITEM) {
                    if (elytra instanceof BestiaryElytraItem bestiaryElytraItem)
                        offerDecoratedElytraRecipes(elytra, bestiaryElytraItem.getComponent());
                }
            }

            private void offerOTORecipe(RecipeCategory category, ItemLike output, ItemLike input) {
                this.shapeless(category, output)
                        .requires(input, 1)
                        .unlockedBy("from_item", this.has(input))
                        .save(this.output, ClutterBestiary.MOD_ID + ":" + getSimpleRecipeName(output));
            }

            private void offerAllCookingRecipes(Item component, Item result, float experience, String group) {
                List<ItemLike> cookingList = List.of(component);
                offerCooking(cookingList, result, experience, 100, group, SmokingRecipe::new, "_from_smoking");
                this.oreSmelting(cookingList, RecipeCategory.FOOD, CookingBookCategory.FOOD, result, experience, 200, group);
                offerCooking(cookingList, result, experience, 600, group, CampfireCookingRecipe::new, "_from_campfire_cooking");
            }

            private <T extends AbstractCookingRecipe> void offerCooking(List<ItemLike> inputs, ItemLike output, float experience, int cookingTime, String group, AbstractCookingRecipe.Factory<T> factory, String fromDesc) {
                for (ItemLike input : inputs) {
                    SimpleCookingRecipeBuilder.generic(Ingredient.of(input), RecipeCategory.FOOD, CookingBookCategory.FOOD, output, experience, cookingTime, factory)
                            .group(group)
                            .unlockedBy(getHasName(input), this.has(input))
                            .save(this.output, getItemName(output) + fromDesc + "_" + getItemName(input));
                }
            }

            private void offerDecoratedElytraRecipes(Item result, Item addition) {
                SmithingTransformRecipeBuilder
                        .smithing(
                                Ingredient.of(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE.get()),
                                this.tag(ModItemTags.C_ELYTRA), Ingredient.of(addition),
                                RecipeCategory.MISC, result)
                        .unlocks("has_elytra_component", this.has(addition))
                        .unlocks("has_elytra", this.has(Items.ELYTRA))
                        .save(this.output, ClutterBestiary.MOD_ID + ":" + getSimpleRecipeName(result));
            }
        };
    }
}
