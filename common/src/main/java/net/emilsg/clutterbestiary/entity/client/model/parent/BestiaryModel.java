package net.emilsg.clutterbestiary.entity.client.model.parent;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public abstract class BestiaryModel<T extends ParentAnimalEntity> extends BestiaryEntityModel<T> {

    protected BestiaryModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    protected abstract ModelPart getHeadPart();

    protected void setHeadAngles(LivingEntity entity, float headYaw, float headPitch, float animationProgress) {
        if (getHeadPart() == null) return;

        headYaw = Mth.clamp(headYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        getHeadPart().yRot = headYaw * 0.017453292F;
        getHeadPart().xRot = headPitch * 0.017453292F;
    }
}
