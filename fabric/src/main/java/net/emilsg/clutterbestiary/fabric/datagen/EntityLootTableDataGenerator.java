package net.emilsg.clutterbestiary.fabric.datagen;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.emilsg.clutterbestiary.entity.variants.MossbloomVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EntityLootTableDataGenerator extends SimpleFabricLootTableProvider {
    private final RegistryWrapper.WrapperLookup registryLookup;

    public EntityLootTableDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.ENTITY);
        this.registryLookup = registryLookup.join();
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        this.register(exporter, ModEntityTypes.CHAMELEON.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                        .with(ItemEntry.builder(Items.WHITE_DYE))
                        .with(ItemEntry.builder(Items.LIGHT_GRAY_DYE))
                        .with(ItemEntry.builder(Items.GRAY_DYE))
                        .with(ItemEntry.builder(Items.BLACK_DYE))
                        .with(ItemEntry.builder(Items.BROWN_DYE))
                        .with(ItemEntry.builder(Items.RED_DYE))
                        .with(ItemEntry.builder(Items.ORANGE_DYE))
                        .with(ItemEntry.builder(Items.YELLOW_DYE))
                        .with(ItemEntry.builder(Items.LIME_DYE))
                        .with(ItemEntry.builder(Items.GREEN_DYE))
                        .with(ItemEntry.builder(Items.CYAN_DYE))
                        .with(ItemEntry.builder(Items.LIGHT_BLUE_DYE))
                        .with(ItemEntry.builder(Items.BLUE_DYE))
                        .with(ItemEntry.builder(Items.PURPLE_DYE))
                        .with(ItemEntry.builder(Items.MAGENTA_DYE))
                        .with(ItemEntry.builder(Items.PINK_DYE))));

        this.register(exporter, ModEntityTypes.BOOPLET.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.STRING)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F), true)))
                        .conditionally(this.booleanNbtCondition("IsFluffy", true))));

        this.register(exporter, ModEntityTypes.CHORUS_BEETLE.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.CHORUS_FLOWER))
                        .conditionally(RandomChanceLootCondition.builder(0.5F)))
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                        .with(ItemEntry.builder(Items.CHORUS_FRUIT)
                                .conditionally(this.fireOrExplosionCondition().invert()))
                        .with(ItemEntry.builder(Items.POPPED_CHORUS_FRUIT)
                                .conditionally(this.fireOrExplosionCondition()))));

        this.register(exporter, ModEntityTypes.CRIMSON_NEWT.get(), this.createNewtLootTable(Items.CRIMSON_FUNGUS));
        this.register(exporter, ModEntityTypes.WARPED_NEWT.get(), this.createNewtLootTable(Items.WARPED_FUNGUS));

        this.register(exporter, ModEntityTypes.ECHOFIN.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.RAW_LEVITATING_ECHOFIN.get())
                                .apply(FurnaceSmeltLootFunction.builder().conditionally(this.onFireCondition()))
                                .conditionally(this.stringNbtCondition("Variant", EchofinVariant.LEVITATING.getId()))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.RAW_CHORUS_ECHOFIN.get())
                                .apply(FurnaceSmeltLootFunction.builder().conditionally(this.onFireCondition()))
                                .conditionally(this.stringNbtCondition("Variant", EchofinVariant.CHORUS.getId())))));

        this.register(exporter, ModEntityTypes.EMBER_TORTOISE.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.MAGMA_BLOCK)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseLootFunction.builder(this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BASALT)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F)))
                                .apply(EnchantedCountIncreaseLootFunction.builder(this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.EMPEROR_PENGUIN.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.0F))
                        .with(ItemEntry.builder(Items.FEATHER)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                        .with(ItemEntry.builder(Items.SALMON)
                                .apply(FurnaceSmeltLootFunction.builder().conditionally(this.onFireCondition()))
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))));

        this.register(exporter, ModEntityTypes.JELLYFISH.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.SLIME_BALL)
                                .apply(EnchantedCountIncreaseLootFunction.builder(this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.KIWI_BIRD.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.FEATHER)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseLootFunction.builder(this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.WOODPECKER.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.FEATHER)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseLootFunction.builder(this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.KOI.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.KOI.get()))));

        this.register(exporter, ModEntityTypes.MOSSBLOOM.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.RAW_VENISON_RIBS.get())
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))
                                        .conditionally(this.stringNbtCondition("Variant", MossbloomVariant.FLOWERING.getId())))
                                .apply(FurnaceSmeltLootFunction.builder().conditionally(this.onFireCondition()))
                                .conditionally(RandomChanceLootCondition.builder(0.5F))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.MOSSBLOOM_ANTLERS.get())
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                                .conditionally(this.booleanNbtCondition("HasHorns", true))
                                .conditionally(this.stringNbtCondition("Variant", MossbloomVariant.HORNED.getId()))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.MOSS_BLOCK)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 4.0F)))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.RAW_VENISON_RIBS.get())
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
                                .apply(FurnaceSmeltLootFunction.builder().conditionally(this.onFireCondition()))
                                .apply(EnchantedCountIncreaseLootFunction.builder(this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))
                                .conditionally(RandomChanceLootCondition.builder(0.5F))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.RAW_VENISON.get())
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F)))
                                .apply(FurnaceSmeltLootFunction.builder().conditionally(this.onFireCondition()))
                                .apply(EnchantedCountIncreaseLootFunction.builder(this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));

        this.register(exporter, ModEntityTypes.ARROWFISH.get(), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.ARROWFISH.get()))));

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

    private LootTable.Builder createNewtLootTable(ItemConvertible fungus) {
        LootPool.Builder pool = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F));
        for (int count = 1; count <= 5; count++) {
            pool.with(ItemEntry.builder(fungus)
                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(count)))
                    .conditionally(this.intNbtCondition("Fungi", count)));
        }
        return LootTable.builder().pool(pool);
    }

    private void register(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter, EntityType<?> entityType, LootTable.Builder builder) {
        exporter.accept(entityType.getLootTableId(), builder.randomSequenceId(Registries.ENTITY_TYPE.getId(entityType)));
    }

    private void registerEmpty(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter, EntityType<?>... entityTypes) {
        for (EntityType<?> entityType : entityTypes) {
            exporter.accept(entityType.getLootTableId(), LootTable.builder());
        }
    }

    private LootCondition.Builder onFireCondition() {
        return EntityPropertiesLootCondition.builder(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true))
        );
    }

    private LootCondition.Builder fireOrExplosionCondition() {
        return DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create()
                        .tag(TagPredicate.expected(DamageTypeTags.IS_FIRE)))
                .or(DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create()
                        .tag(TagPredicate.expected(DamageTypeTags.IS_EXPLOSION))));
    }

    private LootCondition.Builder booleanNbtCondition(String key, boolean value) {
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean(key, value);
        return this.nbtCondition(nbt);
    }

    private LootCondition.Builder intNbtCondition(String key, int value) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(key, value);
        return this.nbtCondition(nbt);
    }

    private LootCondition.Builder stringNbtCondition(String key, String value) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(key, value);
        return this.nbtCondition(nbt);
    }

    private LootCondition.Builder nbtCondition(NbtCompound nbt) {
        return EntityPropertiesLootCondition.builder(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.create().nbt(new NbtPredicate(nbt))
        );
    }
}
