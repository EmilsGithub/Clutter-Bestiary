package net.emilsg.clutterbestiary.util;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(ClutterBestiary.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> CLUTTER_BESTIARY = TABS.register("clutter_bestiary", () ->
            CreativeTabRegistry.create(Component.translatable("itemgroup.clutterbestiary.item_group"), () -> new ItemStack(ModItems.MOSSBLOOM_SPAWN_EGG.get())));

    public static void register() {
        TABS.register();
    }
}
