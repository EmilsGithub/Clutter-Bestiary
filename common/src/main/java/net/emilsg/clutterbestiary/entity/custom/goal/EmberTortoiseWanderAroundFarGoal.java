package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class EmberTortoiseWanderAroundFarGoal extends WaterAvoidingRandomStrollGoal {
    EmberTortoiseEntity emberTortoise;

    public EmberTortoiseWanderAroundFarGoal(EmberTortoiseEntity emberTortoise, double speed, float probability) {
        super(emberTortoise, speed, probability);
        this.emberTortoise = emberTortoise;
    }

    @Override
    public boolean canUse() {
        return !this.emberTortoise.isShielding() && super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.emberTortoise.isShielding()) stop();
    }
}
