package net.emilsg.clutter_bestiary;

import net.emilsg.clutter_bestiary.entity.ModEntityTypes;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.*;
import net.emilsg.clutter_bestiary.entity.client.player.RendererRegistration;
import net.emilsg.clutter_bestiary.entity.client.render.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ClutterBestiaryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        RendererRegistration.register();
        registerEntityModelLayers();
        registerEntityRenderers();
    }


    private void registerEntityModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.NETHER_NEWT, NetherNewtModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BEAVER, BeaverModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BUTTERFLY, ButterflyModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.EMBER_TORTOISE, EmberTortoiseModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.JELLYFISH, JellyfishModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.MANTA_RAY, MantaRayModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CAPYBARA, CapybaraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CHAMELEON, ChameleonModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.KIWI_BIRD, KiwiBirdModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.MOSSBLOOM, MossbloomModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.EMPEROR_PENGUIN, EmperorPenguinModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BABY_EMPEROR_PENGUIN, BabyEmperorPenguinModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.ECHOFIN, EchofinModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.SEAHORSE, SeahorseModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.POTION_WASP, PotionWaspModel::getTexturedModelData);
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntityTypes.CHAMELEON, ChameleonRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.BUTTERFLY, ButterflyRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.CRIMSON_NEWT, CrimsonNewtRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.WARPED_NEWT, WarpedNewtRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.BEAVER, BeaverRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.EMBER_TORTOISE, EmberTortoiseRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.JELLYFISH, JellyfishRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.MANTA_RAY, MantaRayRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.CAPYBARA, CapybaraRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.ECHOFIN, EchofinRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.SEAHORSE, SeahorseRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.KIWI_BIRD, KiwiBirdRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.MOSSBLOOM, MossbloomRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.EMPEROR_PENGUIN, EmperorPenguinRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.POTION_WASP, PotionWaspRenderer::new);
    }
}
