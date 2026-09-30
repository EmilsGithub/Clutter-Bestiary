package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class HoverGoal extends Goal {
    private final ParentAnimalEntity animalEntity;
    private int hoverTime;

    public HoverGoal(ParentAnimalEntity animalEntity) {
        setFlags(EnumSet.of(Flag.MOVE));
        this.animalEntity = animalEntity;
    }

    @Override
    public boolean canUse() {
        return animalEntity.getNavigation().isDone() && animalEntity.getRandom().nextInt(24) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return hoverTime-- > 0;
    }

    @Override
    public void start() {
        hoverTime = animalEntity.getRandom().nextInt(40);
    }

    @Override
    public void tick() {
        if (animalEntity.isInWater()) this.stop();

        animalEntity.getNavigation().stop();
        double hoverY = Math.sin(animalEntity.tickCount * 0.2) * 0.02;
        animalEntity.setDeltaMovement(0, hoverY, 0);
    }
}
