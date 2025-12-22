package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class RedPandaWanderAroundFarGoal extends WanderAroundFarGoal {
    private final RedPandaEntity redPandaEntity;

    public RedPandaWanderAroundFarGoal(RedPandaEntity redPandaEntity, float speed) {
        super(redPandaEntity, speed);
        this.redPandaEntity = redPandaEntity;
    }

    @Override
    public boolean canStart() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (this.mob.getRandom().nextInt(4) == 0) {
                return false;
            }

            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            } else {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                this.ignoringChance = false;
                return true;
            }
        }
    }

    @Nullable
    @Override
    protected Vec3d getWanderTarget() {
        if (!this.redPandaEntity.isStaying()) return super.getWanderTarget();

        BlockPos centerPos = this.redPandaEntity.getStayingPos();
        Vec3d center = Vec3d.ofCenter(centerPos);
        double maxDistSq = 20.0 * 20.0;

        int horizontal = this.redPandaEntity.isInsideWaterOrBubbleColumn() ? 15 : 10;
        int vertical = 7;

        if (!this.redPandaEntity.isInsideWaterOrBubbleColumn() && this.redPandaEntity.getRandom().nextFloat() < this.probability) {
            Vec3d fallback = super.getWanderTarget();
            return fallback != null && fallback.squaredDistanceTo(center) <= maxDistSq ? fallback : null;
        }

        for (int tries = 0; tries < 12; tries++) {
            Vec3d candidate = FuzzyTargeting.find(this.redPandaEntity, horizontal, vertical);
            if (candidate != null && candidate.squaredDistanceTo(center) <= maxDistSq) {
                return candidate;
            }
        }

        Vec3d fallback = super.getWanderTarget();
        return fallback != null && fallback.squaredDistanceTo(center) <= maxDistSq ? fallback : null;
    }
}
