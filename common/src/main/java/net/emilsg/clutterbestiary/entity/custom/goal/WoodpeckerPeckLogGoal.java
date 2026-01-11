package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
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
    @Nullable private Vec3d attachmentPos;
    private int activityTicks;
    private long nextSearchTime;

    public WoodpeckerPeckLogGoal(WoodpeckerEntity woodpecker, double speed) {
        this.woodpecker = woodpecker;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.scheduleNextSearch();
    }

    @Override
    public boolean canStart() {
        if (this.woodpecker.getWorld().getTime() < this.nextSearchTime) return false;
        if (!this.woodpecker.isAlive() || this.woodpecker.isTouchingWater()) return false;
        return this.findLog();
    }

    @Override
    public boolean shouldContinue() {
        if (this.woodpecker.isPeckingInterrupted()) return false;
        if (!this.woodpecker.isAlive() || this.targetLog == null || this.targetFace == null || this.attachmentPos == null) return false;
        if (!this.isValidFace(this.targetLog, this.targetFace)) return false;
        return this.activityTicks < (this.woodpecker.isAttached() ? ATTACH_TICKS : MAX_APPROACH_TICKS);
    }

    @Override
    public void start() {
        this.activityTicks = 0;
        this.woodpecker.setFlying(true);
        if (this.attachmentPos != null) this.woodpecker.getNavigation().startMovingTo(
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
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.attachmentPos == null || this.targetFace == null) return;
        this.activityTicks++;
        if (!this.woodpecker.isAttached()) {
            if (this.woodpecker.squaredDistanceTo(this.attachmentPos) < 0.25) {
                this.woodpecker.attachToLog(this.attachmentPos, this.targetFace);
                this.activityTicks = 0;
            } else if (this.woodpecker.getNavigation().isIdle()) {
                this.woodpecker.getNavigation().startMovingTo(
                        this.attachmentPos.x, this.attachmentPos.y, this.attachmentPos.z, this.speed);
            }
            return;
        }

        if (this.activityTicks <= ATTACHED_IDLE_TICKS) return;

        this.woodpecker.setPecking(true);
        int peckingTicks = this.activityTicks - ATTACHED_IDLE_TICKS;
        if (peckingTicks % PECK_SOUND_INTERVAL_TICKS == PECK_SOUND_OFFSET_TICKS) {
            this.woodpecker.playSound(SoundEvents.BLOCK_WOOD_HIT, 0.5f, 0.9f + this.woodpecker.getRandom().nextFloat() * 0.2f);
            if (this.woodpecker.getWorld() instanceof ServerWorld serverWorld) this.spawnPeckParticles(serverWorld);
        }
    }

    private void spawnPeckParticles(ServerWorld world) {
        if (this.targetLog == null || this.targetFace == null) return;
        BlockState log = world.getBlockState(this.targetLog);
        Vec3d particlePos = Vec3d.ofCenter(this.targetLog).add(
                this.targetFace.getOffsetX() * PECK_PARTICLE_DISTANCE_FROM_LOG_CENTER,
                0.0,
                this.targetFace.getOffsetZ() * PECK_PARTICLE_DISTANCE_FROM_LOG_CENTER);

        for (int i = 0; i < PECK_PARTICLE_COUNT; i++) {
            double x = particlePos.x + (this.woodpecker.getRandom().nextDouble() - 0.5) * 0.08;
            double y = this.woodpecker.getEyeY() + (this.woodpecker.getRandom().nextDouble() - 0.5) * 0.12;
            double z = particlePos.z + (this.woodpecker.getRandom().nextDouble() - 0.5) * 0.08;
            world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, log), x, y, z, 0,
                    this.targetFace.getOffsetX() * 0.02, 0.01, this.targetFace.getOffsetZ() * 0.02, 1.0);
        }
    }

    private boolean findLog() {
        BlockPos origin = this.woodpecker.getBlockPos();
        for (BlockPos logPos : BlockPos.iterateOutwards(origin, SEARCH_RADIUS, SEARCH_HEIGHT_BELOW, SEARCH_RADIUS)) {
            if (logPos.getY() > origin.getY() + SEARCH_HEIGHT_ABOVE) continue;
            if (!this.isValidLog(this.woodpecker.getWorld().getBlockState(logPos))) continue;
            for (Direction face : Direction.Type.HORIZONTAL) {
                if (!this.isValidFace(logPos, face)) continue;
                this.targetLog = logPos.toImmutable();
                this.targetFace = face;
                this.attachmentPos = this.getAttachmentPos(logPos, face);
                return true;
            }
        }
        this.scheduleNextSearch();
        return false;
    }

    private boolean isValidFace(BlockPos logPos, Direction face) {
        BlockState log = this.woodpecker.getWorld().getBlockState(logPos);
        if (!this.isValidLog(log)) return false;

        BlockPos adjacent = logPos.offset(face);
        if (!this.woodpecker.getWorld().getBlockState(adjacent).isAir()
                || !this.woodpecker.getWorld().getBlockState(adjacent.up()).isAir()) return false;

        Vec3d attachmentPos = this.getAttachmentPos(logPos, face);
        Box attachmentBox = this.woodpecker.getBoundingBox().offset(attachmentPos.subtract(this.woodpecker.getPos()));
        return this.woodpecker.getWorld().isSpaceEmpty(this.woodpecker, attachmentBox);
    }

    private boolean isValidLog(BlockState log) {
        return log.isIn(BlockTags.LOGS) && log.contains(PillarBlock.AXIS)
                && log.get(PillarBlock.AXIS) == Direction.Axis.Y;
    }

    private Vec3d getAttachmentPos(BlockPos logPos, Direction face) {
        return Vec3d.ofCenter(logPos).add(
                face.getOffsetX() * ATTACHMENT_DISTANCE_FROM_LOG_CENTER,
                0.0,
                face.getOffsetZ() * ATTACHMENT_DISTANCE_FROM_LOG_CENTER);
    }

    private void scheduleNextSearch() {
        this.nextSearchTime = this.woodpecker.getWorld().getTime()
                + this.woodpecker.getRandom().nextBetween(MIN_SEARCH_DELAY_TICKS, MAX_SEARCH_DELAY_TICKS);
    }
}
