package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.KoiEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class KoiMateGoal extends Goal {
    private static final TargetPredicate VALID_MATE_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(8.0).ignoreVisibility();
    protected final KoiEntity koiEntity;
    protected final World world;
    private final Class<? extends KoiEntity> entityClass;
    private final double speed;
    @Nullable
    protected KoiEntity mate;
    private int timer;

    public KoiMateGoal(KoiEntity koiEntity, double speed, Class<? extends KoiEntity> entityClass) {
        this.koiEntity = koiEntity;
        this.world = koiEntity.getWorld();
        this.entityClass = entityClass;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
        if (!this.koiEntity.isInLove()) {
            return false;
        } else {
            this.mate = this.findMate();
            return this.mate != null;
        }
    }

    public boolean shouldContinue() {
        if (this.mate == null) return false;
        return this.mate.isAlive() && this.mate.isInLove() && this.timer < 60;
    }

    public void stop() {
        this.mate = null;
        this.timer = 0;
    }

    public void tick() {
        this.koiEntity.getLookControl().lookAt(this.mate, 10.0F, (float) this.koiEntity.getMaxLookPitchChange());
        this.koiEntity.getNavigation().startMovingTo(this.mate, this.speed);
        ++this.timer;
        if (this.timer >= this.getTickCount(60) && this.koiEntity.squaredDistanceTo(this.mate) < 9.0) {
            this.breed();
        }

    }

    protected void breed() {
        this.koiEntity.breed((ServerWorld) this.world, this.mate);
    }

    @Nullable
    private KoiEntity findMate() {
        List<? extends KoiEntity> list = this.world.getTargets(this.entityClass, VALID_MATE_PREDICATE, this.koiEntity, this.koiEntity.getBoundingBox().expand(8.0));
        double d = Double.MAX_VALUE;
        KoiEntity koiEntity1 = null;

        for (KoiEntity koiEntity2 : list) {
            if (this.koiEntity.canBreedWith(koiEntity2) && this.koiEntity.squaredDistanceTo(koiEntity2) < d) {
                koiEntity1 = koiEntity2;
                d = this.koiEntity.squaredDistanceTo(koiEntity2);
            }
        }

        return koiEntity1;
    }
}
