package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

public class WanderAroundFarOftenGoal extends RandomStrollGoal {
    private final PathfinderMob pathAwareEntity;

    public WanderAroundFarOftenGoal(PathfinderMob pathAwareEntity, float speed) {
        super(pathAwareEntity, speed);
        this.pathAwareEntity = pathAwareEntity;
    }

    @Override
    public boolean canUse() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (this.mob.getRandom().nextInt(4) == 0) {
                return false;
            }

            Vec3 vec3d = this.getPosition();
            if (vec3d == null) {
                return false;
            } else {
                this.wantedX = vec3d.x;
                this.wantedY = vec3d.y;
                this.wantedZ = vec3d.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.pathAwareEntity.getNavigation().isDone() && !this.pathAwareEntity.isVehicle() && this.pathAwareEntity.getNavigation().isInProgress();
    }

}
