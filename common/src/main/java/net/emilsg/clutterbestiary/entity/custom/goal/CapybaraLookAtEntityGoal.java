package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class CapybaraLookAtEntityGoal extends LookAtPlayerGoal {
    private final CapybaraEntity capybara;

    public CapybaraLookAtEntityGoal(CapybaraEntity capybara, Class<? extends LivingEntity> targetType, float range) {
        super(capybara, targetType, range);
        this.capybara = capybara;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.capybara.isSleeping();
    }
}
