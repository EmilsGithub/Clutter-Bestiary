package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.DragonflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DragonflyHoverLilypadGoal extends Goal {
    private final DragonflyEntity dragonFly;
    private final int scanRadius = 16;
    private final double approachSpeed = 1.8;
    private final int scanEvery = 20;
    private final int samples = 48;
    private final BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
    private final BlockPos.MutableBlockPos above = new BlockPos.MutableBlockPos();
    private BlockPos pad;
    private int hoverTicks;
    private int nextScan;

    public DragonflyHoverLilypadGoal(DragonflyEntity dragonFly) {
        this.dragonFly = dragonFly;
        this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (nextScan > 0) {
            nextScan--;
            return false;
        }
        nextScan = scanEvery;
        if (dragonFly.getRandom().nextInt(6) != 0) return false;

        final var world = dragonFly.level();
        final BlockPos origin = dragonFly.blockPosition();
        BlockPos best = null;
        double bestSq = Double.MAX_VALUE;

        for (int i = 0; i < samples; i++) {
            int dx = dragonFly.getRandom().nextIntBetweenInclusive(-scanRadius, scanRadius);
            int dz = dragonFly.getRandom().nextIntBetweenInclusive(-scanRadius, scanRadius);
            int dy = dragonFly.getRandom().nextIntBetweenInclusive(-2, 3);

            m.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
            if (!world.getBlockState(m).is(net.minecraft.world.level.block.Blocks.LILY_PAD)) continue;

            above.set(m.getX(), m.getY() + 1, m.getZ());
            if (!world.getBlockState(above).isAir()) continue;

            double dsq = above.distToCenterSqr(dragonFly.position());
            if (dsq < bestSq) {
                bestSq = dsq;
                best = m.immutable();
                if (dsq < 9.0) break;
            }
        }

        if (best != null) {
            pad = best;
            hoverTicks = dragonFly.getRandom().nextIntBetweenInclusive(40, 80);
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (hoverTicks <= 0 || pad == null) return false;
        return dragonFly.level().getBlockState(pad).is(net.minecraft.world.level.block.Blocks.LILY_PAD);
    }

    @Override
    public void start() {
        if (pad != null) {
            Vec3 tgt = Vec3.atCenterOf(pad).add(0, 0.6, 0);
            dragonFly.getNavigation().moveTo(tgt.x, tgt.y, tgt.z, approachSpeed);
        }
    }

    @Override
    public void stop() {
        pad = null;
        hoverTicks = 0;
    }

    @Override
    public void tick() {
        if (pad == null) return;

        Vec3 target = Vec3.atCenterOf(pad).add(0, 0.6, 0);
        Vec3 to = target.subtract(dragonFly.position());
        double dist = to.length();

        if (dist > 0.9) {
            if ((dragonFly.tickCount & 3) == 0)
                dragonFly.getNavigation().moveTo(target.x, target.y, target.z, approachSpeed);
            Vec3 dir = to.normalize();
            dragonFly.setDeltaMovement(dragonFly.getDeltaMovement().scale(0.6).add(dir.scale(0.16)));
        } else {
            double bob = Math.sin((dragonFly.tickCount) * 0.3) * 0.015;
            double jitter = (dragonFly.getRandom().nextDouble() - 0.5) * 0.02;
            Vec3 hold = target.add(jitter, bob, jitter).subtract(dragonFly.position()).scale(0.14);
            dragonFly.setDeltaMovement(dragonFly.getDeltaMovement().scale(0.7).add(hold));
        }

        dragonFly.getLookControl().setLookAt(target);
        hoverTicks--;
    }
}