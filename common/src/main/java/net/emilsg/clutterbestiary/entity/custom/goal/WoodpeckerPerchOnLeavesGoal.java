package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WoodpeckerPerchOnLeavesGoal extends Goal {
    private static final int PERCH_CHANCE = 2400;
    private static final int HORIZONTAL_SEARCH_RANGE = 3;
    private static final int VERTICAL_SEARCH_RANGE = 6;
    private static final int MAX_APPROACH_TICKS = 160;
    private static final int MIN_PERCH_TICKS = 80;
    private static final int MAX_PERCH_TICKS = 160;
    private static final double ARRIVAL_DISTANCE_SQUARED = 0.36;
    private static final double MAX_PERCHED_DISTANCE_SQUARED = 1.0;

    private final WoodpeckerEntity woodpecker;
    private final double speed;
    @Nullable private BlockPos perchPos;
    private int approachTicks;
    private int perchTicks;
    private boolean perched;

    public WoodpeckerPerchOnLeavesGoal(WoodpeckerEntity woodpecker, double speed) {
        this.woodpecker = woodpecker;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.woodpecker.isAlive() || !this.woodpecker.isFlying()) return false;
        if (this.woodpecker.isAttached() || this.woodpecker.isTouchingWater()) return false;
        if (this.woodpecker.getRandom().nextInt(toGoalTicks(PERCH_CHANCE)) != 0) return false;
        return this.findPerch();
    }

    @Override
    public boolean shouldContinue() {
        if (!this.woodpecker.isAlive() || this.woodpecker.isTouchingWater() || this.perchPos == null) return false;
        if (!this.isValidPerch(this.perchPos)) return false;
        if (!this.perched) return this.approachTicks < MAX_APPROACH_TICKS;
        return this.perchTicks > 0
                && this.woodpecker.squaredDistanceTo(this.getPerchPosition(this.perchPos)) <= MAX_PERCHED_DISTANCE_SQUARED;
    }

    @Override
    public void start() {
        this.approachTicks = 0;
        this.perchTicks = 0;
        this.perched = false;
        this.woodpecker.setFlying(true);
        this.startMovingToPerch();
    }

    @Override
    public void stop() {
        this.woodpecker.getNavigation().stop();
        this.perchPos = null;
        this.approachTicks = 0;
        this.perchTicks = 0;
        this.perched = false;
        if (!this.woodpecker.isTouchingWater() && this.woodpecker.isAlive() && !this.woodpecker.isAttached()) {
            this.woodpecker.setFlying(true);
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.perchPos == null) return;
        if (this.perched) {
            this.perchTicks--;
            return;
        }

        this.approachTicks++;
        Vec3d perchPosition = this.getPerchPosition(this.perchPos);
        if (this.woodpecker.getBlockPos().equals(this.perchPos)
                || this.woodpecker.squaredDistanceTo(perchPosition) <= ARRIVAL_DISTANCE_SQUARED) {
            this.landOnPerch(perchPosition);
            return;
        }

        if (this.woodpecker.getNavigation().isIdle()) this.startMovingToPerch();
    }

    private boolean findPerch() {
        BlockPos origin = this.woodpecker.getBlockPos();
        for (BlockPos pos : BlockPos.iterate(
                origin.getX() - HORIZONTAL_SEARCH_RANGE,
                origin.getY() - VERTICAL_SEARCH_RANGE,
                origin.getZ() - HORIZONTAL_SEARCH_RANGE,
                origin.getX() + HORIZONTAL_SEARCH_RANGE,
                origin.getY() + VERTICAL_SEARCH_RANGE,
                origin.getZ() + HORIZONTAL_SEARCH_RANGE)) {
            if (origin.equals(pos)) continue;
            if (this.isValidPerch(pos)) {
                this.perchPos = pos.toImmutable();
                return true;
            }
        }
        return false;
    }

    private boolean isValidPerch(BlockPos pos) {
        if (!this.woodpecker.getWorld().getBlockState(pos.down()).isIn(BlockTags.LEAVES)) return false;
        if (!this.woodpecker.getWorld().getBlockState(pos).isAir()
                || !this.woodpecker.getWorld().getBlockState(pos.up()).isAir()) return false;

        Vec3d perchPosition = Vec3d.ofBottomCenter(pos);
        Box perchBox = this.woodpecker.getBoundingBox().offset(perchPosition.subtract(this.woodpecker.getPos()));
        return this.woodpecker.getWorld().isSpaceEmpty(this.woodpecker, perchBox);
    }

    private void startMovingToPerch() {
        if (this.perchPos == null) return;
        Vec3d perchPosition = this.getPerchPosition(this.perchPos);
        this.woodpecker.getNavigation().startMovingTo(perchPosition.x, perchPosition.y, perchPosition.z, this.speed);
    }

    private void landOnPerch(Vec3d perchPosition) {
        this.woodpecker.getNavigation().stop();
        this.woodpecker.setPosition(perchPosition);
        this.woodpecker.setVelocity(Vec3d.ZERO);
        this.woodpecker.setFlying(false);
        this.perchTicks = this.woodpecker.getRandom().nextBetween(MIN_PERCH_TICKS, MAX_PERCH_TICKS);
        this.perched = true;
    }

    private Vec3d getPerchPosition(BlockPos pos) {
        return Vec3d.ofBottomCenter(pos);
    }
}
