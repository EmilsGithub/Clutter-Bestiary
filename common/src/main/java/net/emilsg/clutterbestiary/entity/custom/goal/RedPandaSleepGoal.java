package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RedPandaSleepGoal extends Goal {
    private final RedPandaEntity redPandaEntity;

    public RedPandaSleepGoal(RedPandaEntity redPandaEntity) {
        this.redPandaEntity = redPandaEntity;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.redPandaEntity.isInsideWaterOrBubbleColumn() || !this.redPandaEntity.isOnGround()) {
            return false;
        }

        return this.redPandaEntity.isSleeping();
    }

    @Override
    public boolean shouldContinue() {
        return this.redPandaEntity.isSleeping();
    }

    @Override
    public void start() {
        this.redPandaEntity.getNavigation().stop();
        this.redPandaEntity.startState(RedPandaEntityAnimationState.LAYING_DOWN);
    }

    @Override
    public void stop() {
        this.redPandaEntity.setSleepTracker(0);
        this.redPandaEntity.setSleepTimer(0);
        this.redPandaEntity.setIsSleeping(false);
        this.redPandaEntity.startState(RedPandaEntityAnimationState.STANDING_UP);
    }
}
