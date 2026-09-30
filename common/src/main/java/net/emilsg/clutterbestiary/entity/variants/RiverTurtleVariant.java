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

public enum RiverTurtleVariant implements BestiaryBasicVariant {
    SANDY("sandy", ChatFormatting.YELLOW),
    COCONUT("coconut", ChatFormatting.DARK_GREEN);

    private static final Map<Identifier, RiverTurtleVariant> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName()),
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
                    v -> Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, v.getName())
            );
    private final String name;
    private final ChatFormatting formatting;

    RiverTurtleVariant(String name, ChatFormatting formatting) {
        this.name = name;
        this.formatting = formatting;
    }

    public static RiverTurtleVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getID().equals(id)).findFirst().orElse(SANDY);
    }

    public static RiverTurtleVariant getRandom() {
        List<RiverTurtleVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    @Override
    public ChatFormatting getFormatting() {
        return this.formatting;
    }

    @Override
    public String getID() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/river_turtle/" + getName() + "_river_turtle.png");
    }

}
