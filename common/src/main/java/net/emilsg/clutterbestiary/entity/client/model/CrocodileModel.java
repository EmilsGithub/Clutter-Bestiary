package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.CrocodileEntityAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.CrocodileEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class CrocodileModel<T extends CrocodileEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<CrocodileEntity, CrocodileEntityAnimationState> ANIMATIONS = new AnimationBindings<CrocodileEntity, CrocodileEntityAnimationState>()
            .bind(CrocodileEntityAnimationState.STAYING, CrocodileEntityAnimations.CROCODILE_STAY)
            .bind(CrocodileEntityAnimationState.OPENING_MOUTH, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_OPEN)
            .bind(CrocodileEntityAnimationState.MOUTH_WAITING, CrocodileEntityAnimations.CROCODILE_BITE_WAIT)
            .bind(CrocodileEntityAnimationState.SNAPPING_MOUTH_SHUT, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_SNAP_SHUT)
            .bind(CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_OPEN, 4.0f)
            .bind(CrocodileEntityAnimationState.ATTACKING_SNAP_SHUT, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_SNAP_SHUT);
	private final ModelPart all;
	private final ModelPart frontLeftLeg;
	private final ModelPart frontLeftFoot;
	private final ModelPart frontRightLeg;
	private final ModelPart frontRightFoot;
	private final ModelPart bodyFront;
	private final ModelPart head;
	private final ModelPart upperJaw;
	private final ModelPart lowerJaw;
	private final ModelPart bodyMiddle;
	private final ModelPart bodyBack;
	private final ModelPart tailBase;
	private final ModelPart tailOne;
	private final ModelPart tailTwo;
	private final ModelPart tailThree;
	private final ModelPart backLeftLeg;
	private final ModelPart backLeftFoot;
	private final ModelPart backRightLeg;
	private final ModelPart backRightFoot;

    public CrocodileModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.frontLeftLeg = this.all.getChild("frontLeftLeg");
		this.frontLeftFoot = this.frontLeftLeg.getChild("frontLeftFoot");
		this.frontRightLeg = this.all.getChild("frontRightLeg");
		this.frontRightFoot = this.frontRightLeg.getChild("frontRightFoot");
		this.bodyFront = this.all.getChild("bodyFront");
		this.head = this.bodyFront.getChild("head");
		this.upperJaw = this.head.getChild("upperJaw");
		this.lowerJaw = this.head.getChild("lowerJaw");
		this.bodyMiddle = this.all.getChild("bodyMiddle");
		this.bodyBack = this.all.getChild("bodyBack");
		this.tailBase = this.bodyBack.getChild("tailBase");
		this.tailOne = this.tailBase.getChild("tailOne");
		this.tailTwo = this.tailOne.getChild("tailTwo");
		this.tailThree = this.tailTwo.getChild("tailThree");
		this.backLeftLeg = this.all.getChild("backLeftLeg");
		this.backLeftFoot = this.backLeftLeg.getChild("backLeftFoot");
		this.backRightLeg = this.all.getChild("backRightLeg");
		this.backRightFoot = this.backRightLeg.getChild("backRightFoot");
	}

    public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(-0.125F, 21.5F, -1.0F));

		PartDefinition frontLeftLeg = all.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(62, 13).addBox(-0.5F, -1.5F, -7.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.625F, 1.0F, -4.0F));

		PartDefinition frontLeftFoot = frontLeftLeg.addOrReplaceChild("frontLeftFoot", CubeListBuilder.create().texOffs(69, 78).addBox(-1.0F, 0.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.5F, -5.0F));

		PartDefinition frontRightLeg = all.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(60, 34).addBox(-3.5F, -1.5F, -7.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.375F, 1.0F, -4.0F));

		PartDefinition frontRightFoot = frontRightLeg.addOrReplaceChild("frontRightFoot", CubeListBuilder.create().texOffs(75, 62).addBox(-3.0F, 0.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.5F, -5.0F));

		PartDefinition bodyFront = all.addOrReplaceChild("bodyFront", CubeListBuilder.create().texOffs(31, 19).addBox(-3.75F, -2.5F, -8.0F, 7.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(75, 67).addBox(1.0F, -3.5F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(81, 46).addBox(-2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.125F, -2.0F, -7.0F));

		PartDefinition head = bodyFront.addOrReplaceChild("head", CubeListBuilder.create().texOffs(29, 49).addBox(-2.5F, -3.0F, -7.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.5F, -8.0F));

		PartDefinition upperJaw = head.addOrReplaceChild("upperJaw", CubeListBuilder.create().texOffs(25, 62).addBox(-2.5F, -1.0F, -7.0F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.05F))
		.texOffs(50, 62).addBox(-2.5F, 1.1F, -7.0F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.05F))
		.texOffs(50, 71).addBox(-2.0F, -1.0F, -12.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.05F))
		.texOffs(0, 72).addBox(-2.0F, 1.1F, -12.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.05F)), PartPose.offset(0.5F, 0.0F, -7.0F));

		PartDefinition lowerJaw = head.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(62, 25).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 63).addBox(-2.5F, -1.0F, -7.0F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(69, 71).addBox(-2.0F, 0.0F, -12.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(72, 0).addBox(-2.0F, -1.0F, -12.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.0F, -7.0F));

		PartDefinition bodyMiddle = all.addOrReplaceChild("bodyMiddle", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -3.5F, -6.0F, 9.0F, 6.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(81, 54).addBox(1.0F, -4.5F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(85, 34).addBox(1.0F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(81, 50).addBox(-2.0F, -4.5F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(85, 38).addBox(-2.0F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 79).addBox(1.0F, -4.5F, 2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(18, 81).addBox(-2.0F, -4.5F, 2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.125F, -1.0F, -1.0F));

		PartDefinition bodyBack = all.addOrReplaceChild("bodyBack", CubeListBuilder.create().texOffs(43, 0).addBox(-4.0F, -3.5F, -2.5F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(82, 67).addBox(-2.0F, -4.5F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(81, 58).addBox(1.0F, -4.5F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(65, 83).addBox(-2.0F, -4.5F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(72, 83).addBox(1.0F, -4.5F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.125F, -1.0F, 7.5F));

		PartDefinition tailBase = bodyBack.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(0, 35).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(45, 84).addBox(1.0F, -5.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(27, 81).addBox(0.5F, -5.0F, 4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(52, 84).addBox(-2.0F, -5.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 81).addBox(-1.5F, -5.0F, 4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 3.5F));

		PartDefinition tailOne = tailBase.addOrReplaceChild("tailOne", CubeListBuilder.create().texOffs(0, 19).addBox(-2.5F, -3.0F, -1.0F, 5.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 79).addBox(0.5F, -4.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(9, 79).addBox(-1.5F, -4.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(79, 83).addBox(-1.5F, -4.0F, 5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(0.5F, -4.0F, 5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));

		PartDefinition tailTwo = tailOne.addOrReplaceChild("tailTwo", CubeListBuilder.create().texOffs(31, 34).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(47, 79).addBox(-0.5F, -3.5F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(7, 84).addBox(-0.5F, -3.5F, 5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 9.0F));

		PartDefinition tailThree = tailTwo.addOrReplaceChild("tailThree", CubeListBuilder.create().texOffs(19, 72).addBox(-1.0F, -1.5F, 4.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(34, 72).addBox(-1.0F, -2.5F, 0.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 10.0F));

		PartDefinition backLeftLeg = all.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 50).addBox(-0.5F, -2.5F, -1.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.625F, 1.0F, 7.0F));

		PartDefinition backLeftFoot = backLeftLeg.addOrReplaceChild("backLeftFoot", CubeListBuilder.create().texOffs(43, 13).addBox(-1.0F, 0.0F, -1.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.5F, 5.0F));

		PartDefinition backRightLeg = all.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(56, 49).addBox(-3.5F, -2.5F, -1.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.375F, 1.0F, 7.0F));

		PartDefinition backRightFoot = backRightLeg.addOrReplaceChild("backRightFoot", CubeListBuilder.create().texOffs(72, 7).addBox(-3.0F, 0.0F, -1.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.5F, 5.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}

    @Override
    public void setupAnim(CrocodileEntity crocodileEntity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(crocodileEntity, netHeadYaw, headPitch, animationProgress);
        float tickDelta = Mth.clamp(animationProgress - crocodileEntity.tickCount, 0.0f, 1.0f);
        float swimBlend = crocodileEntity.getSwimBlend(tickDelta);
        if (!crocodileEntity.isInWater() && !crocodileEntity.isBasking() && !crocodileEntity.isOrderedToSit()) {
            this.animateWalk(CrocodileEntityAnimations.CROCODILE_WALK, limbSwing, limbSwingAmount, 3.0f, 2.5f);
        }
        this.animate(crocodileEntity.swimAnimationState, CrocodileEntityAnimations.CROCODILE_SWIM, animationProgress, 1.0f);
        this.all.xRot += crocodileEntity.getSwimPitch(tickDelta) * swimBlend;

        this.animate(crocodileEntity.idleAnimationState, CrocodileEntityAnimations.CROCODILE_IDLE, animationProgress, 1.0f);
        ANIMATIONS.apply(crocodileEntity, crocodileEntity.getAnimationController(), animationProgress, this::animate);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
