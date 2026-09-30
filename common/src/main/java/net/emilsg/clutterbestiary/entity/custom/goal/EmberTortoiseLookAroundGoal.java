package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class EmberTortoiseLookAroundGoal extends RandomLookAroundGoal {
    EmberTortoiseEntity emberTortoise;

    public EmberTortoiseLookAroundGoal(EmberTortoiseEntity emberTortoise) {
        super(emberTortoise);
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
