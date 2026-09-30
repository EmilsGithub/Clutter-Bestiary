package net.emilsg.clutterbestiary.entity.variants.koi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.variants.BestiaryBasicVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public enum KoiPrimaryPatternTypeVariant implements BestiaryBasicVariant {
    NONE("none", ChatFormatting.STRIKETHROUGH),
    PATCHES("patches", ChatFormatting.ITALIC),
    SPOTTED("spotted", ChatFormatting.ITALIC),
    STRIPED("striped", ChatFormatting.ITALIC);

    private static final Map<Identifier, KoiPrimaryPatternTypeVariant> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName()),
                    v -> v
            ));

    public static final Codec<KoiPrimaryPatternTypeVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        var v = BY_ID.get(id);
                        return v != null
                                ? DataResult.success(v)
                                : DataResult.error(() -> "Unknown koi primary pattern type variant: " + id);
                    },
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName())
            );

    private final String name;
    private final ChatFormatting formatting;

    KoiPrimaryPatternTypeVariant(String name, ChatFormatting formatting) {
        this.name = name;
        this.formatting = formatting;
    }

    public static KoiPrimaryPatternTypeVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getID().equals(id)).findFirst().orElse(NONE);
    }

    public static KoiPrimaryPatternTypeVariant getRandom() {
        List<KoiPrimaryPatternTypeVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
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
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_primary_pattern_" + getName() + ".png");
    }
}
