package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.block.custom.ButterflyCocoonBlock;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class ButterflyPlaceCocoonGoal extends MoveToTargetPosGoal {
    private final ButterflyEntity butterfly;

    public ButterflyPlaceCocoonGoal(ButterflyEntity butterfly, double speed) {
        super(butterfly, speed, 16);
        this.butterfly = butterfly;
    }

    @Override
    public boolean canStart() {
        return this.butterfly.hasCocoon() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return this.butterfly.hasCocoon() && super.shouldContinue();
    }

    @Override
    protected void startMovingToTarget() {
        this.butterfly.getNavigation().startMovingTo(
                this.targetPos.getX() + 0.5,
                this.targetPos.getY(),
                this.targetPos.getZ() + 0.5,
                this.speed
        );
    }

    @Override
    protected BlockPos getTargetPos() {
        return this.targetPos;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.hasReached() || !(this.butterfly.getWorld() instanceof ServerWorld serverWorld)) return;
        if (!this.isTargetPos(serverWorld, this.targetPos)) return;

        BlockState cocoonState = this.getCocoonState();
        if (serverWorld.setBlockState(this.targetPos, cocoonState, Block.NOTIFY_ALL)) {
            serverWorld.emitGameEvent(GameEvent.BLOCK_PLACE, this.targetPos, GameEvent.Emitter.of(this.butterfly, cocoonState));
            this.cooldown = 0;
            this.butterfly.setHasCocoon(false);
            this.butterfly.getNavigation().stop();
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState cocoonState = this.getCocoonState();
        return world.getBlockState(pos).isReplaceable() && world.getFluidState(pos).isEmpty()
                && cocoonState.canPlaceAt(world, pos);
    }

    private BlockState getCocoonState() {
        return ModBlocks.BUTTERFLY_COCOON.get().getDefaultState().with(ButterflyCocoonBlock.CAN_HATCH, true);
    }
}
