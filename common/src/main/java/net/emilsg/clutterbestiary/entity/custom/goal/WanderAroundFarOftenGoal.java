package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;

public class WanderAroundFarOftenGoal extends WanderAroundGoal {
    private final PathAwareEntity pathAwareEntity;

    public WanderAroundFarOftenGoal(PathAwareEntity pathAwareEntity, float speed) {
        super(pathAwareEntity, speed);
        this.pathAwareEntity = pathAwareEntity;
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
        return !this.pathAwareEntity.getNavigation().isIdle() && !this.pathAwareEntity.hasPassengers() && this.pathAwareEntity.getNavigation().isFollowingPath();
    }

}
