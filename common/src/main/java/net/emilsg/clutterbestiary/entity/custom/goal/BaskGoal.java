package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RiverTurtleAnimationState;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BaskGoal extends Goal {
    private final RiverTurtleEntity riverTurtleEntity;
    private final float chance;

    public BaskGoal(RiverTurtleEntity riverTurtleEntity, Float chance) {
        this.riverTurtleEntity = riverTurtleEntity;
        this.chance = chance;
        this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        if (!riverTurtleEntity.isOnGround()) return false;
        if (riverTurtleEntity.isTouchingWater() || riverTurtleEntity.isInsideWaterOrBubbleColumn()) return false;
        if (riverTurtleEntity.isHiding()) return false;
        if (riverTurtleEntity.getWorld().isNight()) return false;
        return riverTurtleEntity.getRandom().nextFloat() < chance;
    }

    @Override
    public void start() {
        this.riverTurtleEntity.setBaskingDuration((this.riverTurtleEntity.getRandom().nextInt(3) + 1) * 200);
        this.riverTurtleEntity.setSit(true);
        this.riverTurtleEntity.startState(RiverTurtleAnimationState.SIT_START);
    }

    @Override
    public boolean shouldContinue() {
        if (riverTurtleEntity.isTouchingWater() || riverTurtleEntity.isInsideWaterOrBubbleColumn()) return false;
        if (riverTurtleEntity.isHiding()) return false;
        return riverTurtleEntity.isOnGround() && riverTurtleEntity.getBaskingDuration() > 0;
    }

    @Override
    public void stop() {
        this.riverTurtleEntity.startState(RiverTurtleAnimationState.SIT_END);
        this.riverTurtleEntity.setSit(false);
    }
}
