package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.BoopletEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.BoopletEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BoopletModel<T extends BoopletEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart main;
    private final ModelPart fluff;

    public BoopletModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.main = this.body.getChild("main");
        this.fluff = this.main.getChild("fluff");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -5.0F, 0.0F));

        ModelPartData main = body.addChild("main", ModelPartBuilder.create().uv(31, 47).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.125F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData leftEar = main.addChild("leftEar", ModelPartBuilder.create().uv(0, 32).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -2.0F, 0.0F));

        ModelPartData rightEar = main.addChild("rightEar", ModelPartBuilder.create().uv(8, 32).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-4.0F, -2.0F, 0.0F));

        ModelPartData tail = main.addChild("tail", ModelPartBuilder.create().uv(34, 12).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(34, 28).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.125F)), ModelTransform.of(0.0F, 2.0F, 3.5F, -0.4363F, 0.0F, 0.0F));

        ModelPartData fluff = main.addChild("fluff", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData eyes = main.addChild("eyes", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 5.0F, 0.0F));

        ModelPartData leftEye = eyes.addChild("leftEye", ModelPartBuilder.create().uv(57, 48).cuboid(-1.0F, -1.0F, 0.2667F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(58, 47).cuboid(1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(54, 48).cuboid(-1.0F, -1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(58, 47).cuboid(-1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(56, 48).cuboid(-1.0F, 1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(57, 51).cuboid(-0.5F, -0.5F, 0.1667F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -5.0F, -3.2667F));

        ModelPartData rightEye = eyes.addChild("rightEye", ModelPartBuilder.create().uv(57, 48).cuboid(-1.0F, -1.0F, 0.2667F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(58, 47).cuboid(-1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(56, 48).cuboid(-1.0F, 1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(54, 48).cuboid(-1.0F, -1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(58, 47).cuboid(1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(57, 51).cuboid(-0.5F, -0.5F, 0.1667F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -5.0F, -3.2667F));

        ModelPartData leftHorn = body.addChild("leftHorn", ModelPartBuilder.create().uv(19, 38).cuboid(-1.0F, -1.5F, -3.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -3.5F, -2.0F));

        ModelPartData rightHorn = body.addChild("rightHorn", ModelPartBuilder.create().uv(33, 38).mirrored().cuboid(-1.0F, -1.5F, -3.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-4.0F, -3.5F, -2.0F));

        ModelPartData frontRightLeg = all.addChild("frontRightLeg", ModelPartBuilder.create().uv(10, 42).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -2.5F, -2.0F));

        ModelPartData frontLeftLeg = all.addChild("frontLeftLeg", ModelPartBuilder.create().uv(0, 42).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -2.5F, -2.0F));

        ModelPartData backRightLeg = all.addChild("backRightLeg", ModelPartBuilder.create().uv(10, 49).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -2.5F, 2.0F));

        ModelPartData backLeftLeg = all.addChild("backLeftLeg", ModelPartBuilder.create().uv(0, 49).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -2.5F, 2.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(BoopletEntity boopletEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        if (!boopletEntity.isTouchingWater()) {
            this.animateMovement(boopletEntity.isFleeing() ? BoopletEntityAnimations.BOOPLET_RUN : BoopletEntityAnimations.BOOPLET_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);
        }

        this.updateAnimation(boopletEntity.swimAnimationState, BoopletEntityAnimations.BOOPLET_SWIM, ageInTicks, 1f);
        this.updateAnimation(boopletEntity.boopAnimationState, BoopletEntityAnimations.BOOPLET_BOOP, ageInTicks, 1f);
        this.updateAnimation(boopletEntity.happyAnimationState, BoopletEntityAnimations.BOOPLET_HAPPY, ageInTicks, 1f);

        this.fluff.visible = boopletEntity.isFluffy();
    }

    @Override
    protected ModelPart getHeadPart() {
        return null;
    }
}