package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.RiverTurtleAnimationState;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class RiverTurtleHideGoal extends Goal {
    private final RiverTurtleEntity riverTurtleEntity;

    public RiverTurtleHideGoal(RiverTurtleEntity riverTurtleEntity) {
        this.riverTurtleEntity = riverTurtleEntity;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.riverTurtleEntity.isHiding() && this.riverTurtleEntity.onGround() && !this.riverTurtleEntity.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        return this.riverTurtleEntity.isHiding();
    }

    @Override
    public void start() {
        this.riverTurtleEntity.getNavigation().stop();
        this.riverTurtleEntity.startState(RiverTurtleAnimationState.HIDING);
        this.riverTurtleEntity.setSit(false);
    }

    @Override
    public void stop() {
        this.riverTurtleEntity.startState(RiverTurtleAnimationState.UNHIDING);
    }
}
