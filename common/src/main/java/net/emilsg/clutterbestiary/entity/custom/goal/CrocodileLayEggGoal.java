package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.block.custom.CrocodileEggBlock;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;

public class CrocodileLayEggGoal extends GroundNestLayEggGoal<CrocodileEntity> {
    public CrocodileLayEggGoal(CrocodileEntity crocodile, double speed, BlockState eggState) {
        super(crocodile, speed, eggState);
    }

    @Override
    protected BlockState getEggState(ServerWorld world) {
        return super.getEggState(world).with(CrocodileEggBlock.EGGS, world.random.nextBetween(1, 3));
    }

    @Override
    protected boolean isValidNestBlock(BlockState state) {
        return state.isIn(BlockTags.DIRT) || state.isIn(BlockTags.SAND) || state.isOf(Blocks.MUD);
    }
}
