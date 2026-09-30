package net.emilsg.clutterbestiary.util;

import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModUtil {

    public static final Map<Item, Item> STRIPPED_ITEM_MAP = new HashMap<>();
    public static final Set<Item> SAPLING_ITEM_MAP = new HashSet<>();

    public static void buildItemMapsAndLists() {
        STRIPPED_ITEM_MAP.clear();
        SAPLING_ITEM_MAP.clear();
        for (Item item : BuiltInRegistries.ITEM) {
            Identifier id = BuiltInRegistries.ITEM.getKey(item);
            String path = id.getPath();

            if (path.contains("sapling")) SAPLING_ITEM_MAP.add(item);
            if (path.startsWith("stripped_")) continue;

            Identifier strippedId = Identifier.fromNamespaceAndPath(id.getNamespace(), "stripped_" + path);
            if (BuiltInRegistries.ITEM.containsKey(strippedId)) STRIPPED_ITEM_MAP.put(item, BuiltInRegistries.ITEM.getValue(strippedId));
        }
    }

    /**
     * Reads a field from item component data without logging when the field is absent.
     */
    public static <T> Optional<T> readComponentData(CustomData data, MapCodec<T> codec) {
        CompoundTag tag = data.copyTag();
        return codec.decode(NbtOps.INSTANCE, NbtOps.INSTANCE.getMap(tag).getOrThrow()).result();
    }

    public static float lerp(float a, float b, float alpha) {
        return a + (b - a) * alpha;
    }


    @Nullable
    public static Item getStrippedItem(ItemStack stack) {
        return STRIPPED_ITEM_MAP.get(stack.getItem());
    }

    public static Component buildCyclicFormattedName(String translationKey, int[] colorCycle, int tickOffset, boolean reverse) {
        MutableComponent finalText = Component.literal("");
        String translated = Component.translatable(translationKey).getString();

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
            finalText.append(Component.literal(String.valueOf(translated.charAt(i)))
                    .withStyle(style -> style.withColor(rgb)));
        }

        return finalText;
    }

    public static void grantImpossibleAdvancement(String path, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ModAdvancements.grant(serverPlayer, path);
        }
    }

    public static boolean inEitherHand(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item) || player.getItemInHand(InteractionHand.OFF_HAND).is(item);
    }

    public static boolean inBothHands(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item) && player.getItemInHand(InteractionHand.OFF_HAND).is(item);
    }

    public static void registerSpawnRestrictions() {
        SpawnPlacementsRegistry.register(ModEntityTypes.DRAGONFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DragonflyEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.MOSSBLOOM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MossbloomEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.CHAMELEON, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChameleonEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.KIWI_BIRD, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, KiwiBirdEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.EMPEROR_PENGUIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EmperorPenguinEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.BEAVER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeaverEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.CAPYBARA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CapybaraEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.BOOPLET, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BoopletEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.POTION_WASP, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PotionWaspEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.COATI, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoatiEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.RIVER_TURTLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RiverTurtleEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.RED_PANDA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RedPandaEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.STOAT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoatEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.CROCODILE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CrocodileEntity::checkAnimalSpawnRules);
          SpawnPlacementsRegistry.register(ModEntityTypes.CHORUS_BEETLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChorusBeetleEntity::checkAnimalSpawnRules);
          SpawnPlacementsRegistry.register(ModEntityTypes.WOODPECKER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WoodpeckerEntity::checkAnimalSpawnRules);

        SpawnPlacementsRegistry.register(ModEntityTypes.JELLYFISH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.OCEAN_FLOOR, JellyfishEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.SEAHORSE, SpawnPlacementTypes.IN_WATER, Heightmap.Types.OCEAN_FLOOR, SeahorseEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.MANTA_RAY, SpawnPlacementTypes.IN_WATER, Heightmap.Types.OCEAN_FLOOR, MantaRayEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.KOI, SpawnPlacementTypes.IN_WATER, Heightmap.Types.OCEAN_FLOOR, KoiEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.ARROWFISH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.OCEAN_FLOOR, ArrowfishEntity::isValidNaturalSpawn);

        SpawnPlacementsRegistry.register(ModEntityTypes.BUTTERFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ButterflyEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.CRIMSON_NEWT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CrimsonNewtEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.WARPED_NEWT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WarpedNewtEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.EMBER_TORTOISE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EmberTortoiseEntity::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.ECHOFIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EchofinEntity::checkAnimalSpawnRules);
    }


}
