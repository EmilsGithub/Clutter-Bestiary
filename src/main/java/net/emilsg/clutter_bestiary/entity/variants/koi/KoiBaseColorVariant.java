package net.emilsg.clutter_bestiary.entity.variants.koi;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum KoiBaseColorVariant {
    ORANGE("orange", Formatting.GOLD, 0xFF6416),
    YELLOW("yellow", Formatting.YELLOW,0xF2D33C),
    BLACK("black", Formatting.BLACK,0x3E4549),
    WHITE("white", Formatting.WHITE,0xEAEAFF);

    private final String name;
    private final Formatting colorFormatting;
    private final int colorHex;

    KoiBaseColorVariant(String name, Formatting colorFormatting, int colorHex) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.colorHex = colorHex;
    }

    public static KoiBaseColorVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(ORANGE);
    }

    public static KoiBaseColorVariant getRandom() {
        List<KoiBaseColorVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return this.name;
    }

    public Formatting getColorFormatting() {
        return this.colorFormatting;
    }

    public int getColorHex() {
        return colorHex;
    }

    public Identifier getTextureLocation() {
        return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_base.png");
    }
}
