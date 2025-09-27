package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.sound.SoundEvent;

import java.util.function.Supplier;

public class SeahorseBucketItem extends EntityBucketItem {
    public static final MapCodec<SeahorseVariant> SEAHORSE_VARIANT_MAP_CODEC = SeahorseVariant.CODEC.fieldOf("Variant");
    private final Supplier<? extends EntityType<?>> type;

    public SeahorseBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(null, fluid, emptyingSound, settings);
        this.type = type;
    }

}
