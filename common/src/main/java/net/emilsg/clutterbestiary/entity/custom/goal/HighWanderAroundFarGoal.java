package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HighWanderAroundFarGoal extends RandomStrollGoal {
    protected final float probability;

    public HighWanderAroundFarGoal(PathfinderMob mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
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
        return !this.mob.getNavigation().isDone() && !this.mob.isVehicle() && this.mob.getNavigation().isInProgress();
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 vec3d = LandRandomPos.getPos(this.mob, 15, 9);
        return vec3d == null ? super.getPosition() : vec3d;
    }
}
