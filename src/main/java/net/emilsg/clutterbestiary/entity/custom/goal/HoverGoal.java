package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HoverGoal extends Goal {
    private final ParentAnimalEntity animalEntity;
    private int hoverTime;

    public HoverGoal(ParentAnimalEntity animalEntity) {
        setControls(EnumSet.of(Goal.Control.MOVE));
        this.animalEntity = animalEntity;
    }

    @Override
    public boolean canStart() {
        return animalEntity.getNavigation().isIdle() && animalEntity.getRandom().nextInt(32) == 0;
    }

    @Override
    public boolean shouldContinue() {
        return hoverTime-- > 0;
    }

    @Override
    public void start() {
        hoverTime = animalEntity.getRandom().nextInt(40);
    }

    @Override
    public void tick() {
        if (animalEntity.isTouchingWater()) this.stop();

        animalEntity.getNavigation().stop();
        double hoverY = Math.sin(animalEntity.age * 0.2) * 0.02;
        animalEntity.setVelocity(0, hoverY, 0);
    }
}
