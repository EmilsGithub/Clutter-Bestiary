package net.emilsg.clutterbestiary.item;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.emilsg.clutterbestiary.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item RAW_CHORUS_ECHOFIN = registerItem("raw_chorus_echofin", new RandomTeleportItem(new FabricItemSettings().food(new FoodComponent.Builder().hunger(3).saturationModifier(0.1f).meat().alwaysEdible().build()), 200, 10, 48));
    public static final Item COOKED_CHORUS_ECHOFIN = registerItem("cooked_chorus_echofin", new RandomTeleportItem(new FabricItemSettings().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.2f).meat().alwaysEdible().build()), 150, 20, 96));
    public static final Item RAW_LEVITATING_ECHOFIN = registerItem("raw_levitating_echofin", new FoodWithEffectItem(new FabricItemSettings().food(new FoodComponent.Builder().hunger(3).saturationModifier(0.1f).meat().alwaysEdible().build()), StatusEffects.LEVITATION, 100, 0, 10, 80));
    public static final Item COOKED_LEVITATING_ECHOFIN = registerItem("cooked_levitating_echofin", new FoodWithEffectItem(new FabricItemSettings().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.2f).meat().alwaysEdible().build()), StatusEffects.LEVITATION, 100, 1, 10, 60));
    public static final Item RAW_VENISON = registerItem("raw_venison", new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(3).saturationModifier(0.2f).meat().build())));
    public static final Item COOKED_VENISON = registerItem("cooked_venison", new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.4f).meat().build())));
    public static final Item RAW_VENISON_RIBS = registerItem("raw_venison_ribs", new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(5).saturationModifier(0.2f).meat().build())));
    public static final Item COOKED_VENISON_RIBS = registerItem("cooked_venison_ribs", new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(10).saturationModifier(0.4f).meat().build())));
    public static final Item KOI = registerItem("koi", new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(1).saturationModifier(0.1F).build())));

    public static final Item LEVITATING_ECHOFIN_BUCKET = registerItem("levitating_echofin_bucket", new EchofinBucketItem(new FabricItemSettings().recipeRemainder(Items.BUCKET).maxCount(1), EchofinVariant.LEVITATING));
    public static final Item CHORUS_ECHOFIN_BUCKET = registerItem("chorus_echofin_bucket", new EchofinBucketItem(new FabricItemSettings().recipeRemainder(Items.BUCKET).maxCount(1), EchofinVariant.CHORUS));
    public static final Item SEAHORSE_BUCKET = registerItem("seahorse_bucket", new SeahorseBucketItem(ModEntityTypes.SEAHORSE, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().maxCount(1)));
    public static final Item KOI_BUCKET = registerItem("koi_bucket", new KoiBucketItem(ModEntityTypes.KOI, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().maxCount(1)));

    public static final Item BUTTERFLY_COCOON = registerItem("butterfly_cocoon", new AliasedBlockItem(ModBlocks.BUTTERFLY_COCOON, new FabricItemSettings()));
    public static final Item KIWI_BIRD_EGG = registerItem("kiwi_bird_egg", new AliasedBlockItem(ModBlocks.KIWI_BIRD_EGG, new FabricItemSettings()));
    public static final Item EMPEROR_PENGUIN_EGG = registerItem("emperor_penguin_egg", new AliasedBlockItem(ModBlocks.EMPEROR_PENGUIN_EGG, new FabricItemSettings()));

    public static final Item MOSSBLOOM_ANTLERS = registerItem("mossbloom_antlers", new Item(new Item.Settings()));

    public static final Item BUTTERFLY_IN_A_BOTTLE = registerItem("butterfly_in_a_bottle", new ButterflyBottleItem(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(1)));
    public static final Item BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS = registerItem("butterfly_elytra_smithing_template_shards", new Item(new FabricItemSettings()));
    public static final Item BUTTERFLY_ELYTRA_SMITHING_TEMPLATE = registerItem("butterfly_elytra_smithing_template", new ButterflyElytraSmithingTemplateItem(new FabricItemSettings()));

    public static final Item WHITE_BUTTERFLY_ELYTRA = registerItem("white_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.WHITE_DYE, "white"));
    public static final Item LIGHT_GRAY_BUTTERFLY_ELYTRA = registerItem("light_gray_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.LIGHT_GRAY_DYE, "light_gray"));
    public static final Item GRAY_BUTTERFLY_ELYTRA = registerItem("gray_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.GRAY_DYE, "gray"));
    public static final Item BLACK_BUTTERFLY_ELYTRA = registerItem("black_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.BLACK_DYE, "black"));
    public static final Item BROWN_BUTTERFLY_ELYTRA = registerItem("brown_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.BROWN_DYE, "brown"));
    public static final Item RED_BUTTERFLY_ELYTRA = registerItem("red_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.RED_DYE, "red"));
    public static final Item ORANGE_BUTTERFLY_ELYTRA = registerItem("orange_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.ORANGE_DYE, "orange"));
    public static final Item YELLOW_BUTTERFLY_ELYTRA = registerItem("yellow_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.YELLOW_DYE, "yellow"));
    public static final Item LIME_BUTTERFLY_ELYTRA = registerItem("lime_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.LIME_DYE, "lime"));
    public static final Item GREEN_BUTTERFLY_ELYTRA = registerItem("green_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.GREEN_DYE, "green"));
    public static final Item CYAN_BUTTERFLY_ELYTRA = registerItem("cyan_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.CYAN_DYE, "cyan"));
    public static final Item LIGHT_BLUE_BUTTERFLY_ELYTRA = registerItem("light_blue_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.LIGHT_BLUE_DYE, "light_blue"));
    public static final Item BLUE_BUTTERFLY_ELYTRA = registerItem("blue_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.BLUE_DYE, "blue"));
    public static final Item PURPLE_BUTTERFLY_ELYTRA = registerItem("purple_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.PURPLE_DYE, "purple"));
    public static final Item MAGENTA_BUTTERFLY_ELYTRA = registerItem("magenta_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.MAGENTA_DYE, "magenta"));
    public static final Item PINK_BUTTERFLY_ELYTRA = registerItem("pink_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.PINK_DYE, "pink"));
    public static final Item CRIMSON_BUTTERFLY_ELYTRA = registerItem("crimson_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.CRIMSON_ROOTS, "crimson"));
    public static final Item WARPED_BUTTERFLY_ELYTRA = registerItem("warped_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.WARPED_ROOTS, "warped"));
    public static final Item SOUL_BUTTERFLY_ELYTRA = registerItem("soul_butterfly_elytra", new ButterflyElytraItem(new FabricItemSettings().maxDamage(432), Items.BONE, "soul"));

    public static final Item BUTTERFLY_SPAWN_EGG = registerItem("butterfly_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.BUTTERFLY, 757231, 12, new FabricItemSettings()));
    public static final Item CHAMELEON_SPAWN_EGG = registerItem("chameleon_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.CHAMELEON, 1744148, 16228345, new FabricItemSettings()));
    public static final Item ECHOFIN_SPAWN_EGG = registerItem("echofin_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.ECHOFIN, 16511998, 4661575, new FabricItemSettings()));
    public static final Item MOSSBLOOM_SPAWN_EGG = registerItem("mossbloom_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.MOSSBLOOM, 16053485, 7377453, new FabricItemSettings()));
    public static final Item KIWI_BIRD_SPAWN_EGG = registerItem("kiwi_bird_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.KIWI_BIRD, 6243108, 6275609, new FabricItemSettings()));
    public static final Item EMPEROR_PENGUIN_SPAWN_EGG = registerItem("emperor_penguin_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.EMPEROR_PENGUIN, 1973800, 16777210, new FabricItemSettings()));
    public static final Item BEAVER_SPAWN_EGG = registerItem("beaver_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.BEAVER, 5916211, 3356222, new FabricItemSettings()));
    public static final Item CAPYBARA_SPAWN_EGG = registerItem("capybara_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.CAPYBARA, 2169626, 16651589, new FabricItemSettings()));
    public static final Item CRIMSON_NEWT_SPAWN_EGG = registerItem("crimson_newt_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.CRIMSON_NEWT, 15783361, 11280416, new FabricItemSettings()));
    public static final Item WARPED_NEWT_SPAWN_EGG = registerItem("warped_newt_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.WARPED_NEWT, 1153925, 4464945, new FabricItemSettings()));
    public static final Item EMBER_TORTOISE_SPAWN_EGG = registerItem("ember_tortoise_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.EMBER_TORTOISE, 6052956, 8924463, new FabricItemSettings()));
    public static final Item JELLYFISH_SPAWN_EGG = registerItem("jellyfish_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.JELLYFISH, 5487623, 8732643, new FabricItemSettings()));
    public static final Item MANTA_RAY_SPAWN_EGG = registerItem("manta_ray_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.MANTA_RAY, 12895428, 2566460, new FabricItemSettings()));
    public static final Item SEAHORSE_SPAWN_EGG = registerItem("seahorse_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.SEAHORSE, 14593895, 12351763, new FabricItemSettings()));
    public static final Item POTION_WASP_SPAWN_EGG = registerItem("potion_wasp_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.POTION_WASP, 867453, 2357421, new FabricItemSettings()));
    public static final Item DRAGONFLY_SPAWN_EGG = registerItem("dragonfly_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.DRAGONFLY, 2463756, 87652394, new FabricItemSettings()));
    public static final Item BOOPLET_SPAWN_EGG = registerItem("booplet_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.BOOPLET, 5641645, 4241312, new FabricItemSettings()));
    public static final Item KOI_SPAWN_EGG = registerItem("koi_spawn_egg", new BestiarySpawnEggItem(ModEntityTypes.KOI, 0x3E4549, 0xFF2632, new FabricItemSettings()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(ClutterBestiary.MOD_ID, name), item);
    }

    public static void register() {

    }
}
