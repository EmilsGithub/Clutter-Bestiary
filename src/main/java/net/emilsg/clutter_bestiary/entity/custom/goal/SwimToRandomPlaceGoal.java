package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.parent.ParentFishEntity;
import net.minecraft.entity.ai.goal.SwimAroundGoal;

public class SwimToRandomPlaceGoal extends SwimAroundGoal {
    private final ParentFishEntity fish;

    public SwimToRandomPlaceGoal(ParentFishEntity fish, double speed) {
        super(fish, speed, 40);
        this.fish = fish;
    }

    public boolean canStart() {
        return this.fish.getHasSelfControl() && super.canStart();
    }
}
