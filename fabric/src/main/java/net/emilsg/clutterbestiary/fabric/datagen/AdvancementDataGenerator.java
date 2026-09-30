package net.emilsg.clutterbestiary.fabric.datagen;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderGetter;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.BredAnimalsTrigger;
import net.minecraft.advancements.predicates.DamagePredicate;
import net.minecraft.advancements.triggers.EntityHurtPlayerTrigger;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.ImpossibleTrigger;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.entity.PlayerPredicate;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.advancements.triggers.UsingItemTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementDataGenerator extends FabricAdvancementProvider {

    public AdvancementDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        HolderGetter<EntityType<?>> entityTypes = registryLookup.lookupOrThrow(Registries.ENTITY_TYPE);
        HolderGetter<Item> items = registryLookup.lookupOrThrow(Registries.ITEM);

        AdvancementHolder root = save(consumer, Advancement.Builder.advancement()
                .rootDisplay(
                        ModItems.MOSSBLOOM_SPAWN_EGG.get(),
                        Component.translatable("advancements.bestiary.root.title"),
                        Component.translatable("advancements.bestiary.root.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/husbandry"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion(
                        "exist",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inDimension(Level.OVERWORLD))
                )
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/root")));

        save(consumer, createTask(root, ModItems.BOOPLET_SPAWN_EGG.get(), "boop")
                .addCriterion("boop", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/boop")));

        AdvancementHolder damGoodWork = save(consumer, createTask(root, Blocks.STRIPPED_OAK_LOG, "dam_good_work")
                .addCriterion("strip_log", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/dam_good_work")));

        save(consumer, createTask(damGoodWork, ModItems.BEAVER_SPAWN_EGG.get(), "busy_beaver")
                .addCriterion("strip_25_logs", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/busy_beaver")));

        save(consumer, createTask(root, Items.CHEST, "pack_rat")
                .addCriterion("use_coati_storage", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/pack_rat")));

        save(consumer, createTask(root, ModItems.EMBER_TORTOISE_SPAWN_EGG.get(), "hot_hot_hot")
                .addCriterion("thats_hot", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/hot_hot_hot")));

        save(consumer, createTask(root, ModItems.CAPYBARA_SPAWN_EGG.get(), "melon_friends")
                .addCriterion("tame_capybara", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/melon_friends")));

        save(consumer, createTask(root, ModItems.RED_PANDA_SPAWN_EGG.get(), "three_course_meal")
                .addCriterion("satisfy_three_cravings", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/three_course_meal")));

        AdvancementHolder stoatallyYours = save(consumer, createTask(root, ModItems.STOAT_SPAWN_EGG.get(), "stoatally_yours")
                .addCriterion("tame_stoat", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/stoatally_yours")));

        save(consumer, createTask(stoatallyYours, Items.RABBIT, "stoat_of_affairs")
                .addCriterion("breed_stoats", BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityTypes, ModEntityTypes.STOAT.get())))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/stoat_of_affairs")));

        save(consumer, createTask(root, ModItems.WOODPECKER_SPAWN_EGG.get(), "knock_on_wood")
                .addCriterion("breed_woodpeckers", BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityTypes, ModEntityTypes.WOODPECKER.get())))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/knock_on_wood")));

        save(consumer, createTask(root, ModItems.ARROWFISH.get(), "fish_slap")
                .addCriterion("hit_with_arrowfish", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/fish_slap")));

        save(consumer, createTask(root, ModItems.CROCODILE_SPAWN_EGG.get(), "a_croc_of_trust")
                .addCriterion("tame_crocodile", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/a_croc_of_trust")));

        save(consumer, createTask(root, Items.CHORUS_FLOWER, "special_delivery")
                .addCriterion("receive_chorus_flower", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/special_delivery")));

        save(consumer, createTask(root, ModItems.BUTTERFLY_IN_A_BOTTLE.get(), "flutter_collector")
                .addCriterion(
                        "flutter_collector",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BUTTERFLY_IN_A_BOTTLE.get())
                )
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/flutter_collector")));

        save(consumer, createTask(root, Items.SLIME_BALL, "that_stings")
                .addCriterion(
                        "that_stings",
                        EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(
                                DamagePredicate.Builder.damageInstance()
                                        .sourceEntity(
                                                EntityPredicate.Builder.entity()
                                                        .of(entityTypes, ModEntityTypes.JELLYFISH.get())
                                                        .build()
                                        )
                        )
                )
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/that_stings")));

        save(consumer, createTask(root, ModItems.KIWI_BIRD_EGG.get(), "large_egg_small_bird")
                .addCriterion(
                        "large_egg_small_bird",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.KIWI_BIRD_EGG.get())
                )
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/large_egg_small_bird")));

        AdvancementHolder happyFeet = save(consumer, createTask(root, ModItems.EMPEROR_PENGUIN_SPAWN_EGG.get(), "happy_feet")
                .addCriterion(
                        "view_penguin",
                        UsingItemTrigger.TriggerInstance.lookingAt(
                                EntityPredicate.Builder.entity().player(PlayerPredicate.Builder.player()
                                        .setLookingAt(EntityPredicate.Builder.entity().of(entityTypes, ModEntityTypes.EMPEROR_PENGUIN.get()))
                                        .build()),
                                ItemPredicate.Builder.item().of(items, Items.SPYGLASS)
                        )
                )
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/happy_feet")));

        save(consumer, createTask(happyFeet, ModItems.EMPEROR_PENGUIN_EGG.get(), "large_bird_small_egg")
                .addCriterion(
                        "large_bird_small_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.EMPEROR_PENGUIN_EGG.get())
                )
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/large_bird_small_egg")));

        AdvancementHolder fishyBusiness = save(consumer, createTask(root, ModItems.KOI_BUCKET.get(), "fishy_business")
                .addCriterion("fishy_business", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.KOI_BUCKET.get()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/fishy_business")));

        AdvancementHolder makesSense = save(consumer, createTask(fishyBusiness, ModItems.CHORUS_ECHOFIN_BUCKET.get(), "makes_sense")
                .addCriterion(
                        "makes_sense",
                        InventoryChangeTrigger.TriggerInstance.hasItems(
                                ItemPredicate.Builder.item()
                                        .of(
                                                items,
                                                ModItems.CHORUS_ECHOFIN_BUCKET.get(),
                                                ModItems.LEVITATING_ECHOFIN_BUCKET.get()
                                        )
                        )
                )
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/makes_sense")));

        save(consumer, createTask(fishyBusiness, ModItems.KOI_BUCKET.get(), "pearl_of_the_pond")
                .addCriterion("iridescent_white_koi", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/pearl_of_the_pond")));

        save(consumer, createTask(makesSense, ModItems.JELLYFISH_BUCKET.get(), "gotta_catch_em_all")
                .addCriterion("butterfly", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BUTTERFLY_IN_A_BOTTLE.get()))
                .addCriterion("koi", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.KOI_BUCKET.get()))
                .addCriterion(
                        "echofin",
                        InventoryChangeTrigger.TriggerInstance.hasItems(
                                ItemPredicate.Builder.item()
                                        .of(
                                                items,
                                                ModItems.CHORUS_ECHOFIN_BUCKET.get(),
                                                ModItems.LEVITATING_ECHOFIN_BUCKET.get()
                                        )
                        )
                )
                .addCriterion("seahorse", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SEAHORSE_BUCKET.get()))
                .addCriterion("river_turtle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RIVER_TURTLE_BUCKET.get()))
                .addCriterion("jellyfish", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.JELLYFISH_BUCKET.get()))
                .addCriterion("arrowfish", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ARROWFISH_BUCKET.get()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/gotta_catch_em_all")));

        save(consumer, Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.NETHER_STAR,
                        Component.translatable("advancements.bestiary.seriously_all_of_them.title"),
                        Component.translatable("advancements.bestiary.seriously_all_of_them.description"),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("complete_bestiary", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .build(Identifier.parse(ClutterBestiary.MOD_ID + ":bestiary/seriously_all_of_them")));
    }

    private static AdvancementHolder save(Consumer<AdvancementHolder> consumer, AdvancementHolder advancement) {
        consumer.accept(advancement);
        return advancement;
    }

    private static Advancement.Builder createTask(AdvancementHolder parent, ItemLike icon, String name) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(
                        icon.asItem(),
                        Component.translatable("advancements.bestiary." + name + ".title"),
                        Component.translatable("advancements.bestiary." + name + ".description"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                );
    }
}
