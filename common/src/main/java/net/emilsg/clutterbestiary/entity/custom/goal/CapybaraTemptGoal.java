package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class CapybaraTemptGoal extends TemptGoal {
    private final CapybaraEntity capybara;

    public CapybaraTemptGoal(CapybaraEntity capybara, double speed, Ingredient food, boolean canBeScared) {
        super(capybara, speed, food, canBeScared);
        this.capybara = capybara;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.capybara.isSleeping();
    }
}
