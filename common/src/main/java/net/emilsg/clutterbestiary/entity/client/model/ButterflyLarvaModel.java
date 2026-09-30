package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import net.emilsg.clutterbestiary.entity.client.animation.ButterflyLarvaAnimations;
import net.emilsg.clutterbestiary.entity.custom.ButterflyLarvaEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ButterflyLarvaModel extends BestiaryEntityModel<ButterflyLarvaEntity> {

	private final ModelPart all;
	private final ModelPart middleFront;
	private final ModelPart front;
	private final ModelPart middleBack;
	private final ModelPart back;

	public ButterflyLarvaModel(ModelPart root) {
		super(root);
		this.all = root.getChild("all");
		this.middleFront = this.all.getChild("middleFront");
		this.front = this.middleFront.getChild("front");
		this.middleBack = this.all.getChild("middleBack");
		this.back = this.middleBack.getChild("back");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(-0.5F, 24.0F, 0.0F));

		PartDefinition middleFront = all.addOrReplaceChild("middleFront", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition front = middleFront.addOrReplaceChild("front", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.75F));

		PartDefinition middleBack = all.addOrReplaceChild("middleBack", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1251F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition back = middleBack.addOrReplaceChild("back", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.75F));
		return LayerDefinition.create(modelData, 16, 16);
	}

	@Override
	public void setupAnim(ButterflyLarvaEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.root.getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.walkingAnimState, ButterflyLarvaAnimations.CRAWL, animationProgress, 1.0F);
		if (entity.onClimbable()) {
			this.all.xRot = -(float) Math.PI / 2.0F;
			this.all.z -= 3.0F;
		}
	}
}
