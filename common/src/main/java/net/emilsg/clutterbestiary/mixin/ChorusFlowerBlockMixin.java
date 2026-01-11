package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.util.ChorusBeetleSpawner;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin {

    @Inject(method = "onProjectileHit", at = @At("TAIL"))
    private void clutterbestiary$spawnChorusBeetle(World world, BlockState state, BlockHitResult hitResult, ProjectileEntity projectile, CallbackInfo callbackInfo) {
        BlockPos pos = hitResult.getBlockPos();
        if (state.get(ChorusFlowerBlock.AGE) == ChorusFlowerBlock.MAX_AGE && world.getBlockState(pos).isAir()) {
            ChorusBeetleSpawner.trySpawn(world, pos);
        }
    }
}
