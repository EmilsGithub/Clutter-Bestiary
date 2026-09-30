package net.emilsg.clutterbestiary.entity.custom.goal;
import net.minecraft.world.item.component.SwingAnimation;

import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.pathfinder.Path;

public class EmberTortoiseMeleeGoal extends MeleeAttackGoal {
    private final EmberTortoiseEntity entity;
    private final double speed;
    private int attackDelay = 20;
    private Path path;
    private long lastUpdateTime;
    private int ticksUntilNextAttack = 20;
    private boolean shouldCountTillNextAttack = false;

    public EmberTortoiseMeleeGoal(PathfinderMob mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        entity = ((EmberTortoiseEntity) mob);
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        return !entity.isShielding() && startAttack();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speed);
        this.mob.setAggressive(true);
        attackDelay = 20;
        ticksUntilNextAttack = 20;
        shouldCountTillNextAttack = false;
    }

    @Override
    public void stop() {
        entity.setAggressive(false);
        shouldCountTillNextAttack = false;
        super.stop();
    }

    @Override
    public void tick() {
        if (this.entity.isShielding()) {
            resetAttackCooldown();
            entity.setAggressive(false);
            this.stop();
            return;
        }
        super.tick();

        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.entity.isShielding()) {
            entity.getNavigation().stop();
            resetAttackCooldown();
            entity.setAggressive(false);
            return;
        }
        if (isEnemyWithinAttackDistance(target)) {
            shouldCountTillNextAttack = true;

            if (isTimeToStartAttackAnimation()) {
                entity.setAggressive(true);
            }

            if (isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
                performAttack(target);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAggressive(false);
            entity.attackAnimationTimeout = 0;
        }
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false);
        this.mob.doHurtTarget(getServerLevel(this.mob), pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy) {
        double width = this.mob.getBbWidth() * 2.0;
        return this.mob.distanceToSqr(pEnemy) < width * width + pEnemy.getBbWidth();
    }

    private boolean startAttack() {
        long l = this.mob.level().getGameTime();
        if (l - this.lastUpdateTime < 20L) {
            return false;
        }
        this.lastUpdateTime = l;
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        }
        if (!livingEntity.isAlive()) {
            return false;
        }
        this.path = this.mob.getNavigation().createPath(livingEntity, 1);
        if (this.path != null) {
            return true;
        }
        return 3 >= this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
    }
}
