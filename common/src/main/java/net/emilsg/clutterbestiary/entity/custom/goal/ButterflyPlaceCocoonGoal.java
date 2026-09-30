package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.block.custom.ButterflyCocoonBlock;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ButterflyPlaceCocoonGoal extends MoveToBlockGoal {
    private final ButterflyEntity butterfly;

    public ButterflyPlaceCocoonGoal(ButterflyEntity butterfly, double speed) {
        super(butterfly, speed, 16);
        this.butterfly = butterfly;
    }

    @Override
    public boolean canUse() {
        return this.butterfly.hasCocoon() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.butterfly.hasCocoon() && super.canContinueToUse();
    }

    @Override
    protected void moveMobToBlock() {
        this.butterfly.getNavigation().moveTo(
                this.blockPos.getX() + 0.5,
                this.blockPos.getY(),
                this.blockPos.getZ() + 0.5,
                this.speedModifier
        );
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return this.blockPos;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isReachedTarget() || !(this.butterfly.level() instanceof ServerLevel serverWorld)) return;
        if (!this.isValidTarget(serverWorld, this.blockPos)) return;

        BlockState cocoonState = this.getCocoonState();
        if (serverWorld.setBlock(this.blockPos, cocoonState, Block.UPDATE_ALL)) {
            serverWorld.gameEvent(GameEvent.BLOCK_PLACE, this.blockPos, GameEvent.Context.of(this.butterfly, cocoonState));
            this.nextStartTick = 0;
            this.butterfly.setHasCocoon(false);
            this.butterfly.getNavigation().stop();
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState cocoonState = this.getCocoonState();
        return world.getBlockState(pos).canBeReplaced() && world.getFluidState(pos).isEmpty()
                && cocoonState.canSurvive(world, pos);
    }

    private BlockState getCocoonState() {
        return ModBlocks.BUTTERFLY_COCOON.get().defaultBlockState().setValue(ButterflyCocoonBlock.CAN_HATCH, true);
    }
}
