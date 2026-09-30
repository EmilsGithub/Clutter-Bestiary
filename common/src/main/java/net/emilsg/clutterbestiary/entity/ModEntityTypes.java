package net.emilsg.clutterbestiary.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import java.util.function.Supplier;

public class ModEntityTypes {

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ClutterBestiary.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<ButterflyEntity>> BUTTERFLY = registerEntityType("butterfly", () -> EntityType.Builder.of(ButterflyEntity::new, MobCategory.CREATURE).sized(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<ButterflyLarvaEntity>> BUTTERFLY_LARVA = registerEntityType("butterfly_larva", () -> EntityType.Builder.of(ButterflyLarvaEntity::new, MobCategory.CREATURE).sized(0.35f, 0.25f));
    public static final RegistrySupplier<EntityType<ChameleonEntity>> CHAMELEON = registerEntityType("chameleon", () -> EntityType.Builder.of(ChameleonEntity::new, MobCategory.CREATURE).sized(0.6f, 0.45f));
    public static final RegistrySupplier<EntityType<EchofinEntity>> ECHOFIN = registerEntityType("echofin", () -> EntityType.Builder.of(EchofinEntity::new, MobCategory.AMBIENT).sized(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<MossbloomEntity>> MOSSBLOOM = registerEntityType("mossbloom", () -> EntityType.Builder.of(MossbloomEntity::new, MobCategory.AMBIENT).sized(0.9f, 1.15f));
    public static final RegistrySupplier<EntityType<KiwiBirdEntity>> KIWI_BIRD = registerEntityType("kiwi_bird", () -> EntityType.Builder.of(KiwiBirdEntity::new, MobCategory.CREATURE).sized(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<EmperorPenguinEntity>> EMPEROR_PENGUIN = registerEntityType("emperor_penguin", () -> EntityType.Builder.of(EmperorPenguinEntity::new, MobCategory.CREATURE).sized(0.75f, 1.35f));
    public static final RegistrySupplier<EntityType<BeaverEntity>> BEAVER = registerEntityType("beaver", () -> EntityType.Builder.of(BeaverEntity::new, MobCategory.CREATURE).sized(0.9f, 0.65f));
    public static final RegistrySupplier<EntityType<CapybaraEntity>> CAPYBARA = registerEntityType("capybara", () -> EntityType.Builder.of(CapybaraEntity::new, MobCategory.CREATURE).sized(0.7f, 0.8f));
    public static final RegistrySupplier<EntityType<CrimsonNewtEntity>> CRIMSON_NEWT = registerEntityType("crimson_newt", () -> EntityType.Builder.of(CrimsonNewtEntity::new, MobCategory.CREATURE).sized(0.75f, 0.75f).eyeHeight(0.4F).fireImmune());
    public static final RegistrySupplier<EntityType<WarpedNewtEntity>> WARPED_NEWT = registerEntityType("warped_newt", () -> EntityType.Builder.of(WarpedNewtEntity::new, MobCategory.CREATURE).sized(0.75f, 0.75f).eyeHeight(0.4F).fireImmune());
    public static final RegistrySupplier<EntityType<EmberTortoiseEntity>> EMBER_TORTOISE = registerEntityType("ember_tortoise", () -> EntityType.Builder.of(EmberTortoiseEntity::new, MobCategory.CREATURE).sized(1.5f, 1.45f).fireImmune());
    public static final RegistrySupplier<EntityType<JellyfishEntity>> JELLYFISH = registerEntityType("jellyfish", () -> EntityType.Builder.of(JellyfishEntity::new, MobCategory.WATER_AMBIENT).sized(0.5f, 0.5f));
    public static final RegistrySupplier<EntityType<MantaRayEntity>> MANTA_RAY = registerEntityType("manta_ray", () -> EntityType.Builder.of(MantaRayEntity::new, MobCategory.WATER_CREATURE).sized(1f, 0.5f));
    public static final RegistrySupplier<EntityType<SeahorseEntity>> SEAHORSE = registerEntityType("seahorse", () -> EntityType.Builder.of(SeahorseEntity::new, MobCategory.WATER_AMBIENT).sized(0.4f, 0.5f));
    public static final RegistrySupplier<EntityType<PotionWaspEntity>> POTION_WASP = registerEntityType("potion_wasp", () -> EntityType.Builder.of(PotionWaspEntity::new, MobCategory.CREATURE).sized(0.75f, 0.75f));
    public static final RegistrySupplier<EntityType<PotionSacEntity>> POTION_SAC = registerEntityType("potion_sac", () -> EntityType.Builder.of(PotionSacEntity::new, MobCategory.MISC).sized(0.75f, 0.75f));
    public static final RegistrySupplier<EntityType<DragonflyEntity>> DRAGONFLY = registerEntityType("dragonfly", () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.CREATURE).sized(0.65f, 0.4f));
    public static final RegistrySupplier<EntityType<BoopletEntity>> BOOPLET = registerEntityType("booplet", () -> EntityType.Builder.of(BoopletEntity::new, MobCategory.CREATURE).sized(0.6f, 0.6f));
    public static final RegistrySupplier<EntityType<KoiEntity>> KOI = registerEntityType("koi", () -> EntityType.Builder.of(KoiEntity::new, MobCategory.WATER_AMBIENT).sized(0.7f, 0.5f));
    public static final RegistrySupplier<EntityType<KoiEggsEntity>> KOI_EGGS = registerEntityType("koi_eggs", () -> EntityType.Builder.of(KoiEggsEntity::new, MobCategory.MISC).sized(0.45f, 0.45f));
    public static final RegistrySupplier<EntityType<RiverTurtleEntity>> RIVER_TURTLE = registerEntityType("river_turtle", () -> EntityType.Builder.of(RiverTurtleEntity::new, MobCategory.CREATURE).sized(0.65f, 0.4f));
    public static final RegistrySupplier<EntityType<CoatiEntity>> COATI = registerEntityType("coati", () -> EntityType.Builder.of(CoatiEntity::new, MobCategory.CREATURE).sized(0.8f, 0.7f));
    public static final RegistrySupplier<EntityType<RedPandaEntity>> RED_PANDA = registerEntityType("red_panda", () -> EntityType.Builder.of(RedPandaEntity::new, MobCategory.CREATURE).sized(0.75f, 0.65f));
    public static final RegistrySupplier<EntityType<StoatEntity>> STOAT = registerEntityType("stoat", () -> EntityType.Builder.of(StoatEntity::new, MobCategory.CREATURE).sized(0.65f, 0.5f));
    public static final RegistrySupplier<EntityType<CrocodileEntity>> CROCODILE = registerEntityType("crocodile", () -> EntityType.Builder.of(CrocodileEntity::new, MobCategory.CREATURE).sized(1.5f, 0.65f));
    public static final RegistrySupplier<EntityType<ChorusBeetleEntity>> CHORUS_BEETLE = registerEntityType("chorus_beetle", () -> EntityType.Builder.of(ChorusBeetleEntity::new, MobCategory.CREATURE).sized(0.5f, 0.4f));
    public static final RegistrySupplier<EntityType<WoodpeckerEntity>> WOODPECKER = registerEntityType("woodpecker", () -> EntityType.Builder.of(WoodpeckerEntity::new, MobCategory.CREATURE).sized(0.5f, 0.7f));
    public static final RegistrySupplier<EntityType<ArrowfishEntity>> ARROWFISH = registerEntityType("arrowfish", () -> EntityType.Builder.of(ArrowfishEntity::new, MobCategory.WATER_AMBIENT).sized(0.7f, 0.5f));
    public static final RegistrySupplier<EntityType<ArrowfishProjectileEntity>> ARROWFISH_PROJECTILE = registerEntityType("arrowfish_projectile", () -> EntityType.Builder.<ArrowfishProjectileEntity>of(ArrowfishProjectileEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).eyeHeight(0.13f).clientTrackingRange(4).updateInterval(1));

    private static <T extends Entity> RegistrySupplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType.Builder<T>> builder) {
        Identifier id = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, name);
        // Entity type builders are keyed by their registry key on every loader.
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        return ENTITIES.register(id, () -> builder.get().build(key));
    }

    public static void register() {
        ENTITIES.register();
    }

}
