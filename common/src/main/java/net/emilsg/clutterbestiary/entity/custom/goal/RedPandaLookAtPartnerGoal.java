package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;

public class RedPandaLookAtPartnerGoal extends Goal {
    private final RedPandaEntity goalOwner;
    private RedPandaEntity partner;

    public RedPandaLookAtPartnerGoal(RedPandaEntity goalOwner) {
        this.goalOwner = goalOwner;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        World world = this.goalOwner.getWorld();
        partner = (RedPandaEntity) world.getEntityById(this.goalOwner.getPartnerID());

        if (partner == null) return false;

        return this.goalOwner.getPartnerID() == partner.getId() && partner.getPartnerID() == this.goalOwner.getId();
    }

    @Override
    public void start() {
        this.goalOwner.startState(RedPandaEntityAnimationState.STARTING_Y_POSE);
        this.goalOwner.setIsYPosing(true);
    }

    @Override
    public void stop() {
        this.goalOwner.startState(RedPandaEntityAnimationState.ENDING_Y_POSE);
    }

    @Override
    public void tick() {
        this.goalOwner.setYPoseTicker(this.goalOwner.getYPoseTicker() + 1);

        this.goalOwner.getLookControl().lookAt(partner, 30, 30);

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
