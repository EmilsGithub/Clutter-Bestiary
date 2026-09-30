package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class KiwiBirdLayEggGoal extends GroundNestLayEggGoal<KiwiBirdEntity> {
    public KiwiBirdLayEggGoal(KiwiBirdEntity kiwiBird, double speed, BlockState eggState) {
        super(kiwiBird, speed, eggState);
    }

    @Override
    protected boolean isValidNestBlock(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.HAY_BLOCK);
    }
}
