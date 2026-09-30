package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyLarvaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ButterflyLarvaWanderGoal extends WaterAvoidingRandomStrollGoal {
    private final ButterflyLarvaEntity larva;

    public ButterflyLarvaWanderGoal(ButterflyLarvaEntity larva) {
        super(larva, 0.8);
        this.larva = larva;
    }

    @Override
    protected @Nullable Vec3 getPosition() {
        for (int attempt = 0; attempt < 8; attempt++) {
            Vec3 target = super.getPosition();
            if (target != null
                    && this.larva.getHomePos().closerToCenterThan(target, ButterflyLarvaEntity.HOME_RADIUS)
                    && this.isDryTarget(target)) {
                return target;
            }
        }

        return null;
    }

    private boolean isDryTarget(Vec3 target) {
        Level world = this.larva.level();
        BlockPos targetPos = BlockPos.containing(target);
        return world.getFluidState(targetPos).isEmpty()
                && world.getFluidState(targetPos.below()).isEmpty()
                && world.getBlockState(targetPos.below()).isRedstoneConductor(world, targetPos.below());
    }
}
