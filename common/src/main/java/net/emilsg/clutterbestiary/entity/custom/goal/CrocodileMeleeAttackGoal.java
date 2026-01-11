package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CrocodileEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

public class CrocodileMeleeAttackGoal extends MeleeAttackGoal {
    private final CrocodileEntity crocodileEntity;
    private boolean attackDamagePending;

    public CrocodileMeleeAttackGoal(CrocodileEntity crocodileEntity, double speed) {
        super(crocodileEntity, speed, true);
        this.crocodileEntity = crocodileEntity;
    }

    @Override
    public boolean canStart() {
        if (!this.canAttackTarget(crocodileEntity.getTarget())) {
            this.crocodileEntity.setTarget(null);
            return false;
        }
        return !crocodileEntity.isBasking() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return this.canAttackTarget(crocodileEntity.getTarget()) && !crocodileEntity.isBasking() && super.shouldContinue();
    }

    @Override
    public void stop() {
        super.stop();
        this.attackDamagePending = false;
        if (this.crocodileEntity.isMeleeAttacking()) {
            this.crocodileEntity.startState(this.crocodileEntity.isSitting()
                    ? CrocodileEntityAnimationState.STAYING
                    : CrocodileEntityAnimationState.IDLING);
        }
        if (!this.crocodileEntity.isTamed()) this.crocodileEntity.setTarget(null);
    }

    @Override
    protected void attack(LivingEntity target) {
        CrocodileEntityAnimationState state = this.crocodileEntity.getAnimationController().getState();
        if (state == CrocodileEntityAnimationState.ATTACKING_SNAP_SHUT) {
            if (this.attackDamagePending && this.canDamageTarget(target)) {
                this.crocodileEntity.swingHand(Hand.MAIN_HAND);
                this.crocodileEntity.tryAttack(target);
                this.attackDamagePending = false;
            }
            return;
        }
        if (state == CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH) return;
        this.attackDamagePending = false;
        if (!this.canAttack(target) || !this.crocodileEntity.isFacingTarget(target)) return;

        this.resetCooldown();
        this.attackDamagePending = true;
        this.crocodileEntity.startState(CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH);
    }

    private boolean canAttackTarget(@Nullable LivingEntity target) {
        if (target == null || !target.isAlive() || target.getWorld() != this.crocodileEntity.getWorld()) return false;
        if (target instanceof PlayerEntity player) return this.crocodileEntity.canChasePlayer(player);
        return this.crocodileEntity.canTarget(target);
    }

    private boolean canDamageTarget(LivingEntity target) {
        return this.crocodileEntity.isInAttackRange(target)
                && this.crocodileEntity.getVisibilityCache().canSee(target)
                && this.crocodileEntity.isFacingTarget(target);
    }
}
