package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.ArrowfishEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.animation.KoiAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentFishModel;
import net.emilsg.clutterbestiary.entity.custom.ArrowfishEntity;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiPrimaryPatternTypeVariant;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ArrowfishModel<T extends ArrowfishEntity> extends ParentFishModel<T> {
	private final ModelPart root;
	private final ModelPart all;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart tailFin;
	private final ModelPart topFin;
	private final ModelPart bottomFin;
	private final ModelPart leftFin;
	private final ModelPart rightFin;
	private final ModelPart front;

	public ArrowfishModel(ModelPart root) {
		this.root = root;
		this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.tail = this.body.getChild("tail");
		this.tailFin = this.tail.getChild("tailFin");
		this.topFin = this.body.getChild("topFin");
		this.bottomFin = this.body.getChild("bottomFin");
		this.leftFin = this.body.getChild("leftFin");
		this.rightFin = this.body.getChild("rightFin");
		this.front = this.body.getChild("front");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.5F, -1.0F));

		ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 22).cuboid(-1.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.5F, 1.0F));

		ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(24, 7).cuboid(-0.5F, -1.5F, 0.0F, 1.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, 2.0F));

		ModelPartData tailFin = tail.addChild("tailFin", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -3.5F, -1.0F, 0.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 3.0F));

		ModelPartData topFin = body.addChild("topFin", ModelPartBuilder.create().uv(12, 0).cuboid(0.0F, -2.5F, -3.0F, 0.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.5F, 0.0F));

		ModelPartData bottomFin = body.addChild("bottomFin", ModelPartBuilder.create().uv(12, 9).cuboid(0.0F, -0.5F, -3.0F, 0.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.5F, 0.0F));

		ModelPartData leftFin = body.addChild("leftFin", ModelPartBuilder.create().uv(0, 13).cuboid(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData rightFin = body.addChild("rightFin", ModelPartBuilder.create().uv(12, 18).cuboid(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData front = body.addChild("front", ModelPartBuilder.create().uv(24, 0).cuboid(-1.0F, -1.5F, -4.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
				.uv(25, 15).cuboid(-0.5F, -1.0F, -6.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, -2.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void setAngles(ArrowfishEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float headYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		this.updateAnimation(entity.swimmingAnimationState, ArrowfishEntityAnimations.ARROWFISH_SWIM, animationProgress, 1f);
	}

	@Override
	protected ModelPart getHeadPart() {
		return null;
	}
}