package net.emilsg.clutterbestiary;

import dev.architectury.platform.Platform;
import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.emilsg.clutterbestiary.entity.CommonEntityAttributeRegistry;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModItemGroups;
import net.emilsg.clutterbestiary.util.ModUtil;

public final class ClutterBestiary {
    public static final String MOD_ID = "clutterbestiary";
    public static final boolean IS_TRINKETS_LOADED = Platform.isModLoaded("trinkets");
    public static final boolean IS_ELYTRA_TRINKET_LOADED = Platform.isModLoaded("elytra_trinket");
    public static final boolean IS_CREATE_LOADED = Platform.isModLoaded("create");
    public static final boolean IS_ELYTRA_SLOT_LOADED = Platform.isModLoaded("elytra_slot");
    public static final boolean IS_CURIOS_LOADED = Platform.isModLoaded("curios");

    public static void init() {
        ModConfigManager.loadConfig();

        ModItemGroups.register();
        ModBlocks.register();
        ModItems.register();

        ModEntityTypes.register();

        ModSoundEvents.register();

        ModUtil.registerSpawnRestrictions();

        CommonEntityAttributeRegistry.register();
    }
}
