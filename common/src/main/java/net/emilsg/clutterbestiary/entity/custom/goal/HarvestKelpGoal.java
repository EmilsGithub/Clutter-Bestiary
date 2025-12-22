package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class HarvestKelpGoal extends MoveToTargetPosGoal {
    private final RiverTurtleEntity riverTurtleEntity;
    private final float chancePerTick;

    public HarvestKelpGoal(RiverTurtleEntity riverTurtleEntity, double speed, int range, float chancePerTick) {
        super(riverTurtleEntity, speed, range);
        this.riverTurtleEntity = riverTurtleEntity;
        this.chancePerTick = chancePerTick;
    }

    @Override
    public boolean canStart() {
        return this.findTargetPos() && this.riverTurtleEntity.getRandom().nextFloat() <= this.chancePerTick && this.riverTurtleEntity.getWorld().isDay();
    }

    @Override
    public void tick() {
        super.tick();

        World world = this.riverTurtleEntity.getWorld();
        if (!world.isClient) {
            if (this.hasReached()) {
                world.breakBlock(targetPos, true, this.riverTurtleEntity);
                this.stop();
            }
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return (world.getBlockState(pos).isOf(Blocks.KELP) || world.getBlockState(pos).isOf(Blocks.KELP_PLANT)) && world.getBlockState(pos.down()).isOf(Blocks.KELP_PLANT);
    }
}
