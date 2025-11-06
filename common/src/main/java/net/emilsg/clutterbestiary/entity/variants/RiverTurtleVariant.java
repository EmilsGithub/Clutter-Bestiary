package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum RiverTurtleVariant {
    SANDY("sandy"),
    COCONUT("coconut");

    private final String name;

    RiverTurtleVariant(String name) {
        this.name = name;
    }

    public static RiverTurtleVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(SANDY);
    }

    public static RiverTurtleVariant getRandom() {
        List<RiverTurtleVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
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
