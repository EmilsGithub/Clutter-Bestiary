package net.emilsg.clutter_bestiary.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.item.ModItems;
import net.emilsg.clutter_bestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutter_bestiary.item.custom.BestiarySpawnEggItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModelDataGen extends FabricModelProvider {

    public ModelDataGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {

        generator.register(ModItems.KIWI_BIRD_EGG, Models.GENERATED);
        generator.register(ModItems.EMPEROR_PENGUIN_EGG, Models.GENERATED);
        generator.register(ModItems.BUTTERFLY_COCOON, Models.GENERATED);
        generator.register(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE, Models.GENERATED);
        generator.register(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS, Models.GENERATED);
        generator.register(ModItems.MOSSBLOOM_ANTLERS, Models.GENERATED);

        generator.register(ModItems.CHORUS_ECHOFIN_BUCKET, Models.GENERATED);
        generator.register(ModItems.RAW_CHORUS_ECHOFIN, Models.GENERATED);
        generator.register(ModItems.COOKED_CHORUS_ECHOFIN, Models.GENERATED);
        generator.register(ModItems.LEVITATING_ECHOFIN_BUCKET, Models.GENERATED);
        generator.register(ModItems.RAW_LEVITATING_ECHOFIN, Models.GENERATED);
        generator.register(ModItems.COOKED_LEVITATING_ECHOFIN, Models.GENERATED);
        generator.register(ModItems.RAW_VENISON, Models.GENERATED);
        generator.register(ModItems.COOKED_VENISON, Models.GENERATED);
        generator.register(ModItems.RAW_VENISON_RIBS, Models.GENERATED);
        generator.register(ModItems.COOKED_VENISON_RIBS, Models.GENERATED);

        for (Item item : Registries.ITEM) {
            if (item instanceof BestiaryElytraItem elytra) registerElytra(generator, elytra);
            if (item instanceof BestiarySpawnEggItem spawnEggItem) registerSpawnEggItem(generator, spawnEggItem);
        }
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

            override.addProperty("model", ClutterBestiary.MOD_ID + ":item/" + (id.getPath().replace("item/", "broken_")));
            overrides.add(override);
            jsonObject.add("overrides", overrides);

            return jsonObject;
        });
    }

    private void registerSpawnEggItem(ItemModelGenerator itemModelGenerator, Item egg) {
        itemModelGenerator.register(egg, new Model(Optional.of(Identifier.of("minecraft","item/template_spawn_egg")), Optional.empty()));
    }
}
