package net.emilsg.clutterbestiary.neoforge;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.neoforge.util.ModModelPredicateProvider;
import net.neoforged.fml.common.Mod;

@Mod(ClutterBestiary.MOD_ID)
public final class ClutterBestiaryNeoForge {

    public ClutterBestiaryNeoForge() {
        ClutterBestiary.init();
    }
}
