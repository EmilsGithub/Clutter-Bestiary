package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class CoatiUnburrowingGoal extends Goal {
    private final CoatiEntity coatiEntity;
    private int unBurrowingTicker;

    public CoatiUnburrowingGoal(CoatiEntity coatiEntity) {
        this.coatiEntity = coatiEntity;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.coatiEntity.isUnBurrowing();
    }

    @Override
    public void start() {
        this.coatiEntity.getNavigation().stop();
        this.unBurrowingTicker = this.adjustedTickDelay(CoatiEntity.UNBURROW_DURATION_TICKS);
        this.coatiEntity.startState(CoatiEntityAnimationState.UNBURROWING);
    }

    @Override
    public void stop() {
        this.coatiEntity.setUnBurrowing(false);
        if (this.coatiEntity.getAnimationController().getState() == CoatiEntityAnimationState.UNBURROWING) {
            this.coatiEntity.startState(CoatiEntityAnimationState.IDLING);
        }
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
