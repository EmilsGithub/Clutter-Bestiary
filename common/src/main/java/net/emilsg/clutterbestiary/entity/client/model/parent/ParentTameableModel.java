package net.emilsg.clutterbestiary.entity.client.model.parent;

import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public abstract class ParentTameableModel<T extends ParentTameableEntity> extends SinglePartEntityModel<T> {

    public Vector3f createVec3f(float scale) {
        return new Vector3f(scale, scale, scale);
    }

    @Override
    public abstract void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch);

    protected abstract ModelPart getHeadPart();

    protected void setHeadAngles(LivingEntity entity, float headYaw, float headPitch, float animationProgress) {
        if (getHeadPart() == null) return;
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

        getHeadPart().yaw = headYaw * 0.017453292F;
        getHeadPart().pitch = headPitch * 0.017453292F;
    }

    protected void setBabyHeadSizeAndRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();

        if (this.child) {
            float babyScale = 0.5f;
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5D, 0D);
            this.getHeadPart().scale(createVec3f(0.6f));
        }

        this.getPart().render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }
}
