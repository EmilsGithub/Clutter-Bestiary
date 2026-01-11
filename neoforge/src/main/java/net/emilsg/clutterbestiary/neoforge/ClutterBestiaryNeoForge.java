package net.emilsg.clutterbestiary.neoforge;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.ClutterBestiaryClient;
import net.emilsg.clutterbestiary.entity.client.ArrowfishCrossbowModel;
import net.emilsg.clutterbestiary.menu.ModMenuTypes;
import net.emilsg.clutterbestiary.menu.screen.CoatiInventoryScreen;
import net.emilsg.clutterbestiary.neoforge.spawns.ModBiomeModifierSerializers;
import net.emilsg.clutterbestiary.neoforge.util.ModModelPredicateProvider;
import net.minecraft.client.util.ModelIdentifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(ClutterBestiary.MOD_ID)
public final class ClutterBestiaryNeoForge {

    public ClutterBestiaryNeoForge(IEventBus modEventBus, Dist dist) {
        ClutterBestiary.init();

        ModBiomeModifierSerializers.REGISTER.register(modEventBus);

        if (dist.isClient()) {
            ClutterBestiaryClient.registerEntityRenderers();
            ClutterBestiaryClient.registerEntityModelLayers();
            modEventBus.addListener(this::onClientSetup);
            modEventBus.addListener(this::registerScreens);
            modEventBus.addListener(this::registerAdditionalModels);
            ArrowfishCrossbowModel.setModelLookup(manager -> manager.getModel(ModelIdentifier.standalone(ArrowfishCrossbowModel.MODEL_ID)));
        }
    }

    public void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelIdentifier.standalone(ArrowfishCrossbowModel.MODEL_ID));
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ClutterBestiaryClient::registerClientSetup);
        event.enqueueWork(ModModelPredicateProvider::register);
    }

    public void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COATI.get(), CoatiInventoryScreen::new);
    }

}
