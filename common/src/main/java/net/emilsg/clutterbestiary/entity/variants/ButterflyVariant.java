package net.emilsg.clutterbestiary.entity.variants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum ButterflyVariant {

    WHITE("white", Formatting.WHITE, 0),
    LIGHT_GRAY("light_gray", Formatting.GRAY, 1),
    GRAY("gray", Formatting.DARK_GRAY, 2),
    BLACK("black", Formatting.BLACK, 3),
    BROWN("brown", Formatting.GOLD, 4),
    RED("red", Formatting.DARK_RED, 5),
    ORANGE("orange", Formatting.GOLD, 6),
    YELLOW("yellow", Formatting.YELLOW, 7),
    LIME("lime", Formatting.GREEN, 8),
    GREEN("green", Formatting.DARK_GREEN, 9),
    LIGHT_BLUE("light_blue", Formatting.BLUE, 10),
    CYAN("cyan", Formatting.DARK_AQUA, 11),
    BLUE("blue", Formatting.DARK_BLUE, 12),
    PURPLE("purple", Formatting.DARK_PURPLE, 13),
    MAGENTA("magenta", Formatting.LIGHT_PURPLE, 14),
    PINK("pink", Formatting.RED, 15),
    WARPED("warped", Formatting.DARK_AQUA, 16, true),
    CRIMSON("crimson", Formatting.DARK_RED, 17, true),
    SOUL("soul", Formatting.GOLD, 18, true);

    private final String name;
    private final Formatting colorFormatting;
    private final boolean isFireImmune;
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
    private final int ID;

    ButterflyVariant(String name, Formatting colorFormatting, int ID, boolean isFireImmune) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.isFireImmune = isFireImmune;
        this.ID = ID;
    }

    ButterflyVariant(String name, Formatting colorFormatting, int ID) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.isFireImmune = false;
        this.ID = ID;
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

    public int getOrderedID() {
        return this.ID;
    }
}
