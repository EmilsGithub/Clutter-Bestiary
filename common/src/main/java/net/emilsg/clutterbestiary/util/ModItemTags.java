package net.emilsg.clutterbestiary.util;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {

    public static final TagKey<Item> C_SEEDS = create("c", "seeds");
    public static final TagKey<Item> C_ENTITY_WATER_BUCKETS = create("c", "entity_water_buckets");
    public static final TagKey<Item> C_ELYTRA = create("c", "elytra");

    private static TagKey<Item> create(String path) {
        return create(path, ClutterBestiary.MOD_ID);
    }

    private static TagKey<Item> create(String namespace, String path) {
        return TagKey.of(Registries.ITEM.getKey(), Identifier.of(namespace, path));
    }
}
