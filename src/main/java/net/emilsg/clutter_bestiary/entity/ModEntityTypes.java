package net.emilsg.clutter_bestiary.entity;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.custom.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityTypes {

    public static final EntityType<ButterflyEntity> BUTTERFLY = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "butterfly"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ButterflyEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static final EntityType<ChameleonEntity> CHAMELEON = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "chameleon"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ChameleonEntity::new).dimensions(EntityDimensions.changing(0.75f, 0.5f)).build());

    public static final EntityType<EchofinEntity> ECHOFIN = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "echofin"),
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, EchofinEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static final EntityType<MossbloomEntity> MOSSBLOOM = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "mossbloom"),
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, MossbloomEntity::new).dimensions(EntityDimensions.changing(0.9f, 1.15f)).build());

    public static final EntityType<KiwiBirdEntity> KIWI_BIRD = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "kiwi_bird"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, KiwiBirdEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static final EntityType<EmperorPenguinEntity> EMPEROR_PENGUIN = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "emperor_penguin"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EmperorPenguinEntity::new).dimensions(EntityDimensions.changing(0.75f, 1.35f)).build());

    public static final EntityType<BeaverEntity> BEAVER = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "beaver"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BeaverEntity::new).dimensions(EntityDimensions.changing(0.9f, 0.65f)).build());

    public static final EntityType<CapybaraEntity> CAPYBARA = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "capybara"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CapybaraEntity::new).dimensions(EntityDimensions.changing(0.7f, 0.8f)).build());

    public static final EntityType<CrimsonNewtEntity> CRIMSON_NEWT = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "crimson_newt"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CrimsonNewtEntity::new).dimensions(EntityDimensions.changing(0.75f, 0.75f)).fireImmune().build());

    public static final EntityType<WarpedNewtEntity> WARPED_NEWT = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "warped_newt"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WarpedNewtEntity::new).dimensions(EntityDimensions.changing(0.75f, 0.75f)).fireImmune().build());

    public static final EntityType<EmberTortoiseEntity> EMBER_TORTOISE = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "ember_tortoise"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EmberTortoiseEntity::new).dimensions(EntityDimensions.changing(1.5f, 1.45f)).fireImmune().build());

    public static final EntityType<JellyfishEntity> JELLYFISH = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "jellyfish"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_AMBIENT, JellyfishEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static final EntityType<MantaRayEntity> MANTA_RAY = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "manta_ray"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, MantaRayEntity::new).dimensions(EntityDimensions.changing(1f, 0.5f)).build());

    public static final EntityType<SeahorseEntity> SEAHORSE = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "seahorse"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_AMBIENT, SeahorseEntity::new).dimensions(EntityDimensions.changing(0.4f, 0.5f)).build());

    public static final EntityType<PotionWaspEntity> POTION_WASP = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "potion_wasp"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PotionWaspEntity::new).dimensions(EntityDimensions.changing(0.75f, 0.75f)).build());

    public static final EntityType<PotionSacEntity> POTION_SAC = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "potion_sac"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PotionSacEntity::new).dimensions(EntityDimensions.changing(0.75f, 0.75f)).build());

    public static final EntityType<DragonflyEntity> DRAGONFLY = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "dragonfly"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DragonflyEntity::new).dimensions(EntityDimensions.changing(0.65f, 0.4f)).build());

    public static final EntityType<BoopletEntity> BOOPLET = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "booplet"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BoopletEntity::new).dimensions(EntityDimensions.changing(0.6f, 0.6f)).build());

    public static final EntityType<KoiEntity> KOI = Registry.register(Registries.ENTITY_TYPE, new Identifier(ClutterBestiary.MOD_ID, "koi"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_AMBIENT, KoiEntity::new).dimensions(EntityDimensions.changing(0.7f, 0.5f)).build());

    public static void register() {
    }

}
