package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class NonSittingWanderAroundFarGoal extends WaterAvoidingRandomStrollGoal {
    AbstractNetherNewtEntity mob;

    public NonSittingWanderAroundFarGoal(AbstractNetherNewtEntity mob, double speed, float probability) {
        super(mob, speed, probability);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.mob.isOrderedToSit();
    }
}
