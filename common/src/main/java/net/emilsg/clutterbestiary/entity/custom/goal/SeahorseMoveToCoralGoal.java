package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CoralFanBlock;
import net.minecraft.world.level.block.CoralPlantBlock;
import net.minecraft.world.level.block.CoralWallFanBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SeahorseMoveToCoralGoal extends MoveToBlockGoal {
    SeahorseEntity seahorseEntity;

    public SeahorseMoveToCoralGoal(SeahorseEntity seahorseEntity, double speed, int range) {
        super(seahorseEntity, speed, range);
        this.seahorseEntity = seahorseEntity;
    }

    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof CoralFanBlock || state.getBlock() instanceof CoralWallFanBlock || state.getBlock() instanceof CoralPlantBlock;
    }
}
