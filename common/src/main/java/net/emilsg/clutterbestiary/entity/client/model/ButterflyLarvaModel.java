package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.ButterflyLarvaAnimations;
import net.emilsg.clutterbestiary.entity.custom.ButterflyLarvaEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class ButterflyLarvaModel extends SinglePartEntityModel<ButterflyLarvaEntity> {

	private final ModelPart root;
	private final ModelPart all;
	private final ModelPart middleFront;
	private final ModelPart front;
	private final ModelPart middleBack;
	private final ModelPart back;

	public ButterflyLarvaModel(ModelPart root) {
		this.root = root;
		this.all = root.getChild("all");
		this.middleFront = this.all.getChild("middleFront");
		this.front = this.middleFront.getChild("front");
		this.middleBack = this.all.getChild("middleBack");
		this.back = this.middleBack.getChild("back");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 24.0F, 0.0F));

		ModelPartData middleFront = all.addChild("middleFront", ModelPartBuilder.create().uv(1, 0).cuboid(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.125F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData front = middleFront.addChild("front", ModelPartBuilder.create().uv(1, 0).cuboid(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -1.75F));

		ModelPartData middleBack = all.addChild("middleBack", ModelPartBuilder.create().uv(1, 0).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.1251F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData back = middleBack.addChild("back", ModelPartBuilder.create().uv(1, 0).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 1.75F));
		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void setAngles(ButterflyLarvaEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.root.traverse().forEach(ModelPart::resetTransform);
		this.updateAnimation(entity.walkingAnimState, ButterflyLarvaAnimations.CRAWL, animationProgress, 1.0F);
		if (entity.isClimbing()) {
			this.all.pitch = -(float) Math.PI / 2.0F;
			this.all.pivotZ -= 3.0F;
		}
	}
}
