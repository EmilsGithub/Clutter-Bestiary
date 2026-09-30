package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RedPandaWanderAroundFarGoal extends WaterAvoidingRandomStrollGoal {
    private final RedPandaEntity redPandaEntity;

    public RedPandaWanderAroundFarGoal(RedPandaEntity redPandaEntity, float speed) {
        super(redPandaEntity, speed);
        this.redPandaEntity = redPandaEntity;
    }

    @Override
    public boolean canUse() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (this.mob.getRandom().nextInt(4) == 0) {
                return false;
            }

            Vec3 vec3d = this.getPosition();
            if (vec3d == null) {
                return false;
            } else {
                this.wantedX = vec3d.x;
                this.wantedY = vec3d.y;
                this.wantedZ = vec3d.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (!this.redPandaEntity.isStaying()) return super.getPosition();

        BlockPos centerPos = this.redPandaEntity.getStayingPos();
        Vec3 center = Vec3.atCenterOf(centerPos);
        double maxDistSq = 20.0 * 20.0;

        int horizontal = this.redPandaEntity.isInWater() ? 15 : 10;
        int vertical = 7;

        if (!this.redPandaEntity.isInWater() && this.redPandaEntity.getRandom().nextFloat() < this.probability) {
            Vec3 fallback = super.getPosition();
            return fallback != null && fallback.distanceToSqr(center) <= maxDistSq ? fallback : null;
        }

        for (int tries = 0; tries < 12; tries++) {
            Vec3 candidate = LandRandomPos.getPos(this.redPandaEntity, horizontal, vertical);
            if (candidate != null && candidate.distanceToSqr(center) <= maxDistSq) {
                return candidate;
            }
        }

        Vec3 fallback = super.getPosition();
        return fallback != null && fallback.distanceToSqr(center) <= maxDistSq ? fallback : null;
    }
}
