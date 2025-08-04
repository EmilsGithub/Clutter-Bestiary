package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.ChameleonEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.ChameleonEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class ChameleonModel<T extends ChameleonEntity> extends ParentTameableModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart head;

    public ChameleonModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.5F, 2.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 14).cuboid(-1.5F, -5.0F, -4.0F, 3.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.5F, -2.0F));

        ModelPartData backCrest = body.addChild("backCrest", ModelPartBuilder.create().uv(20, 16).mirrored().cuboid(0.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -5.0F, -1.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 6).cuboid(-1.025F, -1.5F, -4.0F, 2.05F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(22, 9).cuboid(-1.025F, 0.5F, -1.0F, 2.05F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 17).mirrored().cuboid(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 2).cuboid(-1.5F, -1.51F, -3.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.5F, -4.0F));

        ModelPartData jaw = head.addChild("jaw", ModelPartBuilder.create().uv(16, 13).cuboid(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, -1.0F));

        ModelPartData tounge = head.addChild("tounge", ModelPartBuilder.create().uv(8, 2).cuboid(-0.5F, 0.75F, -3.0F, 1.0F, 0.5F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.25F, 0.0F));

        ModelPartData rightEye = head.addChild("rightEye", ModelPartBuilder.create().uv(14, 9).cuboid(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.75F, -0.5F, -2.0F));

        ModelPartData leftEye = head.addChild("leftEye", ModelPartBuilder.create().uv(14, 9).cuboid(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.75F, -0.5F, -2.0F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(0, 24).cuboid(-1.0F, -2.5F, 1.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 23).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.5F, 2.0F));

        ModelPartData frontRightLeg = body.addChild("frontRightLeg", ModelPartBuilder.create().uv(17, 28).cuboid(-0.25F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, -2.5F, -2.5F));

        ModelPartData backRightLeg = body.addChild("backRightLeg", ModelPartBuilder.create().uv(25, 28).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.25F, -2.5F, 0.5F));

        ModelPartData frontLeftLeg = body.addChild("frontLeftLeg", ModelPartBuilder.create().uv(13, 28).cuboid(-0.75F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, -2.5F, -2.5F));

        ModelPartData backLeftLeg = body.addChild("backLeftLeg", ModelPartBuilder.create().uv(21, 28).cuboid(-0.75F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, -2.5F, 0.5F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (this.child) {
            float babyScale = 0.5f;
            matrices.push();
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5D, 0D);
            this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            matrices.pop();
            this.head.scale(createVec3f(0.9f));
        } else {
            matrices.push();
            this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            matrices.pop();
        }
    }

    @Override
    public void setAngles(ChameleonEntity chameleon, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(chameleon, headYaw, headPitch, headPitch);
        if (!chameleon.isSitting()) {
            this.animateMovement(ChameleonEntityAnimations.CHAMELEON_WALK, limbAngle, limbDistance, 1.5f, 2f);
        } else {
            this.updateAnimation(chameleon.sittingAnimationState, ChameleonEntityAnimations.CHAMELEON_LAY_DOWN, animationProgress, 1.0f);
        }

        this.updateAnimation(chameleon.toungeIdleAnimationState, ChameleonEntityAnimations.CHAMELEON_LICK_IDLE, animationProgress, 1f);
        this.updateAnimation(chameleon.tailIdleAnimationState, ChameleonEntityAnimations.CHAMELEON_TAIL_IDLE, animationProgress, 1f);
    }

    protected void setHeadAngles(ChameleonEntity entity, float headYaw, float headPitch, float animationProgress) {
        if (getHeadPart() == null) return;

        boolean isAttacking = entity.isAttacking();

        float maxYaw = isAttacking ? 60.0F : 30.0F;
        float minYaw = -maxYaw;

        float maxPitch = isAttacking ? 60.0F : 45.0F;
        float minPitch = isAttacking ? -45.0F : -25.0F;

        headYaw = MathHelper.clamp(headYaw, minYaw, maxYaw);
        headPitch = MathHelper.clamp(headPitch, minPitch, maxPitch);

        getHeadPart().yaw = headYaw * 0.017453292F;
        getHeadPart().pitch = headPitch * 0.017453292F;
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}