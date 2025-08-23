package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.BeaverEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BeaverModel<T extends BeaverEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart head;

    public BeaverModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 4.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -2.0F, -10.0F, 8.0F, 7.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData frontRightLeg = all.addChild("frontRightLeg", ModelPartBuilder.create().uv(0, 31).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 5.0F, -8.0F));

        ModelPartData backRightLeg = all.addChild("backRightLeg", ModelPartBuilder.create().uv(28, 4).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 5.0F, 0.0F));

        ModelPartData rightFlipper = backRightLeg.addChild("rightFlipper", ModelPartBuilder.create().uv(14, 19).cuboid(-1.5F, -0.125F, -2.5F, 3.0F, 0.25F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 1.865F, -0.5F));

        ModelPartData backLeftLeg = all.addChild("backLeftLeg", ModelPartBuilder.create().uv(0, 24).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 5.0F, 0.0F));

        ModelPartData leftFlipper = backLeftLeg.addChild("leftFlipper", ModelPartBuilder.create().uv(10, 19).cuboid(-1.5F, -0.125F, -2.5F, 3.0F, 0.25F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 1.865F, -0.5F));

        ModelPartData frontLeftLeg = all.addChild("frontLeftLeg", ModelPartBuilder.create().uv(0, 19).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 5.0F, -8.0F));

        ModelPartData tail = all.addChild("tail", ModelPartBuilder.create().uv(0, 19).cuboid(-4.0F, 0.0F, 1.0F, 8.0F, 2.0F, 10.0F, new Dilation(0.0F))
                .uv(28, 0).cuboid(-3.0F, 0.0F, -1.0F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 3.0F));

        ModelPartData head = all.addChild("head", ModelPartBuilder.create().uv(0, 9).cuboid(-1.0F, 2.0F, -5.75F, 2.0F, 1.0F, 0.25F, new Dilation(0.0F))
                .uv(26, 19).cuboid(-3.0F, -2.0F, -5.0F, 6.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(-2.0F, 0.0F, -6.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, -10.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        if (this.child) {
            float babyScale = 0.5f;
            this.head.scale(createVec3f(babyScale));
            matrices.push();
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5f, 0D);
            this.getPart().render(matrices, vertices, light, overlay, color);
            matrices.pop();
            this.head.scale(createVec3f(0.9f));
        } else {
            matrices.push();
            this.getPart().render(matrices, vertices, light, overlay, color);
            matrices.pop();
        }
    }

    @Override
    public void setAngles(BeaverEntity beaverEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(beaverEntity, netHeadYaw, headPitch, ageInTicks);
        boolean isTouchingWater = beaverEntity.isTouchingWater();

        if (!isTouchingWater) {
            this.animateMovement(BeaverEntityAnimations.BEAVER_WALK, limbSwing, limbSwingAmount, 2.0f, 2.5f);
            this.updateAnimation(beaverEntity.idleAnimationState, BeaverEntityAnimations.BEAVER_IDLE, ageInTicks, 1.0f);
        } else {
            float animationSpeed = (float) (beaverEntity.getVelocity().length() * 5) + Math.abs(0.5f);
            if (animationSpeed >= 1.2f) animationSpeed = 1.2f;
            this.updateAnimation(beaverEntity.waterAnimationState, BeaverEntityAnimations.BEAVER_SWIM, ageInTicks, animationSpeed);
        }

    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}