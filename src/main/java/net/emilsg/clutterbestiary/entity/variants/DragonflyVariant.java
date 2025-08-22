package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum DragonflyVariant {
    YELLOW("yellow"),
    BLUE("blue"),
    RED("red");

    private final String name;

    DragonflyVariant(String name) {
        this.name = name;
    }

    public static DragonflyVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(YELLOW);
    }

    public static DragonflyVariant getRandom() {
        List<DragonflyVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/dragonfly/" + getName() + "_dragonfly.png");
    }
}
