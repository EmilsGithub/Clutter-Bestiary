package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class TrackedFleeGoal extends PanicGoal {
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
    public boolean canContinueToUse() {
        if (!this.mob.getNavigation().isDone()) {
            return true;
        }

        if (this.extraFleesDone >= MAX_EXTRA_FLEES) {
            return false;
        }

        if (!this.findRandomPosition()) {
            return false;
        }

        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
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
    protected boolean findRandomPosition() {
        Vec3 vec3d = DefaultRandomPos.getPos(this.mob, 24, 6);
        if (vec3d == null) {
            return false;
        }

        this.posX = vec3d.x;
        this.posY = vec3d.y;
        this.posZ = vec3d.z;
        return true;
    }
}