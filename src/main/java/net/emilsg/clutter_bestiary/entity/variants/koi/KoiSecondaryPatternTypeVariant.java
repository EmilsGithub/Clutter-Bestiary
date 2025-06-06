package net.emilsg.clutter_bestiary.entity.variants.koi;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum KoiSecondaryPatternTypeVariant {
    NONE("none", Formatting.STRIKETHROUGH),
    SMALL("small", Formatting.ITALIC),
    MEDIUM("medium", Formatting.ITALIC);

    private final String name;
    private final Formatting formatting;

    KoiSecondaryPatternTypeVariant(String name, Formatting formatting) {
        this.name = name;
        this.formatting = formatting;
    }

    public static KoiSecondaryPatternTypeVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(NONE);
    }

    public static KoiSecondaryPatternTypeVariant getRandom() {
        List<KoiSecondaryPatternTypeVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return this.name;
    }

    public Formatting getFormatting() {
        return this.formatting;
    }

    public Identifier getTextureLocation() {
        return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_secondary_pattern_" + getName() + ".png");
    }
}
