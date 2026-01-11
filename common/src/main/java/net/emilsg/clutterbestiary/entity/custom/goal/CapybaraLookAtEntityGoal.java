package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;

public class CapybaraLookAtEntityGoal extends LookAtEntityGoal {
    private final CapybaraEntity capybara;

    public CapybaraLookAtEntityGoal(CapybaraEntity capybara, Class<? extends LivingEntity> targetType, float range) {
        super(capybara, targetType, range);
        this.capybara = capybara;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.capybara.isSleeping();
    }
}
