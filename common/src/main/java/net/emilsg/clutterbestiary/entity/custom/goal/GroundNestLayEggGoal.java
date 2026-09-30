package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.IEggLayingAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public abstract class GroundNestLayEggGoal<T extends PathfinderMob & IEggLayingAnimal> extends MoveToBlockGoal {
    private final T eggLayer;
    private final BlockState eggState;

    protected GroundNestLayEggGoal(T eggLayer, double speed, BlockState eggState) {
        super(eggLayer, speed, 16);
        this.eggLayer = eggLayer;
        this.eggState = eggState;
    }

    @Override
    public boolean canUse() {
        return this.eggLayer.isReadyToLayEgg() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.eggLayer.isReadyToLayEgg() && super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isReachedTarget() || !(this.eggLayer.level() instanceof ServerLevel serverWorld)) return;
        if (!this.isValidTarget(serverWorld, this.blockPos)) return;

        BlockPos eggPos = this.blockPos.above();
        BlockState placedEggState = this.getEggState(serverWorld);
        if (serverWorld.setBlock(eggPos, placedEggState, Block.UPDATE_ALL)) {
            serverWorld.gameEvent(GameEvent.BLOCK_PLACE, eggPos, GameEvent.Context.of(this.eggLayer, placedEggState));
            this.nextStartTick = 0;
            this.eggLayer.finishLayingEgg();
            this.eggLayer.getNavigation().stop();
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockPos eggPos = pos.above();
        BlockState replacedState = world.getBlockState(eggPos);
        return replacedState.canBeReplaced() && world.getFluidState(eggPos).isEmpty()
                && this.eggState.canSurvive(world, eggPos) && this.isValidNestBlock(world.getBlockState(pos));
    }

    protected BlockState getEggState(ServerLevel world) {
        return this.eggState;
    }

    protected abstract boolean isValidNestBlock(BlockState state);
}
