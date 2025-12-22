package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.KiwiBirdEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class KiwiBirdModel<T extends KiwiBirdEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart head;

    public KiwiBirdModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.0F, 0.5F));

        ModelPartData torso = all.addChild("torso", ModelPartBuilder.create().uv(13, 8).cuboid(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(13, 21).cuboid(-2.0F, -2.0F, -2.5F, 4.0F, 5.0F, 5.0F, new Dilation(0.125F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tail_r1 = torso.addChild("tail_r1", ModelPartBuilder.create().uv(1, 26).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 2.5F, -0.1309F, 0.0F, 0.0F));

        ModelPartData leftLeg = all.addChild("leftLeg", ModelPartBuilder.create().uv(13, 21).cuboid(-0.5F, 3.01F, -1.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 18).cuboid(-0.5F, 1.01F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(1, 21).cuboid(-1.0F, -0.74F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 1.0F, 0.5F));

        ModelPartData rightLeg = all.addChild("rightLeg", ModelPartBuilder.create().uv(10, 21).cuboid(-0.5F, 3.01F, -1.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(14, 18).cuboid(-0.5F, 1.01F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(1, 16).cuboid(-1.0F, -0.74F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 1.0F, 0.5F));

        ModelPartData head = all.addChild("head", ModelPartBuilder.create().uv(1, 6).cuboid(-1.5F, -1.5F, -2.75F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.75F, -2.0F));

        ModelPartData innerBeak_r1 = head.addChild("innerBeak_r1", ModelPartBuilder.create().uv(8, 1).cuboid(-0.5F, -0.75F, -1.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 3).cuboid(-0.5F, 0.25F, -1.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 1).cuboid(-0.5F, -0.75F, -3.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, -2.25F, 0.0873F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();

        if (this.child) {
            float babyScale = 0.5f;
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5D, 0D);
            this.head.scale(createVec3f(0.6f));
        }

        this.getPart().render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    @Override
    public void setAngles(KiwiBirdEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, netHeadYaw, headPitch, animationProgress);


        this.animateMovement(KiwiBirdEntityAnimations.KIWI_WALK, limbSwing, limbSwingAmount, 3f, 2f);

        this.updateAnimation(entity.idleAnimationState, KiwiBirdEntityAnimations.KIWI_IDLE, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}