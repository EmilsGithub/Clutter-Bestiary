package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CrocodileEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CrocodileBaskGoal extends Goal {
    private final CrocodileEntity crocodileEntity;
    private final float chance;
    private long nextBaskTime;

    public CrocodileBaskGoal(CrocodileEntity crocodileEntity, float chance) {
        this.crocodileEntity = crocodileEntity;
        this.chance = chance;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        if (crocodileEntity.isBaby()) return false;
        if (crocodileEntity.getWorld().getTime() < nextBaskTime) return false;
        if (crocodileEntity.isTamed() || !this.canBask() || crocodileEntity.isBasking() || crocodileEntity.isInLove()) return false;
        return crocodileEntity.getRandom().nextFloat() < chance;
    }

    @Override
    public boolean shouldContinue() {
        return this.canBask() && crocodileEntity.isBasking();
    }

    @Override
    public void start() {
        this.crocodileEntity.getNavigation().stop();
        this.crocodileEntity.setMovementSpeed(0.0f);
        this.crocodileEntity.startState(CrocodileEntityAnimationState.OPENING_MOUTH);
    }

    @Override
    public void stop() {
        this.crocodileEntity.startState(CrocodileEntityAnimationState.IDLING);
        this.nextBaskTime = crocodileEntity.getWorld().getTime() + 600 + crocodileEntity.getRandom().nextInt(601);
    }

    private boolean canBask() {
        if (!crocodileEntity.isAlive() || !crocodileEntity.isOnGround()) return false;
        if (crocodileEntity.isTouchingWater() || crocodileEntity.isInsideWaterOrBubbleColumn()) return false;
        if (crocodileEntity.getTarget() != null || crocodileEntity.hurtTime > 0 || crocodileEntity.isFleeing()) return false;
        return !crocodileEntity.getWorld().isNight();
    }
}
