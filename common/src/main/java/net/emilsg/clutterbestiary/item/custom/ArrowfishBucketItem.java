package net.emilsg.clutterbestiary.item.custom;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;

public class ArrowfishBucketItem extends BestiaryEntityBucketItem {

    public ArrowfishBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Properties settings) {
        super(type, fluid, emptyingSound, settings);
    }
}
