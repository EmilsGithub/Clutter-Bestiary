package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.util.ChorusBeetleSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "onBreak", at = @At("HEAD"))
    private void clutterbestiary$spawnChorusBeetle(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> callbackInfo) {
        if (state.isOf(Blocks.CHORUS_FLOWER) && state.get(ChorusFlowerBlock.AGE) == ChorusFlowerBlock.MAX_AGE) {
            ChorusBeetleSpawner.trySpawn(world, pos);
        }
    }
}
