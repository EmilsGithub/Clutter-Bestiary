package net.emilsg.clutter_bestiary.entity.variants;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum PotionWaspVariant {
    REGENERATION("regeneration", Potions.REGENERATION),
    POISON("poison", Potions.POISON),
    STRENGTH("strength", Potions.STRENGTH),
    SWIFTNESS("swiftness", Potions.SWIFTNESS),
    WEAKNESS("weakness", Potions.WEAKNESS);


    private final String name;
    private final Potion effect;

    PotionWaspVariant(String name, Potion effect) {
        this.name = name;
        this.effect = effect;
    }

    public static PotionWaspVariant fromId(String id) {
        return Arrays.stream(values()).filter(v -> v.getId().equals(id)).findFirst().orElse(REGENERATION);
    }

    public static PotionWaspVariant getRandom() {
        List<PotionWaspVariant> variants = Arrays.stream(values()).toList();
        return variants.get(new Random().nextInt(variants.size()));
    }

    public static List<StatusEffect> getAllStatusEffects() {
        return Arrays.stream(values())
                .flatMap(variant -> variant.effect.getEffects().stream())
                .map(StatusEffectInstance::getEffectType)
                .distinct()
                .toList();
    }

    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Potion getPotionEffect() {
        return effect;
    }

    public Identifier getTextureLocation() {
        return new Identifier(ClutterBestiary.MOD_ID, "textures/entity/potion_wasp/" + getName() + "_potion_wasp.png");
    }
}
