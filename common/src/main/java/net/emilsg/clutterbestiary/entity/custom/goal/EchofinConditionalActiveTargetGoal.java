package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EchofinEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

@SuppressWarnings("unchecked")
public class EchofinConditionalActiveTargetGoal extends NearestAttackableTargetGoal {
    EchofinEntity echofinEntity;

    public EchofinConditionalActiveTargetGoal(EchofinEntity echofinEntity, Class targetClass, boolean checkVisibility) {
        super(echofinEntity, targetClass, checkVisibility);
        this.echofinEntity = echofinEntity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && echofinEntity.hasAbility();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && echofinEntity.hasAbility();
    }
}
