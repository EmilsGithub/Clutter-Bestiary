package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class SeahorseMateGoal extends Goal {
    private static final TargetingConditions VALID_MATE_PREDICATE = TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight();
    protected final SeahorseEntity seahorse;
    protected final Level world;
    private final Class<? extends SeahorseEntity> entityClass;
    private final double speed;
    @Nullable
    protected SeahorseEntity mate;
    private int timer;

    public SeahorseMateGoal(SeahorseEntity seahorse, double speed, Class<? extends SeahorseEntity> entityClass) {
        this.seahorse = seahorse;
        this.world = seahorse.level();
        this.entityClass = entityClass;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (!this.seahorse.isInLove()) {
            return false;
        } else {
            this.mate = this.findMate();
            return this.mate != null;
        }
    }

    public boolean canContinueToUse() {
        if (this.mate == null) return false;
        return this.mate.isAlive() && this.mate.isInLove() && this.timer < 60;
    }

    public void stop() {
        this.mate = null;
        this.timer = 0;
    }

    public void tick() {
        this.seahorse.getLookControl().setLookAt(this.mate, 10.0F, (float) this.seahorse.getMaxHeadXRot());
        this.seahorse.getNavigation().moveTo(this.mate, this.speed);
        ++this.timer;
        if (this.timer >= this.adjustedTickDelay(60) && this.seahorse.distanceToSqr(this.mate) < 9.0) {
            this.breed();
        }

    }

    protected void breed() {
        this.seahorse.breed((ServerLevel) this.world, this.mate);
    }

    @Nullable
    private SeahorseEntity findMate() {
        List<? extends SeahorseEntity> list = getServerLevel(this.world).getNearbyEntities(this.entityClass, VALID_MATE_PREDICATE, this.seahorse, this.seahorse.getBoundingBox().inflate(8.0));
        double d = Double.MAX_VALUE;
        SeahorseEntity seahorse1 = null;

        for (SeahorseEntity seahorse2 : list) {
            if (this.seahorse.canBreedWith(seahorse2) && this.seahorse.distanceToSqr(seahorse2) < d) {
                seahorse1 = seahorse2;
                d = this.seahorse.distanceToSqr(seahorse2);
            }
        }

        return seahorse1;
    }
}
