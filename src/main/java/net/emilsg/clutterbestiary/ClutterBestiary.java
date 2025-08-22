package net.emilsg.clutterbestiary;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.compat.trinkets.TrinketsElytraUse;
import net.emilsg.clutterbestiary.config.Configs;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.entity.BestiaryAttributes;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModItemGroups;
import net.emilsg.clutterbestiary.util.ModModelPredicateProvider;
import net.emilsg.clutterbestiary.world.ModEntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClutterBestiary implements ModInitializer {
    public static final String MOD_ID = "clutterbestiary";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean IS_TRINKETS_LOADED = FabricLoader.getInstance().getModContainer("trinkets").isPresent();
    public static final boolean IS_ELYTRA_TRINKET_LOADED = FabricLoader.getInstance().getModContainer("elytra_trinket").isPresent();

    @Override
    public void onInitialize() {
        ModConfigManager.loadConfig();

        ModItems.register();
        ModBlocks.register();

        ModItemGroups.register();
        ModEntityTypes.register();
        ModSoundEvents.register();
        BestiaryAttributes.register();
        ModModelPredicateProvider.register();
        ModEntitySpawns.register();

        if (IS_TRINKETS_LOADED && ModConfigManager.get(Configs.doTrinketsElytraFlight, true)) TrinketsElytraUse.doFlight();
    }
}