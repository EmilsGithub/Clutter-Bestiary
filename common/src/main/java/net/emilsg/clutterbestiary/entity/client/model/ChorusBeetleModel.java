package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.animation_handling.animation_states.ChorusBeetleAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.CapybaraEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.animation.ChorusBeetleAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class ChorusBeetleModel<T extends ChorusBeetleEntity> extends BestiaryModel<T> {
    private static final float TURN_LEG_ANIMATION_SPEED = 0.8f;
    private static final float TURN_LEG_ANIMATION_AMOUNT = 0.5f;
    private static final float FULL_TURN_RATE = 15.0f;
    private static final AnimationBindings<ChorusBeetleEntity, ChorusBeetleAnimationState> ANIMATIONS = new AnimationBindings<ChorusBeetleEntity, ChorusBeetleAnimationState>()
            .bind(ChorusBeetleAnimationState.WALKING, ChorusBeetleAnimations.CHORUS_BEETLE_WALK)
            .bind(ChorusBeetleAnimationState.FLYING, ChorusBeetleAnimations.CHORUS_BEETLE_FLY)
            .bind(ChorusBeetleAnimationState.HOVERING, ChorusBeetleAnimations.CHORUS_BEETLE_HOVER)
            .bind(ChorusBeetleAnimationState.LANDING, ChorusBeetleAnimations.CHORUS_BEETLE_LAND, ChorusBeetleEntity::getLandingAnimationSpeed);

    private final ModelPart root;
    private final ModelPart all;
	private final ModelPart body;
	private final ModelPart leftWing;
	private final ModelPart leftWingOuter;
	private final ModelPart rightWing;
	private final ModelPart rightWingOuter;
	private final ModelPart rightElytra;
	private final ModelPart leftElytra;
	private final ModelPart head;
	private final ModelPart rightAntenna;
	private final ModelPart leftAntenna;
	private final ModelPart frontLeftLeg;
	private final ModelPart frontRightLeg;
	private final ModelPart middleLeftLeg;
	private final ModelPart middleRightLeg;
	private final ModelPart backLeftLeg;
	private final ModelPart backRightLeg;
	private final ModelPart tail;

    public ChorusBeetleModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.leftWing = this.body.getChild("leftWing");
		this.leftWingOuter = this.leftWing.getChild("leftWingOuter");
		this.rightWing = this.body.getChild("rightWing");
		this.rightWingOuter = this.rightWing.getChild("rightWingOuter");
		this.rightElytra = this.body.getChild("rightElytra");
		this.leftElytra = this.body.getChild("leftElytra");
		this.head = this.body.getChild("head");
		this.rightAntenna = this.head.getChild("rightAntenna");
		this.leftAntenna = this.head.getChild("leftAntenna");
		this.frontLeftLeg = this.all.getChild("frontLeftLeg");
		this.frontRightLeg = this.all.getChild("frontRightLeg");
		this.middleLeftLeg = this.all.getChild("middleLeftLeg");
		this.middleRightLeg = this.all.getChild("middleRightLeg");
		this.backLeftLeg = this.all.getChild("backLeftLeg");
		this.backRightLeg = this.all.getChild("backRightLeg");
		this.tail = this.all.getChild("tail");
	}

    public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 21.5F, 0.5F));

		ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -5.0F, 4.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.5F, 2.5F));

		ModelPartData leftWing = body.addChild("leftWing", ModelPartBuilder.create().uv(0, 30).cuboid(-2.0F, 0.0F, -1.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData leftWingOuter = leftWing.addChild("leftWingOuter", ModelPartBuilder.create().uv(15, 30).cuboid(-2.5F, 0.0F, 0.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 0.0F, 3.0F));

		ModelPartData rightWing = body.addChild("rightWing", ModelPartBuilder.create().uv(30, 17).cuboid(-1.0F, 0.0F, -1.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData rightWingOuter = rightWing.addChild("rightWingOuter", ModelPartBuilder.create().uv(30, 22).cuboid(-0.5F, 0.0F, 0.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 0.0F, 3.0F));

		ModelPartData rightElytra = body.addChild("rightElytra", ModelPartBuilder.create().uv(15, 17).cuboid(-1.75F, 0.0F, -0.5F, 2.0F, 2.0F, 5.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.5F, -3.0F, -4.5F));

		ModelPartData leftElytra = body.addChild("leftElytra", ModelPartBuilder.create().uv(0, 17).cuboid(-0.25F, 0.0F, -0.5F, 2.0F, 2.0F, 5.0F, new Dilation(0.25F)), ModelTransform.pivot(-0.5F, -3.0F, -4.5F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(19, 0).cuboid(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -1.0F, -5.0F));

		ModelPartData rightAntenna = head.addChild("rightAntenna", ModelPartBuilder.create().uv(0, 25).cuboid(-2.0F, 0.0F, -3.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, -2.0F));

		ModelPartData leftAntenna = head.addChild("leftAntenna", ModelPartBuilder.create().uv(15, 25).cuboid(-1.0F, 0.0F, -3.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 0.0F, -2.0F));

		ModelPartData frontLeftLeg = all.addChild("frontLeftLeg", ModelPartBuilder.create().uv(30, 27).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData frontRightLeg = all.addChild("frontRightLeg", ModelPartBuilder.create().uv(32, 10).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 1.0F, -2.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData middleLeftLeg = all.addChild("middleLeftLeg", ModelPartBuilder.create().uv(30, 31).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData middleRightLeg = all.addChild("middleRightLeg", ModelPartBuilder.create().uv(34, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData backLeftLeg = all.addChild("backLeftLeg", ModelPartBuilder.create().uv(32, 6).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, 2.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData backRightLeg = all.addChild("backRightLeg", ModelPartBuilder.create().uv(0, 35).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 1.0F, 2.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData tail = all.addChild("tail", ModelPartBuilder.create().uv(0, 9).cuboid(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 4.0F, new Dilation(-0.01F))
		.uv(17, 9).cuboid(-1.5F, -2.0F, 0.5F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -1.0F, 2.5F));
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
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.translate(0.0D, 1.5D, 0.0D);
        }
        this.getPart().render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, netHeadYaw, headPitch, animationProgress);
        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::updateAnimation);

		this.updateAnimation(entity.mandibleNibbleAnimationState, ChorusBeetleAnimations.CHORUS_BEETLE_MANDIBLE_NIBBLE, animationProgress, 1f);
		this.updateAnimation(entity.wingFlickAnimationState, ChorusBeetleAnimations.CHORUS_BEETLE_WING_FLICK, animationProgress, 1f);

        if (entity.getAnimationController().getState() == ChorusBeetleAnimationState.WALKING) {
            float swing = MathHelper.cos(limbSwing * 2.5f) * limbSwingAmount * 0.9f;
            this.frontLeftLeg.pitch += swing;
            this.middleLeftLeg.pitch -= swing;
            this.backLeftLeg.pitch += swing;
            this.frontRightLeg.pitch += swing;
            this.middleRightLeg.pitch -= swing;
            this.backRightLeg.pitch += swing;
        }

		if (entity.getAnimationController().getState() != ChorusBeetleAnimationState.FLYING && entity.getAnimationController().getState() != ChorusBeetleAnimationState.HOVERING) {
        	float turnAmount = MathHelper.clamp(MathHelper.wrapDegrees(entity.bodyYaw - entity.prevBodyYaw) / FULL_TURN_RATE, -1.0f, 1.0f);
        	float turnSwing = MathHelper.cos(animationProgress * TURN_LEG_ANIMATION_SPEED) * turnAmount * TURN_LEG_ANIMATION_AMOUNT;
        	this.frontLeftLeg.pitch += turnSwing;
        	this.middleLeftLeg.pitch -= turnSwing;
        	this.backLeftLeg.pitch += turnSwing;
        	this.frontRightLeg.pitch -= turnSwing;
        	this.middleRightLeg.pitch += turnSwing;
        	this.backRightLeg.pitch -= turnSwing;
		}
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
