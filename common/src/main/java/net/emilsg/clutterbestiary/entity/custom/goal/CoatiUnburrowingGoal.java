package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CoatiUnburrowingGoal extends Goal {
    private final CoatiEntity coatiEntity;
    private int unBurrowingTicker;

    public CoatiUnburrowingGoal(CoatiEntity coatiEntity) {
        this.coatiEntity = coatiEntity;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.coatiEntity.isUnBurrowing();
    }

    @Override
    public void start() {
        this.coatiEntity.getNavigation().stop();
        this.unBurrowingTicker = 90;
        this.coatiEntity.startState(CoatiEntityAnimationState.UNBURROWING);
    }

    @Override
    public void tick() {
        this.unBurrowingTicker--;
        if (this.unBurrowingTicker <= 0) {
            this.coatiEntity.setUnBurrowing(false);
            this.coatiEntity.startState(CoatiEntityAnimationState.IDLING);
        }
    }
}
