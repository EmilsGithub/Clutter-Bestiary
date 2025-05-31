package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutter_bestiary.entity.custom.parent.ParentTameableEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;

public class AnimalTrackedFleeGoal extends EscapeDangerGoal {
    private ParentAnimalEntity animalEntity = null;
    private ParentTameableEntity tameableEntity = null;

    public AnimalTrackedFleeGoal(ParentAnimalEntity mob, double speed) {
        super(mob, speed);
        this.animalEntity = mob;
    }

    public AnimalTrackedFleeGoal(ParentTameableEntity mob, double speed) {
        super(mob, speed);
        this.tameableEntity = mob;
    }

    @Override
    public void start() {
        super.start();
        if (animalEntity != null) animalEntity.setIsFleeing(true);
        if (tameableEntity != null) tameableEntity.setIsFleeing(true);
    }

    @Override
    public void stop() {
        super.stop();
        if (animalEntity != null) animalEntity.setIsFleeing(false);
        if (tameableEntity != null) tameableEntity.setIsFleeing(false);
    }
}
