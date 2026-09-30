package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import java.util.EnumSet;

public class RedPandaLookAtPartnerGoal extends Goal {
    private final RedPandaEntity goalOwner;
    private RedPandaEntity partner;

    public RedPandaLookAtPartnerGoal(RedPandaEntity goalOwner) {
        this.goalOwner = goalOwner;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.goalOwner.getPartnerID() == -1) return false;
        Level world = this.goalOwner.level();
        if (!(world.getEntity(this.goalOwner.getPartnerID()) instanceof RedPandaEntity redPandaEntity)
                || !redPandaEntity.isAlive() || redPandaEntity.getPartnerID() != this.goalOwner.getId()) {
            this.goalOwner.setPartnerID(-1);
            return false;
        }
        this.partner = redPandaEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.partner != null && this.partner.isAlive()
                && this.goalOwner.getPartnerID() == this.partner.getId()
                && this.partner.getPartnerID() == this.goalOwner.getId();
    }

    @Override
    public void start() {
        this.goalOwner.startState(RedPandaEntityAnimationState.STARTING_Y_POSE);
        this.goalOwner.setIsYPosing(true);
    }

    @Override
    public void stop() {
        this.goalOwner.setIsYPosing(false);
        this.goalOwner.startState(RedPandaEntityAnimationState.ENDING_Y_POSE);
        if (this.partner != null && this.partner.getPartnerID() == this.goalOwner.getId()) {
            this.partner.setPartnerID(-1);
            this.partner.setYPoseDuration(0);
            this.partner.setYPoseTicker(0);
        }
        this.goalOwner.setPartnerID(-1);
        this.goalOwner.setYPoseDuration(0);
        this.goalOwner.setYPoseTicker(0);
        this.partner = null;
    }

    @Override
    public void tick() {
        this.goalOwner.setYPoseTicker(this.goalOwner.getYPoseTicker() + 1);

        this.goalOwner.getLookControl().setLookAt(partner, 30, 30);

        if (this.goalOwner.getYPoseTicker() >= this.goalOwner.getYPoseDuration()) {
            this.goalOwner.setPartnerID(-1);
            this.goalOwner.setYPoseDuration(0);
            this.goalOwner.setYPoseTicker(0);
            this.partner.setPartnerID(-1);
            this.partner.setYPoseDuration(0);
            this.partner.setYPoseTicker(0);
        }
    }
}
