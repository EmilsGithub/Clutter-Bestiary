package net.emilsg.clutter_bestiary.util;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.item.ModItems;
import net.emilsg.clutter_bestiary.item.custom.BestiarySpawnEggItem;
import net.emilsg.clutter_bestiary.item.custom.ButterflyElytraItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> CLUTTER_BESTIARY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(ClutterBestiary.MOD_ID, "clutter-bestiary"));

    public static void register() {

        Registry.register(Registries.ITEM_GROUP, CLUTTER_BESTIARY, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.clutter-bestiary.item_group"))
                .icon(() -> new ItemStack(ModItems.MOSSBLOOM_SPAWN_EGG))
                .entries((displayContext, entries) -> {

                    entries.add(ModItems.SEAHORSE_BUCKET);
                    entries.add(ModItems.CHORUS_ECHOFIN_BUCKET);
                    entries.add(ModItems.LEVITATING_ECHOFIN_BUCKET);

                    entries.add(ModItems.RAW_CHORUS_ECHOFIN);
                    entries.add(ModItems.COOKED_CHORUS_ECHOFIN);
                    entries.add(ModItems.RAW_LEVITATING_ECHOFIN);
                    entries.add(ModItems.COOKED_LEVITATING_ECHOFIN);
                    entries.add(ModItems.RAW_VENISON);
                    entries.add(ModItems.COOKED_VENISON);
                    entries.add(ModItems.RAW_VENISON_RIBS);
                    entries.add(ModItems.COOKED_VENISON_RIBS);

                    entries.add(ModItems.MOSSBLOOM_ANTLERS);
                    entries.add(ModItems.KIWI_BIRD_EGG);
                    entries.add(ModItems.EMPEROR_PENGUIN_EGG);
                    entries.add(ModItems.BUTTERFLY_COCOON);
                    entries.add(ModItems.BUTTERFLY_IN_A_BOTTLE);
                    entries.add(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS);
                    entries.add(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE);

                    for (Item item : Registries.ITEM) {
                        if (item instanceof ButterflyElytraItem elytraItem) entries.add(elytraItem);
                        if (item instanceof BestiarySpawnEggItem spawnEggItem) entries.add(spawnEggItem);
                    }
                }).build());

    }
}
