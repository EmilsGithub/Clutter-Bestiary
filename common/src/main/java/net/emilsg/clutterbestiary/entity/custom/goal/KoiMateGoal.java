package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class KoiMateGoal extends Goal {
    private static final TargetingConditions VALID_MATE_PREDICATE = TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight();
    protected final KoiEntity koiEntity;
    protected final Level world;
    private final Class<? extends KoiEntity> entityClass;
    private final double speed;
    @Nullable
    protected KoiEntity mate;
    private int timer;

    public KoiMateGoal(KoiEntity koiEntity, double speed, Class<? extends KoiEntity> entityClass) {
        this.koiEntity = koiEntity;
        this.world = koiEntity.level();
        this.entityClass = entityClass;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (!this.koiEntity.isInLove()) {
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
        this.koiEntity.getLookControl().setLookAt(this.mate, 10.0F, (float) this.koiEntity.getMaxHeadXRot());
        this.koiEntity.getNavigation().moveTo(this.mate, this.speed);
        ++this.timer;
        if (this.timer >= this.adjustedTickDelay(60) && this.koiEntity.distanceToSqr(this.mate) < 9.0) {
            this.breed();
        }

    }

    protected void breed() {
        this.koiEntity.breed((ServerLevel) this.world, this.mate);
    }

    @Nullable
    private KoiEntity findMate() {
        List<? extends KoiEntity> list = getServerLevel(this.world).getNearbyEntities(this.entityClass, VALID_MATE_PREDICATE, this.koiEntity, this.koiEntity.getBoundingBox().inflate(8.0));
        double d = Double.MAX_VALUE;
        KoiEntity koiEntity1 = null;

        for (KoiEntity koiEntity2 : list) {
            if (this.koiEntity.canBreedWith(koiEntity2) && this.koiEntity.distanceToSqr(koiEntity2) < d) {
                koiEntity1 = koiEntity2;
                d = this.koiEntity.distanceToSqr(koiEntity2);
            }
        }

        return koiEntity1;
    }
}
