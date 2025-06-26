package net.emilsg.clutter_bestiary.item.custom;

import net.emilsg.clutter_bestiary.entity.ModEntityTypes;
import net.emilsg.clutter_bestiary.entity.custom.KoiEntity;
import net.emilsg.clutter_bestiary.entity.variants.koi.*;
import net.emilsg.clutter_bestiary.util.ModUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KoiBucketItem extends EntityBucketItem {
    private final EntityType<KoiEntity> entityType;

    public KoiBucketItem(EntityType<KoiEntity> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(type, fluid, emptyingSound, settings);
        this.entityType = type;
    }

    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        if (world instanceof ServerWorld) {
            this.spawnEntity((ServerWorld)world, stack, pos);
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    private void spawnEntity(ServerWorld world, ItemStack stack, BlockPos pos) {
        KoiEntity koi = ModEntityTypes.KOI.create(world);
        if (koi == null) return;
        koi.copyDataFromNbt(koi, stack.getOrCreateNbt());
        koi.refreshPositionAndAngles(pos, 0, 0);
        koi.setFromBucket(true);

        if (stack.hasCustomName()) {
            koi.setCustomName(stack.getName());
        }

        world.spawnEntity(koi);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        NbtCompound nbt = stack.getNbt();
        if (this.entityType != ModEntityTypes.KOI || nbt == null) return;
        if (!nbt.contains("BaseColor", NbtElement.STRING_TYPE)) return;
        if (!nbt.contains("PrimaryPatternType", NbtElement.STRING_TYPE)) return;
        if (!nbt.contains("SecondaryPatternType", NbtElement.STRING_TYPE)) return;

        KoiBaseColorVariant base = KoiBaseColorVariant.fromId(nbt.getString("BaseColor"));

        tooltip.add(Text.translatable("tooltip.clutter-bestiary.base_color.koi").formatted(Formatting.GRAY));
        if (base.hasSeparateTexture()) {
            int tick = (int)(System.currentTimeMillis() / 100) % base.getColorHex().length;
            tooltip.add(ModUtil.buildCyclicFormattedName("tooltip.clutter-bestiary." + base.getName() + ".koi", base.getColorHex(), tick, true));
        } else {
            tooltip.add(Text.translatable("tooltip.clutter-bestiary." + base.getName() + ".koi").formatted(base.getFormatting()));
        }

        if (!base.hasSeparateTexture()) {
            KoiPrimaryPatternTypeVariant primaryPatternType = KoiPrimaryPatternTypeVariant.fromId(nbt.getString("PrimaryPatternType"));
            KoiPrimaryPatternColorVariant primaryPatternColor = KoiPrimaryPatternColorVariant.fromId(nbt.getString("PrimaryPatternColor"));
            tooltip.add(Text.translatable("tooltip.clutter-bestiary.primary_pattern.koi").formatted(Formatting.GRAY));
            MutableText primaryText = Text.translatable("tooltip.clutter-bestiary." + primaryPatternType.getName() + ".koi").formatted(primaryPatternType.getFormatting()).formatted(primaryPatternColor.getFormatting());

            tooltip.add(primaryText);

            KoiSecondaryPatternTypeVariant secondaryPatternType = KoiSecondaryPatternTypeVariant.fromId(nbt.getString("SecondaryPatternType"));
            KoiSecondaryPatternColorVariant secondaryPatternColor = KoiSecondaryPatternColorVariant.fromId(nbt.getString("SecondaryPatternColor"));
            tooltip.add(Text.translatable("tooltip.clutter-bestiary.secondary_pattern.koi").formatted(Formatting.GRAY));
            MutableText secondaryText = Text.translatable("tooltip.clutter-bestiary." + secondaryPatternType.getName() + ".koi").formatted(secondaryPatternType.getFormatting()).formatted(secondaryPatternColor.getFormatting());

            tooltip.add(secondaryText);
        }

        tooltip.add(ScreenTexts.EMPTY);

    }
}
