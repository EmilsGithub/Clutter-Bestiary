package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.RiverTurtleAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class RiverTurtleModel<T extends RiverTurtleEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart tail;

    public RiverTurtleModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.tail = this.body.getChild("tail");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 24.0F, 3.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, -2.0F, 0.0F));

        ModelPartData frontLegs = body.addChild("frontLegs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, -5.25F));

        ModelPartData frontRightLeg = frontLegs.addChild("frontRightLeg", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 0.0F, -1.01F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -1.0F, 0.0F));

        ModelPartData frontRightLegToes = frontRightLeg.addChild("frontRightLegToes", ModelPartBuilder.create().uv(1, 13).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 1.99F, -1.0F));

        ModelPartData frontLeftLeg = frontLegs.addChild("frontLeftLeg", ModelPartBuilder.create().uv(0, 4).cuboid(-1.0F, 0.0F, -1.01F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -1.0F, 0.0F));

        ModelPartData frontLeftLegToes = frontLeftLeg.addChild("frontLeftLegToes", ModelPartBuilder.create().uv(-1, 35).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.99F, -1.0F));

        ModelPartData backLegs = body.addChild("backLegs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.25F));

        ModelPartData backRightLeg = backLegs.addChild("backRightLeg", ModelPartBuilder.create().uv(8, 21).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(-3.0F, 0.0F, 0.0F));

        ModelPartData backRightLegToes = backRightLeg.addChild("backRightLegToes", ModelPartBuilder.create().uv(1, 15).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.99F, -1.0F));

        ModelPartData backLeftLeg = backLegs.addChild("backLeftLeg", ModelPartBuilder.create().uv(16, 21).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(3.0F, 0.0F, 0.0F));

        ModelPartData backLeftLegToes = backLeftLeg.addChild("backLeftLegToes", ModelPartBuilder.create().uv(1, 17).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.99F, -1.0F));

        ModelPartData stomach = body.addChild("stomach", ModelPartBuilder.create().uv(0, 12).cuboid(-5.0F, -3.0F, -6.0F, 6.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 2.0F, 0.0F));

        ModelPartData shell = body.addChild("shell", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -2.5F, -4.5F, 9.0F, 3.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, -2.5F));

        ModelPartData neck = body.addChild("neck", ModelPartBuilder.create().uv(19, 12).cuboid(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -6.0F));

        ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(0, 21).cuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.125F)), ModelTransform.pivot(0.0F, -1.0F, -3.0F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(19, 27).cuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, 1.0F));
        return TexturedModelData.of(modelData, 64, 64);
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
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, headYaw, headPitch, animationProgress);

        if (!entity.isHiding() && !entity.isTouchingWater()) {
            this.animateMovement(RiverTurtleAnimations.RIVER_TURTLE_WALK, limbAngle, limbDistance, 3f, 2f);
        } else if (entity.isTouchingWater()) {
            this.animateMovement(RiverTurtleAnimations.RIVER_TURTLE_SWIM, limbAngle, limbDistance, 3f, 2f);
        }

        this.updateAnimation(entity.hidingAnimationState, RiverTurtleAnimations.RIVER_TURTLE_HIDE, animationProgress, 1.0f);
        this.updateAnimation(entity.unhidingAnimationState, RiverTurtleAnimations.RIVER_TURTLE_UNHIDE, animationProgress, 1.0f);
        this.updateAnimation(entity.sitStartAnimationState, RiverTurtleAnimations.RIVER_TURTLE_SIT_START, animationProgress, 1.0f);
        this.updateAnimation(entity.sitEndAnimationState, RiverTurtleAnimations.RIVER_TURTLE_SIT_END, animationProgress, 1.0f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return neck;
    }
}
