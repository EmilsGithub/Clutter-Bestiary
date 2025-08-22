package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.CapybaraEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class CapybaraModel<T extends CapybaraEntity> extends ParentTameableModel<T> {
    private final ModelPart all;
    private final ModelPart torso;
    private final ModelPart head;
    private final ModelPart root;

    public CapybaraModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.torso = this.all.getChild("torso");
        this.head = this.torso.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(2.25F, 19.5F, -4.5F));

        ModelPartData torso = all.addChild("torso", ModelPartBuilder.create(), ModelTransform.pivot(-2.25F, 4.5F, 3.5F));

        ModelPartData body = torso.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-4.5F, -4.5F, -7.0F, 9.0F, 9.0F, 14.0F, new Dilation(0.0F))
                .uv(0, 39).cuboid(-4.5F, -4.5F, -7.0F, 9.0F, 5.0F, 14.0F, new Dilation(0.125F))
                .uv(0, 61).cuboid(-4.5F, 4.5F, -7.0F, 9.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 60).cuboid(-4.5F, 4.5F, 7.0F, 9.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 49).cuboid(-4.5F, 4.5F, -7.0F, 0.0F, 1.0F, 14.0F, new Dilation(0.0F))
                .uv(0, 48).cuboid(4.5F, 4.5F, -7.0F, 0.0F, 1.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.5F, 1.0F));

        ModelPartData head = torso.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -7.5F, 6.0F, 6.0F, 9.0F, new Dilation(0.0F))
                .uv(33, 23).cuboid(-3.0F, -4.0F, -3.5F, 6.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -12.25F, -5.5F));

        ModelPartData rightEar = head.addChild("rightEar", ModelPartBuilder.create(), ModelTransform.pivot(-2.5F, -4.0F, 0.5F));

        ModelPartData rightEar_r1 = rightEar.addChild("rightEar_r1", ModelPartBuilder.create().uv(49, 19).cuboid(-2.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        ModelPartData leftEar = head.addChild("leftEar", ModelPartBuilder.create(), ModelTransform.pivot(2.5F, -4.0F, 0.5F));

        ModelPartData leftEar_r1 = leftEar.addChild("leftEar_r1", ModelPartBuilder.create().uv(33, 19).cuboid(0.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 1.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

        ModelPartData frontRightLeg = torso.addChild("frontRightLeg", ModelPartBuilder.create().uv(52, 0).cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.25F, -4.5F, -3.5F));

        ModelPartData frontLeftLeg = torso.addChild("frontLeftLeg", ModelPartBuilder.create().uv(40, 0).cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.25F, -4.5F, -3.5F));

        ModelPartData backLeftLeg = torso.addChild("backLeftLeg", ModelPartBuilder.create().uv(40, 8).cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.25F, -4.5F, 6.25F));

        ModelPartData backRightLeg = torso.addChild("backRightLeg", ModelPartBuilder.create().uv(52, 8).cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.25F, -4.5F, 6.25F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (this.child) {
            float babyScale = 0.5f;
            this.head.scale(createVec3f(babyScale));
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
    public void setAngles(CapybaraEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        if (!entity.isSleeping() && !entity.isForceSleeping())
            this.setHeadAngles(entity, netHeadYaw, headPitch, ageInTicks);

        if (!entity.isMoving() && (entity.isForceSleeping()) || entity.isSleeping()) {
            this.updateAnimation(entity.sleepingAnimationState, entity.sleeperType() == 0 ? CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_ONE : entity.sleeperType() == 1 ? CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_TWO : CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_THREE, ageInTicks, 1f);
        } else {
            this.animateMovement(CapybaraEntityAnimations.CAPYBARA_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);
        }

        this.updateAnimation(entity.earTwitchAnimationStateOne, CapybaraEntityAnimations.CAPYBARA_EAR_TWITCH_ONE, ageInTicks, 1f);
        this.updateAnimation(entity.earTwitchAnimationStateTwo, CapybaraEntityAnimations.CAPYBARA_EAR_TWITCH_TWO, ageInTicks, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
