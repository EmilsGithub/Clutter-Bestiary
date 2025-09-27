package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.DragonflyEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DragonflyHoverLilypadGoal extends Goal {
    private final DragonflyEntity dragonFly;
    private final int scanRadius = 16;
    private final double approachSpeed = 1.8;
    private final int scanEvery = 20;
    private final int samples = 48;
    private final BlockPos.Mutable m = new BlockPos.Mutable();
    private final BlockPos.Mutable above = new BlockPos.Mutable();
    private BlockPos pad;
    private int hoverTicks;
    private int nextScan;

    public DragonflyHoverLilypadGoal(DragonflyEntity dragonFly) {
        this.dragonFly = dragonFly;
        this.setControls(java.util.EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (nextScan > 0) { nextScan--; return false; }
        nextScan = scanEvery;
        if (dragonFly.getRandom().nextInt(6) != 0) return false;

        final var world = dragonFly.getWorld();
        final BlockPos origin = dragonFly.getBlockPos();
        BlockPos best = null;
        double bestSq = Double.MAX_VALUE;

        for (int i = 0; i < samples; i++) {
            int dx = dragonFly.getRandom().nextBetween(-scanRadius, scanRadius);
            int dz = dragonFly.getRandom().nextBetween(-scanRadius, scanRadius);
            int dy = dragonFly.getRandom().nextBetween(-2, 3);

            m.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
            if (!world.getBlockState(m).isOf(net.minecraft.block.Blocks.LILY_PAD)) continue;

            above.set(m.getX(), m.getY() + 1, m.getZ());
            if (!world.getBlockState(above).isAir()) continue;

            double dsq = above.getSquaredDistance(dragonFly.getPos());
            if (dsq < bestSq) {
                bestSq = dsq;
                best = m.toImmutable();
                if (dsq < 9.0) break;
            }
        }

        if (best != null) {
            pad = best;
            hoverTicks = dragonFly.getRandom().nextBetween(40, 80);
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (hoverTicks <= 0 || pad == null) return false;
        return dragonFly.getWorld().getBlockState(pad).isOf(net.minecraft.block.Blocks.LILY_PAD);
    }

    @Override
    public void start() {
        if (pad != null) {
            Vec3d tgt = pad.toCenterPos().add(0, 0.6, 0);
            dragonFly.getNavigation().startMovingTo(tgt.x, tgt.y, tgt.z, approachSpeed);
        }
    }

    @Override
    public void stop() { pad = null; hoverTicks = 0; }

    @Override
    public void tick() {
        if (pad == null) return;

        Vec3d target = pad.toCenterPos().add(0, 0.6, 0);
        Vec3d to = target.subtract(dragonFly.getPos());
        double dist = to.length();

        if (dist > 0.9) {
            if ((dragonFly.age & 3) == 0)
                dragonFly.getNavigation().startMovingTo(target.x, target.y, target.z, approachSpeed);
            Vec3d dir = to.normalize();
            dragonFly.setVelocity(dragonFly.getVelocity().multiply(0.6).add(dir.multiply(0.16)));
        } else {
            double bob = Math.sin((dragonFly.age) * 0.3) * 0.015;
            double jitter = (dragonFly.getRandom().nextDouble() - 0.5) * 0.02;
            Vec3d hold = target.add(jitter, bob, jitter).subtract(dragonFly.getPos()).multiply(0.14);
            dragonFly.setVelocity(dragonFly.getVelocity().multiply(0.7).add(hold));
        }

        dragonFly.getLookControl().lookAt(target);
        hoverTicks--;
    }
}