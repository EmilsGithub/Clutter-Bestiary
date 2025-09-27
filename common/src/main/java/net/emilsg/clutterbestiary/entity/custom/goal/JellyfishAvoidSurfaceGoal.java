package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.JellyfishEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class JellyfishAvoidSurfaceGoal extends Goal {
    private final JellyfishEntity jellyfish;

    public JellyfishAvoidSurfaceGoal(JellyfishEntity jellyfish) {
        this.jellyfish = jellyfish;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!jellyfish.isInsideWaterOrBubbleColumn()) return false;

        double surfaceY = jellyfish.getWorld().getTopY();
        BlockPos pos = jellyfish.getBlockPos();

        for (int i = 0; i < 3; i++) {
            BlockPos check = pos.up(i);
            if (jellyfish.getWorld().getBlockState(check).getFluidState().isEmpty()) {
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