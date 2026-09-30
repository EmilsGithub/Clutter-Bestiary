package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.MossbloomEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import java.util.EnumSet;

public class MossbloomDropHornsGoal extends Goal {
    private final MossbloomEntity mossbloom;
    private int shakeTicks;

    public MossbloomDropHornsGoal(MossbloomEntity mossbloom) {
        this.mossbloom = mossbloom;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mossbloom.getHasHorns() && this.mossbloom.getHornDropTimer() > MossbloomEntity.SHOULD_DROP_HORNS_VALUE;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mossbloom.getHasHorns() && this.mossbloom.getHornDropTimer() > MossbloomEntity.SHOULD_DROP_HORNS_VALUE;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.shakeTicks = 0;
        this.mossbloom.setIsShaking(true);
        this.mossbloom.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.shakeTicks = 0;
        this.mossbloom.setIsShaking(false);
    }

    @Override
    public void tick() {
        this.shakeTicks++;

        if (this.shakeTicks >= 60) {
            this.mossbloom.setHornDropTimer(0);
            this.mossbloom.setHasHorns(false);
            this.mossbloom.spawnAtLocation(getServerLevel(this.mossbloom), new ItemStack(ModItems.MOSSBLOOM_ANTLERS.get(), 2));
            this.mossbloom.setIsShaking(false);
        }
    }
}
