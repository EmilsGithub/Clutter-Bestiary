package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ButterflyEscapeFluidGoal extends Goal {
    private static final int HORIZONTAL_SEARCH_RANGE = 6;
    private static final int VERTICAL_SEARCH_RANGE = 8;
    private static final double ESCAPE_SPEED = 1.5;
    private static final double MIN_UPWARD_VELOCITY = 0.15;
    private static final int RETARGET_INTERVAL_TICKS = 10;
    private final ButterflyEntity butterfly;
    private BlockPos escapePos;
    private int retargetTicks;

    public ButterflyEscapeFluidGoal(ButterflyEntity butterfly) {
        this.butterfly = butterfly;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.butterfly.isInFluid();
    }

    @Override
    public boolean shouldContinue() {
        return this.butterfly.isInFluid();
    }

    @Override
    public void start() {
        this.butterfly.getNavigation().stop();
        this.escapePos = this.findEscapePos();
        this.retargetTicks = RETARGET_INTERVAL_TICKS;
    }

    @Override
    public void stop() {
        this.escapePos = null;
        this.retargetTicks = 0;
    }

    @Override
    public void tick() {
        if ((this.escapePos == null || !this.butterfly.isSafeFlightTarget(this.escapePos)) && --this.retargetTicks <= 0) {
            this.escapePos = this.findEscapePos();
            this.retargetTicks = RETARGET_INTERVAL_TICKS;
        }

        if (this.escapePos != null) {
            Vec3d target = this.escapePos.toCenterPos();
            this.butterfly.getMoveControl().moveTo(target.x, target.y, target.z, ESCAPE_SPEED);
        }

        Vec3d velocity = this.butterfly.getVelocity();
        if (velocity.y < MIN_UPWARD_VELOCITY) {
            this.butterfly.setVelocity(velocity.x, MIN_UPWARD_VELOCITY, velocity.z);
        }
    }

    private BlockPos findEscapePos() {
        BlockPos origin = this.butterfly.getBlockPos();

        for (int y = 1; y <= VERTICAL_SEARCH_RANGE; y++) {
            BlockPos candidate = origin.up(y);
            if (this.canReachDirectly(candidate)) return candidate;
        }

        for (int radius = 1; radius <= HORIZONTAL_SEARCH_RANGE; radius++) {
            for (int y = 0; y <= VERTICAL_SEARCH_RANGE; y++) {
                for (int x = -radius; x <= radius; x++) {
                    BlockPos north = origin.add(x, y, -radius);
                    if (this.canReachDirectly(north)) return north;

                    BlockPos south = origin.add(x, y, radius);
                    if (this.canReachDirectly(south)) return south;
                }

                for (int z = -radius + 1; z < radius; z++) {
                    BlockPos west = origin.add(-radius, y, z);
                    if (this.canReachDirectly(west)) return west;

                    BlockPos east = origin.add(radius, y, z);
                    if (this.canReachDirectly(east)) return east;
                }
            }
        }

        return null;
    }

    private boolean canReachDirectly(BlockPos pos) {
        if (!this.butterfly.isSafeFlightTarget(pos)) return false;

        World world = this.butterfly.getWorld();
        Vec3d start = this.butterfly.getBoundingBox().getCenter();
        Vec3d target = pos.toCenterPos();
        if (world.raycast(new RaycastContext(start, target, RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE, this.butterfly)).getType() != HitResult.Type.MISS) {
            return false;
        }

        return world.isSpaceEmpty(this.butterfly, this.butterfly.getBoundingBox().offset(target.subtract(start)));
    }
}
