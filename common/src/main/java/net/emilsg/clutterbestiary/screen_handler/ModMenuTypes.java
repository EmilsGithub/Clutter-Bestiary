package net.emilsg.clutterbestiary.screen_handler;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.emilsg.clutterbestiary.screen_handler.handler.CoatiScreenHandler;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;

public final class ModMenuTypes {
    private static final DeferredRegister<ScreenHandlerType<?>> MENUS = DeferredRegister.create(ClutterBestiary.MOD_ID, RegistryKeys.SCREEN_HANDLER);

    public static final RegistrySupplier<ScreenHandlerType<CoatiScreenHandler>> COATI =
            MENUS.register("coati", () -> MenuRegistry.ofExtended((syncId, inv, buf) -> {
                int entityId = buf.readInt();
                Entity e = inv.player.getWorld().getEntityById(entityId);
                return new CoatiScreenHandler(syncId, inv, (CoatiEntity) e);
            }));

    public static void register() { MENUS.register(); }
}
