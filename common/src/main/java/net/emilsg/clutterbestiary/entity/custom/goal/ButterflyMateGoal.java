package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.world.entity.ai.goal.BreedGoal;

public class ButterflyMateGoal extends BreedGoal {
    private final ButterflyEntity butterfly;

    public ButterflyMateGoal(ButterflyEntity butterfly, double speed) {
        super(butterfly, speed);
        this.butterfly = butterfly;
    }

    @Override
    public boolean canUse() {
        return !this.butterfly.hasCocoon() && super.canUse();
    }
}
