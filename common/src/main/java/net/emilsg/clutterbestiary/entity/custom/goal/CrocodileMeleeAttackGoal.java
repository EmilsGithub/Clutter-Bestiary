package net.emilsg.clutterbestiary.entity.custom.goal;
import net.minecraft.world.item.component.SwingAnimation;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CrocodileEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class CrocodileMeleeAttackGoal extends MeleeAttackGoal {
    private final CrocodileEntity crocodileEntity;
    private boolean attackDamagePending;

    public CrocodileMeleeAttackGoal(CrocodileEntity crocodileEntity, double speed) {
        super(crocodileEntity, speed, true);
        this.crocodileEntity = crocodileEntity;
    }

    @Override
    public boolean canUse() {
        if (!this.canAttackTarget(crocodileEntity.getTarget())) {
            this.crocodileEntity.setTarget(null);
            return false;
        }
        return !crocodileEntity.isBasking() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canAttackTarget(crocodileEntity.getTarget()) && !crocodileEntity.isBasking() && super.canContinueToUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.attackDamagePending = false;
        if (this.crocodileEntity.isMeleeAttacking()) {
            this.crocodileEntity.startState(this.crocodileEntity.isOrderedToSit()
                    ? CrocodileEntityAnimationState.STAYING
                    : CrocodileEntityAnimationState.IDLING);
        }
        if (!this.crocodileEntity.isTame()) this.crocodileEntity.setTarget(null);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        CrocodileEntityAnimationState state = this.crocodileEntity.getAnimationController().getState();
        if (state == CrocodileEntityAnimationState.ATTACKING_SNAP_SHUT) {
            if (this.attackDamagePending && this.canDamageTarget(target)) {
                this.crocodileEntity.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false);
                this.crocodileEntity.doHurtTarget(getServerLevel(this.crocodileEntity), target);
                this.attackDamagePending = false;
            }
            return;
        }
        if (state == CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH) return;
        this.attackDamagePending = false;
        if (!this.canPerformAttack(target) || !this.crocodileEntity.isFacingTarget(target)) return;

        this.resetAttackCooldown();
        this.attackDamagePending = true;
        this.crocodileEntity.startState(CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH);
    }

    private boolean canAttackTarget(@Nullable LivingEntity target) {
        if (target == null || !target.isAlive() || target.level() != this.crocodileEntity.level()) return false;
        if (target instanceof Player player) return this.crocodileEntity.canChasePlayer(player);
        return this.crocodileEntity.canAttack(target);
    }

    private boolean canDamageTarget(LivingEntity target) {
        return this.crocodileEntity.isWithinMeleeAttackRange(target)
                && this.crocodileEntity.getSensing().hasLineOfSight(target)
                && this.crocodileEntity.isFacingTarget(target);
    }
}
