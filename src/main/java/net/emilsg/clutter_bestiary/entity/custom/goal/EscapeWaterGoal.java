package net.emilsg.clutter_bestiary.entity.custom.goal;

import net.emilsg.clutter_bestiary.entity.custom.parent.ParentAnimalEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EscapeWaterGoal extends Goal {
    private final ParentAnimalEntity animalEntity;

    public EscapeWaterGoal(ParentAnimalEntity animalEntity) {
        this.animalEntity = animalEntity;
    }

    @Override
    public boolean canStart() {
        return animalEntity.isTouchingWater();
    }

    @Override
    public boolean shouldContinue() {
        return animalEntity.getNavigation().isFollowingPath();
    }

    @Override
    public void start() {
        Vec3d rotation = animalEntity.getRotationVec(0.0F);
        Vec3d targetedPos = AboveGroundTargeting.find(animalEntity, 8, 4, rotation.x, rotation.z, 1.5707964F, 3, 1);

        if (targetedPos != null) {
            targetedPos = new Vec3d(targetedPos.x, targetedPos.y + 4, targetedPos.z);
            BlockPos pos = BlockPos.ofFloored(targetedPos.x, targetedPos.y, targetedPos.z);

            Path path = animalEntity.getNavigation().findPathTo(pos, 1);
            if(path != null) animalEntity.getNavigation().startMovingAlong(path, 2);
        }
        super.start();
    }
}
