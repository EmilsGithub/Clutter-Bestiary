package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class ChorusBeetleReturnFlowerGoal extends Goal {
    private final ChorusBeetleEntity chorusBeetle;
    private final double speed;
    private int pathUpdateCountdown;

    public ChorusBeetleReturnFlowerGoal(ChorusBeetleEntity chorusBeetle, double speed) {
        this.chorusBeetle = chorusBeetle;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.canReturnToRequester();
    }

    @Override
    public boolean shouldContinue() {
        return this.canReturnToRequester();
    }

    @Override
    public void start() {
        this.pathUpdateCountdown = 0;
    }

    @Override
    public void stop() {
        this.chorusBeetle.getNavigation().stop();
        if (this.chorusBeetle.isFlying() && !this.chorusBeetle.isCarryingChorusFlower() && !this.chorusBeetle.isLanding()) {
            this.chorusBeetle.beginLanding();
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        PlayerEntity player = this.chorusBeetle.getFlowerRequester();
        if (player == null) return;

        this.chorusBeetle.getLookControl().lookAt(player, 30.0f, 30.0f);
        if (--this.pathUpdateCountdown <= 0) {
            this.pathUpdateCountdown = 10;
            this.chorusBeetle.getNavigation().startMovingTo(player, this.speed);
        }
    }

    private boolean canReturnToRequester() {
        return this.chorusBeetle.isAlive() && this.chorusBeetle.isFlying() && this.chorusBeetle.isCarryingChorusFlower()
                && !this.chorusBeetle.isPostBreakHovering() && !this.chorusBeetle.isLanding()
                && this.chorusBeetle.getFlowerRequester() != null;
    }
}
