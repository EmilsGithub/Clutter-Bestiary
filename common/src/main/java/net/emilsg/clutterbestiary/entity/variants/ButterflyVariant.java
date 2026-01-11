package net.emilsg.clutterbestiary.entity.variants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum ButterflyVariant {

    WHITE("white", Formatting.WHITE, 0, Blocks.WHITE_CANDLE),
    LIGHT_GRAY("light_gray", Formatting.GRAY, 1, Blocks.LIGHT_GRAY_CANDLE),
    GRAY("gray", Formatting.DARK_GRAY, 2, Blocks.GRAY_CANDLE),
    BLACK("black", Formatting.BLACK, 3, Blocks.BLACK_CANDLE),
    BROWN("brown", Formatting.GOLD, 4, Blocks.BROWN_CANDLE),
    RED("red", Formatting.DARK_RED, 5, Blocks.RED_CANDLE),
    ORANGE("orange", Formatting.GOLD, 6, Blocks.ORANGE_CANDLE),
    YELLOW("yellow", Formatting.YELLOW, 7, Blocks.YELLOW_CANDLE),
    LIME("lime", Formatting.GREEN, 8, Blocks.LIME_CANDLE),
    GREEN("green", Formatting.DARK_GREEN, 9, Blocks.GREEN_CANDLE),
    LIGHT_BLUE("light_blue", Formatting.BLUE, 10, Blocks.LIGHT_BLUE_CANDLE),
    CYAN("cyan", Formatting.DARK_AQUA, 11, Blocks.CYAN_CANDLE),
    BLUE("blue", Formatting.DARK_BLUE, 12, Blocks.BLUE_CANDLE),
    PURPLE("purple", Formatting.DARK_PURPLE, 13, Blocks.PURPLE_CANDLE),
    MAGENTA("magenta", Formatting.LIGHT_PURPLE, 14, Blocks.MAGENTA_CANDLE),
    PINK("pink", Formatting.RED, 15, Blocks.PINK_CANDLE),
    WARPED("warped", Formatting.DARK_AQUA, 16, true, Blocks.WARPED_FUNGUS),
    CRIMSON("crimson", Formatting.DARK_RED, 17, true, Blocks.CRIMSON_FUNGUS),
    SOUL("soul", Formatting.WHITE, 18, true, Blocks.SOUL_LANTERN);

    private static final Map<Identifier, ButterflyVariant> BY_ID =
            Arrays.stream(values()).collect(java.util.stream.Collectors.toMap(
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName()),
                    v -> v
            ));
    public static final Codec<ButterflyVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        var v = BY_ID.get(id);
                        return v != null
                                ? DataResult.success(v)
                                : DataResult.error(() -> "Unknown butterfly variant: " + id);
                    },
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName())
            );
    private final String name;
    private final Formatting colorFormatting;
    private final boolean isFireImmune;
    private final int ID;
    private final Block variantDecider;

    ButterflyVariant(String name, Formatting colorFormatting, int ID, boolean isFireImmune, Block variantDecider) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.isFireImmune = isFireImmune;
        this.ID = ID;
        this.variantDecider = variantDecider;
    }

    ButterflyVariant(String name, Formatting colorFormatting, int ID, Block variantDecider) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.isFireImmune = false;
        this.ID = ID;
        this.variantDecider = variantDecider;
    }

    public static boolean isVariantDecider(Block block) {
        return Arrays.stream(values()).anyMatch(v -> v.variantDecider == block);
    }

    public static ButterflyVariant fromVariantDecider(Block block) {
        return Arrays.stream(values()).filter(v -> v.variantDecider == block).findFirst().orElse(WHITE);
    }

    public static ButterflyVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(WHITE);
    }

    public static ButterflyVariant getRandom(boolean overworldOnly) {
        List<ButterflyVariant> filtered = Arrays.stream(values()).filter(v -> !overworldOnly || !v.isFireImmune()).toList();
        return filtered.get(new Random().nextInt(filtered.size()));
    }

    public Formatting getColorFormatting() {
        return this.colorFormatting;
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return this.name;
    }

    public int getOrderedID() {
        return this.ID;
    }

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/butterfly/" + getName() + "_butterfly.png");
    }

    public Block getVariantDecider() {
        return this.variantDecider;
    }

    public boolean isFireImmune() {
        return isFireImmune;
    }
}
