package net.emilsg.clutterbestiary.entity;

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

    public static final RegistrySupplier<EntityType<ButterflyEntity>> BUTTERFLY = registerEntityType("butterfly", () -> EntityType.Builder.create(ButterflyEntity::new, SpawnGroup.CREATURE).dimensions(0.5f, 0.5f).build("butterfly"));
    public static final RegistrySupplier<EntityType<ChameleonEntity>> CHAMELEON = registerEntityType("chameleon", () -> EntityType.Builder.create(ChameleonEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.5f).build("chameleon"));
    public static final RegistrySupplier<EntityType<EchofinEntity>> ECHOFIN = registerEntityType("echofin", () -> EntityType.Builder.create(EchofinEntity::new, SpawnGroup.AMBIENT).dimensions(0.5f, 0.5f).build("echofin"));
    public static final RegistrySupplier<EntityType<MossbloomEntity>> MOSSBLOOM = registerEntityType("mossbloom", () -> EntityType.Builder.create(MossbloomEntity::new, SpawnGroup.AMBIENT).dimensions(0.9f, 1.15f).build("mossbloom"));
    public static final RegistrySupplier<EntityType<KiwiBirdEntity>> KIWI_BIRD = registerEntityType("kiwi_bird", () -> EntityType.Builder.create(KiwiBirdEntity::new, SpawnGroup.CREATURE).dimensions(0.5f, 0.5f).build("kiwi_bird"));
    public static final RegistrySupplier<EntityType<EmperorPenguinEntity>> EMPEROR_PENGUIN = registerEntityType("emperor_penguin", () -> EntityType.Builder.create(EmperorPenguinEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 1.35f).build("emperor_penguin"));
    public static final RegistrySupplier<EntityType<BeaverEntity>> BEAVER = registerEntityType("beaver", () -> EntityType.Builder.create(BeaverEntity::new, SpawnGroup.CREATURE).dimensions(0.9f, 0.65f).build("beaver"));
    public static final RegistrySupplier<EntityType<CapybaraEntity>> CAPYBARA = registerEntityType("capybara", () -> EntityType.Builder.create(CapybaraEntity::new, SpawnGroup.CREATURE).dimensions(0.7f, 0.8f).build("capybara"));
    public static final RegistrySupplier<EntityType<CrimsonNewtEntity>> CRIMSON_NEWT = registerEntityType("crimson_newt", () -> EntityType.Builder.create(CrimsonNewtEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.75f).makeFireImmune().build("crimson_newt"));
    public static final RegistrySupplier<EntityType<WarpedNewtEntity>> WARPED_NEWT = registerEntityType("warped_newt", () -> EntityType.Builder.create(WarpedNewtEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.75f).makeFireImmune().build("warped_newt"));
    public static final RegistrySupplier<EntityType<EmberTortoiseEntity>> EMBER_TORTOISE = registerEntityType("ember_tortoise", () -> EntityType.Builder.create(EmberTortoiseEntity::new, SpawnGroup.CREATURE).dimensions(1.5f, 1.45f).makeFireImmune().build("ember_tortoise"));
    public static final RegistrySupplier<EntityType<JellyfishEntity>> JELLYFISH = registerEntityType("jellyfish", () -> EntityType.Builder.create(JellyfishEntity::new, SpawnGroup.WATER_AMBIENT).dimensions(0.5f, 0.5f).build("jellyfish"));
    public static final RegistrySupplier<EntityType<MantaRayEntity>> MANTA_RAY = registerEntityType("manta_ray", () -> EntityType.Builder.create(MantaRayEntity::new, SpawnGroup.WATER_CREATURE).dimensions(1f, 0.5f).build("manta_ray"));
    public static final RegistrySupplier<EntityType<SeahorseEntity>> SEAHORSE = registerEntityType("seahorse", () -> EntityType.Builder.create(SeahorseEntity::new, SpawnGroup.WATER_AMBIENT).dimensions(0.4f, 0.5f).build("seahorse"));
    public static final RegistrySupplier<EntityType<PotionWaspEntity>> POTION_WASP = registerEntityType("potion_wasp", () -> EntityType.Builder.create(PotionWaspEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.75f).build("potion_wasp"));
    public static final RegistrySupplier<EntityType<PotionSacEntity>> POTION_SAC = registerEntityType("potion_sac", () -> EntityType.Builder.create(PotionSacEntity::new, SpawnGroup.MISC).dimensions(0.75f, 0.75f).build("potion_sac"));
    public static final RegistrySupplier<EntityType<DragonflyEntity>> DRAGONFLY = registerEntityType("dragonfly", () -> EntityType.Builder.create(DragonflyEntity::new, SpawnGroup.CREATURE).dimensions(0.65f, 0.4f).build("dragonfly"));
    public static final RegistrySupplier<EntityType<BoopletEntity>> BOOPLET = registerEntityType("booplet", () -> EntityType.Builder.create(BoopletEntity::new, SpawnGroup.CREATURE).dimensions(0.6f, 0.6f).build("booplet"));
    public static final RegistrySupplier<EntityType<KoiEntity>> KOI = registerEntityType("koi", () -> EntityType.Builder.create(KoiEntity::new, SpawnGroup.WATER_AMBIENT).dimensions(0.7f, 0.5f).build("koi"));
    public static final RegistrySupplier<EntityType<KoiEggsEntity>> KOI_EGGS = registerEntityType("koi_eggs", () -> EntityType.Builder.create(KoiEggsEntity::new, SpawnGroup.MISC).dimensions(0.45f, 0.45f).build("koi_eggs"));

    private static <T extends Entity> RegistrySupplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType<T>> entityType){
        return ENTITIES.register(Identifier.of(ClutterBestiary.MOD_ID, name), entityType);
    }

    public static void register() {
        ENTITIES.register();
    }

}
