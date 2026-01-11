package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public enum MossbloomVariant {
    HORNED("horned", true),
    FLOWERING("flowering", false);

    private static final MossbloomVariant[] VARIANTS = values();

    private final String name;
    private final boolean shouldGlow;
    private final String id;
    private final Identifier textureLocation;
    @Nullable
    private final Identifier emissiveTextureLocation;

    MossbloomVariant(String name, boolean shouldGlow) {
        this.name = name;
        this.shouldGlow = shouldGlow;
        this.id = ClutterBestiary.MOD_ID + ":" + name;
        this.textureLocation = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/mossbloom/" + name + "_mossbloom.png");
        this.emissiveTextureLocation = shouldGlow
                ? Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/mossbloom/" + name + "_mossbloom_emissive.png")
                : null;
    }

    public static MossbloomVariant fromId(String id) {
        for (MossbloomVariant variant : VARIANTS) {
            if (variant.getId().equals(id)) return variant;
        }
        return HORNED;
    }

    public static MossbloomVariant getRandom(Random random) {
        return VARIANTS[random.nextInt(VARIANTS.length)];
    }

    public static MossbloomVariant getRandom() {
        return VARIANTS[ThreadLocalRandom.current().nextInt(VARIANTS.length)];
    }

    @Nullable
    public Identifier getEmissiveTextureLocation() {
        return this.emissiveTextureLocation;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public boolean getShouldGlow() {
        return shouldGlow;
    }

    public Identifier getTextureLocation() {
        return this.textureLocation;
    }
}
