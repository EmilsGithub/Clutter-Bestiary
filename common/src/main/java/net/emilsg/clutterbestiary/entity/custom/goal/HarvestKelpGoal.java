package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

public class HarvestKelpGoal extends MoveToBlockGoal {
    private final RiverTurtleEntity riverTurtleEntity;
    private final float chancePerTick;

    public HarvestKelpGoal(RiverTurtleEntity riverTurtleEntity, double speed, int range, float chancePerTick) {
        super(riverTurtleEntity, speed, range);
        this.riverTurtleEntity = riverTurtleEntity;
        this.chancePerTick = chancePerTick;
    }

    @Override
    public boolean canUse() {
        return this.riverTurtleEntity.level().isBrightOutside()
                && this.riverTurtleEntity.getRandom().nextFloat() <= this.chancePerTick
                && this.findNearestBlock();
    }

    @Override
    public void tick() {
        super.tick();

        Level world = this.riverTurtleEntity.level();
        if (!world.isClientSide()) {
            if (this.isReachedTarget()) {
                world.destroyBlock(blockPos, true, this.riverTurtleEntity);
                this.stop();
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        return (world.getBlockState(pos).is(Blocks.KELP) || world.getBlockState(pos).is(Blocks.KELP_PLANT)) && world.getBlockState(pos.below()).is(Blocks.KELP_PLANT);
    }
}
