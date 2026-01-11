package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;

public class CapybaraLookAroundGoal extends LookAroundGoal {
    private final CapybaraEntity capybara;

    public CapybaraLookAroundGoal(CapybaraEntity capybara) {
        super(capybara);
        this.capybara = capybara;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.capybara.isSleeping();
    }
}
