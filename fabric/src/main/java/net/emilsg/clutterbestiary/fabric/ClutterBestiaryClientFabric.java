package net.emilsg.clutterbestiary.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import net.emilsg.clutterbestiary.ClutterBestiaryClient;
import net.emilsg.clutterbestiary.fabric.entity.client.player.RendererRegistration;
import net.emilsg.clutterbestiary.fabric.util.ModModelPredicateProvider;
import net.emilsg.clutterbestiary.menu.ModMenuTypes;
import net.emilsg.clutterbestiary.menu.screen.CoatiInventoryScreen;
import net.fabricmc.api.ClientModInitializer;

public final class ClutterBestiaryClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClutterBestiaryClient.init();
        RendererRegistration.register();
        ModModelPredicateProvider.register();
        registerFabricMenus();
    }

    public static void registerFabricMenus() {
        MenuRegistry.registerScreenFactory(ModMenuTypes.COATI.get(), CoatiInventoryScreen::new);
    }

}
