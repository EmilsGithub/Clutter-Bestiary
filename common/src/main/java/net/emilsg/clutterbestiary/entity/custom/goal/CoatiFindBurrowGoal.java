package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class CoatiFindBurrowGoal extends Goal {
    private final CoatiEntity coati;
    private final int daysFedHoneyNeeded;
    private boolean handedOff;

    public CoatiFindBurrowGoal(CoatiEntity coatiEntity, int daysFedHoneyNeeded) {
        this.coati = coatiEntity;
        this.daysFedHoneyNeeded = daysFedHoneyNeeded;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.coati.getPostDigCooldown() > 0) return false;
        BlockPos burrowPos = this.coati.getBurrowPos();
        return !this.coati.isBaby() && !this.coati.isDigSessionActive() && this.coati.getDaysFedHoney() >= this.daysFedHoneyNeeded && this.coati.isOnGround() && !this.coati.getWorld().getBlockState(burrowPos.down()).isAir();
    }

    @Override
    public boolean shouldContinue() {
        return !handedOff && !this.coati.getNavigation().isIdle() && this.coati.getNavigation().isFollowingPath();
    }

    @Override
    public void start() {
        handedOff = false;
    }

    @Override
    public void stop() {
        handedOff = true;
    }

    @Override
    public void tick() {
        BlockPos target = this.coati.getBurrowPos();

        // While we're still running toward the burrow, always be sniffing
        if (!target.isWithinDistance(this.coati.getPos(), 1.5f)) {
            // Only force if not already sniffing, so we don't reset the anim timer every tick
            if (!this.coati.isSniffing()
                    && this.coati.isOnGround()
                    && !this.coati.isInsideWaterOrBubbleColumn()) {
                this.coati.startState(CoatiEntityAnimationState.SNIFFING);
            }

            this.coati.getNavigation().startMovingTo(
                    target.getX() + 0.5,
                    target.getY(),
                    target.getZ() + 0.5,
                    0.75
            );
            return;
        }

        // Reached burrow – hand off to digging logic
        if (!handedOff && this.coati.isOnGround() && !this.coati.isInsideWaterOrBubbleColumn()) {
            this.coati.getNavigation().stop();

            // Optional: keep SNIFFING here too, or switch to another state if you want
            if (!this.coati.isSniffing()) {
                this.coati.startState(CoatiEntityAnimationState.SNIFFING);
            }

            this.coati.setDaysFedHoney(0);
            this.coati.setDigging(true);
            this.coati.setPostDigCooldown(40);
            this.coati.setDigSessionActive(true);

            handedOff = true;
            this.stop();
        }
    }
}

