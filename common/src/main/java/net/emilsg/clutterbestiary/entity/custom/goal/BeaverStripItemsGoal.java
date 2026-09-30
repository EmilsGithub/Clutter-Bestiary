package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class BeaverStripItemsGoal extends Goal {
    private final BeaverEntity beaverEntity;

    public BeaverStripItemsGoal(BeaverEntity BeaverEntity) {
        this.beaverEntity = BeaverEntity;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.beaverEntity.isStrippingItems() && this.beaverEntity.onGround() && !this.beaverEntity.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
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
