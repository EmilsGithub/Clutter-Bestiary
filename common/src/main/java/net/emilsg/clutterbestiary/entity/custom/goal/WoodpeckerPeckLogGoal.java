package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WoodpeckerPeckLogGoal extends Goal {
    private static final int SEARCH_RADIUS = 8;
    private static final int SEARCH_HEIGHT_ABOVE = 5;
    private static final int SEARCH_HEIGHT_BELOW = 16;
    private static final int MIN_SEARCH_DELAY_TICKS = 200;
    private static final int MAX_SEARCH_DELAY_TICKS = 400;
    private static final int MAX_APPROACH_TICKS = 160;
    private static final int ATTACH_TICKS = 400;
    private static final int ATTACHED_IDLE_TICKS = 20;
    private static final int PECK_SOUND_INTERVAL_TICKS = 5;
    private static final int PECK_SOUND_OFFSET_TICKS = 3;
    private static final int PECK_PARTICLE_COUNT = 3;
    private static final double ATTACHMENT_DISTANCE_FROM_LOG_CENTER = 1.0;
    private static final double PECK_PARTICLE_DISTANCE_FROM_LOG_CENTER = 0.56;

    private final WoodpeckerEntity woodpecker;
    private final double speed;
    @Nullable private BlockPos targetLog;
    @Nullable private Direction targetFace;
    @Nullable private Vec3 attachmentPos;
    private int activityTicks;
    private long nextSearchTime;

    public WoodpeckerPeckLogGoal(WoodpeckerEntity woodpecker, double speed) {
        this.woodpecker = woodpecker;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.scheduleNextSearch();
    }

    @Override
    public boolean canUse() {
        if (this.woodpecker.level().getGameTime() < this.nextSearchTime) return false;
        if (!this.woodpecker.isAlive() || this.woodpecker.isInWater()) return false;
        return this.findLog();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.woodpecker.isPeckingInterrupted()) return false;
        if (!this.woodpecker.isAlive() || this.targetLog == null || this.targetFace == null || this.attachmentPos == null) return false;
        if (!this.isValidFace(this.targetLog, this.targetFace)) return false;
        return this.activityTicks < (this.woodpecker.isAttached() ? ATTACH_TICKS : MAX_APPROACH_TICKS);
    }

    @Override
    public void start() {
        this.activityTicks = 0;
        this.woodpecker.setFlying(true);
        if (this.attachmentPos != null) this.woodpecker.getNavigation().moveTo(
                this.attachmentPos.x, this.attachmentPos.y, this.attachmentPos.z, this.speed);
    }

    @Override
    public void stop() {
        this.woodpecker.getNavigation().stop();
        this.woodpecker.clearAttachment();
        this.woodpecker.clearPeckingInterruption();
        this.targetLog = null;
        this.targetFace = null;
        this.attachmentPos = null;
        this.scheduleNextSearch();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.attachmentPos == null || this.targetFace == null) return;
        this.activityTicks++;
        if (!this.woodpecker.isAttached()) {
            if (this.woodpecker.distanceToSqr(this.attachmentPos) < 0.25) {
                this.woodpecker.attachToLog(this.attachmentPos, this.targetFace);
                this.activityTicks = 0;
            } else if (this.woodpecker.getNavigation().isDone()) {
                this.woodpecker.getNavigation().moveTo(
                        this.attachmentPos.x, this.attachmentPos.y, this.attachmentPos.z, this.speed);
            }
            return;
        }

        if (this.activityTicks <= ATTACHED_IDLE_TICKS) return;

        this.woodpecker.setPecking(true);
        int peckingTicks = this.activityTicks - ATTACHED_IDLE_TICKS;
        if (peckingTicks % PECK_SOUND_INTERVAL_TICKS == PECK_SOUND_OFFSET_TICKS) {
            this.woodpecker.playSound(SoundEvents.WOOD_HIT, 0.5f, 0.9f + this.woodpecker.getRandom().nextFloat() * 0.2f);
            if (this.woodpecker.level() instanceof ServerLevel serverWorld) this.spawnPeckParticles(serverWorld);
        }
    }

    private void spawnPeckParticles(ServerLevel world) {
        if (this.targetLog == null || this.targetFace == null) return;
        BlockState log = world.getBlockState(this.targetLog);
        Vec3 particlePos = Vec3.atCenterOf(this.targetLog).add(
                this.targetFace.getStepX() * PECK_PARTICLE_DISTANCE_FROM_LOG_CENTER,
                0.0,
                this.targetFace.getStepZ() * PECK_PARTICLE_DISTANCE_FROM_LOG_CENTER);

        for (int i = 0; i < PECK_PARTICLE_COUNT; i++) {
            double x = particlePos.x + (this.woodpecker.getRandom().nextDouble() - 0.5) * 0.08;
            double y = this.woodpecker.getEyeY() + (this.woodpecker.getRandom().nextDouble() - 0.5) * 0.12;
            double z = particlePos.z + (this.woodpecker.getRandom().nextDouble() - 0.5) * 0.08;
            world.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, log), x, y, z, 0,
                    this.targetFace.getStepX() * 0.02, 0.01, this.targetFace.getStepZ() * 0.02, 1.0);
        }
    }

    private boolean findLog() {
        BlockPos origin = this.woodpecker.blockPosition();
        for (BlockPos logPos : BlockPos.withinBoxByManhattanDistance(origin, SEARCH_RADIUS, SEARCH_HEIGHT_BELOW, SEARCH_RADIUS)) {
            if (logPos.getY() > origin.getY() + SEARCH_HEIGHT_ABOVE) continue;
            if (!this.isValidLog(this.woodpecker.level().getBlockState(logPos))) continue;
            for (Direction face : Direction.Plane.HORIZONTAL) {
                if (!this.isValidFace(logPos, face)) continue;
                this.targetLog = logPos.immutable();
                this.targetFace = face;
                this.attachmentPos = this.getAttachmentPos(logPos, face);
                return true;
            }
        }
        this.scheduleNextSearch();
        return false;
    }

    private boolean isValidFace(BlockPos logPos, Direction face) {
        BlockState log = this.woodpecker.level().getBlockState(logPos);
        if (!this.isValidLog(log)) return false;

        BlockPos adjacent = logPos.relative(face);
        if (!this.woodpecker.level().getBlockState(adjacent).isAir()
                || !this.woodpecker.level().getBlockState(adjacent.above()).isAir()) return false;

        Vec3 attachmentPos = this.getAttachmentPos(logPos, face);
        AABB attachmentBox = this.woodpecker.getBoundingBox().move(attachmentPos.subtract(this.woodpecker.position()));
        return this.woodpecker.level().noCollision(this.woodpecker, attachmentBox);
    }

    private boolean isValidLog(BlockState log) {
        return log.is(BlockTags.LOGS) && log.hasProperty(RotatedPillarBlock.AXIS)
                && log.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y;
    }

    private Vec3 getAttachmentPos(BlockPos logPos, Direction face) {
        return Vec3.atCenterOf(logPos).add(
                face.getStepX() * ATTACHMENT_DISTANCE_FROM_LOG_CENTER,
                0.0,
                face.getStepZ() * ATTACHMENT_DISTANCE_FROM_LOG_CENTER);
    }

    private void scheduleNextSearch() {
        this.nextSearchTime = this.woodpecker.level().getGameTime()
                + this.woodpecker.getRandom().nextIntBetweenInclusive(MIN_SEARCH_DELAY_TICKS, MAX_SEARCH_DELAY_TICKS);
    }
}
