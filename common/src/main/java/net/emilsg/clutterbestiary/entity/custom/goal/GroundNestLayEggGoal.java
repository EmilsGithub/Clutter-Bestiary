package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.IEggLayingAnimal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public abstract class GroundNestLayEggGoal<T extends PathAwareEntity & IEggLayingAnimal> extends MoveToTargetPosGoal {
    private final T eggLayer;
    private final BlockState eggState;

    protected GroundNestLayEggGoal(T eggLayer, double speed, BlockState eggState) {
        super(eggLayer, speed, 16);
        this.eggLayer = eggLayer;
        this.eggState = eggState;
    }

    @Override
    public boolean canStart() {
        return this.eggLayer.isReadyToLayEgg() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return this.eggLayer.isReadyToLayEgg() && super.shouldContinue();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.hasReached() || !(this.eggLayer.getWorld() instanceof ServerWorld serverWorld)) return;
        if (!this.isTargetPos(serverWorld, this.targetPos)) return;

        BlockPos eggPos = this.targetPos.up();
        BlockState placedEggState = this.getEggState(serverWorld);
        if (serverWorld.setBlockState(eggPos, placedEggState, Block.NOTIFY_ALL)) {
            serverWorld.emitGameEvent(GameEvent.BLOCK_PLACE, eggPos, GameEvent.Emitter.of(this.eggLayer, placedEggState));
            this.cooldown = 0;
            this.eggLayer.finishLayingEgg();
            this.eggLayer.getNavigation().stop();
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockPos eggPos = pos.up();
        BlockState replacedState = world.getBlockState(eggPos);
        return replacedState.isReplaceable() && world.getFluidState(eggPos).isEmpty()
                && this.eggState.canPlaceAt(world, eggPos) && this.isValidNestBlock(world.getBlockState(pos));
    }

    protected BlockState getEggState(ServerWorld world) {
        return this.eggState;
    }

    protected abstract boolean isValidNestBlock(BlockState state);
}
