package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class TamedEscapeDangerGoal extends PanicGoal {

    public TamedEscapeDangerGoal(ParentTameableEntity entity, double speed) {
        super(entity, speed);
    }

    protected boolean shouldPanic() {
        return this.mob.isFreezing() || this.mob.isOnFire();
    }
}
