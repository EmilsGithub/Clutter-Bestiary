package net.emilsg.clutterbestiary.entity.client.model.parent;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.fish.AbstractFish;

public abstract class ParentFishModel<T extends AbstractFish> extends BestiaryEntityModel<T> {

    protected ParentFishModel(ModelPart root) {
        super(root);
    }

    @Override
    public abstract void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch);

    protected abstract ModelPart getHeadPart();
}
