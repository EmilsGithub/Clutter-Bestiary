package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

public class LeaveWaterGoal extends MoveToBlockGoal {
    private final PathfinderMob mob;

    public LeaveWaterGoal(PathfinderMob mob, double speed) {
        super(mob, speed, 12, 2);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.mob.isInWater() && this.mob.getY() >= this.mob.level().getSeaLevel() - 3 && this.mob.getRandom().nextInt(200) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return world.isEmptyBlock(blockPos) && world.isEmptyBlock(blockPos.above()) && world.getBlockState(pos).entityCanStandOn(world, pos, this.mob);
    }
}
