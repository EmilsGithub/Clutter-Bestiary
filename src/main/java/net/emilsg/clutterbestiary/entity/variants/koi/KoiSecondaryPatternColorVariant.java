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

public enum KoiSecondaryPatternColorVariant implements BestiaryBasicVariant {
    BLACK("black", Formatting.DARK_GRAY,0x1C2226),
    WHITE("white", Formatting.WHITE,0xC3C3E5);

    private static final Map<Identifier, KoiSecondaryPatternColorVariant> BY_ID =
            java.util.Arrays.stream(values()).collect(Collectors.toMap(
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName()),
                    v -> v
            ));

    public static final Codec<KoiSecondaryPatternColorVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        var v = BY_ID.get(id);
                        return v != null
                                ? DataResult.success(v)
                                : DataResult.error(() -> "Unknown koi secondary pattern color variant: " + id);
                    },
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName())
            );

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
