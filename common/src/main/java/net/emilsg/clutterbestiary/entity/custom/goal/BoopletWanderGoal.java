package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.BoopletEntity;

public class BoopletWanderGoal extends WanderAroundFarOftenGoal {
    private final BoopletEntity boopletEntity;

    public BoopletWanderGoal(BoopletEntity boopletEntity, float speed) {
        super(boopletEntity, speed);
        this.boopletEntity = boopletEntity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && boopletEntity.getTimeSinceBoop() >= 10;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && boopletEntity.getTimeSinceBoop() >= 10;
    }

    @Override
    public void tick() {
        if (boopletEntity.getTimeSinceBoop() < 10) {
            this.stop();
            return;
        }
        super.tick();
    }
}
