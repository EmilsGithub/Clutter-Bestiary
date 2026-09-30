package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class PotionWaspWanderAroundGoal extends Goal {
    private final PotionWaspEntity potionWasp;

    public PotionWaspWanderAroundGoal(PotionWaspEntity potionWasp) {
        this.potionWasp = potionWasp;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.potionWasp.getNavigation().isDone() && this.potionWasp.getRandom().nextInt(4) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.potionWasp.getNavigation().isInProgress();
    }

    @Override
    public void start() {
        Vec3 vec3d = this.getRandomLocation();
        if (vec3d != null) {
            this.potionWasp.getNavigation().moveTo(this.potionWasp.getNavigation().createPath(BlockPos.containing(vec3d), 1), 1.0F);
        }
    }

    @Nullable
    private Vec3 getRandomLocation() {
        Vec3 vec3d2 = this.potionWasp.getViewVector(0.0F);
        Vec3 vec3d3 = HoverRandomPos.getPos(this.potionWasp, 8, 7, vec3d2.x, vec3d2.z, ((float) Math.PI / 2F), 3, 1);
        return vec3d3 != null ? vec3d3 : AirAndWaterRandomPos.getPos(this.potionWasp, 8, 4, -2, vec3d2.x, vec3d2.z, (float) Math.PI / 2F);
    }
}
