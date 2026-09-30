package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.pathfinder.Path;
import java.util.EnumSet;

public class ButterflyWanderNetherGoal extends Goal {
    private static final int TARGET_ATTEMPTS = 10;
    private final ButterflyEntity butterfly;

    public ButterflyWanderNetherGoal(ButterflyEntity butterfly) {
        this.butterfly = butterfly;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !this.butterfly.isInLiquid()
                && !this.butterfly.level().dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)
                && this.butterfly.getNavigation().isDone() && this.butterfly.getRandom().nextInt(3) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.butterfly.isInLiquid() && this.butterfly.getNavigation().isInProgress();
    }

    @Override
    public void start() {
        BlockPos origin = this.butterfly.blockPosition();
        for (int attempt = 0; attempt < TARGET_ATTEMPTS; attempt++) {
            BlockPos targetPos = this.getRandomPos(origin);
            if (!this.butterfly.isSafeFlightTarget(targetPos)) continue;

            Path path = this.butterfly.getNavigation().createPath(targetPos, 1);
            if (path != null) {
                this.butterfly.getNavigation().moveTo(path, 1.0);
                return;
            }
        }
    }

    private BlockPos getRandomPos(BlockPos center) {
        return center.offset(
                this.butterfly.getRandom().nextIntBetweenInclusive(-24, 24),
                this.butterfly.getRandom().nextIntBetweenInclusive(-8, 8),
                this.butterfly.getRandom().nextIntBetweenInclusive(-24, 24)
        );
    }
}
