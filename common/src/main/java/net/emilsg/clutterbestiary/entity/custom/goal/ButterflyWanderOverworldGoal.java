package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ButterflyWanderOverworldGoal extends Goal {
    private final ButterflyEntity butterfly;

    public ButterflyWanderOverworldGoal(ButterflyEntity butterfly) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.butterfly = butterfly;
    }

    @Override
    public boolean canUse() {
        return !this.butterfly.isInLiquid()
                && this.butterfly.level().dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)
                && this.butterfly.getNavigation().isDone() && this.butterfly.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.butterfly.isInLiquid() && this.butterfly.getNavigation().isInProgress();
    }

    @Override
    public void start() {
        Vec3 vec3d = this.getRandomLocation();
        if (vec3d == null) return;

        BlockPos targetPos = BlockPos.containing(vec3d);
        if (!this.butterfly.isSafeFlightTarget(targetPos)) return;

        Path path = this.butterfly.getNavigation().createPath(targetPos, 1);
        if (path != null) this.butterfly.getNavigation().moveTo(path, 1.0);
    }

    @Nullable
    private Vec3 getRandomLocation() {
        Vec3 vec3d2 = this.butterfly.getViewVector(0.0f);

        Vec3 vec3d3 = HoverRandomPos.getPos(this.butterfly, 8, 7, vec3d2.x, vec3d2.z, 1.5707964f, 4, 2);
        if (vec3d3 != null) {
            return vec3d3;
        }
        return AirAndWaterRandomPos.getPos(this.butterfly, 8, 4, -2, vec3d2.x, vec3d2.z, 1.5707963705062866);
    }
}
