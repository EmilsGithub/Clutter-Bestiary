package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class BaskGoal extends Goal {
    private final RiverTurtleEntity riverTurtleEntity;
    private final float chance;

    public BaskGoal(RiverTurtleEntity riverTurtleEntity, Float chance) {
        this.riverTurtleEntity = riverTurtleEntity;
        this.chance = chance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!riverTurtleEntity.onGround()) return false;
        if (riverTurtleEntity.isInWater() || riverTurtleEntity.isInWater()) return false;
        if (riverTurtleEntity.isHiding()) return false;
        if (riverTurtleEntity.level().isDarkOutside()) return false;
        return riverTurtleEntity.getRandom().nextFloat() < chance;
    }

    @Override
    public boolean canContinueToUse() {
        if (riverTurtleEntity.isInWater() || riverTurtleEntity.isInWater()) return false;
        if (riverTurtleEntity.isHiding()) return false;
        return riverTurtleEntity.onGround() && riverTurtleEntity.getBaskingDuration() > 0;
    }

    @Override
    public void start() {
        this.riverTurtleEntity.setBaskingDuration((this.riverTurtleEntity.getRandom().nextInt(3) + 1) * 200);
        this.riverTurtleEntity.setSit(true);
    }

    @Override
    public void stop() {
        this.riverTurtleEntity.setSit(false);
    }
}
