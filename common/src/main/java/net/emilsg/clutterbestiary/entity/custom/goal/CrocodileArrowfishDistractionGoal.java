package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ArrowfishProjectileEntity;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class CrocodileArrowfishDistractionGoal extends Goal {
    private static final double SEARCH_RANGE = 16.0;
    private static final double DISTRACTION_DISTANCE = 2.0;
    private static final double APPROACH_SPEED = 1.0;
    private static final int DISTRACTION_DURATION_TICKS = 200;
    private static final int FAILED_PATH_TIMEOUT_TICKS = 200;
    private static final int FED_COOLDOWN_TICKS = 200;

    private final CrocodileEntity crocodileEntity;
    @Nullable
    private ArrowfishProjectileEntity arrowfishProjectile;
    private int distractionTicks;
    private int pursuitTicks;
    private long nextSearchTime;
    private boolean distractionStarted;

    public CrocodileArrowfishDistractionGoal(CrocodileEntity crocodileEntity) {
        this.crocodileEntity = crocodileEntity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.crocodileEntity.isAlive() || this.crocodileEntity.isTame() || this.crocodileEntity.isBaby()) return false;
        if (this.crocodileEntity.level().getGameTime() < this.nextSearchTime) return false;

        List<ArrowfishProjectileEntity> projectiles = this.crocodileEntity.level().getEntitiesOfClass(
                ArrowfishProjectileEntity.class,
                this.crocodileEntity.getBoundingBox().inflate(SEARCH_RANGE, SEARCH_RANGE / 2.0, SEARCH_RANGE),
                ArrowfishProjectileEntity::isStuckInGround
        );
        this.arrowfishProjectile = projectiles.stream()
                .min(Comparator.comparingDouble(this.crocodileEntity::distanceToSqr))
                .orElse(null);
        return this.arrowfishProjectile != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.crocodileEntity.isAlive() || this.crocodileEntity.isTame() || !this.isProjectileValid()) return false;
        if (this.distractionStarted) {
            return this.crocodileEntity.isDistracted() && this.distractionTicks < DISTRACTION_DURATION_TICKS;
        }
        return this.pursuitTicks < FAILED_PATH_TIMEOUT_TICKS;
    }

    @Override
    public void start() {
        this.distractionStarted = false;
        this.distractionTicks = 0;
        this.pursuitTicks = 0;
        this.crocodileEntity.setTarget(null);
        this.crocodileEntity.setDistracted(false);
        if (this.arrowfishProjectile != null) {
            this.crocodileEntity.getNavigation().moveTo(this.arrowfishProjectile, APPROACH_SPEED);
        }
    }

    @Override
    public void stop() {
        boolean fed = this.distractionStarted && !this.crocodileEntity.isDistracted();
        this.crocodileEntity.setDistracted(false);
        this.crocodileEntity.getNavigation().stop();
        this.nextSearchTime = this.crocodileEntity.level().getGameTime() + (fed ? FED_COOLDOWN_TICKS : 20);
        this.arrowfishProjectile = null;
        this.distractionStarted = false;
        this.distractionTicks = 0;
        this.pursuitTicks = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.canContinueToUse()) return;

        this.crocodileEntity.getLookControl().setLookAt(this.arrowfishProjectile, 20.0f, this.crocodileEntity.getMaxHeadXRot());
        if (!this.distractionStarted) {
            this.pursuitTicks++;
            if (this.crocodileEntity.distanceToSqr(this.arrowfishProjectile) > DISTRACTION_DISTANCE * DISTRACTION_DISTANCE) {
                if (this.crocodileEntity.getNavigation().isDone()) {
                    this.crocodileEntity.getNavigation().moveTo(this.arrowfishProjectile, APPROACH_SPEED);
                }
                return;
            }

            this.crocodileEntity.getNavigation().stop();
            this.distractionStarted = true;
            this.crocodileEntity.setDistracted(true);
        }

        this.distractionTicks++;
        if (this.distractionTicks >= DISTRACTION_DURATION_TICKS) {
            this.crocodileEntity.playSound(SoundEvents.GENERIC_EAT.value(), 1.0f, 0.8f + this.crocodileEntity.getRandom().nextFloat() * 0.4f);
            this.arrowfishProjectile.discard();
        }
    }

    private boolean isProjectileValid() {
        return this.arrowfishProjectile != null
                && this.arrowfishProjectile.isAlive()
                && this.arrowfishProjectile.isStuckInGround();
    }
}
