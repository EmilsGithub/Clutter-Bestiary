package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyLarvaEntity;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ButterflyLarvaWanderGoal extends WanderAroundFarGoal {
    private final ButterflyLarvaEntity larva;

    public ButterflyLarvaWanderGoal(ButterflyLarvaEntity larva) {
        super(larva, 0.8);
        this.larva = larva;
    }

    @Override
    protected @Nullable Vec3d getWanderTarget() {
        for (int attempt = 0; attempt < 8; attempt++) {
            Vec3d target = super.getWanderTarget();
            if (target != null
                    && this.larva.getHomePos().isWithinDistance(target, ButterflyLarvaEntity.HOME_RADIUS)
                    && this.isDryTarget(target)) {
                return target;
            }
        }

        return null;
    }

    private boolean isDryTarget(Vec3d target) {
        World world = this.larva.getWorld();
        BlockPos targetPos = BlockPos.ofFloored(target);
        return world.getFluidState(targetPos).isEmpty()
                && world.getFluidState(targetPos.down()).isEmpty()
                && world.getBlockState(targetPos.down()).isSolidBlock(world, targetPos.down());
    }
}
