package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.block.entity.ButterflyBottleBlockEntity;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import java.util.List;
import java.util.Optional;

public class ButterflyBottleItem extends NonDecrementingBlockItem {
    public static final MapCodec<ButterflyVariant> BUTTERFLY_VARIANT_MAP_CODEC = ButterflyVariant.CODEC.fieldOf("Variant");

    public ButterflyBottleItem(Block block, Properties settings) {
        super(block, settings);
    }

    public static ItemStack getEmptiedStack(ItemStack stack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        CustomData nbtComponent = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        Optional<ButterflyVariant> optional = ModUtil.readComponentData(nbtComponent, BUTTERFLY_VARIANT_MAP_CODEC);

        if (optional.isPresent()) {
            ButterflyVariant variant = optional.get();

            ChatFormatting formatting = variant.getColorFormatting();

            String string = "clutterbestiary." + variant.getName() + ".butterfly";

            MutableComponent mutableText = Component.translatable(string);
            mutableText.withStyle(formatting);

            MutableComponent placeableSneak = Component.translatable("tooltip.clutterbestiary.place_sneak");
            placeableSneak.withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);

            tooltip.accept(mutableText);
            tooltip.accept(placeableSneak);
        }
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        if (!result.consumesAction()) return result;

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ButterflyBottleBlockEntity bottleBe) {

                CustomData comp = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
                CompoundTag nbt = comp.copyTag();

                if (nbt.isEmpty()) {
                    ButterflyVariant def = ButterflyVariant.WHITE;
                    nbt.putString("Variant", def.getId());
                    nbt.putInt("FlyingVariant", 0);
                    nbt.putBoolean("HasCocoon", false);
                    nbt.putInt("DupeTimer", 0);
                }

                bottleBe.setButterflyData(nbt);
                stack.consume(1, context.getPlayer());
            }
        }

        return result;
    }

    public InteractionResult useOn(UseOnContext context) {
        Player playerEntity = context.getPlayer();

        if (playerEntity != null && playerEntity.isShiftKeyDown()) {
            return super.useOn(context);
        }

        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if (player == null) return InteractionResult.FAIL;

        if (world instanceof ServerLevel) {
            this.spawnEntity((ServerLevel) world, stack, pos);
            world.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
            world.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.NEUTRAL);
            player.setItemInHand(hand, getEmptiedStack(stack, player));
        }
        return InteractionResult.SUCCESS;
    }

    private void spawnEntity(ServerLevel world, ItemStack stack, BlockPos pos) {
        ButterflyEntity butterfly = ModEntityTypes.BUTTERFLY.get().spawn(world, stack, null, pos, EntitySpawnReason.BUCKET, true, false);
        CustomData nbtComponent = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        if (butterfly != null) butterfly.copyDataFromNbt(nbtComponent.copyTag());
    }
}
