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
import java.util.stream.Collectors;

public enum RiverTurtleVariant {
    SANDY("sandy", Formatting.YELLOW),
    COCONUT("coconut", Formatting.DARK_GREEN);

    private static final Map<Identifier, RiverTurtleVariant> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName()),
                    v -> v
            ));
    public static final Codec<RiverTurtleVariant> CODEC =
            Identifier.CODEC.comapFlatMap(
                    id -> {
                        var v = BY_ID.get(id);
                        return v != null
                                ? DataResult.success(v)
                                : DataResult.error(() -> "Unknown river turtle variant: " + id);
                    },
                    v -> Identifier.of(ClutterBestiary.MOD_ID, v.getName())
            );
    private final String name;
    private final Formatting colorFormatting;

    RiverTurtleVariant(String name, Formatting colorFormatting) {
        this.name = name;
        this.colorFormatting = colorFormatting;
    }

    public static RiverTurtleVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(SANDY);
    }

    public static RiverTurtleVariant getRandom() {
        List<RiverTurtleVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public Formatting getColorFormatting() {
        return this.colorFormatting;
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/river_turtle/" + getName() + "_river_turtle.png");
    }

}
