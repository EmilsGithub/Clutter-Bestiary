package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class CapybaraFollowOwnerGoal extends FollowOwnerGoal {
    private final CapybaraEntity capybara;

    public CapybaraFollowOwnerGoal(CapybaraEntity capybara, double speed, float minDistance, float maxDistance) {
        super(capybara, speed, minDistance, maxDistance);
        this.capybara = capybara;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.capybara.isSleeping();
    }
}
