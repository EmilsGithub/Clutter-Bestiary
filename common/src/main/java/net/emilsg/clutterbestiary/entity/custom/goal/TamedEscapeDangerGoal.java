package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;

public class TamedEscapeDangerGoal extends EscapeDangerGoal {

    public TamedEscapeDangerGoal(ParentTameableEntity entity, double speed) {
        super(entity, speed);
    }

    protected boolean isInDanger() {
        return this.mob.shouldEscapePowderSnow() || this.mob.isOnFire();
    }
}
