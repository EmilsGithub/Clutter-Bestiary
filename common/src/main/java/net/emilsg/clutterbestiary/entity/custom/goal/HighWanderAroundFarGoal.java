package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class HighWanderAroundFarGoal extends WanderAroundGoal {
    protected final float probability;

    public HighWanderAroundFarGoal(PathAwareEntity mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
    }

    @Override
    public boolean canStart() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (this.mob.getRandom().nextInt(4) == 0) {
                return false;
            }

            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            } else {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                this.ignoringChance = false;
                return true;
            }
        }
    }

    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle() && !this.mob.hasPassengers() && this.mob.getNavigation().isFollowingPath();
    }

    @Nullable
    protected Vec3d getWanderTarget() {
        Vec3d vec3d = FuzzyTargeting.find(this.mob, 15, 9);
        return vec3d == null ? super.getWanderTarget() : vec3d;
    }
}
