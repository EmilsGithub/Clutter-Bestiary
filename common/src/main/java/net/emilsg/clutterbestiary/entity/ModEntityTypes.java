package net.emilsg.clutterbestiary.entity;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.custom.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ModEntityTypes {

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ClutterBestiary.MOD_ID, RegistryKeys.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<ButterflyEntity>> BUTTERFLY = registerEntityType("butterfly", () -> EntityType.Builder.create(ButterflyEntity::new, SpawnGroup.CREATURE).dimensions(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<ButterflyLarvaEntity>> BUTTERFLY_LARVA = registerEntityType("butterfly_larva", () -> EntityType.Builder.create(ButterflyLarvaEntity::new, SpawnGroup.CREATURE).dimensions(0.35f, 0.25f));
    public static final RegistrySupplier<EntityType<ChameleonEntity>> CHAMELEON = registerEntityType("chameleon", () -> EntityType.Builder.create(ChameleonEntity::new, SpawnGroup.CREATURE).dimensions(0.6f, 0.45f));
    public static final RegistrySupplier<EntityType<EchofinEntity>> ECHOFIN = registerEntityType("echofin", () -> EntityType.Builder.create(EchofinEntity::new, SpawnGroup.AMBIENT).dimensions(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<MossbloomEntity>> MOSSBLOOM = registerEntityType("mossbloom", () -> EntityType.Builder.create(MossbloomEntity::new, SpawnGroup.AMBIENT).dimensions(0.9f, 1.15f));
    public static final RegistrySupplier<EntityType<KiwiBirdEntity>> KIWI_BIRD = registerEntityType("kiwi_bird", () -> EntityType.Builder.create(KiwiBirdEntity::new, SpawnGroup.CREATURE).dimensions(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<EmperorPenguinEntity>> EMPEROR_PENGUIN = registerEntityType("emperor_penguin", () -> EntityType.Builder.create(EmperorPenguinEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 1.35f));
    public static final RegistrySupplier<EntityType<BeaverEntity>> BEAVER = registerEntityType("beaver", () -> EntityType.Builder.create(BeaverEntity::new, SpawnGroup.CREATURE).dimensions(0.9f, 0.65f));
    public static final RegistrySupplier<EntityType<CapybaraEntity>> CAPYBARA = registerEntityType("capybara", () -> EntityType.Builder.create(CapybaraEntity::new, SpawnGroup.CREATURE).dimensions(0.7f, 0.8f));
    public static final RegistrySupplier<EntityType<CrimsonNewtEntity>> CRIMSON_NEWT = registerEntityType("crimson_newt", () -> EntityType.Builder.create(CrimsonNewtEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.75f).eyeHeight(0.4F).makeFireImmune());
    public static final RegistrySupplier<EntityType<WarpedNewtEntity>> WARPED_NEWT = registerEntityType("warped_newt", () -> EntityType.Builder.create(WarpedNewtEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.75f).eyeHeight(0.4F).makeFireImmune());
    public static final RegistrySupplier<EntityType<EmberTortoiseEntity>> EMBER_TORTOISE = registerEntityType("ember_tortoise", () -> EntityType.Builder.create(EmberTortoiseEntity::new, SpawnGroup.CREATURE).dimensions(1.5f, 1.45f).makeFireImmune());
    public static final RegistrySupplier<EntityType<JellyfishEntity>> JELLYFISH = registerEntityType("jellyfish", () -> EntityType.Builder.create(JellyfishEntity::new, SpawnGroup.WATER_AMBIENT).dimensions(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<MantaRayEntity>> MANTA_RAY = registerEntityType("manta_ray", () -> EntityType.Builder.create(MantaRayEntity::new, SpawnGroup.WATER_CREATURE).dimensions(1f, 0.5f));
    public static final RegistrySupplier<EntityType<SeahorseEntity>> SEAHORSE = registerEntityType("seahorse", () -> EntityType.Builder.create(SeahorseEntity::new, SpawnGroup.WATER_AMBIENT).dimensions(0.4f, 0.5f));
    public static final RegistrySupplier<EntityType<PotionWaspEntity>> POTION_WASP = registerEntityType("potion_wasp", () -> EntityType.Builder.create(PotionWaspEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.75f));
    public static final RegistrySupplier<EntityType<PotionSacEntity>> POTION_SAC = registerEntityType("potion_sac", () -> EntityType.Builder.create(PotionSacEntity::new, SpawnGroup.MISC).dimensions(0.75f, 0.75f));
    public static final RegistrySupplier<EntityType<DragonflyEntity>> DRAGONFLY = registerEntityType("dragonfly", () -> EntityType.Builder.create(DragonflyEntity::new, SpawnGroup.CREATURE).dimensions(0.65f, 0.4f));
    public static final RegistrySupplier<EntityType<BoopletEntity>> BOOPLET = registerEntityType("booplet", () -> EntityType.Builder.create(BoopletEntity::new, SpawnGroup.CREATURE).dimensions(0.6f, 0.6f));
    public static final RegistrySupplier<EntityType<KoiEntity>> KOI = registerEntityType("koi", () -> EntityType.Builder.create(KoiEntity::new, SpawnGroup.WATER_AMBIENT).dimensions(0.7f, 0.5f));
    public static final RegistrySupplier<EntityType<KoiEggsEntity>> KOI_EGGS = registerEntityType("koi_eggs", () -> EntityType.Builder.create(KoiEggsEntity::new, SpawnGroup.MISC).dimensions(0.45f, 0.45f));
    public static final RegistrySupplier<EntityType<RiverTurtleEntity>> RIVER_TURTLE = registerEntityType("river_turtle", () -> EntityType.Builder.create(RiverTurtleEntity::new, SpawnGroup.CREATURE).dimensions(0.65f, 0.4f));
    public static final RegistrySupplier<EntityType<CoatiEntity>> COATI = registerEntityType("coati", () -> EntityType.Builder.create(CoatiEntity::new, SpawnGroup.CREATURE).dimensions(0.8f, 0.7f));
    public static final RegistrySupplier<EntityType<RedPandaEntity>> RED_PANDA = registerEntityType("red_panda", () -> EntityType.Builder.create(RedPandaEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.65f));
    public static final RegistrySupplier<EntityType<StoatEntity>> STOAT = registerEntityType("stoat", () -> EntityType.Builder.create(StoatEntity::new, SpawnGroup.CREATURE).dimensions(0.65f, 0.5f));
    public static final RegistrySupplier<EntityType<CrocodileEntity>> CROCODILE = registerEntityType("crocodile", () -> EntityType.Builder.create(CrocodileEntity::new, SpawnGroup.CREATURE).dimensions(1.5f, 0.65f));
    public static final RegistrySupplier<EntityType<ChorusBeetleEntity>> CHORUS_BEETLE = registerEntityType("chorus_beetle", () -> EntityType.Builder.create(ChorusBeetleEntity::new, SpawnGroup.CREATURE).dimensions(0.5f, 0.4f));
    public static final RegistrySupplier<EntityType<WoodpeckerEntity>> WOODPECKER = registerEntityType("woodpecker", () -> EntityType.Builder.create(WoodpeckerEntity::new, SpawnGroup.CREATURE).dimensions(0.5f, 0.7f));
    public static final RegistrySupplier<EntityType<ArrowfishEntity>> ARROWFISH = registerEntityType("arrowfish", () -> EntityType.Builder.create(ArrowfishEntity::new, SpawnGroup.WATER_AMBIENT).dimensions(0.7f, 0.5f));
    public static final RegistrySupplier<EntityType<ArrowfishProjectileEntity>> ARROWFISH_PROJECTILE = registerEntityType("arrowfish_projectile", () -> EntityType.Builder.<ArrowfishProjectileEntity>create(ArrowfishProjectileEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).eyeHeight(0.13f).maxTrackingRange(4).trackingTickInterval(1));

    private static <T extends Entity> RegistrySupplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType.Builder<T>> builder) {
        Identifier id = Identifier.of(ClutterBestiary.MOD_ID, name);
        // Fabric skips the vanilla data-fixer lookup for null IDs; NeoForge expects a namespaced ID.
        return ENTITIES.register(id, () -> builder.get().build(Platform.isFabric() ? null : id.toString()));
    }

    public static void register() {
        ENTITIES.register();
    }

}
