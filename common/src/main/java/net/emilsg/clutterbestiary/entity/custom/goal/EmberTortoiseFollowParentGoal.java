package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;

public class EmberTortoiseFollowParentGoal extends FollowParentGoal {
    Animal animalEntity;

    public EmberTortoiseFollowParentGoal(Animal animal, double speed) {
        super(animal, speed);
        this.animalEntity = animal;
    }

    @Override
    public boolean canUse() {
        if (animalEntity instanceof EmberTortoiseEntity emberTortoise)
            return !emberTortoise.isShielding() && super.canUse();
        else return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (animalEntity instanceof EmberTortoiseEntity emberTortoise) {
            if (emberTortoise.isShielding()) stop();
        }
    }
}
