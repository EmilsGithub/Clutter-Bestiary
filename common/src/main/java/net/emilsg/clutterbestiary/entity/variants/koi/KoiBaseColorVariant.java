package net.emilsg.clutterbestiary.entity.variants.koi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.BestiaryBasicVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public enum KoiBaseColorVariant implements BestiaryBasicVariant {
    ORANGE("orange", ChatFormatting.GOLD, 0xFF6416, 100, false),
    YELLOW("yellow", ChatFormatting.YELLOW, 0xEFD870, 100, false),
    BLACK("black", ChatFormatting.DARK_GRAY, 0x3E4549, 100, false),
    WHITE("white", ChatFormatting.WHITE, 0xEAEAFF, 100, false),
    IRIDESCENT_WHITE("iridescent_white", ChatFormatting.WHITE, new int[]{0xF9FCFF, 0xFBEBFB, 0xE5E3FF, 0xD1E5FF, 0xC4D9FF, 0xA3CDFF}, 2, true),
    IRIDESCENT_BLUE("iridescent_blue", ChatFormatting.AQUA, new int[]{0xF7FCFF, 0xEDF0FF, 0xD5E8FF, 0xC3E9FF, 0xAEDFFF, 0x96D9FF}, 2, true),
    IRIDESCENT_PINK("iridescent_pink", ChatFormatting.LIGHT_PURPLE, new int[]{0xFEF7FF, 0xFBEBFB, 0xFBDFF7, 0xFADAF5, 0xF7CCEB, 0xF5BFE6}, 2, true),
    IRIDESCENT_PURPLE("iridescent_purple", ChatFormatting.DARK_PURPLE, new int[]{0xFAF8FF, 0xF2E9FF, 0xDDD8FF, 0xD7D2FF, 0xCECBFF, 0xBBBCFF}, 2, true),
    IRIDESCENT_RAINBOW("iridescent_rainbow", ChatFormatting.WHITE, new int[]{0xFF0000, 0xFFA500, 0xFFFF00, 0x008000, 0x0000FF, 0x4B0082, 0x8B00FF}, 1, true),
    PEARL("pearl", ChatFormatting.DARK_AQUA, new int[]{0xFFFFFF, 0xDBECE9, 0xD2D4D6, 0xCCB2B8, 0x8AB6C9, 0x779FC6}, 1, true);

    private static final Map<Identifier, KoiBaseColorVariant> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName()),
                    v -> v
            ));

    public static final Codec<KoiBaseColorVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        var v = BY_ID.get(id);
                        return v != null
                                ? DataResult.success(v)
                                : DataResult.error(() -> "Unknown koi base color: " + id);
                    },
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName())
            );

    private final String name;
    private final ChatFormatting formatting;
    private final int[] colorHex;
    private final int weight;
    private final boolean separateTexture;

    KoiBaseColorVariant(String name, ChatFormatting formatting, int colorHex, int weight, boolean separateTexture) {
        this.name = name;
        this.formatting = formatting;
        this.colorHex = new int[]{colorHex};
        this.weight = weight;
        this.separateTexture = separateTexture;
    }

    KoiBaseColorVariant(String name, ChatFormatting formatting, int[] colorHexArray, int weight, boolean separateTexture) {
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

    @Nullable
    public static Identifier getEmissiveTextureFromEntity(KoiEntity koiEntity) {
        if (!koiEntity.getBaseColorVariant().hasSeparateTexture()) return null;
        String prefix = koiEntity.isBaby() ? "baby_koi_" : "koi_";
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/koi/" + prefix + koiEntity.getBaseColorVariant().getName() + "_emissive.png");
    }

    public int[] getColorHex() {
        return colorHex;
    }

    public ChatFormatting getFormatting() {
        return this.formatting;
    }

    public String getID() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return this.name;
    }

    public Identifier getTextureLocation() {
        return this.hasSeparateTexture() ? Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_" + this.getName() + ".png") : Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_base.png");
    }

    public Identifier getBabyTextureLocation() {
        return this.hasSeparateTexture() ? Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/koi/baby_koi_" + this.getName() + ".png") : Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/koi/baby_koi_base.png");
    }

    public int getWeight() {
        return this.weight;
    }

    public boolean hasSeparateTexture() {
        return this.separateTexture;
    }
}
