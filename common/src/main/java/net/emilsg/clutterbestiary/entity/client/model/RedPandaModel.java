package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.RedPandaAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class RedPandaModel<T extends RedPandaEntity> extends ParentTameableModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart head;
    private float dropPitch;

    public RedPandaModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 19.0F, 2.5F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -3.0F));

        ModelPartData torso = body.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -4.5F, 4.0F, 4.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tailOne = body.addChild("tailOne", ModelPartBuilder.create().uv(19, 14).cuboid(-1.5F, -1.5F, -0.15F, 3.0F, 3.0F, 5.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, -0.5F, 4.15F, 0.1745F, 0.0F, 0.0F));

        ModelPartData tailTwo = tailOne.addChild("tailTwo", ModelPartBuilder.create().uv(0, 23).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 4.8F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 14).cuboid(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, -4.5F));

        ModelPartData snout = head.addChild("snout", ModelPartBuilder.create().uv(17, 30).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, -4.5F));

        ModelPartData rightEar = head.addChild("rightEar", ModelPartBuilder.create().uv(0, 32).cuboid(-1.5F, -1.5F, -1.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -1.5F, -2.0F));

        ModelPartData leftEar = head.addChild("leftEar", ModelPartBuilder.create().uv(26, 30).cuboid(-0.5F, -1.5F, -1.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -1.5F, -2.0F));

        ModelPartData frontLeftLeg = all.addChild("frontLeftLeg", ModelPartBuilder.create().uv(17, 23).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 1.0F, -6.0F));

        ModelPartData frontRightLeg = all.addChild("frontRightLeg", ModelPartBuilder.create().uv(26, 23).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 1.0F, -6.0F));

        ModelPartData backLeftLeg = all.addChild("backLeftLeg", ModelPartBuilder.create().uv(27, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 1.0F, 0.0F));

        ModelPartData backRightLeg = all.addChild("backRightLeg", ModelPartBuilder.create().uv(27, 7).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 1.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.setBabyHeadSizeAndRender(matrices, vertices, light, overlay, color);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, headYaw, headPitch, animationProgress);

        this.animateMovement(RedPandaAnimations.RED_PANDA_WALK, limbAngle, limbDistance, 3f, 2f);

        this.updateAnimation(entity.layingDownAnimationState, RedPandaAnimations.RED_PANDA_LAY_DOWN, animationProgress, 1f);
        this.updateAnimation(entity.sleepingAnimationState, RedPandaAnimations.RED_PANDA_SLEEP, animationProgress, 1f);
        this.updateAnimation(entity.standingUpAnimationState, RedPandaAnimations.RED_PANDA_STAND_UP, animationProgress, 1f);
        this.updateAnimation(entity.startingYPoseAnimationState, RedPandaAnimations.RED_PANDA_Y_POSE_START, animationProgress, 1f);
        this.updateAnimation(entity.yPosingAnimationState, RedPandaAnimations.RED_PANDA_Y_POSING, animationProgress, 1f);
        this.updateAnimation(entity.endingYPoseAnimationState, RedPandaAnimations.RED_PANDA_Y_POSE_END, animationProgress, 1f);
        this.updateAnimation(entity.rightEarTwitchAnimationState, RedPandaAnimations.RED_PANDA_RIGHT_EAR_TWITCH, animationProgress, 1f);
        this.updateAnimation(entity.leftEarTwitchAnimationState, RedPandaAnimations.RED_PANDA_LEFT_EAR_TWITCH, animationProgress, 1f);
        this.updateAnimation(entity.sniffAnimationState, RedPandaAnimations.RED_PANDA_SNIFF, animationProgress, 1f);
        this.updateAnimation(entity.sitStartAnimationState, RedPandaAnimations.RED_PANDA_SIT_START, animationProgress, 1f);
        this.updateAnimation(entity.sitEndAnimationState, RedPandaAnimations.RED_PANDA_SIT_END, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return this.head;
    }
}
