package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.DragonflyEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class DragonflyFastWanderGoal extends Goal {
    private final DragonflyEntity dragonflyEntity;

    public DragonflyFastWanderGoal(DragonflyEntity dragonflyEntity) {
        setControls(EnumSet.of(Goal.Control.MOVE));
        this.dragonflyEntity = dragonflyEntity;
    }

    public static int rayCastDown(World world, BlockPos start, int maxDepth) {
        for (int i = 1; i <= maxDepth; i++) {
            BlockPos checkPos = start.down(i);
            if (!world.getBlockState(checkPos).isAir()) {
                return i;
            }
        }
        return maxDepth;
    }

    @Override
    public boolean canStart() {
        return dragonflyEntity.getNavigation().isIdle() && dragonflyEntity.getRandom().nextInt(8) == 0;
    }

    @Override
    public boolean shouldContinue() {
        return dragonflyEntity.getNavigation().isFollowingPath();
    }

    public void start() {
        Vec3d vec3d = this.getRandomLocation();

        if (vec3d != null) {
            BlockPos pos = BlockPos.ofFloored(vec3d.x, vec3d.y, vec3d.z);
            double speed = dragonflyEntity.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED);

            Path path = dragonflyEntity.getNavigation().findPathTo(pos, 1);
            if (path != null) dragonflyEntity.getNavigation().startMovingAlong(path, speed);
        }
        super.start();
    }

    @Override
    public void tick() {
        if (dragonflyEntity.isTouchingWater()) this.stop();
    }

    private Vec3d getRandomLocation() {
        int blocksToGround = rayCastDown(dragonflyEntity.getWorld(), dragonflyEntity.getBlockPos(), 16);

        Vec3d dragonflyRotation = dragonflyEntity.getRotationVec(0.0F);
        Vec3d targetedPos = AboveGroundTargeting.find(dragonflyEntity, 8, 4, dragonflyRotation.x, dragonflyRotation.z, 1.5707964F, 4, 2);

        if (targetedPos != null) {
            if (blocksToGround > 5) {
                targetedPos = new Vec3d(targetedPos.x, targetedPos.y - blocksToGround * 0.5, targetedPos.z);
            }
            return targetedPos;
        }

        return NoPenaltySolidTargeting.find(dragonflyEntity, 8, 4, -2, dragonflyRotation.x, dragonflyRotation.z, ((float) Math.PI / 2F));
    }
}
