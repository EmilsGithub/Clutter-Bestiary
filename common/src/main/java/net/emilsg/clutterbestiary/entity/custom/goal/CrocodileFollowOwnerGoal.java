package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.vehicle.BoatEntity;
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
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity owner = this.crocodile.getOwner();
        if (owner == null || this.crocodile.cannotFollowOwner()) return false;
        if (this.crocodile.squaredDistanceTo(this.getFollowTarget(owner)) < this.minDistance * this.minDistance) return false;

        this.owner = owner;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return this.owner != null && this.owner.isAlive() && this.owner.getWorld() == this.crocodile.getWorld()
                && !this.crocodile.cannotFollowOwner()
                && this.crocodile.squaredDistanceTo(this.getFollowTarget(this.owner)) > this.maxDistance * this.maxDistance;
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.crocodile.getPathfindingPenalty(PathNodeType.WATER);
        this.crocodile.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        this.crocodile.setFollowingOwner(true);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.crocodile.getNavigation().stop();
        this.crocodile.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
        this.crocodile.setFollowingOwner(false);
    }

    @Override
    public void tick() {
        if (this.owner == null) return;

        Entity followTarget = this.getFollowTarget(this.owner);
        boolean followingBoat = followTarget instanceof BoatEntity;
        this.crocodile.getLookControl().lookAt(this.owner, 10.0f, this.crocodile.getMaxLookPitchChange());

        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.getTickCount(10);
            if (!followingBoat && this.crocodile.shouldTryTeleportToOwner()) {
                this.crocodile.tryTeleportToOwner();
            } else {
                this.crocodile.getNavigation().startMovingTo(followTarget, followingBoat ? this.boatSpeed : this.speed);
            }
        }
    }

    private Entity getFollowTarget(LivingEntity owner) {
        return owner.getVehicle() instanceof BoatEntity boat ? boat : owner;
    }
}
