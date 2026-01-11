package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.EchofinEntity;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class EchofinBucketItem extends Item {
    private final EchofinVariant variant;

    public EchofinBucketItem(Settings settings, EchofinVariant variant) {
        super(settings);
        this.variant = variant;
    }

    public static ItemStack getEmptiedStack(ItemStack stack, @Nullable PlayerEntity player) {
        return player == null || !player.getAbilities().creativeMode ? new ItemStack(Items.BUCKET) : stack;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld serverWorld)) return ActionResult.SUCCESS;

        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);
        BlockPos spawnPos = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);
        ItemStack stack = context.getStack();

        EchofinEntity echofin = ModEntityTypes.ECHOFIN.get().spawnFromItemStack(serverWorld, stack, context.getPlayer(), spawnPos, SpawnReason.BUCKET, true, false);
        if (echofin == null) return ActionResult.FAIL;

        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        echofin.copyDataFromNbt(entityData.copyNbt());
        echofin.setVariant(this.variant);
        echofin.setPersistent();
        echofin.setHomePos(spawnPos);

        world.playSound(null, spawnPos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0f, 1.0f);
        world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, spawnPos);

        PlayerEntity player = context.getPlayer();
        if (player != null) {
            player.setStackInHand(context.getHand(), getEmptiedStack(stack, player));
        }

        return ActionResult.CONSUME;
    }
}
