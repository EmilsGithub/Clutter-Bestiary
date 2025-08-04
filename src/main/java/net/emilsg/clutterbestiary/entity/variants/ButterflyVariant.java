package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum ButterflyVariant {

    WHITE("white", Formatting.WHITE),
    LIGHT_GRAY("light_gray", Formatting.GRAY),
    GRAY("gray", Formatting.DARK_GRAY),
    BLACK("black", Formatting.BLACK),
    BROWN("brown", Formatting.GOLD),
    RED("red", Formatting.DARK_RED),
    ORANGE("orange", Formatting.GOLD),
    YELLOW("yellow", Formatting.YELLOW),
    LIME("lime", Formatting.GREEN),
    GREEN("green", Formatting.DARK_GREEN),
    LIGHT_BLUE("light_blue", Formatting.BLUE),
    CYAN("cyan", Formatting.DARK_AQUA),
    BLUE("blue", Formatting.DARK_BLUE),
    PURPLE("purple", Formatting.DARK_PURPLE),
    MAGENTA("magenta", Formatting.LIGHT_PURPLE),
    PINK("pink", Formatting.RED),
    WARPED("warped", Formatting.DARK_AQUA, true),
    CRIMSON("crimson", Formatting.DARK_RED, true),
    SOUL("soul", Formatting.GOLD, true);

    private final String name;
    private final Formatting colorFormatting;
    private final boolean isFireImmune;

    ButterflyVariant(String name, Formatting colorFormatting, boolean isFireImmune) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.isFireImmune = isFireImmune;
    }

    ButterflyVariant(String name, Formatting colorFormatting) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.isFireImmune = false;
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

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/butterfly/" + getName() + "_butterfly.png");
    }

    public boolean isFireImmune() {
        return isFireImmune;
    }
}
