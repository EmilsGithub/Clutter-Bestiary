package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.BoopletEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;

public class BoopletFleeGoal extends EscapeDangerGoal {
    private final BoopletEntity boopletEntity;

    public BoopletFleeGoal(BoopletEntity mob, double speed) {
        super(mob, speed);
        this.boopletEntity = mob;
    }

    @Override
    public void start() {
        super.start();
        boopletEntity.setIsFleeing(true);
    }

    @Override
    public void stop() {
        super.stop();
        boopletEntity.setIsFleeing(false);
    }
}
