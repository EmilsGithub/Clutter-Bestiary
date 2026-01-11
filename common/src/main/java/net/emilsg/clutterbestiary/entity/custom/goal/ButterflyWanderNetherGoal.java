package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.EnumSet;

public class ButterflyWanderNetherGoal extends Goal {
    private static final int TARGET_ATTEMPTS = 10;
    private final ButterflyEntity butterfly;

    public ButterflyWanderNetherGoal(ButterflyEntity butterfly) {
        this.butterfly = butterfly;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return !this.butterfly.isInFluid()
                && !this.butterfly.getWorld().getDimensionEntry().matchesKey(DimensionTypes.OVERWORLD)
                && this.butterfly.getNavigation().isIdle() && this.butterfly.getRandom().nextInt(3) == 0;
    }

    @Override
    public boolean shouldContinue() {
        return !this.butterfly.isInFluid() && this.butterfly.getNavigation().isFollowingPath();
    }

    @Override
    public void start() {
        BlockPos origin = this.butterfly.getBlockPos();
        for (int attempt = 0; attempt < TARGET_ATTEMPTS; attempt++) {
            BlockPos targetPos = this.getRandomPos(origin);
            if (!this.butterfly.isSafeFlightTarget(targetPos)) continue;

            Path path = this.butterfly.getNavigation().findPathTo(targetPos, 1);
            if (path != null) {
                this.butterfly.getNavigation().startMovingAlong(path, 1.0);
                return;
            }
        }
    }

    private BlockPos getRandomPos(BlockPos center) {
        return center.add(
                this.butterfly.getRandom().nextBetween(-24, 24),
                this.butterfly.getRandom().nextBetween(-8, 8),
                this.butterfly.getRandom().nextBetween(-24, 24)
        );
    }
}
