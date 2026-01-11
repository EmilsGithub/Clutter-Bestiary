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

public enum SeahorseVariant implements BestiaryBasicVariant {
    YELLOW("yellow", Formatting.YELLOW),
    LIGHT_BLUE("light_blue", Formatting.AQUA),
    RED("red", Formatting.RED),
    PURPLE("purple", Formatting.DARK_PURPLE);

    private static final Map<Identifier, SeahorseVariant> BY_ID =
            Arrays.stream(values()).collect(java.util.stream.Collectors.toMap(
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName()),
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
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName())
            );
    private final String name;
    private final Formatting formatting;

    SeahorseVariant(String name, Formatting formatting) {
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
    public Formatting getFormatting() {
        return this.formatting;
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/seahorse/" + getName() + "_seahorse.png");
    }
}
