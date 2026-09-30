package net.emilsg.clutterbestiary.entity.variants;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum PotionWaspVariant {
    REGENERATION("regeneration", Potions.REGENERATION),
    POISON("poison", Potions.POISON),
    STRENGTH("strength", Potions.STRENGTH),
    SWIFTNESS("swiftness", Potions.SWIFTNESS),
    WEAKNESS("weakness", Potions.WEAKNESS);

    private static final List<Holder<MobEffect>> ALL_STATUS_EFFECTS = Arrays.stream(values())
            .map(PotionWaspVariant::getPotionEffect)
            .map(Holder::value)
            .flatMap(potion -> potion.getEffects().stream())
            .map(MobEffectInstance::getEffect)
            .distinct()
            .toList();

    private final String name;
    private final Holder<Potion> effect;

    PotionWaspVariant(String name, Holder<Potion> effect) {
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

    public static List<Holder<MobEffect>> getAllStatusEffects() {
        return ALL_STATUS_EFFECTS;
    }


    public String getId() {
        return ClutterBestiary.MOD_ID + ":" + this.getName();
    }

    public String getName() {
        return name;
    }

    public Holder<Potion> getPotionEffect() {
        return effect;
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/potion_wasp/" + getName() + "_potion_wasp.png");
    }
}
