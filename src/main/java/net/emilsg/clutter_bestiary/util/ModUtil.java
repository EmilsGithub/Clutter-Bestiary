package net.emilsg.clutter_bestiary.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

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



}
