package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
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

public class RiverTurtleBucketItem extends BestiaryEntityBucketItem {
    public static final MapCodec<RiverTurtleVariant> RIVER_TURTLE_VARIANT_MAP_CODEC = RiverTurtleVariant.CODEC.fieldOf("Variant");

    public RiverTurtleBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(type, fluid, emptyingSound, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        if (nbtComponent.isEmpty()) return;

        Optional<RiverTurtleVariant> optional = nbtComponent.get(RIVER_TURTLE_VARIANT_MAP_CODEC).result();
        if (optional.isEmpty()) return;

        RiverTurtleVariant variant = optional.get();
        Formatting formatting = variant.getFormatting();
        String key = "clutterbestiary." + variant.getName() + ".river_turtle";

        MutableText text = Text.translatable(key);
        text.formatted(formatting);
        tooltip.add(text);
    }
}
