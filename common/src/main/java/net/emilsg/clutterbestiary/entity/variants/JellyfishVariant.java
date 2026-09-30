package net.emilsg.clutterbestiary.entity.variants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public enum JellyfishVariant {
    GREEN("green", ChatFormatting.GREEN),
    BLUE("blue", ChatFormatting.BLUE),
    PURPLE("purple", ChatFormatting.DARK_PURPLE);

    private static final Map<Identifier, JellyfishVariant> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(
                    variant -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, variant.getName()),
                    variant -> variant
            ));
    public static final Codec<JellyfishVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        JellyfishVariant variant = BY_ID.get(id);
                        return variant != null
                                ? DataResult.success(variant)
                                : DataResult.error(() -> "Unknown jellyfish variant: " + id);
                    },
                    variant -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, variant.getName())
            );
    private final String name;
    private final ChatFormatting colorFormatting;

    JellyfishVariant(String name, ChatFormatting colorFormatting) {
        this.name = name;
        this.colorFormatting = colorFormatting;
    }

    public static JellyfishVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(GREEN);
    }

    public static JellyfishVariant getRandom() {
        List<JellyfishVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public ChatFormatting getColorFormatting() {
        return this.colorFormatting;
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/jellyfish/" + getName() + "_jellyfish.png");
    }
}
