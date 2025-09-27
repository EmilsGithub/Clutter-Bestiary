package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
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

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/jellyfish/" + getName() + "_jellyfish.png");
    }
}
