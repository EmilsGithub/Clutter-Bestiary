package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.util.math.Vec3d;

public class TrackedFleeGoal extends EscapeDangerGoal {
    private static final int MAX_EXTRA_FLEES = 4;

    private ParentAnimalEntity animalEntity;
    private ParentTameableEntity tameableEntity;

    private int extraFleesDone;

    public TrackedFleeGoal(ParentAnimalEntity mob, double speed) {
        super(mob, speed);
        this.animalEntity = mob;
    }

    public TrackedFleeGoal(ParentTameableEntity mob, double speed) {
        super(mob, speed);
        this.tameableEntity = mob;
    }

    @Override
    public boolean shouldContinue() {
        if (!this.mob.getNavigation().isIdle()) {
            return true;
        }

        if (this.extraFleesDone >= MAX_EXTRA_FLEES) {
            return false;
        }

        if (!this.findTarget()) {
            return false;
        }

        this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed);
        this.extraFleesDone++;
        return true;
    }

    @Override
    public void start() {
        this.extraFleesDone = 0;
        super.start();

        if (this.animalEntity != null) this.animalEntity.setIsFleeing(true);
        if (this.tameableEntity != null) this.tameableEntity.setIsFleeing(true);
    }

    @Override
    public void stop() {
        super.stop();

        if (this.animalEntity != null) this.animalEntity.setIsFleeing(false);
        if (this.tameableEntity != null) this.tameableEntity.setIsFleeing(false);
    }

    @Override
    protected boolean findTarget() {
        Vec3d vec3d = NoPenaltyTargeting.find(this.mob, 24, 6);
        if (vec3d == null) {
            return false;
        }

        this.targetX = vec3d.x;
        this.targetY = vec3d.y;
        this.targetZ = vec3d.z;
        return true;
    }
}