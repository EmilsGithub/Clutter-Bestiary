package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.EmperorPenguinEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.EmperorPenguinEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BabyEmperorPenguinModel<T extends EmperorPenguinEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart head;

    public BabyEmperorPenguinModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.head = all.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData All = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = All.addChild("body", ModelPartBuilder.create().uv(0, 12).cuboid(-3.0F, -2.0F, -2.0F, 6.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -8.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head = All.addChild("head", ModelPartBuilder.create().uv(0, 19).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(18, 20).cuboid(-0.5F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        ModelPartData leftWing = All.addChild("leftWing", ModelPartBuilder.create().uv(24, 0).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -8.0F, 0.0F));

        ModelPartData rightWing = All.addChild("rightWing", ModelPartBuilder.create().uv(24, 8).cuboid(-1.0F, 0.0F, -1.0F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -8.0F, 0.0F));

        ModelPartData leftLeg = All.addChild("leftLeg", ModelPartBuilder.create().uv(24, 21).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(-2, 27).cuboid(-1.0F, 1.0F, -3.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -1.0F, 0.0F));

        ModelPartData rightLeg = All.addChild("rightLeg", ModelPartBuilder.create().uv(24, 17).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(2, 27).cuboid(-1.0F, 1.0F, -3.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -1.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
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
    public void setAngles(EmperorPenguinEntity emperorPenguinEntity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(emperorPenguinEntity, netHeadYaw, headPitch, animationProgress);

        this.animateMovement(EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_WALK, limbSwing, limbSwingAmount, 1.5f, 1f);

        this.updateAnimation(emperorPenguinEntity.flapAnimationStateOne, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP, animationProgress, 1f);
        this.updateAnimation(emperorPenguinEntity.flapAnimationStateTwo, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP_TWO, animationProgress, 1f);
        this.updateAnimation(emperorPenguinEntity.preenAnimationState, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_PREEN, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}