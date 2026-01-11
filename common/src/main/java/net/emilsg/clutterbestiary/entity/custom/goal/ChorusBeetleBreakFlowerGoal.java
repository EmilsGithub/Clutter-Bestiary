package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class ChorusBeetleBreakFlowerGoal extends MoveToTargetPosGoal {
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
    public boolean canStart() {
        return !this.chorusBeetle.isBaby() && !this.chorusBeetle.isFlying() && this.chorusBeetle.hasFlowerFetchRequest()
                && this.chorusBeetle.hasFlowerRequester() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        if (!this.chorusBeetle.hasFlowerRequester()) return false;
        if (this.chorusBeetle.isCarryingChorusFlower()) return false;
        if (this.waitingToNoticeMissingTarget) {
            return this.chorusBeetle.isAlive() && this.chorusBeetle.isFlying() && !this.chorusBeetle.isLanding();
        }
        if (this.isTargetPos(this.chorusBeetle.getWorld(), this.targetPos)) return super.shouldContinue();
        if (!this.chorusBeetle.isFlying() || this.chorusBeetle.isPostBreakHovering() || this.chorusBeetle.isLanding()) return false;

        this.waitingToNoticeMissingTarget = true;
        this.noticeDelayTicks = this.chorusBeetle.getRandom().nextBetween(MIN_NOTICE_DELAY_TICKS, MAX_NOTICE_DELAY_TICKS);
        this.chorusBeetle.getNavigation().stop();
        return true;
    }

    @Override
    public void start() {
        this.waitingToNoticeMissingTarget = false;
        this.cooldown = 0;
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
            if (this.chorusBeetle.isOnGround()) {
                this.chorusBeetle.setFlying(false);
            } else {
                this.chorusBeetle.beginLanding();
            }
        }
        super.stop();
    }

    @Override
    public double getDesiredDistanceToTarget() {
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
        if (!this.hasReached() || !(this.chorusBeetle.getWorld() instanceof ServerWorld serverWorld)) return;

        if (this.isTargetPos(serverWorld, this.targetPos)) {
            if (serverWorld.breakBlock(this.targetPos, false, this.chorusBeetle)) {
                this.chorusBeetle.queueChorusFlowerDrop();
                this.chorusBeetle.beginPostBreakHover(this.chorusBeetle.getRandom().nextBetween(0, 20));
            }
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isOf(Blocks.CHORUS_FLOWER) && state.get(ChorusFlowerBlock.AGE) == ChorusFlowerBlock.MAX_AGE;
    }
}
