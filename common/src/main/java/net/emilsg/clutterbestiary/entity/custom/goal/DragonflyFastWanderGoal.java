package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.DragonflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class DragonflyFastWanderGoal extends Goal {
    private final DragonflyEntity dragonflyEntity;

    public DragonflyFastWanderGoal(DragonflyEntity dragonflyEntity) {
        setFlags(EnumSet.of(Flag.MOVE));
        this.dragonflyEntity = dragonflyEntity;
    }

    public static int rayCastDown(Level world, BlockPos start, int maxDepth) {
        for (int i = 1; i <= maxDepth; i++) {
            BlockPos checkPos = start.below(i);
            if (!world.getBlockState(checkPos).isAir()) {
                return i;
            }
        }
        return maxDepth;
    }

    @Override
    public boolean canUse() {
        return dragonflyEntity.getNavigation().isDone() && dragonflyEntity.getRandom().nextInt(8) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return dragonflyEntity.getNavigation().isInProgress();
    }

    public void start() {
        Vec3 vec3d = this.getRandomLocation();

        if (vec3d != null) {
            BlockPos pos = BlockPos.containing(vec3d.x, vec3d.y, vec3d.z);
            double speed = dragonflyEntity.getAttributeValue(Attributes.FLYING_SPEED);

            Path path = dragonflyEntity.getNavigation().createPath(pos, 1);
            if (path != null) dragonflyEntity.getNavigation().moveTo(path, speed);
        }
        super.start();
    }

    @Override
    public void tick() {
        if (dragonflyEntity.isInWater()) this.stop();
    }

    private Vec3 getRandomLocation() {
        int blocksToGround = rayCastDown(dragonflyEntity.level(), dragonflyEntity.blockPosition(), 16);

        Vec3 dragonflyRotation = dragonflyEntity.getViewVector(0.0F);
        Vec3 targetedPos = HoverRandomPos.getPos(dragonflyEntity, 8, 4, dragonflyRotation.x, dragonflyRotation.z, 1.5707964F, 4, 2);

        if (targetedPos != null) {
            if (blocksToGround > 5) {
                targetedPos = new Vec3(targetedPos.x, targetedPos.y - blocksToGround * 0.5, targetedPos.z);
            }
            return targetedPos;
        }

        return AirAndWaterRandomPos.getPos(dragonflyEntity, 8, 4, -2, dragonflyRotation.x, dragonflyRotation.z, ((float) Math.PI / 2F));
    }
}
