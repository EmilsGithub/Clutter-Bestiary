package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

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
