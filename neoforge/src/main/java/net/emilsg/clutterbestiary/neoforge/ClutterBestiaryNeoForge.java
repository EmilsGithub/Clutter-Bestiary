package net.emilsg.clutterbestiary.neoforge;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.ClutterBestiaryClient;
import net.emilsg.clutterbestiary.block.ICutoutRenderable;
import net.emilsg.clutterbestiary.block.entity.ModBlockEntityTypes;
import net.emilsg.clutterbestiary.block.entity.renderer.ButterflyBottleBlockEntityRenderer;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.*;
import net.emilsg.clutterbestiary.entity.client.render.*;
import net.emilsg.clutterbestiary.neoforge.entity.NeoForgeEntitySpawnConfigHandler;
import net.emilsg.clutterbestiary.neoforge.util.ModModelPredicateProvider;
import net.emilsg.clutterbestiary.menu.ModMenuTypes;
import net.emilsg.clutterbestiary.menu.screen.CoatiInventoryScreen;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

import java.util.function.BooleanSupplier;

@Mod(ClutterBestiary.MOD_ID)
public final class ClutterBestiaryNeoForge {

    public ClutterBestiaryNeoForge(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
        ClutterBestiary.init();

        modEventBus.addListener(this::onCommonSetup);
        NeoForge.EVENT_BUS.addListener(this::checkSpawnConditionEvent);

        if (dist.isClient()) {
            modEventBus.addListener(this::onClientSetup);
            modEventBus.addListener(this::registerEntityRenders);
            modEventBus.addListener(this::registerScreens);
            modEventBus.addListener(this::registerEntityLayers);
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NeoForgeEntitySpawnConfigHandler::register);
    }

    private void checkSpawnConditionEvent(MobSpawnEvent.PositionCheck event) {
        EntityType<?> type = event.getEntity().getType();

        BooleanSupplier configCheck = NeoForgeEntitySpawnConfigHandler.SPAWN_CONFIG.get(type);
        if (configCheck != null && !configCheck.getAsBoolean()) {
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ModModelPredicateProvider::register);
        event.enqueueWork(ClutterBestiaryClient::registerCutoutRenderable);
        event.enqueueWork(ClutterBestiaryClient::registerBlockEntityRenderers);
    }

    public void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COATI.get(), CoatiInventoryScreen::new);
    }

    public void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.NETHER_NEWT, NetherNewtModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.BEAVER, BeaverModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.BUTTERFLY, ButterflyModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.EMBER_TORTOISE, EmberTortoiseModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.JELLYFISH, JellyfishModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.MANTA_RAY, MantaRayModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.CAPYBARA, CapybaraModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.CHAMELEON, ChameleonModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.KIWI_BIRD, KiwiBirdModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.MOSSBLOOM, MossbloomModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.EMPEROR_PENGUIN, EmperorPenguinModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.BABY_EMPEROR_PENGUIN, BabyEmperorPenguinModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.ECHOFIN, EchofinModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.SEAHORSE, SeahorseModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.POTION_WASP, PotionWaspModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.POTION_SAC, PotionSacModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.DRAGONFLY, DragonflyModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.BOOPLET, BoopletModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.KOI_BASE, KoiModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.KOI_PRIMARY_COLOR, KoiModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.KOI_SECONDARY_COLOR, KoiModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.KOI_EGGS, KoiEggsModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.RIVER_TURTLE, RiverTurtleModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.COATI, CoatiModel::getTexturedModelData);
        event.registerLayerDefinition(ModModelLayers.RED_PANDA, RedPandaModel::getTexturedModelData);
    }

    public void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.CHAMELEON.get(), ChameleonRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BUTTERFLY.get(), ButterflyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CRIMSON_NEWT.get(), CrimsonNewtRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WARPED_NEWT.get(), WarpedNewtRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BEAVER.get(), BeaverRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.EMBER_TORTOISE.get(), EmberTortoiseRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.JELLYFISH.get(), JellyfishRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MANTA_RAY.get(), MantaRayRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CAPYBARA.get(), CapybaraRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ECHOFIN.get(), EchofinRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SEAHORSE.get(), SeahorseRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.KIWI_BIRD.get(), KiwiBirdRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MOSSBLOOM.get(), MossbloomRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.EMPEROR_PENGUIN.get(), EmperorPenguinRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.POTION_WASP.get(), PotionWaspRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.POTION_SAC.get(), PotionSacRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BOOPLET.get(), BoopletRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.KOI.get(), KoiRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.KOI_EGGS.get(), KoiEggsRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.RIVER_TURTLE.get(), RiverTurtleRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.COATI.get(), CoatiRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.RED_PANDA.get(), RedPandaRenderer::new);
    }

}
