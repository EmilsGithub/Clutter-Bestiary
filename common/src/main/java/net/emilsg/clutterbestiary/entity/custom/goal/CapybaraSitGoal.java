package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CapybaraEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.ai.goal.SitGoal;

import java.util.EnumSet;

public class CapybaraSitGoal extends SitGoal {
    private final CapybaraEntity capybara;

    public CapybaraSitGoal(CapybaraEntity capybara) {
        super(capybara);
        this.capybara = capybara;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        super.canStart();
        return capybara.isTamed() && capybara.isForceSleeping();
    }

    @Override
    public boolean shouldContinue() {
        return capybara.isTamed() && capybara.isForceSleeping();
    }

    @Override
    public void start() {
        capybara.getNavigation().stop();
        capybara.setIsSleeping(true);
        capybara.startState(CapybaraEntityAnimationState.LAYING_DOWN);
    }

    @Override
    public void stop() {
        capybara.setIsForceSleeping(false);
        capybara.setInSittingPose(false);
        capybara.setIsSleeping(false);
        capybara.startState(CapybaraEntityAnimationState.STANDING_UP);
    }
}
