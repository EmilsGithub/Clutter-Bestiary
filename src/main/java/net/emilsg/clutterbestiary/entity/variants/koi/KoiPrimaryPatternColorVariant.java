package net.emilsg.clutterbestiary.entity.variants.koi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.variants.BestiaryBasicVariant;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public enum KoiPrimaryPatternColorVariant implements BestiaryBasicVariant {
    ORANGE("orange",Formatting.GOLD, 0xFF6416),
    YELLOW("yellow", Formatting.YELLOW,0xEFD870),
    BLACK("black", Formatting.DARK_GRAY,0x3E4549),
    RED("red", Formatting.RED,0xFF2632),
    WHITE("white", Formatting.WHITE,0xEAEAFF);

    private static final Map<Identifier, KoiPrimaryPatternColorVariant> BY_ID =
            java.util.Arrays.stream(values()).collect(Collectors.toMap(
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName()),
                    v -> v
            ));

    public static final Codec<KoiPrimaryPatternColorVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        var v = BY_ID.get(id);
                        return v != null
                                ? DataResult.success(v)
                                : DataResult.error(() -> "Unknown koi primary pattern color variant: " + id);
                    },
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName())
            );

    private final String name;
    private final Formatting colorFormatting;
    private final int colorHex;

    KoiPrimaryPatternColorVariant(String name, Formatting colorFormatting, int colorHex) {
        this.name = name;
        this.colorFormatting = colorFormatting;
        this.colorHex = colorHex;
    }

    public static KoiPrimaryPatternColorVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getID().equals(id)).findFirst().orElse(ORANGE);
    }

    public static KoiPrimaryPatternColorVariant getRandom() {
        List<KoiPrimaryPatternColorVariant> variants = Arrays.stream(values()).toList();
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
