package net.emilsg.clutter_bestiary.entity.variants.koi;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.custom.KoiEntity;
import net.emilsg.clutter_bestiary.entity.variants.BestiaryBasicVariant;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Random;

public enum KoiBaseColorVariant implements BestiaryBasicVariant {
    ORANGE("orange", Formatting.GOLD, 0xFF6416, 100, false),
    YELLOW("yellow", Formatting.YELLOW, 0xEFD870, 100, false),
    BLACK("black", Formatting.DARK_GRAY, 0x3E4549, 100, false),
    WHITE("white", Formatting.WHITE, 0xEAEAFF, 100, false),
    IRIDESCENT_WHITE("iridescent_white", Formatting.WHITE, new int[]{0xF9FCFF, 0xFBEBFB, 0xE5E3FF, 0xD1E5FF, 0xC4D9FF, 0xA3CDFF}, 2, true),
    IRIDESCENT_BLUE("iridescent_blue", Formatting.AQUA, new int[]{0xF7FCFF, 0xEDF0FF, 0xD5E8FF, 0xC3E9FF, 0xAEDFFF, 0x96D9FF}, 2, true),
    IRIDESCENT_PINK("iridescent_pink", Formatting.LIGHT_PURPLE, new int[]{0xFEF7FF, 0xFBEBFB, 0xFBDFF7, 0xFADAF5, 0xF7CCEB, 0xF5BFE6}, 2, true),
    IRIDESCENT_PURPLE("iridescent_purple", Formatting.DARK_PURPLE, new int[]{0xFAF8FF, 0xF2E9FF, 0xDDD8FF, 0xD7D2FF, 0xCECBFF, 0xBBBCFF}, 2, true),
    IRIDESCENT_RAINBOW("iridescent_rainbow", Formatting.WHITE, new int[]{0xFF0000, 0xFFA500, 0xFFFF00, 0x008000, 0x0000FF, 0x4B0082, 0x8B00FF}, 1, true),
    PEARL("pearl", Formatting.DARK_AQUA, new int[]{0xFFFFFF, 0xDBECE9, 0xD2D4D6, 0xCCB2B8, 0x8AB6C9, 0x779FC6}, 1, true);

    private final String name;
    private final Formatting formatting;
    private final int[] colorHex;
    private final int weight;
    private final boolean separateTexture;

    KoiBaseColorVariant(String name, Formatting formatting, int colorHex, int weight, boolean separateTexture) {
        this.name = name;
        this.formatting = formatting;
        this.colorHex = new int[]{colorHex};
        this.weight = weight;
        this.separateTexture = separateTexture;
    }

    KoiBaseColorVariant(String name, Formatting formatting, int[] colorHexArray, int weight, boolean separateTexture) {
        this.name = name;
        this.formatting = formatting;
        this.colorHex = colorHexArray;
        this.weight = weight;
        this.separateTexture = separateTexture;
    }

    public static KoiBaseColorVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getID().equals(id)).findFirst().orElse(ORANGE);
    }

    public static KoiBaseColorVariant getRandom() {
        Random random = new Random();
        int totalWeight = Arrays.stream(values()).mapToInt(KoiBaseColorVariant::getWeight).sum();
        int roll = random.nextInt(totalWeight);

        int cumulative = 0;
        for (KoiBaseColorVariant variant : values()) {
            cumulative += variant.getWeight();
            if (roll < cumulative) {
                return variant;
            }
        }

        return ORANGE;
    }

    public String getID() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return this.name;
    }

    public Formatting getFormatting() {
        return this.formatting;
    }

    public boolean hasSeparateTexture() {
        return this.separateTexture;
    }

    public int[] getColorHex() {
        return colorHex;
    }

    public int getWeight() {
        return this.weight;
    }

    public Identifier getTextureLocation() {
        return this.hasSeparateTexture() ? Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_" + this.getName() + ".png") : Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_base.png");
    }

    @Nullable
    public static Identifier getEmissiveTextureFromEntity(KoiEntity koiEntity) {
        return koiEntity.getBaseColorVariant().hasSeparateTexture() ? Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_" + koiEntity.getBaseColorVariant().getName() + "_emissive.png") : null;
    }
}
