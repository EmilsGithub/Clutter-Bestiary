package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class LeaveWaterGoal extends MoveToTargetPosGoal {
    private final PathAwareEntity mob;

    public LeaveWaterGoal(PathAwareEntity mob, double speed) {
        super(mob, speed, 12, 2);
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && this.mob.isTouchingWater() && this.mob.getY() >= this.mob.getWorld().getSeaLevel() - 3 && this.mob.getRandom().nextInt(200) == 0;
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return world.isAir(blockPos) && world.isAir(blockPos.up()) && world.getBlockState(pos).hasSolidTopSurface(world, pos, this.mob);
    }
}
