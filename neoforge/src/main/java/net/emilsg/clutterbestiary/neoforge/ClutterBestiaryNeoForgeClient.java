package net.emilsg.clutterbestiary.neoforge;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.ClutterBestiaryClient;
import net.emilsg.clutterbestiary.neoforge.util.ModModelPredicateProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ClutterBestiary.MOD_ID, dist = Dist.CLIENT)
public class ClutterBestiaryNeoForgeClient {

    public ClutterBestiaryNeoForgeClient() {
        ClutterBestiaryClient.init();
        ModModelPredicateProvider.register();

    }
}
