package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.woodpecker.isAlive() || !this.woodpecker.isFlying()) return false;
        if (this.woodpecker.isAttached() || this.woodpecker.isInWater()) return false;
        if (this.woodpecker.getRandom().nextInt(reducedTickDelay(PERCH_CHANCE)) != 0) return false;
        return this.findPerch();
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.woodpecker.isAlive() || this.woodpecker.isInWater() || this.perchPos == null) return false;
        if (!this.isValidPerch(this.perchPos)) return false;
        if (!this.perched) return this.approachTicks < MAX_APPROACH_TICKS;
        return this.perchTicks > 0
                && this.woodpecker.distanceToSqr(this.getPerchPosition(this.perchPos)) <= MAX_PERCHED_DISTANCE_SQUARED;
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
        if (!this.woodpecker.isInWater() && this.woodpecker.isAlive() && !this.woodpecker.isAttached()) {
            this.woodpecker.setFlying(true);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
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
        Vec3 perchPosition = this.getPerchPosition(this.perchPos);
        if (this.woodpecker.blockPosition().equals(this.perchPos)
                || this.woodpecker.distanceToSqr(perchPosition) <= ARRIVAL_DISTANCE_SQUARED) {
            this.landOnPerch(perchPosition);
            return;
        }

        if (this.woodpecker.getNavigation().isDone()) this.startMovingToPerch();
    }

    private boolean findPerch() {
        BlockPos origin = this.woodpecker.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.getX() - HORIZONTAL_SEARCH_RANGE,
                origin.getY() - VERTICAL_SEARCH_RANGE,
                origin.getZ() - HORIZONTAL_SEARCH_RANGE,
                origin.getX() + HORIZONTAL_SEARCH_RANGE,
                origin.getY() + VERTICAL_SEARCH_RANGE,
                origin.getZ() + HORIZONTAL_SEARCH_RANGE)) {
            if (origin.equals(pos)) continue;
            if (this.isValidPerch(pos)) {
                this.perchPos = pos.immutable();
                return true;
            }
        }
        return false;
    }

    private boolean isValidPerch(BlockPos pos) {
        if (!this.woodpecker.level().getBlockState(pos.below()).is(BlockTags.LEAVES)) return false;
        if (!this.woodpecker.level().getBlockState(pos).isAir()
                || !this.woodpecker.level().getBlockState(pos.above()).isAir()) return false;

        Vec3 perchPosition = Vec3.atBottomCenterOf(pos);
        AABB perchBox = this.woodpecker.getBoundingBox().move(perchPosition.subtract(this.woodpecker.position()));
        return this.woodpecker.level().noCollision(this.woodpecker, perchBox);
    }

    private void startMovingToPerch() {
        if (this.perchPos == null) return;
        Vec3 perchPosition = this.getPerchPosition(this.perchPos);
        this.woodpecker.getNavigation().moveTo(perchPosition.x, perchPosition.y, perchPosition.z, this.speed);
    }

    private void landOnPerch(Vec3 perchPosition) {
        this.woodpecker.getNavigation().stop();
        this.woodpecker.setPos(perchPosition);
        this.woodpecker.setDeltaMovement(Vec3.ZERO);
        this.woodpecker.setFlying(false);
        this.perchTicks = this.woodpecker.getRandom().nextIntBetweenInclusive(MIN_PERCH_TICKS, MAX_PERCH_TICKS);
        this.perched = true;
    }

    private Vec3 getPerchPosition(BlockPos pos) {
        return Vec3.atBottomCenterOf(pos);
    }
}
