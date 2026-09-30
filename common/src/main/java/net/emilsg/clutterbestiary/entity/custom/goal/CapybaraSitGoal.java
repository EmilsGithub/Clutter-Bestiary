package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CapybaraEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import java.util.EnumSet;

public class CapybaraSitGoal extends SitWhenOrderedToGoal {
    private final CapybaraEntity capybara;

    public CapybaraSitGoal(CapybaraEntity capybara) {
        super(capybara);
        this.capybara = capybara;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        super.canUse();
        return capybara.isTame() && capybara.isForceSleeping();
    }

    @Override
    public boolean canContinueToUse() {
        return capybara.isTame() && capybara.isForceSleeping();
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
