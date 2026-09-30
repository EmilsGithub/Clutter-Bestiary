package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.util.ChorusBeetleSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin {

    @Inject(method = "onProjectileHit", at = @At("TAIL"))
    private void clutterbestiary$spawnChorusBeetle(Level world, BlockState state, BlockHitResult hitResult, Projectile projectile, CallbackInfo callbackInfo) {
        BlockPos pos = hitResult.getBlockPos();
        if (state.getValue(ChorusFlowerBlock.AGE) == ChorusFlowerBlock.DEAD_AGE && world.getBlockState(pos).isAir()) {
            ChorusBeetleSpawner.trySpawn(world, pos);
        }
    }
}
