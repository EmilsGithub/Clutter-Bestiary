package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CrocodileEntityAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.CrocodileEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CrocodileModel<T extends CrocodileEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<CrocodileEntity, CrocodileEntityAnimationState> ANIMATIONS = new AnimationBindings<CrocodileEntity, CrocodileEntityAnimationState>()
            .bind(CrocodileEntityAnimationState.STAYING, CrocodileEntityAnimations.CROCODILE_STAY)
            .bind(CrocodileEntityAnimationState.OPENING_MOUTH, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_OPEN)
            .bind(CrocodileEntityAnimationState.MOUTH_WAITING, CrocodileEntityAnimations.CROCODILE_BITE_WAIT)
            .bind(CrocodileEntityAnimationState.SNAPPING_MOUTH_SHUT, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_SNAP_SHUT)
            .bind(CrocodileEntityAnimationState.ATTACKING_OPEN_MOUTH, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_OPEN, 4.0f)
            .bind(CrocodileEntityAnimationState.ATTACKING_SNAP_SHUT, CrocodileEntityAnimations.CROCODILE_BITE_MOUTH_SNAP_SHUT);
    private final ModelPart root;
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
        this.root = root;
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

    public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(-0.125F, 21.5F, -1.0F));

		ModelPartData frontLeftLeg = all.addChild("frontLeftLeg", ModelPartBuilder.create().uv(62, 13).cuboid(-0.5F, -1.5F, -7.0F, 4.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(4.625F, 1.0F, -4.0F));

		ModelPartData frontLeftFoot = frontLeftLeg.addChild("frontLeftFoot", ModelPartBuilder.create().uv(69, 78).cuboid(-1.0F, 0.0F, -3.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 1.5F, -5.0F));

		ModelPartData frontRightLeg = all.addChild("frontRightLeg", ModelPartBuilder.create().uv(60, 34).cuboid(-3.5F, -1.5F, -7.0F, 4.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.375F, 1.0F, -4.0F));

		ModelPartData frontRightFoot = frontRightLeg.addChild("frontRightFoot", ModelPartBuilder.create().uv(75, 62).cuboid(-3.0F, 0.0F, -3.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 1.5F, -5.0F));

		ModelPartData bodyFront = all.addChild("bodyFront", ModelPartBuilder.create().uv(31, 19).cuboid(-3.75F, -2.5F, -8.0F, 7.0F, 6.0F, 8.0F, new Dilation(0.0F))
		.uv(75, 67).cuboid(1.0F, -3.5F, -3.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(81, 46).cuboid(-2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.125F, -2.0F, -7.0F));

		ModelPartData head = bodyFront.addChild("head", ModelPartBuilder.create().uv(29, 49).cuboid(-2.5F, -3.0F, -7.0F, 6.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 1.5F, -8.0F));

		ModelPartData upperJaw = head.addChild("upperJaw", ModelPartBuilder.create().uv(25, 62).cuboid(-2.5F, -1.0F, -7.0F, 5.0F, 2.0F, 7.0F, new Dilation(0.05F))
		.uv(50, 62).cuboid(-2.5F, 1.1F, -7.0F, 5.0F, 1.0F, 7.0F, new Dilation(0.05F))
		.uv(50, 71).cuboid(-2.0F, -1.0F, -12.0F, 4.0F, 2.0F, 5.0F, new Dilation(0.05F))
		.uv(0, 72).cuboid(-2.0F, 1.1F, -12.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.05F)), ModelTransform.pivot(0.5F, 0.0F, -7.0F));

		ModelPartData lowerJaw = head.addChild("lowerJaw", ModelPartBuilder.create().uv(62, 25).cuboid(-2.5F, 0.0F, -7.0F, 5.0F, 1.0F, 7.0F, new Dilation(0.0F))
		.uv(0, 63).cuboid(-2.5F, -1.0F, -7.0F, 5.0F, 1.0F, 7.0F, new Dilation(0.0F))
		.uv(69, 71).cuboid(-2.0F, 0.0F, -12.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F))
		.uv(72, 0).cuboid(-2.0F, -1.0F, -12.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 1.0F, -7.0F));

		ModelPartData bodyMiddle = all.addChild("bodyMiddle", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -3.5F, -6.0F, 9.0F, 6.0F, 12.0F, new Dilation(0.0F))
		.uv(81, 54).cuboid(1.0F, -4.5F, -5.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(85, 34).cuboid(1.0F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(81, 50).cuboid(-2.0F, -4.5F, -5.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(85, 38).cuboid(-2.0F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(56, 79).cuboid(1.0F, -4.5F, 2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(18, 81).cuboid(-2.0F, -4.5F, 2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.125F, -1.0F, -1.0F));

		ModelPartData bodyBack = all.addChild("bodyBack", ModelPartBuilder.create().uv(43, 0).cuboid(-4.0F, -3.5F, -2.5F, 8.0F, 6.0F, 6.0F, new Dilation(0.0F))
		.uv(82, 67).cuboid(-2.0F, -4.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(81, 58).cuboid(1.0F, -4.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(65, 83).cuboid(-2.0F, -4.5F, 1.5F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(72, 83).cuboid(1.0F, -4.5F, 1.5F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.125F, -1.0F, 7.5F));

		ModelPartData tailBase = bodyBack.addChild("tailBase", ModelPartBuilder.create().uv(0, 35).cuboid(-3.0F, -4.0F, 0.0F, 6.0F, 6.0F, 8.0F, new Dilation(0.0F))
		.uv(45, 84).cuboid(1.0F, -5.0F, 1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(27, 81).cuboid(0.5F, -5.0F, 4.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(52, 84).cuboid(-2.0F, -5.0F, 1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(36, 81).cuboid(-1.5F, -5.0F, 4.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, 3.5F));

		ModelPartData tailOne = tailBase.addChild("tailOne", ModelPartBuilder.create().uv(0, 19).cuboid(-2.5F, -3.0F, -1.0F, 5.0F, 5.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 79).cuboid(0.5F, -4.0F, 0.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(9, 79).cuboid(-1.5F, -4.0F, 0.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(79, 83).cuboid(-1.5F, -4.0F, 5.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 84).cuboid(0.5F, -4.0F, 5.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 9.0F));

		ModelPartData tailTwo = tailOne.addChild("tailTwo", ModelPartBuilder.create().uv(31, 34).cuboid(-2.0F, -2.5F, 0.0F, 4.0F, 4.0F, 10.0F, new Dilation(0.0F))
		.uv(47, 79).cuboid(-0.5F, -3.5F, 0.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(7, 84).cuboid(-0.5F, -3.5F, 5.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, 9.0F));

		ModelPartData tailThree = tailTwo.addChild("tailThree", ModelPartBuilder.create().uv(19, 72).cuboid(-1.0F, -1.5F, 4.0F, 2.0F, 3.0F, 5.0F, new Dilation(0.0F))
		.uv(34, 72).cuboid(-1.0F, -2.5F, 0.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 10.0F));

		ModelPartData backLeftLeg = all.addChild("backLeftLeg", ModelPartBuilder.create().uv(0, 50).cuboid(-0.5F, -2.5F, -1.0F, 4.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(4.625F, 1.0F, 7.0F));

		ModelPartData backLeftFoot = backLeftLeg.addChild("backLeftFoot", ModelPartBuilder.create().uv(43, 13).cuboid(-1.0F, 0.0F, -1.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 1.5F, 5.0F));

		ModelPartData backRightLeg = all.addChild("backRightLeg", ModelPartBuilder.create().uv(56, 49).cuboid(-3.5F, -2.5F, -1.0F, 4.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.375F, 1.0F, 7.0F));

		ModelPartData backRightFoot = backRightLeg.addChild("backRightFoot", ModelPartBuilder.create().uv(72, 7).cuboid(-3.0F, 0.0F, -1.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 1.5F, 5.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();

        if (this.child) {
            float babyScale = 0.25f;
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 4.5D, 0D);
            this.head.scale(createVec3f(0.6f));
        }

        this.getPart().render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    @Override
    public void setAngles(CrocodileEntity crocodileEntity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(crocodileEntity, netHeadYaw, headPitch, animationProgress);
        float tickDelta = MathHelper.clamp(animationProgress - crocodileEntity.age, 0.0f, 1.0f);
        float swimBlend = crocodileEntity.getSwimBlend(tickDelta);
        if (!crocodileEntity.isTouchingWater() && !crocodileEntity.isBasking() && !crocodileEntity.isSitting()) {
            this.animateMovement(CrocodileEntityAnimations.CROCODILE_WALK, limbSwing, limbSwingAmount, 3.0f, 2.5f);
        }
        this.updateAnimation(crocodileEntity.swimAnimationState, CrocodileEntityAnimations.CROCODILE_SWIM, animationProgress, 1.0f);
        this.all.pitch += crocodileEntity.getSwimPitch(tickDelta) * swimBlend;

        this.updateAnimation(crocodileEntity.idleAnimationState, CrocodileEntityAnimations.CROCODILE_IDLE, animationProgress, 1.0f);
        ANIMATIONS.apply(crocodileEntity, crocodileEntity.getAnimationController(), animationProgress, this::updateAnimation);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
