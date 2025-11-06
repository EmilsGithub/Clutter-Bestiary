package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RiverTurtleHideGoal extends Goal {
    private final RiverTurtleEntity riverTurtleEntity;

    public RiverTurtleHideGoal(RiverTurtleEntity riverTurtleEntity) {
        this.riverTurtleEntity = riverTurtleEntity;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean shouldContinue() {
        return this.riverTurtleEntity.isHiding();
    }

    @Override
    public boolean canStart() {
        return this.riverTurtleEntity.isHiding() && this.riverTurtleEntity.isOnGround() && !this.riverTurtleEntity.isInsideWaterOrBubbleColumn();
    }

    @Override
    public void start() {
        this.riverTurtleEntity.getNavigation().stop();
        this.riverTurtleEntity.startState(RiverTurtleEntity.RiverTurtleAnimationState.HIDING);
    }

    @Override
    public void stop() {
        this.riverTurtleEntity.startState(RiverTurtleEntity.RiverTurtleAnimationState.UNHIDING);
    }
}
