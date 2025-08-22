package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.custom.MossbloomEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {

    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    private void stopDestruction(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        var box = entity.getBoundingBox().expand(5.0);
        boolean mossbloomNearby = !world.getOtherEntities(entity, box, e -> e instanceof MossbloomEntity).isEmpty();

        if (mossbloomNearby) {
            entity.handleFallDamage(fallDistance, 1.0F, world.getDamageSources().fall());
            ci.cancel();
        }
    }
}

