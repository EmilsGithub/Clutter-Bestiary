package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class TamedTemptGoal extends TemptGoal {
    private final TameableEntity entity;

    public TamedTemptGoal(TameableEntity entity, double speed, Predicate<ItemStack> foodPredicate, boolean canBeScared) {
        super(entity, speed, foodPredicate, canBeScared);
        this.entity = entity;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && this.entity.isTamed();
    }
}
