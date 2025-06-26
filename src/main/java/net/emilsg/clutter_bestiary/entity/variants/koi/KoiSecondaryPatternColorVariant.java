package net.emilsg.clutter_bestiary.entity.variants.koi;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.variants.BestiaryBasicVariant;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum KoiSecondaryPatternColorVariant implements BestiaryBasicVariant {
    BLACK("black", Formatting.DARK_GRAY,0x1C2226),
    WHITE("white", Formatting.WHITE,0xC3C3E5);

    private final String name;
    private final Formatting colorFormatting;
    private final int colorHex;

    KoiSecondaryPatternColorVariant(String name, Formatting colorFormatting, int colorHex) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.colorHex = colorHex;
    }

    public static KoiSecondaryPatternColorVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getID().equals(id)).findFirst().orElse(BLACK);
    }

    public static KoiSecondaryPatternColorVariant getRandom() {
        List<KoiSecondaryPatternColorVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getID() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }


    public String getName() {
        return this.name;
    }

    public Formatting getFormatting() {
        return this.colorFormatting;
    }

    public int getColorHex() {
        return colorHex;
    }
}
