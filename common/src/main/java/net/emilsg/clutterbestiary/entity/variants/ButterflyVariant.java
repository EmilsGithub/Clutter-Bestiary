package net.emilsg.clutterbestiary.entity.variants;

import net.minecraft.world.item.DyeColor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum ButterflyVariant {

    WHITE("white", ChatFormatting.WHITE, 0, Blocks.DYED_CANDLE.pick(DyeColor.WHITE)),
    LIGHT_GRAY("light_gray", ChatFormatting.GRAY, 1, Blocks.DYED_CANDLE.pick(DyeColor.LIGHT_GRAY)),
    GRAY("gray", ChatFormatting.DARK_GRAY, 2, Blocks.DYED_CANDLE.pick(DyeColor.GRAY)),
    BLACK("black", ChatFormatting.BLACK, 3, Blocks.DYED_CANDLE.pick(DyeColor.BLACK)),
    BROWN("brown", ChatFormatting.GOLD, 4, Blocks.DYED_CANDLE.pick(DyeColor.BROWN)),
    RED("red", ChatFormatting.DARK_RED, 5, Blocks.DYED_CANDLE.pick(DyeColor.RED)),
    ORANGE("orange", ChatFormatting.GOLD, 6, Blocks.DYED_CANDLE.pick(DyeColor.ORANGE)),
    YELLOW("yellow", ChatFormatting.YELLOW, 7, Blocks.DYED_CANDLE.pick(DyeColor.YELLOW)),
    LIME("lime", ChatFormatting.GREEN, 8, Blocks.DYED_CANDLE.pick(DyeColor.LIME)),
    GREEN("green", ChatFormatting.DARK_GREEN, 9, Blocks.DYED_CANDLE.pick(DyeColor.GREEN)),
    LIGHT_BLUE("light_blue", ChatFormatting.BLUE, 10, Blocks.DYED_CANDLE.pick(DyeColor.LIGHT_BLUE)),
    CYAN("cyan", ChatFormatting.DARK_AQUA, 11, Blocks.DYED_CANDLE.pick(DyeColor.CYAN)),
    BLUE("blue", ChatFormatting.DARK_BLUE, 12, Blocks.DYED_CANDLE.pick(DyeColor.BLUE)),
    PURPLE("purple", ChatFormatting.DARK_PURPLE, 13, Blocks.DYED_CANDLE.pick(DyeColor.PURPLE)),
    MAGENTA("magenta", ChatFormatting.LIGHT_PURPLE, 14, Blocks.DYED_CANDLE.pick(DyeColor.MAGENTA)),
    PINK("pink", ChatFormatting.RED, 15, Blocks.DYED_CANDLE.pick(DyeColor.PINK)),
    WARPED("warped", ChatFormatting.DARK_AQUA, 16, true, Blocks.WARPED_FUNGUS),
    CRIMSON("crimson", ChatFormatting.DARK_RED, 17, true, Blocks.CRIMSON_FUNGUS),
    SOUL("soul", ChatFormatting.WHITE, 18, true, Blocks.SOUL_LANTERN);

    private static final Map<Identifier, ButterflyVariant> BY_ID =
            Arrays.stream(values()).collect(java.util.stream.Collectors.toMap(
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName()),
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
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName())
            );
    private final String name;
    private final ChatFormatting colorFormatting;
    private final boolean isFireImmune;
    private final int ID;
    private final Block variantDecider;

    ButterflyVariant(String name, ChatFormatting colorFormatting, int ID, boolean isFireImmune, Block variantDecider) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.isFireImmune = isFireImmune;
        this.ID = ID;
        this.variantDecider = variantDecider;
    }

    ButterflyVariant(String name, ChatFormatting colorFormatting, int ID, Block variantDecider) {
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

    public ChatFormatting getColorFormatting() {
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
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/butterfly/" + getName() + "_butterfly.png");
    }

    public Block getVariantDecider() {
        return this.variantDecider;
    }

    public boolean isFireImmune() {
        return isFireImmune;
    }
}
