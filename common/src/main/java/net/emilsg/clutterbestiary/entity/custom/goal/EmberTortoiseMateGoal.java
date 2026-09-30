package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.world.entity.ai.goal.BreedGoal;

public class EmberTortoiseMateGoal extends BreedGoal {
    EmberTortoiseEntity emberTortoise;

    public EmberTortoiseMateGoal(EmberTortoiseEntity emberTortoise, double speed) {
        super(emberTortoise, speed);
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
