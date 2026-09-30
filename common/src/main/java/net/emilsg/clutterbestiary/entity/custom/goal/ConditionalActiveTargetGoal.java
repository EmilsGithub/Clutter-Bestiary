package net.emilsg.clutterbestiary.entity.custom.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class ConditionalActiveTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final float chance;

    public ConditionalActiveTargetGoal(Mob mob, Class<T> targetClass, boolean checkVisibility, float chance) {
        super(mob, targetClass, checkVisibility);
        this.chance = chance;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mob.getRandom().nextFloat() < this.chance;
    }
}
