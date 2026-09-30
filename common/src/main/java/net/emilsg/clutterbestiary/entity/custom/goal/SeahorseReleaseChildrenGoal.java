package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.minecraft.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.ArrayList;
import java.util.List;

public class SeahorseReleaseChildrenGoal extends Goal {
    private final SeahorseEntity seahorse;

    public SeahorseReleaseChildrenGoal(SeahorseEntity seahorse) {
        this.seahorse = seahorse;
    }

    @Override
    public boolean canUse() {
        return this.areChildrenReady();
    }

    @Override
    public boolean canContinueToUse() {
        return this.areChildrenReady();
    }

    @Override
    public void start() {
        if (seahorse.level() instanceof ServerLevel serverWorld) {
            RandomSource random = serverWorld.getRandom();
            List<SeahorseEntity> children = new ArrayList<>();
            int childCount = random.nextInt(seahorse.getMaxChildren()) + 1;

            for (int i = 0; i < childCount; i++) {
                SeahorseEntity child = seahorse.createChild(serverWorld, seahorse);
                if (child == null) return;
                child.setBaby(true);
                child.setPos(seahorse.position());
                child.setVariant(random.nextBoolean() ? seahorse.getVariant() : Util.getRandom(SeahorseVariant.values(), serverWorld.getRandom()));
                children.add(child);
            }

            for (SeahorseEntity spawnedChild : children) {
                serverWorld.addFreshEntity(spawnedChild);
            }

            seahorse.setHasChildren(false);
            seahorse.setHasChildrenTimer(0.0f);
        }
    }

    private boolean areChildrenReady() {
        return seahorse.getHasChildrenTimer() >= 0.35;
    }
}
