package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class EmberTortoiseTemptGoal extends TemptGoal {
    EmberTortoiseEntity emberTortoise;

    public EmberTortoiseTemptGoal(EmberTortoiseEntity emberTortoise, double speed, Ingredient food, boolean canBeScared) {
        super(emberTortoise, speed, food, canBeScared);
        this.emberTortoise = emberTortoise;
    }

    @Override
    public boolean canUse() {
        return !this.emberTortoise.isShielding() && super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.emberTortoise.isShielding()) stop();
    }
}
