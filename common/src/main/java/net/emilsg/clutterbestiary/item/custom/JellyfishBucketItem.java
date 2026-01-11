package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.JellyfishVariant;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class JellyfishBucketItem extends BestiaryEntityBucketItem {
    public static final MapCodec<JellyfishVariant> JELLYFISH_VARIANT_MAP_CODEC = JellyfishVariant.CODEC.fieldOf("Variant");

    public JellyfishBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(type, fluid, emptyingSound, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        if (nbtComponent.isEmpty()) return;

        Optional<JellyfishVariant> optional = nbtComponent.get(JELLYFISH_VARIANT_MAP_CODEC).result();
        if (optional.isEmpty()) return;

        JellyfishVariant variant = optional.get();
        tooltip.add(Text.translatable("clutterbestiary." + variant.getName() + ".jellyfish")
                .formatted(variant.getColorFormatting()));
    }
}
