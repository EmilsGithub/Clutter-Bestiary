package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class CrocodileStayGoal extends Goal {
    private final CrocodileEntity crocodile;

    public CrocodileStayGoal(CrocodileEntity crocodile) {
        this.crocodile = crocodile;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.crocodile.isTame() && this.crocodile.isOrderedToSit();
    }

    @Override
    public boolean canContinueToUse() {
        return this.crocodile.isTame() && this.crocodile.isOrderedToSit();
    }

    @Override
    public void start() {
        this.crocodile.getNavigation().stop();
        this.crocodile.setInSittingPose(true);
    }

    @Override
    public void stop() {
        this.crocodile.setInSittingPose(false);
    }
}
