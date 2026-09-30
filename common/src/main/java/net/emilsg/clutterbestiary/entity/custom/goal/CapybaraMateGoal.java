package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.world.entity.ai.goal.BreedGoal;

public class CapybaraMateGoal extends BreedGoal {
    private final CapybaraEntity capybara;

    public CapybaraMateGoal(CapybaraEntity capybara, double speed) {
        super(capybara, speed);
        this.capybara = capybara;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.capybara.isSleeping();
    }
}
