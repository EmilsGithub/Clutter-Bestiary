package net.emilsg.clutter_bestiary.entity.client.model;

import net.emilsg.clutter_bestiary.entity.client.animation.KoiAnimations;
import net.emilsg.clutter_bestiary.entity.client.model.parent.ParentFishModel;
import net.emilsg.clutter_bestiary.entity.custom.KoiEntity;
import net.emilsg.clutter_bestiary.entity.variants.koi.KoiPrimaryPatternTypeVariant;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class KoiModel<T extends KoiEntity> extends ParentFishModel<T> {
	private final ModelPart root;
	private final ModelPart all;
	private final ModelPart body;
	private final ModelPart bodyPatternLayer;
	private final ModelPart head;
	private final ModelPart headPatternLayer;

	public KoiModel(ModelPart root) {
		this.root = root;
		this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.bodyPatternLayer = this.body.getChild("bodyPatternLayer");
		this.head = this.body.getChild("head");
		this.headPatternLayer = this.head.getChild("headPatternLayer");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.0F, -5.0F));

		ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bodyPatternLayer = body.addChild("bodyPatternLayer", ModelPartBuilder.create().uv(0, 13).cuboid(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 9.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bodySecondaryPatternLayer = body.addChild("bodySecondaryPatternLayer", ModelPartBuilder.create().uv(0, 40).cuboid(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 9.0F, new Dilation(0.02F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData backFin = body.addChild("backFin", ModelPartBuilder.create().uv(26, 0).cuboid(0.0F, -2.0F, -3.5F, 0.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 3.5F));

		ModelPartData frontLeftFin = body.addChild("frontLeftFin", ModelPartBuilder.create().uv(26, 21).cuboid(0.0F, 0.0F, -1.5F, 2.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 0.0F, 0.5F));

		ModelPartData frontRightFin = body.addChild("frontRightFin", ModelPartBuilder.create().uv(24, 26).cuboid(-2.0F, 0.0F, -1.5F, 2.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, 0.5F));

		ModelPartData backLeftFin = body.addChild("backLeftFin", ModelPartBuilder.create().uv(26, 15).cuboid(0.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 0.0F, 5.5F));

		ModelPartData backRightFin = body.addChild("backRightFin", ModelPartBuilder.create().uv(26, 18).cuboid(-3.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, 5.5F));

		ModelPartData tailFin = body.addChild("tailFin", ModelPartBuilder.create().uv(0, 26).cuboid(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 8.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(26, 9).cuboid(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.5F, -1.0F));

		ModelPartData headPatternLayer = head.addChild("headPatternLayer", ModelPartBuilder.create().uv(12, 26).cuboid(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData leftWhisker = head.addChild("leftWhisker", ModelPartBuilder.create().uv(26, 24).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 1.5F, -1.5F));

		ModelPartData rightWhisker = head.addChild("rightWhisker", ModelPartBuilder.create().uv(28, 24).cuboid(0.0F, 1.0F, 1.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 0.5F, -3.5F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(KoiEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		this.updatePatternVisibility(entity.getPrimaryPatternTypeVariant() != KoiPrimaryPatternTypeVariant.NONE);

		this.updateAnimation(entity.swimmingAnimationState, KoiAnimations.KOI_SWIM, ageInTicks, 1f);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	private void updatePatternVisibility(boolean patternVisibility) {
		this.bodyPatternLayer.visible = patternVisibility;
	}

	@Override
	protected ModelPart getHeadPart() {
		return null;
	}

	@Override
	public ModelPart getPart() {
		return root;
	}
}