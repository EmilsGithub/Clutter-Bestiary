package net.emilsg.clutterbestiary.item;

import net.minecraft.world.item.DyeColor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.emilsg.clutterbestiary.item.custom.*;
import net.emilsg.clutterbestiary.util.ModItemGroups;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.material.Fluids;
import java.util.List;
import java.util.function.Function;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ClutterBestiary.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> RAW_CHORUS_ECHOFIN = registerItem("raw_chorus_echofin", properties -> new RandomTeleportItem(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.1f).alwaysEdible().build(), timedFood(10).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 200, 10, 48));
    public static final RegistrySupplier<Item> COOKED_CHORUS_ECHOFIN = registerItem("cooked_chorus_echofin", properties -> new RandomTeleportItem(properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.2f).alwaysEdible().build(), timedFood(20).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 150, 20, 96));
    public static final RegistrySupplier<Item> RAW_LEVITATING_ECHOFIN = registerItem("raw_levitating_echofin", properties -> new UseTimeItem(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.1f).alwaysEdible().build(), timedFood(10).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 1200, 0), 1.0F)).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 10, 80));
    public static final RegistrySupplier<Item> COOKED_LEVITATING_ECHOFIN = registerItem("cooked_levitating_echofin", properties -> new UseTimeItem(properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.2f).alwaysEdible().build(), timedFood(10).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 1), 1.0F)).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 10, 60));
    public static final RegistrySupplier<Item> RAW_VENISON = registerItem("raw_venison", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> COOKED_VENISON = registerItem("cooked_venison", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> RAW_VENISON_RIBS = registerItem("raw_venison_ribs", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.2f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> COOKED_VENISON_RIBS = registerItem("cooked_venison_ribs", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(10).saturationModifier(0.4f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KOI = registerItem("koi", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1F).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> ARROWFISH = registerItem("arrowfish", properties -> new ArrowfishItem(properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> LEVITATING_ECHOFIN_BUCKET = registerItem("levitating_echofin_bucket", properties -> new EchofinBucketItem(properties.craftRemainder(Items.BUCKET).stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY), EchofinVariant.LEVITATING));
    public static final RegistrySupplier<Item> CHORUS_ECHOFIN_BUCKET = registerItem("chorus_echofin_bucket", properties -> new EchofinBucketItem(properties.craftRemainder(Items.BUCKET).stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY), EchofinVariant.CHORUS));
    public static final RegistrySupplier<Item> SEAHORSE_BUCKET = registerItem("seahorse_bucket", properties -> new SeahorseBucketItem(ModEntityTypes.SEAHORSE, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties.stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> RIVER_TURTLE_BUCKET = registerItem("river_turtle_bucket", properties -> new RiverTurtleBucketItem(ModEntityTypes.RIVER_TURTLE, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties.stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KOI_BUCKET = registerItem("koi_bucket", properties -> new KoiBucketItem(ModEntityTypes.KOI, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties.stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> JELLYFISH_BUCKET = registerItem("jellyfish_bucket", properties -> new JellyfishBucketItem(ModEntityTypes.JELLYFISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties.stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> ARROWFISH_BUCKET = registerItem("arrowfish_bucket", properties -> new ArrowfishBucketItem(ModEntityTypes.ARROWFISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties.stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> BUTTERFLY_COCOON = registerItem("butterfly_cocoon", properties -> new ModAliasedBlockItem(ModBlocks.BUTTERFLY_COCOON, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KIWI_BIRD_EGG = registerItem("kiwi_bird_egg", properties -> new ModAliasedBlockItem(ModBlocks.KIWI_BIRD_EGG, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> EMPEROR_PENGUIN_EGG = registerItem("emperor_penguin_egg", properties -> new ModAliasedBlockItem(ModBlocks.EMPEROR_PENGUIN_EGG, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CROCODILE_EGG = registerItem("crocodile_egg", properties -> new ModAliasedBlockItem(ModBlocks.CROCODILE_EGG, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> MOSSBLOOM_ANTLERS = registerItem("mossbloom_antlers", properties -> new Item(properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> BUTTERFLY_IN_A_BOTTLE = registerItem("butterfly_in_a_bottle", properties -> new ButterflyBottleItem(ModBlocks.BUTTERFLY_IN_A_BOTTLE.get(), properties.useBlockDescriptionPrefix().craftRemainder(Items.GLASS_BOTTLE).stacksTo(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS = registerItem("butterfly_elytra_smithing_template_shards", properties -> new Item(properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BUTTERFLY_ELYTRA_SMITHING_TEMPLATE = registerItem("butterfly_elytra_smithing_template", properties -> new ButterflyElytraSmithingTemplateItem(properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> WHITE_BUTTERFLY_ELYTRA = registerItem("white_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "white").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.WHITE), "white"));
    public static final RegistrySupplier<Item> LIGHT_GRAY_BUTTERFLY_ELYTRA = registerItem("light_gray_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "light_gray").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.LIGHT_GRAY), "light_gray"));
    public static final RegistrySupplier<Item> GRAY_BUTTERFLY_ELYTRA = registerItem("gray_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "gray").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.GRAY), "gray"));
    public static final RegistrySupplier<Item> BLACK_BUTTERFLY_ELYTRA = registerItem("black_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "black").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.BLACK), "black"));
    public static final RegistrySupplier<Item> BROWN_BUTTERFLY_ELYTRA = registerItem("brown_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "brown").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.BROWN), "brown"));
    public static final RegistrySupplier<Item> RED_BUTTERFLY_ELYTRA = registerItem("red_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "red").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.RED), "red"));
    public static final RegistrySupplier<Item> ORANGE_BUTTERFLY_ELYTRA = registerItem("orange_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "orange").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.ORANGE), "orange"));
    public static final RegistrySupplier<Item> YELLOW_BUTTERFLY_ELYTRA = registerItem("yellow_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "yellow").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.YELLOW), "yellow"));
    public static final RegistrySupplier<Item> LIME_BUTTERFLY_ELYTRA = registerItem("lime_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "lime").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.LIME), "lime"));
    public static final RegistrySupplier<Item> GREEN_BUTTERFLY_ELYTRA = registerItem("green_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "green").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.GREEN), "green"));
    public static final RegistrySupplier<Item> CYAN_BUTTERFLY_ELYTRA = registerItem("cyan_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "cyan").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.CYAN), "cyan"));
    public static final RegistrySupplier<Item> LIGHT_BLUE_BUTTERFLY_ELYTRA = registerItem("light_blue_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "light_blue").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.LIGHT_BLUE), "light_blue"));
    public static final RegistrySupplier<Item> BLUE_BUTTERFLY_ELYTRA = registerItem("blue_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "blue").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.BLUE), "blue"));
    public static final RegistrySupplier<Item> PURPLE_BUTTERFLY_ELYTRA = registerItem("purple_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "purple").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.PURPLE), "purple"));
    public static final RegistrySupplier<Item> MAGENTA_BUTTERFLY_ELYTRA = registerItem("magenta_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "magenta").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.MAGENTA), "magenta"));
    public static final RegistrySupplier<Item> PINK_BUTTERFLY_ELYTRA = registerItem("pink_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "pink").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.DYE.pick(DyeColor.PINK), "pink"));
    public static final RegistrySupplier<Item> CRIMSON_BUTTERFLY_ELYTRA = registerItem("crimson_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "crimson").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.CRIMSON_ROOTS, "crimson"));
    public static final RegistrySupplier<Item> WARPED_BUTTERFLY_ELYTRA = registerItem("warped_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "warped").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.WARPED_ROOTS, "warped"));
    public static final RegistrySupplier<Item> SOUL_BUTTERFLY_ELYTRA = registerItem("soul_butterfly_elytra", properties -> new ButterflyElytraItem(butterflyElytra(properties, "soul").arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.BONE, "soul"));

    public static final List<RegistrySupplier<Item>> BUTTERFLY_ELYTRAS = List.of(
            WHITE_BUTTERFLY_ELYTRA,
            LIGHT_GRAY_BUTTERFLY_ELYTRA,
            GRAY_BUTTERFLY_ELYTRA,
            BLACK_BUTTERFLY_ELYTRA,
            BROWN_BUTTERFLY_ELYTRA,
            RED_BUTTERFLY_ELYTRA,
            ORANGE_BUTTERFLY_ELYTRA,
            YELLOW_BUTTERFLY_ELYTRA,
            LIME_BUTTERFLY_ELYTRA,
            GREEN_BUTTERFLY_ELYTRA,
            CYAN_BUTTERFLY_ELYTRA,
            LIGHT_BLUE_BUTTERFLY_ELYTRA,
            BLUE_BUTTERFLY_ELYTRA,
            PURPLE_BUTTERFLY_ELYTRA,
            MAGENTA_BUTTERFLY_ELYTRA,
            PINK_BUTTERFLY_ELYTRA,
            CRIMSON_BUTTERFLY_ELYTRA,
            WARPED_BUTTERFLY_ELYTRA,
            SOUL_BUTTERFLY_ELYTRA
    );

    public static final RegistrySupplier<Item> MOSSBLOOM_SPAWN_EGG = registerItem("mossbloom_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.MOSSBLOOM, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BUTTERFLY_SPAWN_EGG = registerItem("butterfly_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.BUTTERFLY, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CHAMELEON_SPAWN_EGG = registerItem("chameleon_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.CHAMELEON, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> ECHOFIN_SPAWN_EGG = registerItem("echofin_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.ECHOFIN, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KIWI_BIRD_SPAWN_EGG = registerItem("kiwi_bird_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.KIWI_BIRD, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> EMPEROR_PENGUIN_SPAWN_EGG = registerItem("emperor_penguin_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.EMPEROR_PENGUIN, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BEAVER_SPAWN_EGG = registerItem("beaver_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.BEAVER, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CAPYBARA_SPAWN_EGG = registerItem("capybara_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.CAPYBARA, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CRIMSON_NEWT_SPAWN_EGG = registerItem("crimson_newt_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.CRIMSON_NEWT, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> WARPED_NEWT_SPAWN_EGG = registerItem("warped_newt_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.WARPED_NEWT, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> EMBER_TORTOISE_SPAWN_EGG = registerItem("ember_tortoise_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.EMBER_TORTOISE, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> JELLYFISH_SPAWN_EGG = registerItem("jellyfish_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.JELLYFISH, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> MANTA_RAY_SPAWN_EGG = registerItem("manta_ray_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.MANTA_RAY, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> SEAHORSE_SPAWN_EGG = registerItem("seahorse_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.SEAHORSE, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> POTION_WASP_SPAWN_EGG = registerItem("potion_wasp_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.POTION_WASP, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> DRAGONFLY_SPAWN_EGG = registerItem("dragonfly_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.DRAGONFLY, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BOOPLET_SPAWN_EGG = registerItem("booplet_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.BOOPLET, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KOI_SPAWN_EGG = registerItem("koi_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.KOI, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> RIVER_TURTLE_SPAWN_EGG = registerItem("river_turtle_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.RIVER_TURTLE, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> COATI_SPAWN_EGG = registerItem("coati_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.COATI, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> RED_PANDA_SPAWN_EGG = registerItem("red_panda_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.RED_PANDA, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> STOAT_SPAWN_EGG = registerItem("stoat_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.STOAT, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CROCODILE_SPAWN_EGG = registerItem("crocodile_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.CROCODILE, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CHORUS_BEETLE_SPAWN_EGG = registerItem("chorus_beetle_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.CHORUS_BEETLE, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> WOODPECKER_SPAWN_EGG = registerItem("woodpecker_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.WOODPECKER, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> ARROWFISH_SPAWN_EGG = registerItem("arrowfish_spawn_egg", properties -> new BestiarySpawnEggItem(ModEntityTypes.ARROWFISH, properties.arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, name);
        // Item properties must carry their registry key before the item is constructed.
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        return ITEMS.register(id, () -> factory.apply(new Item.Properties().setId(key)));
    }

    private static Consumable.Builder timedFood(int useTimeInTicks) {
        return Consumables.defaultFood().consumeSeconds(useTimeInTicks / 20.0F);
    }

    private static Item.Properties butterflyElytra(Item.Properties properties, String color) {
        ResourceKey<EquipmentAsset> asset = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, color + "_butterfly_elytra"));
        return properties
                .durability(432)
                .repairable(BUTTERFLY_IN_A_BOTTLE.get())
                .component(DataComponents.GLIDER, Unit.INSTANCE)
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST).setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA).setAsset(asset).setDamageOnHurt(false).build());
    }

    public static void register() {
        ITEMS.register();
    }
}
