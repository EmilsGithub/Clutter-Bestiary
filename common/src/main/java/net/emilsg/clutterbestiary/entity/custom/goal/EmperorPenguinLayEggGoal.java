package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmperorPenguinEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;

public class EmperorPenguinLayEggGoal extends GroundNestLayEggGoal<EmperorPenguinEntity> {
    public EmperorPenguinLayEggGoal(EmperorPenguinEntity emperorPenguinEntity, double speed, BlockState eggState) {
        super(emperorPenguinEntity, speed, eggState);
    }

    @Override
    protected boolean isValidNestBlock(BlockState state) {
        return state.isIn(BlockTags.ICE) || state.isOf(Blocks.SNOW_BLOCK) || state.isIn(BlockTags.DIRT);
    }
}
