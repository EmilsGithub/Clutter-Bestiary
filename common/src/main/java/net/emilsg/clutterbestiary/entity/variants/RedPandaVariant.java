package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.resources.Identifier;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum RedPandaVariant {
    FLUFF("fluff"),
    SIDEBURNS("sideburns"),
    FULL("full");

    private final String name;

    RedPandaVariant(String name) {
        this.name = name;
    }

    public static RedPandaVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(FLUFF);
    }

    public static RedPandaVariant getRandom() {
        List<RedPandaVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getSleepingTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/red_panda/" + getName() + "_red_panda_sleeping.png");
    }

    public Identifier getBabySleepingTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/red_panda/baby_" + getName() + "_red_panda_sleeping.png");
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/red_panda/" + getName() + "_red_panda.png");
    }

    public Identifier getBabyTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/red_panda/baby_" + getName() + "_red_panda.png");
    }
}
