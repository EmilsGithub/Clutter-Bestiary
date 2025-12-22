package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BeaverStripItemsGoal extends Goal {
    private final BeaverEntity beaverEntity;

    public BeaverStripItemsGoal(BeaverEntity BeaverEntity) {
        this.beaverEntity = BeaverEntity;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.beaverEntity.isStrippingItems() && this.beaverEntity.isOnGround() && !this.beaverEntity.isInsideWaterOrBubbleColumn();
    }

    @Override
    public boolean shouldContinue() {
        return this.beaverEntity.isStrippingItems();
    }

    @Override
    public void start() {
        this.beaverEntity.getNavigation().stop();
        this.beaverEntity.startState(BeaverEntity.BeaverEntityAnimationState.STRIPPING_ITEMS);
    }

    @Override
    public void stop() {

    }
}
