package net.emilsg.clutterbestiary.fabric;

import net.emilsg.clutterbestiary.ClutterBestiaryClient;
import net.emilsg.clutterbestiary.entity.client.render.ModElytraRenderer;
import net.emilsg.clutterbestiary.fabric.entity.client.player.RendererRegistration;
import net.emilsg.clutterbestiary.fabric.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;

public final class ClutterBestiaryClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClutterBestiaryClient.init();
        RendererRegistration.register();
        ModModelPredicateProvider.register();

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            registrationHelper.register(new ModElytraRenderer<>(entityRenderer, context.getModelLoader()));
        });
    }
}
