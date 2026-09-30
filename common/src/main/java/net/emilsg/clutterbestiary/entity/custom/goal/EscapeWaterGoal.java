package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class EscapeWaterGoal extends Goal {
    private final ParentAnimalEntity animalEntity;

    public EscapeWaterGoal(ParentAnimalEntity animalEntity) {
        this.animalEntity = animalEntity;
    }

    @Override
    public boolean canUse() {
        return animalEntity.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        return animalEntity.getNavigation().isInProgress();
    }

    @Override
    public void start() {
        Vec3 rotation = animalEntity.getViewVector(0.0F);
        Vec3 targetedPos = HoverRandomPos.getPos(animalEntity, 8, 4, rotation.x, rotation.z, 1.5707964F, 3, 1);

        if (targetedPos != null) {
            targetedPos = new Vec3(targetedPos.x, targetedPos.y + 4, targetedPos.z);
            BlockPos pos = BlockPos.containing(targetedPos.x, targetedPos.y, targetedPos.z);

            Path path = animalEntity.getNavigation().createPath(pos, 1);
            if (path != null) animalEntity.getNavigation().moveTo(path, 2);
        }
        super.start();
    }
}
