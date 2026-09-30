package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class CapybaraEscapeDangerGoal extends PanicGoal {
    private final CapybaraEntity capybara;

    public CapybaraEscapeDangerGoal(CapybaraEntity capybara, double speed) {
        super(capybara, speed);
        this.capybara = capybara;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.capybara.isSleeping();
    }
}
