package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmperorPenguinEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EmperorPenguinLayEggGoal extends GroundNestLayEggGoal<EmperorPenguinEntity> {
    public EmperorPenguinLayEggGoal(EmperorPenguinEntity emperorPenguinEntity, double speed, BlockState eggState) {
        super(emperorPenguinEntity, speed, eggState);
    }

    @Override
    protected boolean isValidNestBlock(BlockState state) {
        return state.is(BlockTags.ICE) || state.is(Blocks.SNOW_BLOCK) || state.is(BlockTags.DIRT);
    }
}
