package net.emilsg.clutterbestiary.entity.client.model.parent;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.fish.WaterAnimal;

public abstract class BestiaryAquaticModel<T extends WaterAnimal> extends BestiaryEntityModel<T> {

    protected BestiaryAquaticModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
