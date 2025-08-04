package net.emilsg.clutterbestiary.entity.variants.koi;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.variants.BestiaryBasicVariant;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum KoiSecondaryPatternTypeVariant implements BestiaryBasicVariant {
    NONE("none", Formatting.STRIKETHROUGH),
    SMALL_SPOTS("small_spots", Formatting.ITALIC),
    MEDIUM_SPOTS("medium_spots", Formatting.ITALIC);

    private final String name;
    private final Formatting formatting;

    KoiSecondaryPatternTypeVariant(String name, Formatting formatting) {
        this.name = name;
        this.formatting = formatting;
    }

    public static KoiSecondaryPatternTypeVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getID().equals(id)).findFirst().orElse(NONE);
    }

    public static KoiSecondaryPatternTypeVariant getRandom() {
        List<KoiSecondaryPatternTypeVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
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

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_secondary_pattern_" + getName() + ".png");
    }
}
