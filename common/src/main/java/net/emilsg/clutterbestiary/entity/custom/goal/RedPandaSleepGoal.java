package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class RedPandaSleepGoal extends Goal {
    private final RedPandaEntity redPandaEntity;

    public RedPandaSleepGoal(RedPandaEntity redPandaEntity) {
        this.redPandaEntity = redPandaEntity;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.redPandaEntity.isInWater() || !this.redPandaEntity.onGround()) {
            return false;
        }

        return this.redPandaEntity.isSleeping();
    }

    @Override
    public boolean canContinueToUse() {
        return this.redPandaEntity.isSleeping();
    }

    @Override
    public void start() {
        this.redPandaEntity.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.redPandaEntity.setSleepTracker(0);
        this.redPandaEntity.setSleepTimer(0);
        this.redPandaEntity.setIsSleeping(false);
    }
}
