package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ChorusBeetleBreakFlowerGoal extends MoveToBlockGoal {
    private static final int MIN_NOTICE_DELAY_TICKS = 20;
    private static final int MAX_NOTICE_DELAY_TICKS = 40;
    private final ChorusBeetleEntity chorusBeetle;
    private boolean waitingToNoticeMissingTarget;
    private int noticeDelayTicks;

    public ChorusBeetleBreakFlowerGoal(ChorusBeetleEntity chorusBeetle, double speed, int range) {
        super(chorusBeetle, speed, range, range);
        this.chorusBeetle = chorusBeetle;
    }

    @Override
    public boolean canUse() {
        return !this.chorusBeetle.isBaby() && !this.chorusBeetle.isFlying() && this.chorusBeetle.hasFlowerFetchRequest()
                && this.chorusBeetle.hasFlowerRequester() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.chorusBeetle.hasFlowerRequester()) return false;
        if (this.chorusBeetle.isCarryingChorusFlower()) return false;
        if (this.waitingToNoticeMissingTarget) {
            return this.chorusBeetle.isAlive() && this.chorusBeetle.isFlying() && !this.chorusBeetle.isLanding();
        }
        if (this.isValidTarget(this.chorusBeetle.level(), this.blockPos)) return super.canContinueToUse();
        if (!this.chorusBeetle.isFlying() || this.chorusBeetle.isPostBreakHovering() || this.chorusBeetle.isLanding()) return false;

        this.waitingToNoticeMissingTarget = true;
        this.noticeDelayTicks = this.chorusBeetle.getRandom().nextIntBetweenInclusive(MIN_NOTICE_DELAY_TICKS, MAX_NOTICE_DELAY_TICKS);
        this.chorusBeetle.getNavigation().stop();
        return true;
    }

    @Override
    public void start() {
        this.waitingToNoticeMissingTarget = false;
        this.nextStartTick = 0;
        this.chorusBeetle.consumeFlowerFetchRequest();
        this.chorusBeetle.setFlying(true);
        super.start();
    }

    @Override
    public void stop() {
        this.waitingToNoticeMissingTarget = false;
        this.chorusBeetle.getNavigation().stop();
        if (this.chorusBeetle.isFlying() && !this.chorusBeetle.isCarryingChorusFlower()
                && !this.chorusBeetle.isPostBreakHovering() && !this.chorusBeetle.isLanding()) {
            if (this.chorusBeetle.onGround()) {
                this.chorusBeetle.setFlying(false);
            } else {
                this.chorusBeetle.beginLanding();
            }
        }
        super.stop();
    }

    @Override
    public double acceptedDistance() {
        return 1.5;
    }

    @Override
    public void tick() {
        if (this.waitingToNoticeMissingTarget) {
            this.chorusBeetle.getNavigation().stop();
            if (--this.noticeDelayTicks <= 0) {
                this.waitingToNoticeMissingTarget = false;
                this.chorusBeetle.cancelFlowerFetchTaskWithAnger();
            }
            return;
        }

        super.tick();
        if (!this.isReachedTarget() || !(this.chorusBeetle.level() instanceof ServerLevel serverWorld)) return;

        if (this.isValidTarget(serverWorld, this.blockPos)) {
            if (serverWorld.destroyBlock(this.blockPos, false, this.chorusBeetle)) {
                this.chorusBeetle.queueChorusFlowerDrop();
                this.chorusBeetle.beginPostBreakHover(this.chorusBeetle.getRandom().nextIntBetweenInclusive(0, 20));
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.is(Blocks.CHORUS_FLOWER) && state.getValue(ChorusFlowerBlock.AGE) == ChorusFlowerBlock.DEAD_AGE;
    }
}
