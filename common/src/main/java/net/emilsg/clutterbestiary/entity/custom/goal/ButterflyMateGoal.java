package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;

public class ButterflyMateGoal extends AnimalMateGoal {
    private final ButterflyEntity butterfly;

    public ButterflyMateGoal(ButterflyEntity butterfly, double speed) {
        super(butterfly, speed);
        this.butterfly = butterfly;
    }

    @Override
    public boolean canStart() {
        return !this.butterfly.hasCocoon() && super.canStart();
    }
}
