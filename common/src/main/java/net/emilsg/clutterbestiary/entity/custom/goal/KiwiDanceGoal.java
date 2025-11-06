package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class KiwiDanceGoal extends Goal {
    private final KiwiBirdEntity kiwiBirdEntity;

    public KiwiDanceGoal(KiwiBirdEntity kiwiBirdEntity) {
        this.kiwiBirdEntity = kiwiBirdEntity;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean shouldContinue() {
        return this.kiwiBirdEntity.isDancing();
    }

    @Override
    public boolean canStart() {
        return this.kiwiBirdEntity.isDancing() || this.kiwiBirdEntity.isSongPlaying();
    }

    @Override
    public void start() {
        this.kiwiBirdEntity.getNavigation().stop();
    }
}
