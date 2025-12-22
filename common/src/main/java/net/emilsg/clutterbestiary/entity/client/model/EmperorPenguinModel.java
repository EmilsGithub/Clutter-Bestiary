package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.EmperorPenguinEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.EmperorPenguinEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class EmperorPenguinModel<T extends EmperorPenguinEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart head;

    public EmperorPenguinModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.head = all.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 13.0F, 0.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 41).cuboid(-4.0F, -17.0F, -4.0F, 8.0F, 15.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 29).cuboid(-4.0F, -17.0F, -5.0F, 8.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(45, 17).cuboid(-3.0F, -2.0F, 2.0F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 11.0F, 0.0F));

        ModelPartData head = all.addChild("head", ModelPartBuilder.create().uv(38, 52).cuboid(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, -1.0F));

        ModelPartData beak = head.addChild("beak", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData upperBeak = beak.addChild("upperBeak", ModelPartBuilder.create().uv(32, 46).cuboid(-0.5F, -1.0F, -4.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0625F))
                .uv(1, 1).cuboid(-0.5F, 0.125F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0625F)), ModelTransform.pivot(0.0F, -2.0F, -3.0F));

        ModelPartData lowerBeak = beak.addChild("lowerBeak", ModelPartBuilder.create().uv(33, 42).cuboid(-0.5F, 0.0F, -3.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -3.0F));

        ModelPartData rightWing = all.addChild("rightWing", ModelPartBuilder.create().uv(43, 37).cuboid(-1.0F, 0.0F, -1.0F, 1.0F, 11.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -6.0F, -1.0F));

        ModelPartData leftWing = all.addChild("leftWing", ModelPartBuilder.create().uv(54, 37).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 11.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -6.0F, -1.0F));

        ModelPartData leftLeg = all.addChild("leftLeg", ModelPartBuilder.create().uv(47, 5).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(2, 13).cuboid(-1.5F, 1.95F, -4.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 9.0F, -0.5F));

        ModelPartData rightLeg = all.addChild("rightLeg", ModelPartBuilder.create().uv(2, 5).cuboid(-1.5F, 1.95F, -4.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(47, 11).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 9.0F, -0.5F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.getPart().render(matrices, vertices, light, overlay, color);
    }

    @Override
    public void setAngles(EmperorPenguinEntity emperorPenguinEntity, float limbSwing, float limbSwingAmount, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(emperorPenguinEntity, headYaw, headPitch, animationProgress);

        if (!emperorPenguinEntity.isTouchingWater()) {
            this.animateMovement(EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_WALK, limbSwing, limbSwingAmount, 2.0f, 2.5f);
        } else {
            this.animateMovement(EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_PADDLE, limbSwing, limbSwingAmount, 2.0f, 2.5f);
        }
        this.updateAnimation(emperorPenguinEntity.flapAnimationStateOne, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP, animationProgress, 1f);
        this.updateAnimation(emperorPenguinEntity.flapAnimationStateTwo, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP_TWO, animationProgress, 1f);
        this.updateAnimation(emperorPenguinEntity.preenAnimationState, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_PREEN, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}