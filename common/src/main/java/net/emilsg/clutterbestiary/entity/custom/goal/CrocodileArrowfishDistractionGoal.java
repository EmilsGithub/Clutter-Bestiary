package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ArrowfishProjectileEntity;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.sound.SoundEvents;
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
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!this.crocodileEntity.isAlive() || this.crocodileEntity.isTamed() || this.crocodileEntity.isBaby()) return false;
        if (this.crocodileEntity.getWorld().getTime() < this.nextSearchTime) return false;

        List<ArrowfishProjectileEntity> projectiles = this.crocodileEntity.getWorld().getEntitiesByClass(
                ArrowfishProjectileEntity.class,
                this.crocodileEntity.getBoundingBox().expand(SEARCH_RANGE, SEARCH_RANGE / 2.0, SEARCH_RANGE),
                ArrowfishProjectileEntity::isStuckInGround
        );
        this.arrowfishProjectile = projectiles.stream()
                .min(Comparator.comparingDouble(this.crocodileEntity::squaredDistanceTo))
                .orElse(null);
        return this.arrowfishProjectile != null;
    }

    @Override
    public boolean shouldContinue() {
        if (!this.crocodileEntity.isAlive() || this.crocodileEntity.isTamed() || !this.isProjectileValid()) return false;
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
            this.crocodileEntity.getNavigation().startMovingTo(this.arrowfishProjectile, APPROACH_SPEED);
        }
    }

    @Override
    public void stop() {
        boolean fed = this.distractionStarted && !this.crocodileEntity.isDistracted();
        this.crocodileEntity.setDistracted(false);
        this.crocodileEntity.getNavigation().stop();
        this.nextSearchTime = this.crocodileEntity.getWorld().getTime() + (fed ? FED_COOLDOWN_TICKS : 20);
        this.arrowfishProjectile = null;
        this.distractionStarted = false;
        this.distractionTicks = 0;
        this.pursuitTicks = 0;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.shouldContinue()) return;

        this.crocodileEntity.getLookControl().lookAt(this.arrowfishProjectile, 20.0f, this.crocodileEntity.getMaxLookPitchChange());
        if (!this.distractionStarted) {
            this.pursuitTicks++;
            if (this.crocodileEntity.squaredDistanceTo(this.arrowfishProjectile) > DISTRACTION_DISTANCE * DISTRACTION_DISTANCE) {
                if (this.crocodileEntity.getNavigation().isIdle()) {
                    this.crocodileEntity.getNavigation().startMovingTo(this.arrowfishProjectile, APPROACH_SPEED);
                }
                return;
            }

            this.crocodileEntity.getNavigation().stop();
            this.distractionStarted = true;
            this.crocodileEntity.setDistracted(true);
        }

        this.distractionTicks++;
        if (this.distractionTicks >= DISTRACTION_DURATION_TICKS) {
            this.crocodileEntity.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0f, 0.8f + this.crocodileEntity.getRandom().nextFloat() * 0.4f);
            this.arrowfishProjectile.discard();
        }
    }

    private boolean isProjectileValid() {
        return this.arrowfishProjectile != null
                && this.arrowfishProjectile.isAlive()
                && this.arrowfishProjectile.isStuckInGround();
    }
}
