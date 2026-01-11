package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.JellyfishEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

public class JellyfishSwimGoal extends Goal {
    private final JellyfishEntity jellyfish;

    public JellyfishSwimGoal(JellyfishEntity jellyfish) {
        this.jellyfish = jellyfish;
    }

    @Override
    public boolean canStart() {
        return this.jellyfish.isInsideWaterOrBubbleColumn();
    }

    @Override
    public void tick() {
        int i = this.jellyfish.getDespawnCounter();
        if (i > 100) {
            this.jellyfish.setSwimmingVector(0.0f, 0.0f, 0.0f);
        } else if (this.jellyfish.getRandom().nextInt(JellyfishSwimGoal.toGoalTicks(50)) == 0 || !this.jellyfish.isTouchingWater() || !this.jellyfish.hasSwimmingVector()) {
            float f = this.jellyfish.getRandom().nextFloat() * ((float) Math.PI * 2);
            float g = MathHelper.cos(f) * 0.2f;
            float h = -0.1f + this.jellyfish.getRandom().nextFloat() * 0.2f;
            float j = MathHelper.sin(f) * 0.2f;
            this.jellyfish.setSwimmingVector(g, h, j);
        }
    }
}
