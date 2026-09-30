package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;
import java.util.List;

public class RedPandaChallengeOtherGoal extends Goal {
    private final RedPandaEntity goalOwner;
    private final float chancePerTick;
    private RedPandaEntity partner;

    public RedPandaChallengeOtherGoal(RedPandaEntity goalOwner, float chancePerTick) {
        this.goalOwner = goalOwner;
        this.chancePerTick = chancePerTick;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.goalOwner.isBaby()) return false;
        if (this.goalOwner.getSleepState()) return false;
        if (this.goalOwner.getPartnerID() != -1) return false;
        if (this.goalOwner.level().getRandom().nextFloat() > chancePerTick) return false;

        List<RedPandaEntity> candidates = this.goalOwner.level().getEntitiesOfClass(
                RedPandaEntity.class,
                this.goalOwner.getBoundingBox().inflate(3.0D),
                p -> p.canBeChallenged() && p != this.goalOwner
        );

        if (candidates.isEmpty()) {
            return false;
        }

        this.partner = candidates.getFirst();
        return this.partner.getPartnerID() == -1;
    }

    @Override
    public void start() {
        int duration = (this.goalOwner.getRandom().nextInt(3) + 1) * 5 * 20;

        this.goalOwner.setPartnerID(this.partner.getId());
        this.partner.setPartnerID(this.goalOwner.getId());

        this.goalOwner.setYPoseDuration(duration);
        this.partner.setYPoseDuration(duration);


    }
}
