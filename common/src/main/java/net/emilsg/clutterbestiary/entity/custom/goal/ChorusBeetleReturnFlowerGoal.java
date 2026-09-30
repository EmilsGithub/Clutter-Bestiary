package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import java.util.EnumSet;

public class ChorusBeetleReturnFlowerGoal extends Goal {
    private final ChorusBeetleEntity chorusBeetle;
    private final double speed;
    private int pathUpdateCountdown;

    public ChorusBeetleReturnFlowerGoal(ChorusBeetleEntity chorusBeetle, double speed) {
        this.chorusBeetle = chorusBeetle;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.canReturnToRequester();
    }

    @Override
    public boolean canContinueToUse() {
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
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        Player player = this.chorusBeetle.getFlowerRequester();
        if (player == null) return;

        this.chorusBeetle.getLookControl().setLookAt(player, 30.0f, 30.0f);
        if (--this.pathUpdateCountdown <= 0) {
            this.pathUpdateCountdown = 10;
            this.chorusBeetle.getNavigation().moveTo(player, this.speed);
        }
    }

    private boolean canReturnToRequester() {
        return this.chorusBeetle.isAlive() && this.chorusBeetle.isFlying() && this.chorusBeetle.isCarryingChorusFlower()
                && !this.chorusBeetle.isPostBreakHovering() && !this.chorusBeetle.isLanding()
                && this.chorusBeetle.getFlowerRequester() != null;
    }
}
