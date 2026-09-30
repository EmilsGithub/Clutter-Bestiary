package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.ChorusBeetleAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.CapybaraEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.animation.ChorusBeetleAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ChorusBeetleModel<T extends ChorusBeetleEntity> extends BestiaryModel<T> {
    private static final float TURN_LEG_ANIMATION_SPEED = 0.8f;
    private static final float TURN_LEG_ANIMATION_AMOUNT = 0.5f;
    private static final float FULL_TURN_RATE = 15.0f;
    private static final AnimationBindings<ChorusBeetleEntity, ChorusBeetleAnimationState> ANIMATIONS = new AnimationBindings<ChorusBeetleEntity, ChorusBeetleAnimationState>()
            .bind(ChorusBeetleAnimationState.WALKING, ChorusBeetleAnimations.CHORUS_BEETLE_WALK)
            .bind(ChorusBeetleAnimationState.FLYING, ChorusBeetleAnimations.CHORUS_BEETLE_FLY)
            .bind(ChorusBeetleAnimationState.HOVERING, ChorusBeetleAnimations.CHORUS_BEETLE_HOVER)
            .bind(ChorusBeetleAnimationState.LANDING, ChorusBeetleAnimations.CHORUS_BEETLE_LAND, ChorusBeetleEntity::getLandingAnimationSpeed);

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
        super(root);
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

    public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(1.0F, 21.5F, 0.5F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 2.5F));

		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition leftWingOuter = leftWing.addOrReplaceChild("leftWingOuter", CubeListBuilder.create().texOffs(15, 30).addBox(-2.5F, 0.0F, 0.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 3.0F));

		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(30, 17).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition rightWingOuter = rightWing.addOrReplaceChild("rightWingOuter", CubeListBuilder.create().texOffs(30, 22).addBox(-0.5F, 0.0F, 0.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 3.0F));

		PartDefinition rightElytra = body.addOrReplaceChild("rightElytra", CubeListBuilder.create().texOffs(15, 17).addBox(-1.75F, 0.0F, -0.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.5F, -3.0F, -4.5F));

		PartDefinition leftElytra = body.addOrReplaceChild("leftElytra", CubeListBuilder.create().texOffs(0, 17).addBox(-0.25F, 0.0F, -0.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(-0.5F, -3.0F, -4.5F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(19, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, -5.0F));

		PartDefinition rightAntenna = head.addOrReplaceChild("rightAntenna", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, 0.0F, -3.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, -2.0F));

		PartDefinition leftAntenna = head.addOrReplaceChild("leftAntenna", CubeListBuilder.create().texOffs(15, 25).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -2.0F));

		PartDefinition frontLeftLeg = all.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(30, 27).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition frontRightLeg = all.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(32, 10).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.0F, -2.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition middleLeftLeg = all.addOrReplaceChild("middleLeftLeg", CubeListBuilder.create().texOffs(30, 31).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition middleRightLeg = all.addOrReplaceChild("middleRightLeg", CubeListBuilder.create().texOffs(34, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition backLeftLeg = all.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(32, 6).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 2.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition backRightLeg = all.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.0F, 2.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition tail = all.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(-0.01F))
		.texOffs(17, 9).addBox(-1.5F, -2.0F, 0.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, 2.5F));
		return LayerDefinition.create(modelData, 64, 64);
	}

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(entity, netHeadYaw, headPitch, animationProgress);
        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::animate);

		this.animate(entity.mandibleNibbleAnimationState, ChorusBeetleAnimations.CHORUS_BEETLE_MANDIBLE_NIBBLE, animationProgress, 1f);
		this.animate(entity.wingFlickAnimationState, ChorusBeetleAnimations.CHORUS_BEETLE_WING_FLICK, animationProgress, 1f);

        if (entity.getAnimationController().getState() == ChorusBeetleAnimationState.WALKING) {
            float swing = Mth.cos(limbSwing * 2.5f) * limbSwingAmount * 0.9f;
            this.frontLeftLeg.xRot += swing;
            this.middleLeftLeg.xRot -= swing;
            this.backLeftLeg.xRot += swing;
            this.frontRightLeg.xRot += swing;
            this.middleRightLeg.xRot -= swing;
            this.backRightLeg.xRot += swing;
        }

		if (entity.getAnimationController().getState() != ChorusBeetleAnimationState.FLYING && entity.getAnimationController().getState() != ChorusBeetleAnimationState.HOVERING) {
        	float turnAmount = Mth.clamp(Mth.wrapDegrees(entity.yBodyRot - entity.yBodyRotO) / FULL_TURN_RATE, -1.0f, 1.0f);
        	float turnSwing = Mth.cos(animationProgress * TURN_LEG_ANIMATION_SPEED) * turnAmount * TURN_LEG_ANIMATION_AMOUNT;
        	this.frontLeftLeg.xRot += turnSwing;
        	this.middleLeftLeg.xRot -= turnSwing;
        	this.backLeftLeg.xRot += turnSwing;
        	this.frontRightLeg.xRot -= turnSwing;
        	this.middleRightLeg.xRot += turnSwing;
        	this.backRightLeg.xRot -= turnSwing;
		}
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }

    @Override
    public float getBabyScale() {
        return 0.5F;
    }

    @Override
    public float getBabyYOffset() {
        return 1.5F;
    }
}
