package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.util.ChorusBeetleSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void clutterbestiary$spawnChorusBeetle(Level world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> callbackInfo) {
        if (state.is(Blocks.CHORUS_FLOWER) && state.getValue(ChorusFlowerBlock.AGE) == ChorusFlowerBlock.DEAD_AGE) {
            ChorusBeetleSpawner.trySpawn(world, pos);
        }
    }
}
