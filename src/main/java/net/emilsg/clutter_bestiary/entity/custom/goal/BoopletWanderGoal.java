package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.BoopletEntity;

public class BoopletWanderGoal extends WanderAroundFarOftenGoal {
    private final BoopletEntity boopletEntity;

    public BoopletWanderGoal(BoopletEntity boopletEntity, float speed) {
        super(boopletEntity, speed);
        this.boopletEntity = boopletEntity;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && boopletEntity.getTimeSinceBoop() >= 10;
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && boopletEntity.getTimeSinceBoop() >= 10;
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
