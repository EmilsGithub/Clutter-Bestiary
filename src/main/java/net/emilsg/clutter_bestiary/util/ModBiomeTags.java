package net.emilsg.clutter_bestiary.util;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModBiomeTags {

    public static final TagKey<Biome> SPAWNS_CRIMSON_NEWTS = create(ClutterBestiary.MOD_ID, "spawns_crimson_newts");
    public static final TagKey<Biome> SPAWNS_WARPED_NEWTS = create(ClutterBestiary.MOD_ID, "spawns_warped_newts");
    public static final TagKey<Biome> SPAWNS_EMBER_TORTOISES = create(ClutterBestiary.MOD_ID, "spawns_ember_tortoises");
    public static final TagKey<Biome> SPAWNS_ECHOFINS = create(ClutterBestiary.MOD_ID, "spawns_echofins");
    public static final TagKey<Biome> SPAWNS_SEAHORSES = create(ClutterBestiary.MOD_ID, "spawns_seahorses");
    public static final TagKey<Biome> SPAWNS_MANTA_RAYS = create(ClutterBestiary.MOD_ID, "spawns_manta_rays");
    public static final TagKey<Biome> SPAWNS_JELLYFISHES = create(ClutterBestiary.MOD_ID, "spawns_jellyfishes");
    public static final TagKey<Biome> SPAWNS_BUTTERFLIES = create(ClutterBestiary.MOD_ID, "spawns_butterflies");
    public static final TagKey<Biome> SPAWNS_CAPYBARAS = create(ClutterBestiary.MOD_ID, "spawns_capybaras");
    public static final TagKey<Biome> SPAWNS_BEAVERS = create(ClutterBestiary.MOD_ID, "spawns_beavers");
    public static final TagKey<Biome> SPAWNS_EMPEROR_PENGUINS = create(ClutterBestiary.MOD_ID, "spawns_emperor_penguins");
    public static final TagKey<Biome> SPAWNS_KIWIS = create(ClutterBestiary.MOD_ID, "spawns_kiwis");
    public static final TagKey<Biome> SPAWNS_CHAMELEONS = create(ClutterBestiary.MOD_ID, "spawns_chameleons");
    public static final TagKey<Biome> SPAWNS_MOSSBLOOMS = create(ClutterBestiary.MOD_ID, "spawns_mossblooms");

    private static TagKey<Biome> create(String namespace, String path) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(namespace, path));
    }
}
