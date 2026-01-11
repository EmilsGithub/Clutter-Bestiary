package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;

public class CapybaraWanderGoal extends WanderAroundFarGoal {
    private final CapybaraEntity capybara;

    public CapybaraWanderGoal(CapybaraEntity capybara, double speed, float probability) {
        super(capybara, speed, probability);
        this.capybara = capybara;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.capybara.isSleeping();
    }

    @Override
    public void tick() {
        if (this.capybara.isSleeping()) {
            this.stop();
            mob.getNavigation().stop();
        }
        super.tick();
    }
}
