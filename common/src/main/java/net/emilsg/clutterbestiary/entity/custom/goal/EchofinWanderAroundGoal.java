package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.EchofinEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class EchofinWanderAroundGoal extends Goal {
    EchofinEntity echofinEntity;
    private BlockPos homePos;

    public EchofinWanderAroundGoal(EchofinEntity echofinEntity) {
        this.echofinEntity = echofinEntity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return echofinEntity.getNavigation().isDone();
    }

    public boolean canContinueToUse() {
        return echofinEntity.getNavigation().isInProgress();
    }

    public void start() {
        Vec3 vec3d = this.getRandomLocation();
        if (vec3d != null) {
            echofinEntity.getNavigation().moveTo(echofinEntity.getNavigation().createPath(BlockPos.containing(vec3d), 1), 1.0);
        }
    }

    private Vec3 getRandomLocation() {
        if (this.homePos == null) {
            this.homePos = echofinEntity.getHomePos();
        }

        Vec3 vec3d2 = echofinEntity.getViewVector(0.0F);
        Vec3 vec3d3 = HoverRandomPos.getPos(echofinEntity, 24, 7, vec3d2.x, vec3d2.z, 1.5707964F, 3, 2);

        if (vec3d3 != null && echofinEntity.blockPosition().distToCenterSqr(Vec3.atCenterOf(homePos)) > 2 * 2) {
            return vec3d3;
        }

        if (echofinEntity.level().isDarkOutside() && echofinEntity.blockPosition().distToCenterSqr(Vec3.atCenterOf(homePos)) > 8 * 8) {
            return Vec3.atCenterOf(homePos);
        }

        BlockPos blockpos = homePos.offset(-2 + echofinEntity.getRandom().nextInt(5), -1 + echofinEntity.getRandom().nextInt(3), -2 + echofinEntity.getRandom().nextInt(5));

        if (!echofinEntity.level().getBlockState(blockpos).canOcclude()) {
            return Vec3.atCenterOf(blockpos);
        }

        return null;
    }
}
