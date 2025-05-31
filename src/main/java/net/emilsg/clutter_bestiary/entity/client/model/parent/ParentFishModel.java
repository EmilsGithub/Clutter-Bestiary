package net.emilsg.clutter_bestiary.entity.client.model.parent;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.passive.FishEntity;
import org.joml.Vector3f;

public abstract class ParentFishModel<T extends FishEntity> extends SinglePartEntityModel<T> {

    public Vector3f createVec3f(float scale) {
        return new Vector3f(scale, scale, scale);
    }

    @Override
    public abstract void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch);

    protected abstract ModelPart getHeadPart();
}
