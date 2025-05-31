package net.emilsg.clutter_bestiary.entity.variants;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum EchofinVariant {
    CHORUS("chorus"),
    LEVITATING("levitating");

    private final String name;

    EchofinVariant(String name) {
        this.name = name;
    }

    public static EchofinVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(CHORUS);
    }

    public static EchofinVariant getRandom() {
        List<EchofinVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public Identifier getEmissiveTextureLocation() {
        return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/echofin/" + getName() + "_echofin_emissive.png");
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/echofin/" + getName() + "_echofin.png");
    }
}
