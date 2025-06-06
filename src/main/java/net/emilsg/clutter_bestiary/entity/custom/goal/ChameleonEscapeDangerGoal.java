package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.ChameleonEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;

public class ChameleonEscapeDangerGoal extends EscapeDangerGoal {

    public ChameleonEscapeDangerGoal(ChameleonEntity chameleonEntity, double speed) {
        super(chameleonEntity, speed);
    }

    protected boolean isInDanger() {
        return this.mob.shouldEscapePowderSnow() || this.mob.isOnFire();
    }
}
