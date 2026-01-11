package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;

public class CapybaraEscapeDangerGoal extends EscapeDangerGoal {
    private final CapybaraEntity capybara;

    public CapybaraEscapeDangerGoal(CapybaraEntity capybara, double speed) {
        super(capybara, speed);
        this.capybara = capybara;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.capybara.isSleeping();
    }
}
