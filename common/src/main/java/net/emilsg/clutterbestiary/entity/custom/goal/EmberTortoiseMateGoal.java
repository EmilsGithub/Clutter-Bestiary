package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;

public class EmberTortoiseMateGoal extends AnimalMateGoal {
    EmberTortoiseEntity emberTortoise;

    public EmberTortoiseMateGoal(EmberTortoiseEntity emberTortoise, double speed) {
        super(emberTortoise, speed);
        this.emberTortoise = emberTortoise;
    }

    @Override
    public boolean canStart() {
        return !this.emberTortoise.isShielding() && super.canStart();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.emberTortoise.isShielding()) stop();
    }
}
