package net.emilsg.clutterbestiary.fabric;

import net.emilsg.clutterbestiary.ClutterBestiaryClient;
import net.emilsg.clutterbestiary.entity.client.ArrowfishCrossbowModel;
import net.emilsg.clutterbestiary.fabric.entity.client.player.RendererRegistration;
import net.emilsg.clutterbestiary.fabric.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public final class ClutterBestiaryClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClutterBestiaryClient.init();
        RendererRegistration.register();
        ModModelPredicateProvider.register();

        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(ArrowfishCrossbowModel.MODEL_ID));
        ArrowfishCrossbowModel.setModelLookup(manager -> manager.getModel(ArrowfishCrossbowModel.MODEL_ID));
    }

}
