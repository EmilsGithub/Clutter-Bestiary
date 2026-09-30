package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.JellyfishEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

public class JellyfishSwimGoal extends Goal {
    private final JellyfishEntity jellyfish;

    public JellyfishSwimGoal(JellyfishEntity jellyfish) {
        this.jellyfish = jellyfish;
    }

    @Override
    public boolean canUse() {
        return this.jellyfish.isInWater();
    }

    @Override
    public void tick() {
        int i = this.jellyfish.getNoActionTime();
        if (i > 100) {
            this.jellyfish.setSwimmingVector(0.0f, 0.0f, 0.0f);
        } else if (this.jellyfish.getRandom().nextInt(JellyfishSwimGoal.reducedTickDelay(50)) == 0 || !this.jellyfish.isInWater() || !this.jellyfish.hasSwimmingVector()) {
            float f = this.jellyfish.getRandom().nextFloat() * ((float) Math.PI * 2);
            float g = Mth.cos(f) * 0.2f;
            float h = -0.1f + this.jellyfish.getRandom().nextFloat() * 0.2f;
            float j = Mth.sin(f) * 0.2f;
            this.jellyfish.setSwimmingVector(g, h, j);
        }
    }
}
