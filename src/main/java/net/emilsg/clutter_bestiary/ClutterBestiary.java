package net.emilsg.clutter_bestiary;

import net.emilsg.clutter_bestiary.block.ModBlocks;
import net.emilsg.clutter_bestiary.compat.trinkets.TrinketsElytraUse;
import net.emilsg.clutter_bestiary.config.Configs;
import net.emilsg.clutter_bestiary.config.ModConfigManager;
import net.emilsg.clutter_bestiary.entity.BestiaryAttributes;
import net.emilsg.clutter_bestiary.entity.ModEntityTypes;
import net.emilsg.clutter_bestiary.item.ModItems;
import net.emilsg.clutter_bestiary.sound.ModSoundEvents;
import net.emilsg.clutter_bestiary.util.ModItemGroups;
import net.emilsg.clutter_bestiary.util.ModModelPredicateProvider;
import net.emilsg.clutter_bestiary.world.ModEntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClutterBestiary implements ModInitializer {
	public static final String MOD_ID = "clutter-bestiary";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean IS_TRINKETS_LOADED = FabricLoader.getInstance().getModContainer("trinkets").isPresent();
	public static final boolean IS_ELYTRA_TRINKET_LOADED = FabricLoader.getInstance().getModContainer("elytra_trinket").isPresent();

	@Override
	public void onInitialize() {
		ModConfigManager.loadConfig();

		ModItems.register();
		ModItemGroups.register();
		ModBlocks.register();
		ModEntityTypes.register();

		BestiaryAttributes.register();
		ModSoundEvents.register();

		ModModelPredicateProvider.register();
		if (ModConfigManager.get(Configs.spawnBestiaryMobs, true)) ModEntitySpawns.register();
		if (IS_TRINKETS_LOADED && ModConfigManager.get(Configs.doTrinketsElytraFlight, true)) TrinketsElytraUse.doFlight();
	}
}