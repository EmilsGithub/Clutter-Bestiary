package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CrocodileStayGoal extends Goal {
    private final CrocodileEntity crocodile;

    public CrocodileStayGoal(CrocodileEntity crocodile) {
        this.crocodile = crocodile;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.crocodile.isTamed() && this.crocodile.isSitting();
    }

    @Override
    public boolean shouldContinue() {
        return this.crocodile.isTamed() && this.crocodile.isSitting();
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
