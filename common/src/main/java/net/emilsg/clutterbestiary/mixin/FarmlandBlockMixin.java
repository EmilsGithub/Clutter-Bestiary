package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.custom.MossbloomEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin {

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void clutterbestiary$preventFarmlandTrampling(Level world, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo callbackInfo) {
        AABB searchBox = entity.getBoundingBox().inflate(5.0);
        boolean mossbloomNearby = !world.getEntities(entity, searchBox, nearbyEntity -> nearbyEntity instanceof MossbloomEntity).isEmpty();

        if (mossbloomNearby) {
            entity.causeFallDamage(fallDistance, 1.0F, world.damageSources().fall());
            callbackInfo.cancel();
        }
    }
}

