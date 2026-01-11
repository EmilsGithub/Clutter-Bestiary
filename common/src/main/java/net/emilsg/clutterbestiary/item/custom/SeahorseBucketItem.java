package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SeahorseBucketItem extends BestiaryEntityBucketItem {
    public static final MapCodec<SeahorseVariant> SEAHORSE_VARIANT_MAP_CODEC = SeahorseVariant.CODEC.fieldOf("Variant");

    public SeahorseBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(type, fluid, emptyingSound, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        if (nbtComponent.isEmpty()) return;

        Optional<SeahorseVariant> optional = nbtComponent.get(SEAHORSE_VARIANT_MAP_CODEC).result();
        if (optional.isEmpty()) return;

        SeahorseVariant variant = optional.get();
        Formatting formatting = variant.getFormatting();
        String key = "clutterbestiary." + variant.getName() + ".seahorse";

        MutableText text = Text.translatable(key);
        text.formatted(formatting);
        tooltip.add(text);
    }
}
