package net.emilsg.clutter_bestiary.entity.client.model.parent;

import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.WaterCreatureEntity;
import org.joml.Vector3f;

public abstract class BestiaryAquaticModel<T extends WaterCreatureEntity> extends SinglePartEntityModel<T> {

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    public Vector3f createVec3f(float scale) {
        return new Vector3f(scale, scale, scale);
    }
}
