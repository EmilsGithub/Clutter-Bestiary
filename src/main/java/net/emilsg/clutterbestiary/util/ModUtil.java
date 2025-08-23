package net.emilsg.clutterbestiary.util;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModUtil {

    public static Text buildCyclicFormattedName(String translationKey, int[] colorCycle, int tickOffset, boolean reverse) {
        MutableText finalText = Text.literal("");
        String translated = Text.translatable(translationKey).getString();

        int cycleLength = colorCycle.length;

        for (int i = 0; i < translated.length(); i++) {
            int colorIndex;

            if (reverse) {
                colorIndex = (i - tickOffset) % cycleLength;
            } else {
                colorIndex = (i + tickOffset) % cycleLength;
            }

            if (colorIndex < 0) {
                colorIndex += cycleLength;
            }

            int rgb = colorCycle[colorIndex];
            finalText.append(Text.literal(String.valueOf(translated.charAt(i)))
                    .styled(style -> style.withColor(rgb)));
        }

        return finalText;
    }

    public static void grantImpossibleAdvancement(String path, ServerWorld world, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            MinecraftServer server = world.getServer();
            AdvancementEntry advancement = server.getAdvancementLoader().get(Identifier.of(ClutterBestiary.MOD_ID, path));

            if (serverPlayer.getAdvancementTracker().getProgress(advancement).isDone()) return;

            if (advancement != null) {
                AdvancementProgress progress = serverPlayer.getAdvancementTracker().getProgress(advancement);
                if (!progress.isDone()) {
                    for (String criterion : progress.getUnobtainedCriteria()) {
                        serverPlayer.getAdvancementTracker().grantCriterion(advancement, criterion);
                    }
                }
            }
        }
    }



}
