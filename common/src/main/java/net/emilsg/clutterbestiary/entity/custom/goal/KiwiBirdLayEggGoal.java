package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;

public class KiwiBirdLayEggGoal extends GroundNestLayEggGoal<KiwiBirdEntity> {
    public KiwiBirdLayEggGoal(KiwiBirdEntity kiwiBird, double speed, BlockState eggState) {
        super(kiwiBird, speed, eggState);
    }

    @Override
    protected boolean isValidNestBlock(BlockState state) {
        return state.isIn(BlockTags.DIRT) || state.isOf(Blocks.HAY_BLOCK);
    }
}
