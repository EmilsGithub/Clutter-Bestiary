package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.custom.KoiEggsEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class KoiEggsModel<T extends KoiEggsEntity> extends SinglePartEntityModel<T> {
	private final ModelPart root;
	private final ModelPart all;
	private final ModelPart egg;
	private final ModelPart eggTwo;
	private final ModelPart eggThree;
	private final ModelPart eggFour;
	private final ModelPart eggFive;
	private final ModelPart eggSix;
	private final ModelPart eggSeven;
	private final ModelPart eggEight;


	public KoiEggsModel(ModelPart root) {
		this.root = root;
		this.all = root.getChild("all");
		this.egg = this.all.getChild("egg");
		this.eggTwo = this.all.getChild("eggTwo");
		this.eggThree = this.all.getChild("eggThree");
		this.eggFour = this.all.getChild("eggFour");
		this.eggFive = this.all.getChild("eggFive");
		this.eggSix = this.all.getChild("eggSix");
		this.eggSeven = this.all.getChild("eggSeven");
		this.eggEight = this.all.getChild("eggEight");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

		ModelPartData egg = all.addChild("egg", ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(7, 1).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, -4.0F));

		ModelPartData eggTwo = all.addChild("eggTwo", ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(7, 1).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 4.0F));

		ModelPartData eggThree = all.addChild("eggThree", ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(7, 1).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -2.0F, 1.0F));

		ModelPartData eggFour = all.addChild("eggFour", ModelPartBuilder.create().uv(11, 13).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(1, 11).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 0.0F, -2.0F));

		ModelPartData eggFive = all.addChild("eggFive", ModelPartBuilder.create().uv(11, 13).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(1, 11).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 3.0F, 0.0F));

		ModelPartData eggSix = all.addChild("eggSix", ModelPartBuilder.create().uv(11, 13).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(1, 11).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -1.0F, -3.0F));

		ModelPartData eggSeven = all.addChild("eggSeven", ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(7, 1).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -4.0F, 1.0F));

		ModelPartData eggEight = all.addChild("eggEight", ModelPartBuilder.create().uv(11, 13).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(1, 11).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 4.0F, 0.0F));
		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		this.getPart().render(matrices, vertices, light, overlay, color);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		applyBob(egg, animationProgress, 0.012f, 0.5f, 0f);
		applyBob(eggTwo, animationProgress, 0.01f, 0.4f, 0.5f);
		applyBob(eggThree, animationProgress, 0.008f, 0.35f, 1.0f);
		applyBob(eggFour, animationProgress, 0.014f, 0.45f, 1.5f);
		applyBob(eggFive, animationProgress, 0.011f, 0.3f, 0.25f);
		applyBob(eggSix, animationProgress, 0.009f, 0.5f, 0.75f);
		applyBob(eggSeven, animationProgress, 0.013f, 0.35f, 1.25f);
		applyBob(eggEight, animationProgress, 0.01f, 0.4f, 1.75f);
	}

	private void applyBob(ModelPart part, float time, float speed, float amplitude, float offset) {
		part.pivotY += (float) (Math.sin((time + offset) * speed * Math.PI * 2) * amplitude);
	}

	@Override
	public ModelPart getPart() {
		return root;
	}
}