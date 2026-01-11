package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.recipe.Ingredient;

public class CapybaraTemptGoal extends TemptGoal {
    private final CapybaraEntity capybara;

    public CapybaraTemptGoal(CapybaraEntity capybara, double speed, Ingredient food, boolean canBeScared) {
        super(capybara, speed, food, canBeScared);
        this.capybara = capybara;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.capybara.isSleeping();
    }
}
