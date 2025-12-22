package net.emilsg.clutterbestiary.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.BestiarySpawnEggItem;
import net.emilsg.clutterbestiary.item.custom.ButterflyElytraItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ModelDataGenerator extends FabricModelProvider {
    record VariantsRecord(float v, String name, boolean isCustomModel) {}


    public ModelDataGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ModItems.RAW_CHORUS_ECHOFIN.get(), Models.GENERATED);
        generator.register(ModItems.COOKED_CHORUS_ECHOFIN.get(), Models.GENERATED);
        generator.register(ModItems.RAW_LEVITATING_ECHOFIN.get(), Models.GENERATED);
        generator.register(ModItems.COOKED_LEVITATING_ECHOFIN.get(), Models.GENERATED);
        generator.register(ModItems.RAW_VENISON.get(), Models.GENERATED);
        generator.register(ModItems.COOKED_VENISON.get(), Models.GENERATED);
        generator.register(ModItems.RAW_VENISON_RIBS.get(), Models.GENERATED);
        generator.register(ModItems.COOKED_VENISON_RIBS.get(), Models.GENERATED);
        generator.register(ModItems.KOI.get(), Models.GENERATED);

        generator.register(ModItems.LEVITATING_ECHOFIN_BUCKET.get(), Models.GENERATED);
        generator.register(ModItems.CHORUS_ECHOFIN_BUCKET.get(), Models.GENERATED);

        generator.register(ModItems.BUTTERFLY_COCOON.get(), Models.GENERATED);
        generator.register(ModItems.KIWI_BIRD_EGG.get(), Models.GENERATED);
        generator.register(ModItems.EMPEROR_PENGUIN_EGG.get(), Models.GENERATED);

        generator.register(ModItems.MOSSBLOOM_ANTLERS.get(), Models.GENERATED);

        generator.register(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE.get(), Models.GENERATED);
        generator.register(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS.get(), Models.GENERATED);

        this.registerKoiBucket(generator, ModItems.KOI_BUCKET.get());
        this.registerSeahorseBucket(generator, ModItems.SEAHORSE_BUCKET.get());
        this.registerRiverTurtleBucket(generator, ModItems.RIVER_TURTLE_BUCKET.get());
        this.registerButterflyInABottle(generator, ModItems.BUTTERFLY_IN_A_BOTTLE.get());

        for (Item item : Registries.ITEM) {
            if (item instanceof ButterflyElytraItem elytra) registerElytra(generator, elytra);
            if (item instanceof BestiarySpawnEggItem spawnEggItem) registerSpawnEggItem(generator, spawnEggItem);
        }
    }

    private void registerSpawnEggItem(ItemModelGenerator itemModelGenerator, Item egg) {
        itemModelGenerator.register(egg, new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));
    }

    private void registerElytra(ItemModelGenerator itemGen, Item elytra) {
        String idString = ModelIds.getItemModelId(elytra).getPath().replace("item/", "item/broken_");
        TextureMap brokenMap = (new TextureMap()).put(TextureKey.LAYER0, Identifier.of(ClutterBestiary.MOD_ID, idString));

        Models.GENERATED.upload(Identifier.of(ClutterBestiary.MOD_ID, idString), brokenMap, itemGen.writer);

        Models.GENERATED.upload(ModelIds.getItemModelId(elytra), TextureMap.layer0(elytra), itemGen.writer, (id, textures) -> {
            JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
            JsonArray overrides = new JsonArray();
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("broken", 1);
            override.add("predicate", predicate);

            override.addProperty("model", "clutterbestiary:item/" + (id.getPath().replace("item/", "broken_")));
            overrides.add(override);
            jsonObject.add("overrides", overrides);

            return jsonObject;
        });
    }

    private void registerItemWithPredicate(ItemModelGenerator gen, Item item, String predicateKey, String defaultItemTexName, List<VariantsRecord> variants) {
        for (var x : variants) {
            if (!x.isCustomModel()) {
                var modelId = Identifier.of(ClutterBestiary.MOD_ID, "item/" + x.name());
                var texId   = Identifier.of(ClutterBestiary.MOD_ID, "item/" + x.name());
                Models.GENERATED.upload(modelId, new TextureMap().put(TextureKey.LAYER0, texId), gen.writer);
            }
        }

        Models.GENERATED.upload(
                ModelIds.getItemModelId(item),
                TextureMap.layer0(Identifier.of(ClutterBestiary.MOD_ID, "item/" + defaultItemTexName)),
                gen.writer,
                (mid, textures) -> {
                    JsonObject root = Models.GENERATED.createJson(mid, textures);
                    JsonArray overrides = new JsonArray();

                    for (VariantsRecord x : variants) {
                        JsonObject jsonObject = new JsonObject();
                        JsonObject predicate = new JsonObject();

                        predicate.addProperty(predicateKey, x.v());
                        jsonObject.add("predicate", predicate);
                        jsonObject.addProperty("model", ClutterBestiary.MOD_ID + ":item/" + x.name());

                        overrides.add(jsonObject);
                    }

                    root.add("overrides", overrides);
                    return root;
                }
        );
    }

    private void registerKoiBucket(ItemModelGenerator gen, Item koiBucket) {
        List<@NotNull VariantsRecord> variants = List.of(
                new VariantsRecord(0.0f, "white_koi_bucket", false),
                new VariantsRecord(0.1f, "orange_koi_bucket", false),
                new VariantsRecord(0.2f, "yellow_koi_bucket", false),
                new VariantsRecord(0.3f, "black_koi_bucket", false),
                new VariantsRecord(0.4f, "pearl_koi_bucket", false)
        );
        registerItemWithPredicate(gen, koiBucket, "type", "white_koi_bucket", variants);
    }

    private void registerSeahorseBucket(ItemModelGenerator gen, Item seahorseBucket) {
        List<@NotNull VariantsRecord> variants = List.of(
                new VariantsRecord(0.0f, "yellow_seahorse_bucket", false),
                new VariantsRecord(0.1f, "light_blue_seahorse_bucket", false),
                new VariantsRecord(0.2f, "red_seahorse_bucket", false),
                new VariantsRecord(0.3f, "purple_seahorse_bucket", false)
        );
        registerItemWithPredicate(gen, seahorseBucket, "type", "yellow_seahorse_bucket", variants);
    }

    private void registerButterflyInABottle(ItemModelGenerator gen, Item bottle) {
        List<@NotNull VariantsRecord> variants = List.of(
                new VariantsRecord(0.0f, "white_butterfly_in_a_bottle", false),
                new VariantsRecord(0.01f, "light_gray_butterfly_in_a_bottle", false),
                new VariantsRecord(0.02f, "gray_butterfly_in_a_bottle", false),
                new VariantsRecord(0.03f, "black_butterfly_in_a_bottle", false),
                new VariantsRecord(0.04f, "brown_butterfly_in_a_bottle", false),
                new VariantsRecord(0.05f, "red_butterfly_in_a_bottle", false),
                new VariantsRecord(0.06f, "orange_butterfly_in_a_bottle", false),
                new VariantsRecord(0.07f, "yellow_butterfly_in_a_bottle", false),
                new VariantsRecord(0.08f, "lime_butterfly_in_a_bottle", false),
                new VariantsRecord(0.09f, "green_butterfly_in_a_bottle", false),
                new VariantsRecord(0.10f, "light_blue_butterfly_in_a_bottle", false),
                new VariantsRecord(0.11f, "cyan_butterfly_in_a_bottle", false),
                new VariantsRecord(0.12f, "blue_butterfly_in_a_bottle", false),
                new VariantsRecord(0.13f, "purple_butterfly_in_a_bottle", false),
                new VariantsRecord(0.14f, "magenta_butterfly_in_a_bottle", false),
                new VariantsRecord(0.15f, "pink_butterfly_in_a_bottle", false),
                new VariantsRecord(0.16f, "warped_butterfly_in_a_bottle", false),
                new VariantsRecord(0.17f, "crimson_butterfly_in_a_bottle", false),
                new VariantsRecord(0.18f, "soul_butterfly_in_a_bottle", false)
        );
        registerItemWithPredicate(gen, bottle, "type", "default_butterfly_in_a_bottle", variants);
    }

    private void registerRiverTurtleBucket(ItemModelGenerator gen, Item riverTurtleBucket) {
        List<@NotNull VariantsRecord> variants = List.of(
                new VariantsRecord(0.0f, "sandy_river_turtle_bucket", false),
                new VariantsRecord(0.1f, "coconut_river_turtle_bucket", false)
        );
        registerItemWithPredicate(gen, riverTurtleBucket, "type", "sandy_river_turtle_bucket", variants);
    }
}
