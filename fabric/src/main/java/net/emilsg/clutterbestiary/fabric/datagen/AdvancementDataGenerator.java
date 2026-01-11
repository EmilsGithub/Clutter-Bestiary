package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.BredAnimalsCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.EntityHurtPlayerCriterion;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.advancement.criterion.UsingItemCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.entity.PlayerPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementDataGenerator extends FabricAdvancementProvider {

    public AdvancementDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry root = Advancement.Builder.create()
                .display(
                        ModItems.MOSSBLOOM_SPAWN_EGG.get(),
                        Text.translatable("advancements.bestiary.root.title"),
                        Text.translatable("advancements.bestiary.root.description"),
                        Identifier.ofVanilla("textures/gui/advancements/backgrounds/husbandry.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                )
                .criterion(
                        "exist",
                        TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createDimension(World.OVERWORLD))
                )
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/root");

        createTask(root, ModItems.BOOPLET_SPAWN_EGG.get(), "boop")
                .criterion("boop", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/boop");

        AdvancementEntry damGoodWork = createTask(root, Blocks.STRIPPED_OAK_LOG, "dam_good_work")
                .criterion("strip_log", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/dam_good_work");

        createTask(damGoodWork, ModItems.BEAVER_SPAWN_EGG.get(), "busy_beaver")
                .criterion("strip_25_logs", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/busy_beaver");

        createTask(root, Items.CHEST, "pack_rat")
                .criterion("use_coati_storage", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/pack_rat");

        createTask(root, ModItems.EMBER_TORTOISE_SPAWN_EGG.get(), "hot_hot_hot")
                .criterion("thats_hot", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/hot_hot_hot");

        createTask(root, ModItems.CAPYBARA_SPAWN_EGG.get(), "melon_friends")
                .criterion("tame_capybara", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/melon_friends");

        createTask(root, ModItems.RED_PANDA_SPAWN_EGG.get(), "three_course_meal")
                .criterion("satisfy_three_cravings", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/three_course_meal");

        AdvancementEntry stoatallyYours = createTask(root, ModItems.STOAT_SPAWN_EGG.get(), "stoatally_yours")
                .criterion("tame_stoat", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/stoatally_yours");

        createTask(stoatallyYours, Items.RABBIT, "stoat_of_affairs")
                .criterion("breed_stoats", BredAnimalsCriterion.Conditions.create(EntityPredicate.Builder.create().type(ModEntityTypes.STOAT.get())))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/stoat_of_affairs");

        createTask(root, ModItems.WOODPECKER_SPAWN_EGG.get(), "knock_on_wood")
                .criterion("breed_woodpeckers", BredAnimalsCriterion.Conditions.create(EntityPredicate.Builder.create().type(ModEntityTypes.WOODPECKER.get())))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/knock_on_wood");

        createTask(root, ModItems.ARROWFISH.get(), "fish_slap")
                .criterion("hit_with_arrowfish", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/fish_slap");

        createTask(root, ModItems.CROCODILE_SPAWN_EGG.get(), "a_croc_of_trust")
                .criterion("tame_crocodile", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/a_croc_of_trust");

        createTask(root, Items.CHORUS_FLOWER, "special_delivery")
                .criterion("receive_chorus_flower", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/special_delivery");

        createTask(root, ModItems.BUTTERFLY_IN_A_BOTTLE.get(), "flutter_collector")
                .criterion(
                        "flutter_collector",
                        InventoryChangedCriterion.Conditions.items(ModItems.BUTTERFLY_IN_A_BOTTLE.get())
                )
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/flutter_collector");

        createTask(root, Items.SLIME_BALL, "that_stings")
                .criterion(
                        "that_stings",
                        EntityHurtPlayerCriterion.Conditions.create(
                                DamagePredicate.Builder.create()
                                        .sourceEntity(
                                                EntityPredicate.Builder.create()
                                                        .type(ModEntityTypes.JELLYFISH.get())
                                                        .build()
                                        )
                        )
                )
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/that_stings");

        createTask(root, ModItems.KIWI_BIRD_EGG.get(), "large_egg_small_bird")
                .criterion(
                        "large_egg_small_bird",
                        InventoryChangedCriterion.Conditions.items(ModItems.KIWI_BIRD_EGG.get())
                )
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/large_egg_small_bird");

        AdvancementEntry happyFeet = createTask(root, ModItems.EMPEROR_PENGUIN_SPAWN_EGG.get(), "happy_feet")
                .criterion(
                        "view_penguin",
                        UsingItemCriterion.Conditions.create(
                                EntityPredicate.Builder.create().typeSpecific(PlayerPredicate.Builder.create()
                                        .lookingAt(EntityPredicate.Builder.create().type(ModEntityTypes.EMPEROR_PENGUIN.get()))
                                        .build()),
                                ItemPredicate.Builder.create().items(Items.SPYGLASS)
                        )
                )
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/happy_feet");

        createTask(happyFeet, ModItems.EMPEROR_PENGUIN_EGG.get(), "large_bird_small_egg")
                .criterion(
                        "large_bird_small_egg",
                        InventoryChangedCriterion.Conditions.items(ModItems.EMPEROR_PENGUIN_EGG.get())
                )
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/large_bird_small_egg");

        AdvancementEntry fishyBusiness = createTask(root, ModItems.KOI_BUCKET.get(), "fishy_business")
                .criterion("fishy_business", InventoryChangedCriterion.Conditions.items(ModItems.KOI_BUCKET.get()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/fishy_business");

        AdvancementEntry makesSense = createTask(fishyBusiness, ModItems.CHORUS_ECHOFIN_BUCKET.get(), "makes_sense")
                .criterion(
                        "makes_sense",
                        InventoryChangedCriterion.Conditions.items(
                                ItemPredicate.Builder.create()
                                        .items(
                                                ModItems.CHORUS_ECHOFIN_BUCKET.get(),
                                                ModItems.LEVITATING_ECHOFIN_BUCKET.get()
                                        )
                        )
                )
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/makes_sense");

        createTask(fishyBusiness, ModItems.KOI_BUCKET.get(), "pearl_of_the_pond")
                .criterion("iridescent_white_koi", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/pearl_of_the_pond");

        createTask(makesSense, ModItems.JELLYFISH_BUCKET.get(), "gotta_catch_em_all")
                .criterion("butterfly", InventoryChangedCriterion.Conditions.items(ModItems.BUTTERFLY_IN_A_BOTTLE.get()))
                .criterion("koi", InventoryChangedCriterion.Conditions.items(ModItems.KOI_BUCKET.get()))
                .criterion(
                        "echofin",
                        InventoryChangedCriterion.Conditions.items(
                                ItemPredicate.Builder.create()
                                        .items(
                                                ModItems.CHORUS_ECHOFIN_BUCKET.get(),
                                                ModItems.LEVITATING_ECHOFIN_BUCKET.get()
                                        )
                        )
                )
                .criterion("seahorse", InventoryChangedCriterion.Conditions.items(ModItems.SEAHORSE_BUCKET.get()))
                .criterion("river_turtle", InventoryChangedCriterion.Conditions.items(ModItems.RIVER_TURTLE_BUCKET.get()))
                .criterion("jellyfish", InventoryChangedCriterion.Conditions.items(ModItems.JELLYFISH_BUCKET.get()))
                .criterion("arrowfish", InventoryChangedCriterion.Conditions.items(ModItems.ARROWFISH_BUCKET.get()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/gotta_catch_em_all");

        Advancement.Builder.create()
                .parent(root)
                .display(
                        Items.NETHER_STAR,
                        Text.translatable("advancements.bestiary.seriously_all_of_them.title"),
                        Text.translatable("advancements.bestiary.seriously_all_of_them.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        true
                )
                .criterion("complete_bestiary", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, ClutterBestiary.MOD_ID + ":bestiary/seriously_all_of_them");
    }

    private static Advancement.Builder createTask(AdvancementEntry parent, ItemConvertible icon, String name) {
        return Advancement.Builder.create()
                .parent(parent)
                .display(
                        icon,
                        Text.translatable("advancements.bestiary." + name + ".title"),
                        Text.translatable("advancements.bestiary." + name + ".description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                );
    }
}
