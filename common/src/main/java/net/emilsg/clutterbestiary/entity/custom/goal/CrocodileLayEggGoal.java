package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.block.custom.CrocodileEggBlock;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CrocodileLayEggGoal extends GroundNestLayEggGoal<CrocodileEntity> {
    public CrocodileLayEggGoal(CrocodileEntity crocodile, double speed, BlockState eggState) {
        super(crocodile, speed, eggState);
    }

    @Override
    protected BlockState getEggState(ServerLevel world) {
        return super.getEggState(world).setValue(CrocodileEggBlock.EGGS, world.getRandom().nextIntBetweenInclusive(1, 3));
    }

    @Override
    protected boolean isValidNestBlock(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.MUD);
    }
}
