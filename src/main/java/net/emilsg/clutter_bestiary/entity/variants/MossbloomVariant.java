package net.emilsg.clutter_bestiary.entity.variants;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum MossbloomVariant {
    HORNED("horned", true),
    FLOWERING("flowering", false);

    private final String name;
    private final boolean shouldGlow;

    MossbloomVariant(String name, boolean shouldGlow) {
        this.name = name;
        this.shouldGlow = shouldGlow;
    }

    public static MossbloomVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(HORNED);
    }

    public static MossbloomVariant getRandom() {
        List<MossbloomVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Identifier getTextureLocation() {
        return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/mossbloom/" + getName() + "_mossbloom.png");
    }

    public boolean getShouldGlow() {
        return shouldGlow;
    }

    @Nullable
    public Identifier getEmissiveTextureLocation() {
        if (this.getShouldGlow()) {
            return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/mossbloom/" + getName() + "_mossbloom_emissive.png");
        }
        return null;
    }
}
