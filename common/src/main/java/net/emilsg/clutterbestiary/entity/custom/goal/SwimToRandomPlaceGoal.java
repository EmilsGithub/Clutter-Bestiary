package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentFishEntity;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;

public class SwimToRandomPlaceGoal extends RandomSwimmingGoal {
    private final ParentFishEntity fish;

    public SwimToRandomPlaceGoal(ParentFishEntity fish, double speed) {
        super(fish, speed, 40);
        this.fish = fish;
    }

    public boolean canUse() {
        return this.fish.getHasSelfControl() && super.canUse();
    }
}
