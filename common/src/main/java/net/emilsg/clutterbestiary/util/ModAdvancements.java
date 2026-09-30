package net.emilsg.clutterbestiary.util;

import dev.architectury.event.events.common.PlayerEvent;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class ModAdvancements {
    public static final String DAM_GOOD_WORK = "bestiary/dam_good_work";
    public static final String BUSY_BEAVER = "bestiary/busy_beaver";
    public static final String PACK_RAT = "bestiary/pack_rat";
    public static final String PEARL_OF_THE_POND = "bestiary/pearl_of_the_pond";
    public static final String MELON_FRIENDS = "bestiary/melon_friends";
    public static final String THREE_COURSE_MEAL = "bestiary/three_course_meal";
    public static final String STOATALLY_YOURS = "bestiary/stoatally_yours";
    public static final String A_CROC_OF_TRUST = "bestiary/a_croc_of_trust";
    public static final String SPECIAL_DELIVERY = "bestiary/special_delivery";
    public static final String FISH_SLAP = "bestiary/fish_slap";
    public static final String SERIOUSLY_ALL_OF_THEM = "bestiary/seriously_all_of_them";

    private ModAdvancements() {
    }

    public static void register() {
        PlayerEvent.PLAYER_ADVANCEMENT.register(ModAdvancements::onAdvancementEarned);
    }

    public static void grant(ServerPlayer player, String path) {
        MinecraftServer server = player.level().getServer();
        if (server == null) return;

        AdvancementHolder advancement = server.getAdvancements().get(Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, path));
        if (advancement == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone()) return;

        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }

    public static void grant(Player player, String path) {
        if (player instanceof ServerPlayer serverPlayer) {
            grant(serverPlayer, path);
        }
    }

    private static void onAdvancementEarned(ServerPlayer player, AdvancementHolder earnedAdvancement) {
        Identifier earnedId = earnedAdvancement.id();
        if (!earnedId.getNamespace().equals(ClutterBestiary.MOD_ID)) return;
        if (!earnedId.getPath().startsWith("bestiary/")) return;
        if (earnedId.getPath().equals(SERIOUSLY_ALL_OF_THEM)) return;

        MinecraftServer server = player.level().getServer();
        if (server == null) return;

        boolean hasCompletedBestiary = server.getAdvancements().getAllAdvancements().stream()
                .filter(advancement -> advancement.id().getNamespace().equals(ClutterBestiary.MOD_ID))
                .filter(advancement -> advancement.id().getPath().startsWith("bestiary/"))
                .filter(advancement -> !advancement.id().getPath().equals("bestiary/root"))
                .filter(advancement -> !advancement.id().getPath().equals(SERIOUSLY_ALL_OF_THEM))
                .allMatch(advancement -> player.getAdvancements().getOrStartProgress(advancement).isDone());

        if (hasCompletedBestiary) {
            grant(player, SERIOUSLY_ALL_OF_THEM);
        }
    }
}
