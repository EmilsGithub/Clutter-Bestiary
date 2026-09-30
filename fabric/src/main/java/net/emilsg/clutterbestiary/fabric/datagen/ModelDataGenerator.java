package net.emilsg.clutterbestiary.fabric.datagen;
import net.minecraft.client.resources.model.sprite.Material;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.BucketEntityVariantProperty;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.BestiarySpawnEggItem;
import net.emilsg.clutterbestiary.item.custom.ButterflyElytraItem;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModelDataGenerator extends FabricModelProvider {
    public ModelDataGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(ModItems.RAW_CHORUS_ECHOFIN.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.COOKED_CHORUS_ECHOFIN.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.RAW_LEVITATING_ECHOFIN.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.COOKED_LEVITATING_ECHOFIN.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.RAW_VENISON.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.COOKED_VENISON.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.RAW_VENISON_RIBS.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.COOKED_VENISON_RIBS.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.KOI.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.ARROWFISH.get(), ModelTemplates.FLAT_ITEM);

        generator.generateFlatItem(ModItems.LEVITATING_ECHOFIN_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.CHORUS_ECHOFIN_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.ARROWFISH_BUCKET.get(), ModelTemplates.FLAT_ITEM);

        generator.generateFlatItem(ModItems.BUTTERFLY_COCOON.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.KIWI_BIRD_EGG.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.EMPEROR_PENGUIN_EGG.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.CROCODILE_EGG.get(), ModelTemplates.FLAT_ITEM);

        generator.generateFlatItem(ModItems.MOSSBLOOM_ANTLERS.get(), ModelTemplates.FLAT_ITEM);

        generator.generateFlatItem(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS.get(), ModelTemplates.FLAT_ITEM);

        this.registerKoiBucket(generator, ModItems.KOI_BUCKET.get());
        this.registerSeahorseBucket(generator, ModItems.SEAHORSE_BUCKET.get());
        this.registerRiverTurtleBucket(generator, ModItems.RIVER_TURTLE_BUCKET.get());
        this.registerJellyfishBucket(generator, ModItems.JELLYFISH_BUCKET.get());
        this.registerButterflyInABottle(generator, ModItems.BUTTERFLY_IN_A_BOTTLE.get());

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof ButterflyElytraItem elytra) registerElytra(generator, elytra);
        }

        // Spawn eggs use their own flat texture (item/<name>_spawn_egg) like vanilla 26.3 eggs.
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BestiarySpawnEggItem) generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }
    }

    private void registerButterflyInABottle(ItemModelGenerators gen, Item bottle) {
        List<VariantsRecord> variants = List.of(
                new VariantsRecord("white", "white_butterfly_in_a_bottle"),
                new VariantsRecord("light_gray", "light_gray_butterfly_in_a_bottle"),
                new VariantsRecord("gray", "gray_butterfly_in_a_bottle"),
                new VariantsRecord("black", "black_butterfly_in_a_bottle"),
                new VariantsRecord("brown", "brown_butterfly_in_a_bottle"),
                new VariantsRecord("red", "red_butterfly_in_a_bottle"),
                new VariantsRecord("orange", "orange_butterfly_in_a_bottle"),
                new VariantsRecord("yellow", "yellow_butterfly_in_a_bottle"),
                new VariantsRecord("lime", "lime_butterfly_in_a_bottle"),
                new VariantsRecord("green", "green_butterfly_in_a_bottle"),
                new VariantsRecord("light_blue", "light_blue_butterfly_in_a_bottle"),
                new VariantsRecord("cyan", "cyan_butterfly_in_a_bottle"),
                new VariantsRecord("blue", "blue_butterfly_in_a_bottle"),
                new VariantsRecord("purple", "purple_butterfly_in_a_bottle"),
                new VariantsRecord("magenta", "magenta_butterfly_in_a_bottle"),
                new VariantsRecord("pink", "pink_butterfly_in_a_bottle"),
                new VariantsRecord("warped", "warped_butterfly_in_a_bottle"),
                new VariantsRecord("crimson", "crimson_butterfly_in_a_bottle"),
                new VariantsRecord("soul", "soul_butterfly_in_a_bottle")
        );
        registerVariantItem(gen, bottle, "Variant", "white_butterfly_in_a_bottle", variants);
    }

    private void registerElytra(ItemModelGenerators itemGen, Item elytra) {
        Identifier modelId = ModelLocationUtils.getModelLocation(elytra);
        Identifier brokenId = modelId.withPath(path -> path.replace("item/", "item/broken_"));

        ModelTemplates.FLAT_ITEM.create(modelId, TextureMapping.layer0(elytra), itemGen.modelOutput);
        ModelTemplates.FLAT_ITEM.create(brokenId, TextureMapping.layer0(new Material(brokenId)), itemGen.modelOutput);

        itemGen.generateBooleanDispatch(elytra, new Broken(), ItemModelUtils.plainModel(brokenId), ItemModelUtils.plainModel(modelId));
    }

    /**
     * Selects a flat model from the variant stored in the item's bucket entity data, falling back to a default model.
     */
    private void registerVariantItem(ItemModelGenerators gen, Item item, String field, String defaultItemTexName, List<VariantsRecord> variants) {
        List<SelectItemModel.SwitchCase<String>> cases = new ArrayList<>();
        for (VariantsRecord variant : variants) {
            Identifier modelId = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "item/" + variant.model());
            ModelTemplates.FLAT_ITEM.create(modelId, TextureMapping.layer0(new Material(modelId)), gen.modelOutput);
            cases.add(ItemModelUtils.when(ClutterBestiary.MOD_ID + ":" + variant.variant(), ItemModelUtils.plainModel(modelId)));
        }

        Identifier defaultModelId = ModelLocationUtils.getModelLocation(item);
        ModelTemplates.FLAT_ITEM.create(defaultModelId, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "item/" + defaultItemTexName))), gen.modelOutput);
        ItemModel.Unbaked fallback = ItemModelUtils.plainModel(defaultModelId);

        gen.itemModelOutput.accept(item, ItemModelUtils.select(new BucketEntityVariantProperty(field), fallback, cases));
    }

    private void registerKoiBucket(ItemModelGenerators gen, Item koiBucket) {
        List<VariantsRecord> variants = List.of(
                new VariantsRecord("orange", "orange_koi_bucket"),
                new VariantsRecord("yellow", "yellow_koi_bucket"),
                new VariantsRecord("black", "black_koi_bucket"),
                new VariantsRecord("pearl", "pearl_koi_bucket")
        );
        registerVariantItem(gen, koiBucket, "BaseColor", "white_koi_bucket", variants);
    }

    private void registerRiverTurtleBucket(ItemModelGenerators gen, Item riverTurtleBucket) {
        List<VariantsRecord> variants = List.of(
                new VariantsRecord("coconut", "coconut_river_turtle_bucket")
        );
        registerVariantItem(gen, riverTurtleBucket, "Variant", "sandy_river_turtle_bucket", variants);
    }

    private void registerSeahorseBucket(ItemModelGenerators gen, Item seahorseBucket) {
        List<VariantsRecord> variants = List.of(
                new VariantsRecord("light_blue", "light_blue_seahorse_bucket"),
                new VariantsRecord("red", "red_seahorse_bucket"),
                new VariantsRecord("purple", "purple_seahorse_bucket")
        );
        registerVariantItem(gen, seahorseBucket, "Variant", "yellow_seahorse_bucket", variants);
    }

    private void registerJellyfishBucket(ItemModelGenerators gen, Item jellyfishBucket) {
        List<VariantsRecord> variants = List.of(
                new VariantsRecord("blue", "blue_jellyfish_bucket"),
                new VariantsRecord("purple", "purple_jellyfish_bucket")
        );
        registerVariantItem(gen, jellyfishBucket, "Variant", "green_jellyfish_bucket", variants);
    }

    /**
     * @param variant the variant name stored (namespaced) in the bucket entity data
     * @param model   the item model and texture name used for that variant
     */
    record VariantsRecord(String variant, String model) {
    }
}
