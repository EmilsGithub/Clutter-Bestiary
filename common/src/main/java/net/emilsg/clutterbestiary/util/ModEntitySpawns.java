package net.emilsg.clutterbestiary.util;

import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import java.util.List;

public final class ModEntitySpawns {
    public static final List<SpawnDefinition> DEFINITIONS = List.of(
            new SpawnDefinition("echofin", ModBiomeTags.SPAWNS_ECHOFINS, MobCategory.AMBIENT, ModEntityTypes.ECHOFIN),
            new SpawnDefinition("crimson_newt", ModBiomeTags.SPAWNS_CRIMSON_NEWTS, MobCategory.CREATURE, ModEntityTypes.CRIMSON_NEWT),
            new SpawnDefinition("warped_newt", ModBiomeTags.SPAWNS_WARPED_NEWTS, MobCategory.CREATURE, ModEntityTypes.WARPED_NEWT),
            new SpawnDefinition("ember_tortoise", ModBiomeTags.SPAWNS_EMBER_TORTOISES, MobCategory.CREATURE, ModEntityTypes.EMBER_TORTOISE),
            new SpawnDefinition("butterfly", ModBiomeTags.SPAWNS_BUTTERFLIES, MobCategory.CREATURE, ModEntityTypes.BUTTERFLY),
            new SpawnDefinition("chameleon", ModBiomeTags.SPAWNS_CHAMELEONS, MobCategory.CREATURE, ModEntityTypes.CHAMELEON),
            new SpawnDefinition("mossbloom", ModBiomeTags.SPAWNS_MOSSBLOOMS, MobCategory.AMBIENT, ModEntityTypes.MOSSBLOOM),
            new SpawnDefinition("kiwi", ModBiomeTags.SPAWNS_KIWIS, MobCategory.CREATURE, ModEntityTypes.KIWI_BIRD),
            new SpawnDefinition("emperor_penguin", ModBiomeTags.SPAWNS_EMPEROR_PENGUINS, MobCategory.CREATURE, ModEntityTypes.EMPEROR_PENGUIN),
            new SpawnDefinition("beaver", ModBiomeTags.SPAWNS_BEAVERS, MobCategory.CREATURE, ModEntityTypes.BEAVER),
            new SpawnDefinition("capybara", ModBiomeTags.SPAWNS_CAPYBARAS, MobCategory.CREATURE, ModEntityTypes.CAPYBARA),
            new SpawnDefinition("jellyfish", ModBiomeTags.SPAWNS_JELLYFISHES, MobCategory.WATER_AMBIENT, ModEntityTypes.JELLYFISH),
            new SpawnDefinition("seahorse", ModBiomeTags.SPAWNS_SEAHORSES, MobCategory.WATER_AMBIENT, ModEntityTypes.SEAHORSE),
            new SpawnDefinition("manta_ray", ModBiomeTags.SPAWNS_MANTA_RAYS, MobCategory.WATER_CREATURE, ModEntityTypes.MANTA_RAY),
            new SpawnDefinition("dragonfly", ModBiomeTags.SPAWNS_DRAGONFLIES, MobCategory.CREATURE, ModEntityTypes.DRAGONFLY),
            new SpawnDefinition("koi", ModBiomeTags.SPAWNS_KOI, MobCategory.WATER_AMBIENT, ModEntityTypes.KOI),
            new SpawnDefinition("booplet", ModBiomeTags.SPAWNS_BOOPLETS, MobCategory.CREATURE, ModEntityTypes.BOOPLET),
            new SpawnDefinition("potion_wasp", ModBiomeTags.SPAWNS_POTION_WASPS, MobCategory.CREATURE, ModEntityTypes.POTION_WASP),
            new SpawnDefinition("river_turtle", ModBiomeTags.SPAWNS_RIVER_TURTLES, MobCategory.CREATURE, ModEntityTypes.RIVER_TURTLE),
            new SpawnDefinition("coati", ModBiomeTags.SPAWNS_COATIS, MobCategory.CREATURE, ModEntityTypes.COATI),
            new SpawnDefinition("red_panda", ModBiomeTags.SPAWNS_RED_PANDAS, MobCategory.CREATURE, ModEntityTypes.RED_PANDA),
            new SpawnDefinition("stoat", ModBiomeTags.SPAWNS_STOATS, MobCategory.CREATURE, ModEntityTypes.STOAT),
            new SpawnDefinition("crocodile", ModBiomeTags.SPAWNS_CROCODILES, MobCategory.CREATURE, ModEntityTypes.CROCODILE),
            new SpawnDefinition("chorus_beetle", ModBiomeTags.SPAWNS_CHORUS_BEETLES, MobCategory.CREATURE, ModEntityTypes.CHORUS_BEETLE),
            new SpawnDefinition("woodpecker", ModBiomeTags.SPAWNS_WOODPECKERS, MobCategory.CREATURE, ModEntityTypes.WOODPECKER),
            new SpawnDefinition("arrowfish", ModBiomeTags.SPAWNS_ARROWFISH, MobCategory.WATER_AMBIENT, ModEntityTypes.ARROWFISH)
    );

    private ModEntitySpawns() {
    }

    public record SpawnDefinition(String configName, TagKey<Biome> biomeTag, MobCategory spawnGroup,
                                  RegistrySupplier<? extends EntityType<?>> entityType) {
        public boolean matches(Holder<Biome> biome) {
            return biome.is(this.biomeTag);
        }
    }
}
