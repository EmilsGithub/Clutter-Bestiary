package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class CapybaraLookAroundGoal extends RandomLookAroundGoal {
    private final CapybaraEntity capybara;

    public CapybaraLookAroundGoal(CapybaraEntity capybara) {
        super(capybara);
        this.capybara = capybara;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.capybara.isSleeping();
    }
}
