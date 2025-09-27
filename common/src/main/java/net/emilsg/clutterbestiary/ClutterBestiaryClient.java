package net.emilsg.clutterbestiary;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.*;
import net.emilsg.clutterbestiary.entity.client.render.*;

public final class ClutterBestiaryClient {

    public static void init() {
        registerEntityRenderers();
        registerEntityModelLayers();
    }

    private static void registerEntityRenderers() {
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
        EntityRendererRegistry.register(ModEntityTypes.POTION_SAC, PotionSacRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.DRAGONFLY, DragonflyRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.BOOPLET, BoopletRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.KOI, KoiRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.KOI_EGGS, KoiEggsRenderer::new);

    }

    private static void registerEntityModelLayers() {
        EntityModelLayerRegistry.register(ModModelLayers.NETHER_NEWT, NetherNewtModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.BEAVER, BeaverModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.BUTTERFLY, ButterflyModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.EMBER_TORTOISE, EmberTortoiseModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.JELLYFISH, JellyfishModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.MANTA_RAY, MantaRayModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.CAPYBARA, CapybaraModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.CHAMELEON, ChameleonModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.KIWI_BIRD, KiwiBirdModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.MOSSBLOOM, MossbloomModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.EMPEROR_PENGUIN, EmperorPenguinModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.BABY_EMPEROR_PENGUIN, BabyEmperorPenguinModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.ECHOFIN, EchofinModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.SEAHORSE, SeahorseModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.POTION_WASP, PotionWaspModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.POTION_SAC, PotionSacModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.DRAGONFLY, DragonflyModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.BOOPLET, BoopletModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.KOI_BASE, KoiModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.KOI_PRIMARY_COLOR, KoiModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.KOI_SECONDARY_COLOR, KoiModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ModModelLayers.KOI_EGGS, KoiEggsModel::getTexturedModelData);
    }
}
