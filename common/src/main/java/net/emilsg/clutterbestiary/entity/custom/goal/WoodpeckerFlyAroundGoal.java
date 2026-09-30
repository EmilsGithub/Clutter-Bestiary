package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class WoodpeckerFlyAroundGoal extends Goal {
    private static final int HORIZONTAL_RANGE = 8;
    private static final int MIN_HEIGHT_ABOVE_SURFACE = 2;
    private static final int MAX_HEIGHT_ABOVE_SURFACE = 5;
    private static final int TARGET_ATTEMPTS = 12;

    private final WoodpeckerEntity woodpecker;
    private final double speed;

    public WoodpeckerFlyAroundGoal(WoodpeckerEntity woodpecker, double speed) {
        this.woodpecker = woodpecker;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.woodpecker.isAlive() && !this.woodpecker.isAttached()
                && !this.woodpecker.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        return this.woodpecker.isAlive() && this.woodpecker.isFlying()
                && !this.woodpecker.isAttached() && !this.woodpecker.isInWater();
    }

    @Override
    public void start() {
        this.woodpecker.setFlying(true);
        this.startMovingToNextPosition();
    }

    @Override
    public void stop() {
        this.woodpecker.getNavigation().stop();
        if (this.woodpecker.isInWater()) this.woodpecker.setFlying(false);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.woodpecker.getNavigation().isDone()) this.startMovingToNextPosition();
    }

    private boolean startMovingToNextPosition() {
        return this.tryStartMovingToSurfaceTarget(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)
                || this.tryStartMovingToSurfaceTarget(Heightmap.Types.MOTION_BLOCKING);
    }

    private boolean tryStartMovingToSurfaceTarget(Heightmap.Types heightmapType) {
        BlockPos origin = this.woodpecker.blockPosition();
        for (int attempt = 0; attempt < TARGET_ATTEMPTS; attempt++) {
            int targetX = origin.getX() + this.woodpecker.getRandom().nextIntBetweenInclusive(-HORIZONTAL_RANGE, HORIZONTAL_RANGE);
            int targetZ = origin.getZ() + this.woodpecker.getRandom().nextIntBetweenInclusive(-HORIZONTAL_RANGE, HORIZONTAL_RANGE);
            int surfaceY = this.woodpecker.level().getHeight(heightmapType, targetX, targetZ);
            int targetY = surfaceY + this.woodpecker.getRandom().nextIntBetweenInclusive(MIN_HEIGHT_ABOVE_SURFACE, MAX_HEIGHT_ABOVE_SURFACE);
            Vec3 target = Vec3.atBottomCenterOf(new BlockPos(targetX, targetY, targetZ));
            if (!this.isTargetSpaceEmpty(target)) continue;
            if (this.woodpecker.getNavigation().moveTo(target.x, target.y, target.z, this.speed)) return true;
        }
        return false;
    }

    private boolean isTargetSpaceEmpty(Vec3 target) {
        Vec3 offset = target.subtract(this.woodpecker.position());
        return this.woodpecker.level().noCollision(this.woodpecker, this.woodpecker.getBoundingBox().move(offset));
    }
}
