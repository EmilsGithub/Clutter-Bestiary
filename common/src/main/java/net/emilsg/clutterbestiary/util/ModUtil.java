package net.emilsg.clutterbestiary.util;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.*;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;

import java.util.List;

public class ModUtil {

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

    public static void grantImpossibleAdvancement(String path, ServerWorld world, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            MinecraftServer server = world.getServer();
            AdvancementEntry advancement = server.getAdvancementLoader().get(Identifier.of(ClutterBestiary.MOD_ID, path));

            if (serverPlayer.getAdvancementTracker().getProgress(advancement).isDone()) return;

            if (advancement != null) {
                AdvancementProgress progress = serverPlayer.getAdvancementTracker().getProgress(advancement);
                if (!progress.isDone()) {
                    for (String criterion : progress.getUnobtainedCriteria()) {
                        serverPlayer.getAdvancementTracker().grantCriterion(advancement, criterion);
                    }
                }
            }
        }
    }

    //Only works on Fabric side, hopefully fixed in the future.
    public static void registerSpawns() {
        BiomeModifications.addProperties(ctx -> ctx.hasTag(ModBiomeTags.SPAWNS_CHAMELEONS), (ctx, props) ->
                props.getSpawnProperties().addSpawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntityTypes.CHAMELEON.get(), 100, 2, 4)));
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

        SpawnPlacementsRegistry.register(ModEntityTypes.JELLYFISH, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, JellyfishEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.SEAHORSE, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, SeahorseEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.MANTA_RAY, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, MantaRayEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.KOI, SpawnLocationTypes.IN_WATER, Heightmap.Type.OCEAN_FLOOR, KoiEntity::isValidNaturalSpawn);

        SpawnPlacementsRegistry.register(ModEntityTypes.BUTTERFLY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ButterflyEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.CRIMSON_NEWT, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CrimsonNewtEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.WARPED_NEWT, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WarpedNewtEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.EMBER_TORTOISE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EmberTortoiseEntity::isValidNaturalSpawn);
        SpawnPlacementsRegistry.register(ModEntityTypes.ECHOFIN, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EchofinEntity::isValidNaturalSpawn);

    }


}
