package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;

public class NonSittingWanderAroundFarGoal extends WanderAroundFarGoal {
    AbstractNetherNewtEntity mob;

    public NonSittingWanderAroundFarGoal(AbstractNetherNewtEntity mob, double speed, float probability) {
        super(mob, speed, probability);
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.mob.isSitting();
    }
}
