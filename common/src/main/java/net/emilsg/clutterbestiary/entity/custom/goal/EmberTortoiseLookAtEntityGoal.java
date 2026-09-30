package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.animal.Animal;

public class EmberTortoiseLookAtEntityGoal extends LookAtPlayerGoal {
    Animal animalEntity;

    public EmberTortoiseLookAtEntityGoal(Animal animal, Class<? extends LivingEntity> targetType, float range) {
        super(animal, targetType, range);
        this.animalEntity = animal;
    }

    @Override
    public boolean canUse() {
        if (animalEntity instanceof EmberTortoiseEntity emberTortoise)
            return !emberTortoise.isShielding() && super.canUse();
        else return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (animalEntity instanceof EmberTortoiseEntity emberTortoise && emberTortoise.isShielding()) return false;
        return this.lookAt != null && super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (animalEntity instanceof EmberTortoiseEntity emberTortoise) {
            if (emberTortoise.isShielding()) stop();
        }
    }
}
