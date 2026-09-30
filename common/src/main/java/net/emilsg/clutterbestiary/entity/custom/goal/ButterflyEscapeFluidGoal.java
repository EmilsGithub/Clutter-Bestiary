package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.butterfly.isInLiquid();
    }

    @Override
    public boolean canContinueToUse() {
        return this.butterfly.isInLiquid();
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
            Vec3 target = Vec3.atCenterOf(this.escapePos);
            this.butterfly.getMoveControl().setWantedPosition(target.x, target.y, target.z, ESCAPE_SPEED);
        }

        Vec3 velocity = this.butterfly.getDeltaMovement();
        if (velocity.y < MIN_UPWARD_VELOCITY) {
            this.butterfly.setDeltaMovement(velocity.x, MIN_UPWARD_VELOCITY, velocity.z);
        }
    }

    private BlockPos findEscapePos() {
        BlockPos origin = this.butterfly.blockPosition();

        for (int y = 1; y <= VERTICAL_SEARCH_RANGE; y++) {
            BlockPos candidate = origin.above(y);
            if (this.canReachDirectly(candidate)) return candidate;
        }

        for (int radius = 1; radius <= HORIZONTAL_SEARCH_RANGE; radius++) {
            for (int y = 0; y <= VERTICAL_SEARCH_RANGE; y++) {
                for (int x = -radius; x <= radius; x++) {
                    BlockPos north = origin.offset(x, y, -radius);
                    if (this.canReachDirectly(north)) return north;

                    BlockPos south = origin.offset(x, y, radius);
                    if (this.canReachDirectly(south)) return south;
                }

                for (int z = -radius + 1; z < radius; z++) {
                    BlockPos west = origin.offset(-radius, y, z);
                    if (this.canReachDirectly(west)) return west;

                    BlockPos east = origin.offset(radius, y, z);
                    if (this.canReachDirectly(east)) return east;
                }
            }
        }

        return null;
    }

    private boolean canReachDirectly(BlockPos pos) {
        if (!this.butterfly.isSafeFlightTarget(pos)) return false;

        Level world = this.butterfly.level();
        Vec3 start = this.butterfly.getBoundingBox().getCenter();
        Vec3 target = Vec3.atCenterOf(pos);
        if (world.clip(new ClipContext(start, target, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, this.butterfly)).getType() != HitResult.Type.MISS) {
            return false;
        }

        return world.noCollision(this.butterfly, this.butterfly.getBoundingBox().move(target.subtract(start)));
    }
}
