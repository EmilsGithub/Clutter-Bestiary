package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum SeahorseVariant {
    YELLOW("yellow"),
    LIGHT_BLUE("light_blue"),
    RED("red"),
    PURPLE("purple");

    private final String name;

    SeahorseVariant(String name) {
        this.name = name;
    }

    public static SeahorseVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(YELLOW);
    }

    public static SeahorseVariant getRandom() {
        List<SeahorseVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/seahorse/" + getName() + "_seahorse.png");
    }
}
