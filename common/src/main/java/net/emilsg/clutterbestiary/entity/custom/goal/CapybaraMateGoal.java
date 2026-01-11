package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;

public class CapybaraMateGoal extends AnimalMateGoal {
    private final CapybaraEntity capybara;

    public CapybaraMateGoal(CapybaraEntity capybara, double speed) {
        super(capybara, speed);
        this.capybara = capybara;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.capybara.isSleeping();
    }
}
