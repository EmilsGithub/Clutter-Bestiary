package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.block.entity.ButterflyBottleBlockEntity;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
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

import java.util.List;
import java.util.Optional;

public class ButterflyBottleItem extends NonDecrementingBlockItem {
    public static final MapCodec<ButterflyVariant> BUTTERFLY_VARIANT_MAP_CODEC = ButterflyVariant.CODEC.fieldOf("Variant");

    public ButterflyBottleItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
        return !player.getAbilities().creativeMode ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        Optional<ButterflyVariant> optional = nbtComponent.get(BUTTERFLY_VARIANT_MAP_CODEC).result();

        if (optional.isPresent()) {
            ButterflyVariant variant = optional.get();

            Formatting formatting = variant.getColorFormatting();

            String string = "clutterbestiary." + variant.getName() + ".butterfly";

            MutableText mutableText = Text.translatable(string);
            mutableText.formatted(formatting);

            MutableText placeableSneak = Text.translatable("tooltip.clutterbestiary.place_sneak");
            placeableSneak.formatted(Formatting.BLUE, Formatting.ITALIC);

            tooltip.add(mutableText);
            tooltip.add(placeableSneak);
        }
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ActionResult result = super.place(context);
        if (!result.isAccepted()) return result;

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();

        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ButterflyBottleBlockEntity bottleBe) {

                NbtComponent comp = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
                NbtCompound nbt = comp.copyNbt();

                if (nbt.isEmpty()) {
                    ButterflyVariant def = ButterflyVariant.WHITE;
                    nbt.putString("Variant", def.getId());
                    nbt.putInt("FlyingVariant", 0);
                    nbt.putBoolean("HasCocoon", false);
                    nbt.putInt("DupeTimer", 0);
                }

                bottleBe.setButterflyData(nbt);
                stack.decrementUnlessCreative(1, context.getPlayer());
            }
        }

        return result;
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();

        if (playerEntity != null && playerEntity.isSneaking()) {
            return super.useOnBlock(context);
        }

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
        ButterflyEntity butterfly = ModEntityTypes.BUTTERFLY.get().spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        if (butterfly != null) butterfly.copyDataFromNbt(butterfly, nbtComponent.copyNbt());
    }
}
