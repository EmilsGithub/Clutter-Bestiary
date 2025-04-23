package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WanderAroundFarOftenGoal extends Goal {
    private final PathAwareEntity pathAwareEntity;
    private final float speed;

    public WanderAroundFarOftenGoal(PathAwareEntity pathAwareEntity, float speed) {
        this.pathAwareEntity = pathAwareEntity;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public boolean shouldContinue() {
        return !this.pathAwareEntity.getNavigation().isIdle() && !this.pathAwareEntity.hasPassengers() && this.pathAwareEntity.getNavigation().isFollowingPath();
    }

    public void start() {
        Vec3d wanderTarget = getWanderTarget();
        if (wanderTarget == null) return;

        this.pathAwareEntity.getNavigation().startMovingTo(wanderTarget.getX(), wanderTarget.getY(), wanderTarget.getZ(), this.speed);
    }

    public void stop() {
        this.pathAwareEntity.getNavigation().stop();
        super.stop();
    }

    @Override
    public boolean canStart() {
        return this.pathAwareEntity.getNavigation().isIdle() && this.pathAwareEntity.getRandom().nextInt(4) == 0;
    }

    @Nullable
    protected Vec3d getWanderTarget() {
        if (this.pathAwareEntity.isInsideWaterOrBubbleColumn()) {
            Vec3d preferredPath = FuzzyTargeting.find(this.pathAwareEntity, 15, 6);
            return preferredPath == null ? NoPenaltyTargeting.find(this.pathAwareEntity, 10, 6) : preferredPath;
        } else {
            return FuzzyTargeting.find(this.pathAwareEntity, 10, 6);
        }
    }


}
