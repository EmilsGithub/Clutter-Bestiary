package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.EchofinEntity;
import net.emilsg.clutterbestiary.entity.variants.EchofinVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class EchofinBucketItem extends Item {
    private final EchofinVariant variant;

    public EchofinBucketItem(Properties settings, EchofinVariant variant) {
        super(settings);
        this.variant = variant;
    }

    public static ItemStack getEmptiedStack(ItemStack stack, @Nullable Player player) {
        return player == null || !player.getAbilities().instabuild ? new ItemStack(Items.BUCKET) : stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel serverWorld)) return InteractionResult.SUCCESS;

        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState blockState = world.getBlockState(blockPos);
        BlockPos spawnPos = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.relative(direction);
        ItemStack stack = context.getItemInHand();

        EchofinEntity echofin = ModEntityTypes.ECHOFIN.get().spawn(serverWorld, stack, context.getPlayer(), spawnPos, EntitySpawnReason.BUCKET, true, false);
        if (echofin == null) return InteractionResult.FAIL;

        CustomData entityData = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        echofin.copyDataFromNbt(entityData.copyTag());
        echofin.setVariant(this.variant);
        echofin.setPersistenceRequired();
        echofin.setHomePos(spawnPos);

        world.playSound(null, spawnPos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
        world.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, spawnPos);

        Player player = context.getPlayer();
        if (player != null) {
            player.setItemInHand(context.getHand(), getEmptiedStack(stack, player));
        }

        return InteractionResult.CONSUME;
    }
}
