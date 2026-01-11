package net.emilsg.clutterbestiary.util;

import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.*;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModUtil {

    public static final Map<Item, Item> STRIPPED_ITEM_MAP = new HashMap<>();
    public static final Set<Item> SAPLING_ITEM_MAP = new HashSet<>();

    public static void buildItemMapsAndLists() {
        STRIPPED_ITEM_MAP.clear();
        SAPLING_ITEM_MAP.clear();
        for (Item item : Registries.ITEM) {
            Identifier id = Registries.ITEM.getId(item);
            String path = id.getPath();

            if (path.contains("sapling")) SAPLING_ITEM_MAP.add(item);
            if (path.startsWith("stripped_")) continue;

            Identifier strippedId = Identifier.of(id.getNamespace(), "stripped_" + path);
            if (Registries.ITEM.containsId(strippedId)) STRIPPED_ITEM_MAP.put(item, Registries.ITEM.get(strippedId));
        }
    }

    public static float lerp(float a, float b, float alpha) {
        return a + (b - a) * alpha;
    }


    @Nullable
    public static Item getStrippedItem(ItemStack stack) {
        return STRIPPED_ITEM_MAP.get(stack.getItem());
    }

    public static Text buildCyclicFormattedName(String translationKey, int[] colorCycle, int tickOffset, boolean reverse) {
        MutableText finalText = Text.literal("");
        String translated = Text.translatable(translationKey).getString();

        int cycleLength = colorCycle.length;

        for (int i = 0; i < translated.length(); i++) {
            int colorIndex;

            if (reverse) {
                colorIndex = (i - tickOffset) % cycleLength;
            } else {
                colorIndex = (i + tickOffset) % cycleLength;
            }

            if (colorIndex < 0) {
                colorIndex += cycleLength;
            }

            int rgb = colorCycle[colorIndex];
            finalText.append(Text.literal(String.valueOf(translated.charAt(i)))
                    .styled(style -> style.withColor(rgb)));
        }

        return finalText;
    }

    public static void grantImpossibleAdvancement(String path, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ModAdvancements.grant(serverPlayer, path);
        }
    }

    public static boolean inEitherHand(PlayerEntity player, Item item) {
        return player.getStackInHand(Hand.MAIN_HAND).isOf(item) || player.getStackInHand(Hand.OFF_HAND).isOf(item);
    }

    public static boolean inBothHands(PlayerEntity player, Item item) {
        return player.getStackInHand(Hand.MAIN_HAND).isOf(item) && player.getStackInHand(Hand.OFF_HAND).isOf(item);
    }

    public static void registerSpawnRestrictions() {
        SpawnPlacementsRegistry.register(ModEntityTypes.DRAGONFLY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DragonflyEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.MOSSBLOOM, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MossbloomEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.CHAMELEON, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChameleonEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.KIWI_BIRD, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, KiwiBirdEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.EMPEROR_PENGUIN, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EmperorPenguinEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.BEAVER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BeaverEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.CAPYBARA, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CapybaraEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.BOOPLET, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BoopletEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.POTION_WASP, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PotionWaspEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.COATI, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CoatiEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.RIVER_TURTLE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RiverTurtleEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.RED_PANDA, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RedPandaEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.STOAT, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StoatEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.CROCODILE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CrocodileEntity::isValidNaturalSpawn);
          SpawnPlacementsRegistry.register(ModEntityTypes.CHORUS_BEETLE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChorusBeetleEntity::isValidNaturalSpawn);
          SpawnPlacementsRegistry.register(ModEntityTypes.WOODPECKER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WoodpeckerEntity::isValidNaturalSpawn);

        SpawnPlacementsRegistry.register(ModEntityTypes.JELLYFISH, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, JellyfishEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.SEAHORSE, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, SeahorseEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.MANTA_RAY, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, MantaRayEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.KOI, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, KoiEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.ARROWFISH, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, ArrowfishEntity::isValidNaturalSpawn);

        SpawnPlacementsRegistry.register(ModEntityTypes.BUTTERFLY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ButterflyEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.CRIMSON_NEWT, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CrimsonNewtEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.WARPED_NEWT, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WarpedNewtEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.EMBER_TORTOISE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EmberTortoiseEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.ECHOFIN, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EchofinEntity::isValidNaturalSpawn);
    }


}
