package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.animation_handling.animation_states.StoatEntityAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.StoatEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.StoatEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class StoatModel<T extends StoatEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<StoatEntity, StoatEntityAnimationState> ANIMATIONS = new AnimationBindings<StoatEntity, StoatEntityAnimationState>()
            .bind(StoatEntityAnimationState.LAYING_DOWN, StoatEntityAnimations.STOAT_LAYING_DOWN)
            .bind(StoatEntityAnimationState.SLEEPING, StoatEntityAnimations.STOAT_SLEEPING)
            .bind(StoatEntityAnimationState.STANDING_UP, StoatEntityAnimations.STOAT_STANDING_UP)
            .bind(StoatEntityAnimationState.SIT_START, StoatEntityAnimations.STOAT_SIT_START)
            .bind(StoatEntityAnimationState.SITTING, StoatEntityAnimations.STOAT_SITTING)
            .bind(StoatEntityAnimationState.SITTING_UP, StoatEntityAnimations.STOAT_SITTING_UP)
            .bind(StoatEntityAnimationState.SIT_END, StoatEntityAnimations.STOAT_SIT_END);
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart front;
    private final ModelPart neck;
    private final ModelPart head;

    public StoatModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.front = this.all.getChild("front");
        this.neck = this.front.getChild("neck");
        this.head = this.neck.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 22.5F, -2.0F));

        ModelPartData back = all.addChild("back", ModelPartBuilder.create().uv(1, 9).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 2.0F));

        ModelPartData tail = back.addChild("tail", ModelPartBuilder.create().uv(13, 15).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 4.0F));

        ModelPartData tailTip = tail.addChild("tailTip", ModelPartBuilder.create().uv(20, 17).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 3.0F));

        ModelPartData backLeftLeg = back.addChild("backLeftLeg", ModelPartBuilder.create().uv(0, 27).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.pivot(1.0F, 1.5F, 3.5F));

        ModelPartData backRightLeg = back.addChild("backRightLeg", ModelPartBuilder.create().uv(5, 27).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.pivot(-1.0F, 1.5F, 3.5F));

        ModelPartData front = all.addChild("front", ModelPartBuilder.create().uv(1, 1).cuboid(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 2.0F));

        ModelPartData frontLeftLeg = front.addChild("frontLeftLeg", ModelPartBuilder.create().uv(0, 23).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.pivot(1.0F, 1.5F, -2.5F));

        ModelPartData frontRightLeg = front.addChild("frontRightLeg", ModelPartBuilder.create().uv(5, 23).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.pivot(-1.0F, 1.5F, -2.5F));

        ModelPartData neck = front.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.5F, -4.0F));

        ModelPartData neck_r1 = neck.addChild("neck_r1", ModelPartBuilder.create().uv(16, 11).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.25F, -0.5F, -0.3927F, 0.0F, 0.0F));

        ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(0, 16).cuboid(-1.5F, -2.5F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(16, 0).cuboid(-1.0F, -1.5F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 9).cuboid(-0.5F, -1.5F, -3.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.5F, -1.5F));

        ModelPartData leftEar = head.addChild("leftEar", ModelPartBuilder.create().uv(16, 3).cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(1.25F, -2.25F, 0.25F, 0.0F, -0.7854F, 0.0F));

        ModelPartData rightEar = head.addChild("rightEar", ModelPartBuilder.create().uv(16, 6).cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(-1.25F, -2.25F, 0.25F, 0.0F, 0.7854F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
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
    public void setAngles(StoatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, netHeadYaw, headPitch, ageInTicks);

        this.animateMovement(entity.isFleeing() ? StoatEntityAnimations.STOAT_HOP_WALK : StoatEntityAnimations.STOAT_WALK, limbSwing, limbSwingAmount, 3.5f, 3.0f);

        this.updateAnimation(entity.rightEarTwitchAnimationState, StoatEntityAnimations.STOAT_RIGHT_EAR_TWITCH, ageInTicks, 1f);
        this.updateAnimation(entity.leftEarTwitchAnimationState, StoatEntityAnimations.STOAT_LEFT_EAR_TWITCH, ageInTicks, 1f);

        ANIMATIONS.apply(entity, entity.getAnimationController(), ageInTicks, this::updateAnimation);
    }

    @Override
    protected ModelPart getHeadPart() {
        return this.head;
    }
}