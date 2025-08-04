package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralBlock;
import net.minecraft.block.CoralFanBlock;
import net.minecraft.block.CoralWallFanBlock;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class SeahorseMoveToCoralGoal extends MoveToTargetPosGoal {
    SeahorseEntity seahorseEntity;

    public SeahorseMoveToCoralGoal(SeahorseEntity seahorseEntity, double speed, int range) {
        super(seahorseEntity, speed, range);
        this.seahorseEntity = seahorseEntity;
    }

    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof CoralFanBlock || state.getBlock() instanceof CoralWallFanBlock || state.getBlock() instanceof CoralBlock;
    }
}
