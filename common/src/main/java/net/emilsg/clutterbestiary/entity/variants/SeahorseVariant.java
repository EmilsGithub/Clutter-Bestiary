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

public enum SeahorseVariant implements BestiaryBasicVariant {
    YELLOW("yellow", ChatFormatting.YELLOW),
    LIGHT_BLUE("light_blue", ChatFormatting.AQUA),
    RED("red", ChatFormatting.RED),
    PURPLE("purple", ChatFormatting.DARK_PURPLE);

    private static final Map<Identifier, SeahorseVariant> BY_ID =
            Arrays.stream(values()).collect(java.util.stream.Collectors.toMap(
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName()),
                    v -> v
            ));
    public static final Codec<SeahorseVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        var v = BY_ID.get(id);
                        return v != null
                                ? DataResult.success(v)
                                : DataResult.error(() -> "Unknown seahorse variant: " + id);
                    },
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName())
            );
    private final String name;
    private final ChatFormatting formatting;

    SeahorseVariant(String name, ChatFormatting formatting) {
        this.name = name;
        this.formatting = formatting;
    }

    public static SeahorseVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getID().equals(id)).findFirst().orElse(YELLOW);
    }

    public static SeahorseVariant getRandom() {
        List<SeahorseVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getID() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    @Override
    public ChatFormatting getFormatting() {
        return this.formatting;
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/seahorse/" + getName() + "_seahorse.png");
    }
}
