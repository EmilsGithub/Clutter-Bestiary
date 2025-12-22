package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;

public class ConditionalActiveTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final float chance;

    public ConditionalActiveTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility, float chance) {
        super(mob, targetClass, checkVisibility);
        this.chance = chance;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && mob.getRandom().nextFloat() < this.chance;
    }
}
