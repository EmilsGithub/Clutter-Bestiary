package net.emilsg.clutterbestiary.item;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.emilsg.clutterbestiary.item.custom.*;
import net.emilsg.clutterbestiary.util.ModItemGroups;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ClutterBestiary.MOD_ID, RegistryKeys.ITEM);

    public static final RegistrySupplier<Item> RAW_CHORUS_ECHOFIN = registerItem("raw_chorus_echofin", () -> new RandomTeleportItem(new Item.Settings().food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.1f).alwaysEdible().build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 200, 10, 48));
    public static final RegistrySupplier<Item> COOKED_CHORUS_ECHOFIN = registerItem("cooked_chorus_echofin", () -> new RandomTeleportItem(new Item.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.2f).alwaysEdible().build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 150, 20, 96));
    public static final RegistrySupplier<Item> RAW_LEVITATING_ECHOFIN = registerItem("raw_levitating_echofin", () -> new UseTimeItem(new Item.Settings().food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.1f).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.POISON, 1200, 0), 1.0F).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 10, 80));
    public static final RegistrySupplier<Item> COOKED_LEVITATING_ECHOFIN = registerItem("cooked_levitating_echofin", () -> new UseTimeItem(new Item.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.2f).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1), 1.0F).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY), 10, 60));
    public static final RegistrySupplier<Item> RAW_VENISON = registerItem("raw_venison", () -> new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.2f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> COOKED_VENISON = registerItem("cooked_venison", () -> new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.4f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> RAW_VENISON_RIBS = registerItem("raw_venison_ribs", () -> new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(5).saturationModifier(0.2f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> COOKED_VENISON_RIBS = registerItem("cooked_venison_ribs", () -> new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(10).saturationModifier(0.4f).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KOI = registerItem("koi", () -> new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(1).saturationModifier(0.1F).build()).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> LEVITATING_ECHOFIN_BUCKET = registerItem("levitating_echofin_bucket", () -> new EchofinBucketItem(new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY), EchofinVariant.LEVITATING));
    public static final RegistrySupplier<Item> CHORUS_ECHOFIN_BUCKET = registerItem("chorus_echofin_bucket", () -> new EchofinBucketItem(new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY), EchofinVariant.CHORUS));
    public static final RegistrySupplier<Item> SEAHORSE_BUCKET = registerItem("seahorse_bucket", () -> new SeahorseBucketItem(ModEntityTypes.SEAHORSE, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().maxCount(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KOI_BUCKET = registerItem("koi_bucket", () -> new KoiBucketItem(ModEntityTypes.KOI, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().maxCount(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> BUTTERFLY_COCOON = registerItem("butterfly_cocoon", () -> new ModAliasedBlockItem(ModBlocks.BUTTERFLY_COCOON, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KIWI_BIRD_EGG = registerItem("kiwi_bird_egg", () -> new ModAliasedBlockItem(ModBlocks.KIWI_BIRD_EGG, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> EMPEROR_PENGUIN_EGG = registerItem("emperor_penguin_egg", () -> new ModAliasedBlockItem(ModBlocks.EMPEROR_PENGUIN_EGG, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> MOSSBLOOM_ANTLERS = registerItem("mossbloom_antlers", () -> new Item(new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> BUTTERFLY_IN_A_BOTTLE = registerItem("butterfly_in_a_bottle", () -> new ButterflyBottleItem(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(1).arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS = registerItem("butterfly_elytra_smithing_template_shards", () -> new Item(new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BUTTERFLY_ELYTRA_SMITHING_TEMPLATE = registerItem("butterfly_elytra_smithing_template", () -> new ButterflyElytraSmithingTemplateItem(new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static final RegistrySupplier<Item> WHITE_BUTTERFLY_ELYTRA = registerItem("white_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.WHITE_DYE, "white"));
    public static final RegistrySupplier<Item> LIGHT_GRAY_BUTTERFLY_ELYTRA = registerItem("light_gray_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.LIGHT_GRAY_DYE, "light_gray"));
    public static final RegistrySupplier<Item> GRAY_BUTTERFLY_ELYTRA = registerItem("gray_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.GRAY_DYE, "gray"));
    public static final RegistrySupplier<Item> BLACK_BUTTERFLY_ELYTRA = registerItem("black_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.BLACK_DYE, "black"));
    public static final RegistrySupplier<Item> BROWN_BUTTERFLY_ELYTRA = registerItem("brown_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.BROWN_DYE, "brown"));
    public static final RegistrySupplier<Item> RED_BUTTERFLY_ELYTRA = registerItem("red_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.RED_DYE, "red"));
    public static final RegistrySupplier<Item> ORANGE_BUTTERFLY_ELYTRA = registerItem("orange_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.ORANGE_DYE, "orange"));
    public static final RegistrySupplier<Item> YELLOW_BUTTERFLY_ELYTRA = registerItem("yellow_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.YELLOW_DYE, "yellow"));
    public static final RegistrySupplier<Item> LIME_BUTTERFLY_ELYTRA = registerItem("lime_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.LIME_DYE, "lime"));
    public static final RegistrySupplier<Item> GREEN_BUTTERFLY_ELYTRA = registerItem("green_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.GREEN_DYE, "green"));
    public static final RegistrySupplier<Item> CYAN_BUTTERFLY_ELYTRA = registerItem("cyan_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.CYAN_DYE, "cyan"));
    public static final RegistrySupplier<Item> LIGHT_BLUE_BUTTERFLY_ELYTRA = registerItem("light_blue_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.LIGHT_BLUE_DYE, "light_blue"));
    public static final RegistrySupplier<Item> BLUE_BUTTERFLY_ELYTRA = registerItem("blue_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.BLUE_DYE, "blue"));
    public static final RegistrySupplier<Item> PURPLE_BUTTERFLY_ELYTRA = registerItem("purple_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.PURPLE_DYE, "purple"));
    public static final RegistrySupplier<Item> MAGENTA_BUTTERFLY_ELYTRA = registerItem("magenta_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.MAGENTA_DYE, "magenta"));
    public static final RegistrySupplier<Item> PINK_BUTTERFLY_ELYTRA = registerItem("pink_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.PINK_DYE, "pink"));
    public static final RegistrySupplier<Item> CRIMSON_BUTTERFLY_ELYTRA = registerItem("crimson_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.CRIMSON_ROOTS, "crimson"));
    public static final RegistrySupplier<Item> WARPED_BUTTERFLY_ELYTRA = registerItem("warped_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.WARPED_ROOTS, "warped"));
    public static final RegistrySupplier<Item> SOUL_BUTTERFLY_ELYTRA = registerItem("soul_butterfly_elytra", () -> new ButterflyElytraItem(new Item.Settings().maxDamage(432).arch$tab(ModItemGroups.CLUTTER_BESTIARY), Items.BONE, "soul"));

    public static final RegistrySupplier<Item> MOSSBLOOM_SPAWN_EGG = registerItem("mossbloom_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.MOSSBLOOM, 16053485, 7377453, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BUTTERFLY_SPAWN_EGG = registerItem("butterfly_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.BUTTERFLY, 757231, 12, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CHAMELEON_SPAWN_EGG = registerItem("chameleon_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.CHAMELEON, 1744148, 16228345, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> ECHOFIN_SPAWN_EGG = registerItem("echofin_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.ECHOFIN, 16511998, 4661575, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KIWI_BIRD_SPAWN_EGG = registerItem("kiwi_bird_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.KIWI_BIRD, 6243108, 6275609, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> EMPEROR_PENGUIN_SPAWN_EGG = registerItem("emperor_penguin_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.EMPEROR_PENGUIN, 1973800, 16777210, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BEAVER_SPAWN_EGG = registerItem("beaver_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.BEAVER, 5916211, 3356222, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CAPYBARA_SPAWN_EGG = registerItem("capybara_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.CAPYBARA, 2169626, 16651589, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> CRIMSON_NEWT_SPAWN_EGG = registerItem("crimson_newt_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.CRIMSON_NEWT, 15783361, 11280416, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> WARPED_NEWT_SPAWN_EGG = registerItem("warped_newt_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.WARPED_NEWT, 1153925, 4464945, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> EMBER_TORTOISE_SPAWN_EGG = registerItem("ember_tortoise_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.EMBER_TORTOISE, 6052956, 8924463, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> JELLYFISH_SPAWN_EGG = registerItem("jellyfish_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.JELLYFISH, 5487623, 8732643, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> MANTA_RAY_SPAWN_EGG = registerItem("manta_ray_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.MANTA_RAY, 12895428, 2566460, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> SEAHORSE_SPAWN_EGG = registerItem("seahorse_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.SEAHORSE, 14593895, 12351763, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> POTION_WASP_SPAWN_EGG = registerItem("potion_wasp_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.POTION_WASP, 867453, 2357421, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> DRAGONFLY_SPAWN_EGG = registerItem("dragonfly_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.DRAGONFLY, 2463756, 87652394, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> BOOPLET_SPAWN_EGG = registerItem("booplet_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.BOOPLET, 5641645, 4241312, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));
    public static final RegistrySupplier<Item> KOI_SPAWN_EGG = registerItem("koi_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.KOI, 0x3E4549, 0xFF2632, new Item.Settings().arch$tab(ModItemGroups.CLUTTER_BESTIARY)));

    public static RegistrySupplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(Identifier.of(ClutterBestiary.MOD_ID, name), item);
    }

    public static void register() {
        ITEMS.register();
    }
}
