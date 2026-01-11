package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

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
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.woodpecker.isAlive() && !this.woodpecker.isAttached()
                && !this.woodpecker.isTouchingWater();
    }

    @Override
    public boolean shouldContinue() {
        return this.woodpecker.isAlive() && this.woodpecker.isFlying()
                && !this.woodpecker.isAttached() && !this.woodpecker.isTouchingWater();
    }

    @Override
    public void start() {
        this.woodpecker.setFlying(true);
        this.startMovingToNextPosition();
    }

    @Override
    public void stop() {
        this.woodpecker.getNavigation().stop();
        if (this.woodpecker.isTouchingWater()) this.woodpecker.setFlying(false);
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.woodpecker.getNavigation().isIdle()) this.startMovingToNextPosition();
    }

    private boolean startMovingToNextPosition() {
        return this.tryStartMovingToSurfaceTarget(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
                || this.tryStartMovingToSurfaceTarget(Heightmap.Type.MOTION_BLOCKING);
    }

    private boolean tryStartMovingToSurfaceTarget(Heightmap.Type heightmapType) {
        BlockPos origin = this.woodpecker.getBlockPos();
        for (int attempt = 0; attempt < TARGET_ATTEMPTS; attempt++) {
            int targetX = origin.getX() + this.woodpecker.getRandom().nextBetween(-HORIZONTAL_RANGE, HORIZONTAL_RANGE);
            int targetZ = origin.getZ() + this.woodpecker.getRandom().nextBetween(-HORIZONTAL_RANGE, HORIZONTAL_RANGE);
            int surfaceY = this.woodpecker.getWorld().getTopY(heightmapType, targetX, targetZ);
            int targetY = surfaceY + this.woodpecker.getRandom().nextBetween(MIN_HEIGHT_ABOVE_SURFACE, MAX_HEIGHT_ABOVE_SURFACE);
            Vec3d target = Vec3d.ofBottomCenter(new BlockPos(targetX, targetY, targetZ));
            if (!this.isTargetSpaceEmpty(target)) continue;
            if (this.woodpecker.getNavigation().startMovingTo(target.x, target.y, target.z, this.speed)) return true;
        }
        return false;
    }

    private boolean isTargetSpaceEmpty(Vec3d target) {
        Vec3d offset = target.subtract(this.woodpecker.getPos());
        return this.woodpecker.getWorld().isSpaceEmpty(this.woodpecker, this.woodpecker.getBoundingBox().offset(offset));
    }
}
