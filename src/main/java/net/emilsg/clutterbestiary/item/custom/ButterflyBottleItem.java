package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ButterflyBottleItem extends Item {

    public ButterflyBottleItem(Settings settings) {
        super(settings);
    }

    public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
        return !player.getAbilities().creativeMode ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains("Variant")) {
            String variantStringId = nbtCompound.getString("Variant");
            ButterflyVariant butterflyVariant = ButterflyVariant.fromId(variantStringId);

            Formatting formatting = butterflyVariant.getColorFormatting();

            String string = "clutterbestiary." + butterflyVariant.getName() + ".butterfly";

            MutableText mutableText = Text.translatable(string);
            mutableText.formatted(formatting);
            tooltip.add(mutableText);
        }
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();

        if (player == null) return ActionResult.FAIL;

        if (world instanceof ServerWorld) {
            this.spawnEntity((ServerWorld) world, stack, pos);
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.NEUTRAL);
            player.setStackInHand(hand, getEmptiedStack(stack, player));
        }
        return ActionResult.success(world.isClient);
    }

    private void spawnEntity(ServerWorld world, ItemStack stack, BlockPos pos) {
        ButterflyEntity butterfly = ModEntityTypes.BUTTERFLY.spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        if (butterfly != null) {
            butterfly.copyDataFromNbt(butterfly, stack.getOrCreateNbt());
        }
    }
}
