package net.emilsg.clutterbestiary.fabric.datagen;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import net.minecraft.world.item.DyeColor;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.emilsg.clutterbestiary.entity.variants.MossbloomVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.NbtPredicate;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EntityLootTableDataGenerator extends SimpleFabricLootTableSubProvider {
    private final HolderGetter<Enchantment> enchantments;
    private final HolderGetter<DamageType> damageTypes;

    public EntityLootTableDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.ENTITY);
        this.enchantments = registryLookup.join().lookupOrThrow(Registries.ENCHANTMENT);
        this.damageTypes = registryLookup.join().lookupOrThrow(Registries.DAMAGE_TYPE);
    }

    @Override
    public void run() {
        // Tables are produced through generate(BiConsumer), which is what the Fabric loot provider invokes.
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
        this.register(exporter, ModEntityTypes.CHAMELEON.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.between(1, 3))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.WHITE)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.LIGHT_GRAY)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.GRAY)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.BLACK)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.BROWN)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.RED)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.ORANGE)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.YELLOW)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.LIME)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.GREEN)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.CYAN)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.LIGHT_BLUE)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.BLUE)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.PURPLE)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.MAGENTA)))
                        .add(LootItem.lootTableItem(Items.DYE.pick(DyeColor.PINK)))));

        this.register(exporter, ModEntityTypes.BOOPLET.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.STRING)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2), true)))
                        .when(this.booleanNbtCondition("IsFluffy", true))));

        this.register(exporter, ModEntityTypes.CHORUS_BEETLE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.CHORUS_FLOWER))
                        .when(LootItemRandomChanceCondition.randomChance(0.5F)))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.between(1, 3))
                        .add(LootItem.lootTableItem(Items.CHORUS_FRUIT)
                                .when(this.fireOrExplosionCondition().invert()))
                        .add(LootItem.lootTableItem(Items.POPPED_CHORUS_FRUIT)
                                .when(this.fireOrExplosionCondition()))));

        this.register(exporter, ModEntityTypes.CRIMSON_NEWT.get(), this.createNewtLootTable(Items.CRIMSON_FUNGUS));
        this.register(exporter, ModEntityTypes.WARPED_NEWT.get(), this.createNewtLootTable(Items.WARPED_FUNGUS));

        this.register(exporter, ModEntityTypes.ECHOFIN.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.RAW_LEVITATING_ECHOFIN.get())
                                .apply(SmeltItemFunction.smelted().when(this.onFireCondition()))
                                .when(this.stringNbtCondition("Variant", EchofinVariant.LEVITATING.getId()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.RAW_CHORUS_ECHOFIN.get())
                                .apply(SmeltItemFunction.smelted().when(this.onFireCondition()))
                                .when(this.stringNbtCondition("Variant", EchofinVariant.CHORUS.getId())))));

        this.register(exporter, ModEntityTypes.EMBER_TORTOISE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.MAGMA_BLOCK)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.BASALT)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 4)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.EMPEROR_PENGUIN.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(2))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
                        .add(LootItem.lootTableItem(Items.SALMON)
                                .apply(SmeltItemFunction.smelted().when(this.onFireCondition()))
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))));

        this.register(exporter, ModEntityTypes.JELLYFISH.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.SLIME_BALL)
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.KIWI_BIRD.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.WOODPECKER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.KOI.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.KOI.get()))));

        this.register(exporter, ModEntityTypes.MOSSBLOOM.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.RAW_VENISON_RIBS.get())
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))
                                        .when(this.stringNbtCondition("Variant", MossbloomVariant.FLOWERING.getId())))
                                .apply(SmeltItemFunction.smelted().when(this.onFireCondition()))
                                .when(LootItemRandomChanceCondition.randomChance(0.5F))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.MOSSBLOOM_ANTLERS.get())
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(2)))
                                .when(this.booleanNbtCondition("HasHorns", true))
                                .when(this.stringNbtCondition("Variant", MossbloomVariant.HORNED.getId()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.MOSS_BLOCK)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 4)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.RAW_VENISON_RIBS.get())
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                .apply(SmeltItemFunction.smelted().when(this.onFireCondition()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F)))
                                .when(LootItemRandomChanceCondition.randomChance(0.5F))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.RAW_VENISON.get())
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3)))
                                .apply(SmeltItemFunction.smelted().when(this.onFireCondition()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.ARROWFISH.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.ARROWFISH.get()))));

        this.registerEmpty(exporter,
                ModEntityTypes.BUTTERFLY.get(),
                ModEntityTypes.BUTTERFLY_LARVA.get(),
                ModEntityTypes.BEAVER.get(),
                ModEntityTypes.CAPYBARA.get(),
                ModEntityTypes.MANTA_RAY.get(),
                ModEntityTypes.SEAHORSE.get(),
                ModEntityTypes.POTION_WASP.get(),
                ModEntityTypes.POTION_SAC.get(),
                ModEntityTypes.DRAGONFLY.get(),
                ModEntityTypes.KOI_EGGS.get(),
                ModEntityTypes.RIVER_TURTLE.get(),
                ModEntityTypes.COATI.get(),
                ModEntityTypes.RED_PANDA.get(),
                ModEntityTypes.STOAT.get(),
                ModEntityTypes.CROCODILE.get()
        );
    }

    private LootTable.Builder createNewtLootTable(ItemLike fungus) {
        LootPool.Builder pool = LootPool.lootPool().setRolls(ContextIntProviders.exactly(1));
        for (int count = 1; count <= 5; count++) {
            pool.add(LootItem.lootTableItem(fungus)
                    .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(count)))
                    .when(this.intNbtCondition("Fungi", count)));
        }
        return LootTable.lootTable().withPool(pool);
    }

    private void register(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter, EntityType<?> entityType, LootTable.Builder builder) {
        exporter.accept(entityType.getDefaultLootTable().orElseThrow(), builder.setRandomSequence(BuiltInRegistries.ENTITY_TYPE.getKey(entityType)));
    }

    private void registerEmpty(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter, EntityType<?>... entityTypes) {
        for (EntityType<?> entityType : entityTypes) {
            exporter.accept(entityType.getDefaultLootTable().orElseThrow(), LootTable.lootTable());
        }
    }

    private LootItemCondition.Builder onFireCondition() {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))
        );
    }

    private LootItemCondition.Builder fireOrExplosionCondition() {
        return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
                        .tag(TagPredicate.is(this.damageTypes, DamageTypeTags.IS_FIRE)))
                .or(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
                        .tag(TagPredicate.is(this.damageTypes, DamageTypeTags.IS_EXPLOSION))));
    }

    private LootItemCondition.Builder booleanNbtCondition(String key, boolean value) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean(key, value);
        return this.nbtCondition(nbt);
    }

    private LootItemCondition.Builder intNbtCondition(String key, int value) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(key, value);
        return this.nbtCondition(nbt);
    }

    private LootItemCondition.Builder stringNbtCondition(String key, String value) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString(key, value);
        return this.nbtCondition(nbt);
    }

    private LootItemCondition.Builder nbtCondition(CompoundTag nbt) {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().nbt(new NbtPredicate(nbt))
        );
    }
}
