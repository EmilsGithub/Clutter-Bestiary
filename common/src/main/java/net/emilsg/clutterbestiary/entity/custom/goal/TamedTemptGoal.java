package net.emilsg.clutterbestiary.entity.custom.goal;

import java.util.function.Predicate;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.ItemStack;

public class TamedTemptGoal extends TemptGoal {
    private final TamableAnimal entity;

    public TamedTemptGoal(TamableAnimal entity, double speed, Predicate<ItemStack> foodPredicate, boolean canBeScared) {
        super(entity, speed, foodPredicate, canBeScared);
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.entity.isTame();
    }
}
