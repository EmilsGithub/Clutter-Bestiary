package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.sound.SoundEvent;

import java.util.function.Supplier;

public class ArrowfishBucketItem extends BestiaryEntityBucketItem {

    public ArrowfishBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(type, fluid, emptyingSound, settings);
    }
}
