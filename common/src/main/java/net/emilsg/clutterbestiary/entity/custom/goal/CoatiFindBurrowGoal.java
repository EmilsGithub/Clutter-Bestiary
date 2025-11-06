package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class CoatiFindBurrowGoal extends Goal {
    private final CoatiEntity coati;
    private boolean handedOff;

    public CoatiFindBurrowGoal(CoatiEntity coatiEntity) {
        this.coati = coatiEntity;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.coati.getPostDigCooldown() > 0) return false;
        BlockPos burrowPos = this.coati.getBurrowPos();
        return !this.coati.isBaby() && !this.coati.isDigSessionActive() && this.coati.getDaysFedHoney() >= 1 && this.coati.isOnGround() && !this.coati.getWorld().getBlockState(burrowPos.down()).isAir();
    }

    @Override
    public void start() {
        handedOff = false;
    }

    @Override
    public void tick() {
        BlockPos target = this.coati.getBurrowPos();
        if (!target.isWithinDistance(this.coati.getPos(), 1.5f)) {
            this.coati.getNavigation().startMovingTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 0.75);
            return;
        }

        if (!handedOff && this.coati.isOnGround() && !this.coati.isInsideWaterOrBubbleColumn()) {
            this.coati.getNavigation().stop();
            this.coati.startState(CoatiEntity.CoatiEntityAnimationState.SNIFFING);

            this.coati.setDaysFedHoney(0);
            this.coati.setDigging(true);
            this.coati.setPostDigCooldown(40);

            this.coati.setDigSessionActive(true);
            handedOff = true;
            this.stop();
        }
    }

    @Override
    public boolean shouldContinue() {
        return !handedOff && !this.coati.getNavigation().isIdle() && this.coati.getNavigation().isFollowingPath();
    }

    @Override
    public void stop() { handedOff = true; }
}

