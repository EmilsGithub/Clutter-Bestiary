package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.JellyfishVariant;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluid;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class JellyfishBucketItem extends BestiaryEntityBucketItem {
    public static final MapCodec<JellyfishVariant> JELLYFISH_VARIANT_MAP_CODEC = JellyfishVariant.CODEC.fieldOf("Variant");

    public JellyfishBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Properties settings) {
        super(type, fluid, emptyingSound, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        CustomData nbtComponent = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        if (nbtComponent.isEmpty()) return;

        Optional<JellyfishVariant> optional = ModUtil.readComponentData(nbtComponent, JELLYFISH_VARIANT_MAP_CODEC);
        if (optional.isEmpty()) return;

        JellyfishVariant variant = optional.get();
        tooltip.accept(Component.translatable("clutterbestiary." + variant.getName() + ".jellyfish")
                .withStyle(variant.getColorFormatting()));
    }
}
