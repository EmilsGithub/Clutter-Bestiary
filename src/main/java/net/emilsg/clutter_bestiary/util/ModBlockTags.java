package net.emilsg.clutter_bestiary.util;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModBlockTags {

    public static final TagKey<Block> KIWI_EGG_HATCH_BOOST = create(ClutterBestiary.MOD_ID, "kiwi_egg_hatch_boost");
    public static final TagKey<Block> EMPEROR_PENGUIN_EGG_HATCH_BOOST = create(ClutterBestiary.MOD_ID, "emperor_penguin_egg_hatch_boost");

    public static final TagKey<Block> CRIMSON_NEWTS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "crimson_newts_spawn_on");
    public static final TagKey<Block> WARPED_NEWTS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "warped_newts_spawn_on");
    public static final TagKey<Block> EMBER_TORTOISES_SPAWN_ON = create(ClutterBestiary.MOD_ID, "ember_tortoises_spawn_on");
    public static final TagKey<Block> ECHOFINS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "echofins_spawn_on");
    public static final TagKey<Block> BUTTERFLIES_SPAWN_ON = create(ClutterBestiary.MOD_ID, "butterflies_spawn_on");
    public static final TagKey<Block> SEAHORSES_SPAWN_ON = create(ClutterBestiary.MOD_ID, "seahorses_spawn_on");
    public static final TagKey<Block> MANTA_RAYS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "manta_rays_spawn_on");
    public static final TagKey<Block> JELLYFISHES_SPAWN_ON = create(ClutterBestiary.MOD_ID, "jellyfishes_spawn_on");
    public static final TagKey<Block> CAPYBARAS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "capybaras_spawn_on");
    public static final TagKey<Block> BEAVERS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "beavers_spawn_on");
    public static final TagKey<Block> EMPEROR_PENGUINS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "emperor_penguins_spawn_on");
    public static final TagKey<Block> KIWIS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "kiwis_spawn_on");
    public static final TagKey<Block> CHAMELEONS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "chameleons_spawn_on");
    public static final TagKey<Block> MOSSBLOOMS_SPAWN_ON = create(ClutterBestiary.MOD_ID, "mossblooms_spawn_on");

    private static TagKey<Block> create(String namespace, String path) {
        return TagKey.of(Registries.BLOCK.getKey(), new Identifier(namespace, path));
    }
}
