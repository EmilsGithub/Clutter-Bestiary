package net.emilsg.clutterbestiary.util;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class ModItemGroups {
    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(ClutterBestiary.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static RegistrySupplier<ItemGroup> CLUTTER_BESTIARY;

    public static void register() {
        CLUTTER_BESTIARY = TABS.register("clutter_bestiary", () -> CreativeTabRegistry.create(Text.translatable("itemgroup.clutterbestiary.item_group"), () -> new ItemStack(ModItems.MOSSBLOOM_SPAWN_EGG)));
        TABS.register();
    }
}
