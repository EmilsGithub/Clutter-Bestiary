package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.koi.*;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Supplier;

public class KoiBucketItem extends BestiaryEntityBucketItem {
    public static final MapCodec<KoiBaseColorVariant> BASE_COLOR_CODEC = KoiBaseColorVariant.CODEC.fieldOf("BaseColor");
    public static final MapCodec<KoiPrimaryPatternTypeVariant> PRIMARY_TYPE_CODEC = KoiPrimaryPatternTypeVariant.CODEC.fieldOf("PrimaryPatternType");
    public static final MapCodec<KoiPrimaryPatternColorVariant> PRIMARY_COLOR_CODEC = KoiPrimaryPatternColorVariant.CODEC.fieldOf("PrimaryPatternColor");
    public static final MapCodec<KoiSecondaryPatternTypeVariant> SECONDARY_TYPE_CODEC = KoiSecondaryPatternTypeVariant.CODEC.fieldOf("SecondaryPatternType");
    public static final MapCodec<KoiSecondaryPatternColorVariant> SECONDARY_COLOR_CODEC = KoiSecondaryPatternColorVariant.CODEC.fieldOf("SecondaryPatternColor");
    public KoiBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(type, fluid, emptyingSound, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext ctx, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, ctx, tooltip, type);

        var cmp = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);

        var baseOpt = cmp.get(BASE_COLOR_CODEC).result();
        if (baseOpt.isEmpty()) return;
        var base = baseOpt.get();

        tooltip.add(Text.translatable("tooltip.clutterbestiary.base_color.koi").formatted(Formatting.GRAY));
        if (base.hasSeparateTexture()) {
            int tick = (int) (System.currentTimeMillis() / 100) % base.getColorHex().length;
            tooltip.add(ModUtil.buildCyclicFormattedName("tooltip.clutterbestiary." + base.getName() + ".koi", base.getColorHex(), tick, true));
            return;
        } else {
            tooltip.add(Text.translatable("tooltip.clutterbestiary." + base.getName() + ".koi").formatted(base.getFormatting()));
        }

        var pType = cmp.get(PRIMARY_TYPE_CODEC).result().orElse(null);
        var pColor = cmp.get(PRIMARY_COLOR_CODEC).result().orElse(null);
        var sType = cmp.get(SECONDARY_TYPE_CODEC).result().orElse(null);
        var sColor = cmp.get(SECONDARY_COLOR_CODEC).result().orElse(null);

        if (pType != null && pColor != null) {
            tooltip.add(Text.translatable("tooltip.clutterbestiary.primary_pattern.koi").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.clutterbestiary." + pType.getName() + ".koi")
                    .formatted(pType.getFormatting())
                    .formatted(pColor.getFormatting()));
        }
        if (sType != null && sColor != null) {
            tooltip.add(Text.translatable("tooltip.clutterbestiary.secondary_pattern.koi").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.clutterbestiary." + sType.getName() + ".koi")
                    .formatted(sType.getFormatting())
                    .formatted(sColor.getFormatting()));
        }

        tooltip.add(ScreenTexts.EMPTY);
    }

}
