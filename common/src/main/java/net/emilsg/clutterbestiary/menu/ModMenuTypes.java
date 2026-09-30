package net.emilsg.clutterbestiary.menu;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.emilsg.clutterbestiary.menu.handler.CoatiScreenHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;

public final class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ClutterBestiary.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<CoatiScreenHandler>> COATI =
            MENUS.register("coati_screen", () -> MenuRegistry.ofExtended((syncId, inv, entityId) -> {
                Entity e = inv.player.level().getEntity(entityId);
                return new CoatiScreenHandler(syncId, inv, (CoatiEntity) e);
            }, CoatiScreenHandler.ENTITY_ID_CODEC));

    public static void register() {
        MENUS.register();
    }
}
