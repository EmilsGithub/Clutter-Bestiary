package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class PotionWaspWanderAroundGoal extends Goal {
    private final PotionWaspEntity potionWasp;

    public PotionWaspWanderAroundGoal(PotionWaspEntity potionWasp) {
        this.potionWasp = potionWasp;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.potionWasp.getNavigation().isIdle() && this.potionWasp.getRandom().nextInt(4) == 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.potionWasp.getNavigation().isFollowingPath();
    }

    @Override
    public void start() {
        Vec3d vec3d = this.getRandomLocation();
        if (vec3d != null) {
            this.potionWasp.getNavigation().startMovingAlong(this.potionWasp.getNavigation().findPathTo(BlockPos.ofFloored(vec3d), 1), 1.0F);
        }
    }

    @Nullable
    private Vec3d getRandomLocation() {
        Vec3d vec3d2 = this.potionWasp.getRotationVec(0.0F);
        Vec3d vec3d3 = AboveGroundTargeting.find(this.potionWasp, 8, 7, vec3d2.x, vec3d2.z, ((float) Math.PI / 2F), 3, 1);
        return vec3d3 != null ? vec3d3 : NoPenaltySolidTargeting.find(this.potionWasp, 8, 4, -2, vec3d2.x, vec3d2.z, (float) Math.PI / 2F);
    }
}
