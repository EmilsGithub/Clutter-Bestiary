package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.resources.Identifier;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum StoatVariant {
    SUMMER("summer"),
    WINTER("winter");

    private final String name;

    StoatVariant(String name) {
        this.name = name;
    }

    public static StoatVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(SUMMER);
    }

    public static StoatVariant getRandom() {
        List<StoatVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getSleepingTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/stoat/" + getName() + "_stoat_sleeping.png");
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/stoat/" + getName() + "_stoat.png");
    }
}
