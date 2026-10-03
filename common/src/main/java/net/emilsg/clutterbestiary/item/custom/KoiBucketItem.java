package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.koi.*;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public class KoiBucketItem extends BestiaryEntityBucketItem {
    public static final MapCodec<KoiBaseColorVariant> BASE_COLOR_CODEC = KoiBaseColorVariant.CODEC.fieldOf("BaseColor");
    public static final MapCodec<KoiPrimaryPatternTypeVariant> PRIMARY_TYPE_CODEC = KoiPrimaryPatternTypeVariant.CODEC.fieldOf("PrimaryPatternType");
    public static final MapCodec<KoiPrimaryPatternColorVariant> PRIMARY_COLOR_CODEC = KoiPrimaryPatternColorVariant.CODEC.fieldOf("PrimaryPatternColor");
    public static final MapCodec<KoiSecondaryPatternTypeVariant> SECONDARY_TYPE_CODEC = KoiSecondaryPatternTypeVariant.CODEC.fieldOf("SecondaryPatternType");
    public static final MapCodec<KoiSecondaryPatternColorVariant> SECONDARY_COLOR_CODEC = KoiSecondaryPatternColorVariant.CODEC.fieldOf("SecondaryPatternColor");
    public static final MapCodec<Float> SIZE_CODEC = Codec.FLOAT.fieldOf("Size");

    public KoiBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Properties settings) {
        super(type, fluid, emptyingSound, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, ctx, display, tooltip, type);

        var cmp = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);

        var baseOpt = ModUtil.readComponentData(cmp, BASE_COLOR_CODEC);
        if (baseOpt.isEmpty()) return;
        var base = baseOpt.get();

        tooltip.accept(Component.translatable("tooltip.clutterbestiary.base_color.koi").withStyle(ChatFormatting.GRAY));
        if (base.hasSeparateTexture()) {
            int tick = (int) (System.currentTimeMillis() / 100) % base.getColorHex().length;
            tooltip.accept(ModUtil.buildCyclicFormattedName("tooltip.clutterbestiary." + base.getName() + ".koi", base.getColorHex(), tick, true));
        } else {
            tooltip.accept(Component.translatable("tooltip.clutterbestiary." + base.getName() + ".koi").withStyle(base.getFormatting()));
            appendPatterns(cmp, tooltip);
        }

        var sizeOpt = ModUtil.readComponentData(cmp, SIZE_CODEC);
        if (sizeOpt.isPresent()) {
            tooltip.accept(CommonComponents.EMPTY);
            tooltip.accept(Component.translatable("tooltip.clutterbestiary.size.koi").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal(String.format(Locale.ROOT, "%.2fx", sizeOpt.get())).withStyle(ChatFormatting.WHITE));
        } else {
            tooltip.accept(CommonComponents.EMPTY);
        }
    }

    private static void appendPatterns(CustomData cmp, Consumer<Component> tooltip) {
        var pType = ModUtil.readComponentData(cmp, PRIMARY_TYPE_CODEC).orElse(null);
        var pColor = ModUtil.readComponentData(cmp, PRIMARY_COLOR_CODEC).orElse(null);
        var sType = ModUtil.readComponentData(cmp, SECONDARY_TYPE_CODEC).orElse(null);
        var sColor = ModUtil.readComponentData(cmp, SECONDARY_COLOR_CODEC).orElse(null);

        if (pType != null && pColor != null) {
            tooltip.accept(Component.translatable("tooltip.clutterbestiary.primary_pattern.koi").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.translatable("tooltip.clutterbestiary." + pType.getName() + ".koi")
                    .withStyle(pType.getFormatting())
                    .withStyle(pColor.getFormatting()));
        }
        if (sType != null && sColor != null) {
            tooltip.accept(Component.translatable("tooltip.clutterbestiary.secondary_pattern.koi").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.translatable("tooltip.clutterbestiary." + sType.getName() + ".koi")
                    .withStyle(sType.getFormatting())
                    .withStyle(sColor.getFormatting()));
        }
    }

}
