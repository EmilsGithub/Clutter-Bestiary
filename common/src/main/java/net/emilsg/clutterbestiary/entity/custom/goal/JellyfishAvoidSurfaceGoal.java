package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.JellyfishEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class JellyfishAvoidSurfaceGoal extends Goal {
    private final JellyfishEntity jellyfish;

    public JellyfishAvoidSurfaceGoal(JellyfishEntity jellyfish) {
        this.jellyfish = jellyfish;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!jellyfish.isInWater()) return false;

        double surfaceY = jellyfish.level().getMaxY();
        BlockPos pos = jellyfish.blockPosition();

        for (int i = 0; i < 3; i++) {
            BlockPos check = pos.above(i);
            if (jellyfish.level().getBlockState(check).getFluidState().isEmpty()) {
                surfaceY = check.getY();
                break;
            }
        }

        return surfaceY - jellyfish.getY() < 1.0;
    }

    @Override
    public void tick() {
        this.jellyfish.setSwimmingVector(this.jellyfish.getSwimX(), -0.2f, this.jellyfish.getSwimZ());
    }
}