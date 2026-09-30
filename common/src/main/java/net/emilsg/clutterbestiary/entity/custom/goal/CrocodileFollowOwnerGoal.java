package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class CrocodileFollowOwnerGoal extends Goal {
    private final CrocodileEntity crocodile;
    private final double speed;
    private final double boatSpeed;
    private final float minDistance;
    private final float maxDistance;
    @Nullable
    private LivingEntity owner;
    private int updateCountdownTicks;
    private float oldWaterPathfindingPenalty;

    public CrocodileFollowOwnerGoal(CrocodileEntity crocodile, double speed, double boatSpeed, float minDistance, float maxDistance) {
        this.crocodile = crocodile;
        this.speed = speed;
        this.boatSpeed = boatSpeed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.crocodile.getOwner();
        if (owner == null || this.crocodile.unableToMoveToOwner()) return false;
        if (this.crocodile.distanceToSqr(this.getFollowTarget(owner)) < this.minDistance * this.minDistance) return false;

        this.owner = owner;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner != null && this.owner.isAlive() && this.owner.level() == this.crocodile.level()
                && !this.crocodile.unableToMoveToOwner()
                && this.crocodile.distanceToSqr(this.getFollowTarget(this.owner)) > this.maxDistance * this.maxDistance;
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.crocodile.getPathfindingMalus(PathType.WATER);
        this.crocodile.setPathfindingMalus(PathType.WATER, 0.0f);
        this.crocodile.setFollowingOwner(true);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.crocodile.getNavigation().stop();
        this.crocodile.setPathfindingMalus(PathType.WATER, this.oldWaterPathfindingPenalty);
        this.crocodile.setFollowingOwner(false);
    }

    @Override
    public void tick() {
        if (this.owner == null) return;

        Entity followTarget = this.getFollowTarget(this.owner);
        boolean followingBoat = followTarget instanceof Boat;
        this.crocodile.getLookControl().setLookAt(this.owner, 10.0f, this.crocodile.getMaxHeadXRot());

        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.adjustedTickDelay(10);
            if (!followingBoat && this.crocodile.shouldTryTeleportToOwner()) {
                this.crocodile.tryToTeleportToOwner();
            } else {
                this.crocodile.getNavigation().moveTo(followTarget, followingBoat ? this.boatSpeed : this.speed);
            }
        }
    }

    private Entity getFollowTarget(LivingEntity owner) {
        return owner.getVehicle() instanceof Boat boat ? boat : owner;
    }
}
