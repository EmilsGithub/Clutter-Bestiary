package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class RedPandaFollowOwnerGoal extends FollowOwnerGoal {
    private final RedPandaEntity redPandaEntity;

    public RedPandaFollowOwnerGoal(RedPandaEntity redPandaEntity, double speed, float minDistance, float maxDistance) {
        super(redPandaEntity, speed, minDistance, maxDistance);
        this.redPandaEntity = redPandaEntity;
    }

    @Override
    public boolean canStart() {
        return !this.redPandaEntity.isStaying() && super.canStart();
    }
}
