package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class ChorusBeetleFlyAroundGoal extends Goal {
    private static final int MIN_FLIGHT_DELAY_TICKS = 600;
    private static final int MAX_FLIGHT_DELAY_TICKS = 1800;
    private static final int MIN_FLIGHT_TICKS = 80;
    private static final int MAX_FLIGHT_TICKS = 160;
    private static final int HORIZONTAL_RANGE = 8;
    private static final int VERTICAL_RANGE = 4;
    private static final int MIN_HEIGHT_ABOVE_GROUND = 2;
    private static final int MAX_HEIGHT_ABOVE_GROUND = 5;
    private static final float DIRECTION_RANGE = (float) Math.PI / 2.0f;

    private final ChorusBeetleEntity chorusBeetle;
    private final double speed;
    private int flightTicks;
    private long nextFlightTime;

    public ChorusBeetleFlyAroundGoal(ChorusBeetleEntity chorusBeetle, double speed) {
        this.chorusBeetle = chorusBeetle;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.scheduleNextFlight();
    }

    @Override
    public boolean canStart() {
        if (this.chorusBeetle.getWorld().getTime() < this.nextFlightTime) return false;
        if (!this.chorusBeetle.isAlive() || !this.chorusBeetle.isOnGround() || this.chorusBeetle.isFlying()) return false;
        if (this.chorusBeetle.isTouchingWater() || this.chorusBeetle.isInLove()) return false;
        if (this.chorusBeetle.hasFlowerFetchRequest() || this.chorusBeetle.isCarryingChorusFlower()) return false;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return this.chorusBeetle.isAlive() && this.chorusBeetle.isFlying()
                && !this.chorusBeetle.isLanding() && this.flightTicks > 0;
    }

    @Override
    public void start() {
        this.flightTicks = this.chorusBeetle.getRandom().nextBetween(MIN_FLIGHT_TICKS, MAX_FLIGHT_TICKS);
        this.chorusBeetle.setFlying(true);
        this.startMovingToNextPosition();
    }

    @Override
    public void stop() {
        this.chorusBeetle.getNavigation().stop();
        if (this.chorusBeetle.isFlying() && !this.chorusBeetle.isLanding()) {
            this.chorusBeetle.beginLanding();
        }
        this.scheduleNextFlight();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (--this.flightTicks <= 0) {
            this.chorusBeetle.beginLanding();
            return;
        }

        if (this.chorusBeetle.getNavigation().isIdle()) this.startMovingToNextPosition();
    }

    private void startMovingToNextPosition() {
        Vec3d direction = this.chorusBeetle.getRotationVec(0.0f);
        Vec3d target = AboveGroundTargeting.find(this.chorusBeetle, HORIZONTAL_RANGE, VERTICAL_RANGE,
                direction.x, direction.z, DIRECTION_RANGE, MAX_HEIGHT_ABOVE_GROUND, MIN_HEIGHT_ABOVE_GROUND);
        if (target != null) {
            this.chorusBeetle.getNavigation().startMovingTo(target.x, target.y, target.z, this.speed);
        }
    }

    private void scheduleNextFlight() {
        this.nextFlightTime = this.chorusBeetle.getWorld().getTime()
                + this.chorusBeetle.getRandom().nextBetween(MIN_FLIGHT_DELAY_TICKS, MAX_FLIGHT_DELAY_TICKS);
    }
}
