package net.emilsg.clutter_bestiary.entity.variants;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum JellyfishVariant {
    GREEN("green"),
    BLUE("blue"),
    PURPLE("purple");

    private final String name;

    JellyfishVariant(String name) {
        this.name = name;
    }

    public static JellyfishVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(GREEN);
    }

    public static JellyfishVariant getRandom() {
        List<JellyfishVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public Identifier getTextureLocation() {
        return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/jellyfish/" + getName() + "_jellyfish.png");
    }

    public String getName() {
        return name;
    }
}
