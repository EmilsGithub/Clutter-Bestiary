package net.emilsg.clutterbestiary.entity.client.model.parent;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public abstract class ParentTameableModel<T extends ParentTameableEntity> extends BestiaryEntityModel<T> {
    // Shared baby look: half-size body with an enlarged head (formerly setBabyHeadSizeAndRender).
    protected static final float DEFAULT_BABY_SCALE = 0.5F;
    protected static final float DEFAULT_BABY_Y_OFFSET = 1.5F;

    protected ParentTameableModel(ModelPart root) {
        super(root);
    }

    @Override
    public abstract void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch);

    protected abstract ModelPart getHeadPart();

    protected void setHeadAngles(LivingEntity entity, float headYaw, float headPitch, float animationProgress) {
        if (getHeadPart() == null) return;
        headYaw = Mth.clamp(headYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        getHeadPart().yRot = headYaw * 0.017453292F;
        getHeadPart().xRot = headPitch * 0.017453292F;
    }
}
