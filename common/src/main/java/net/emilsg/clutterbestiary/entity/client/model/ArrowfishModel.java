package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.ArrowfishEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.animation.KoiAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentFishModel;
import net.emilsg.clutterbestiary.entity.custom.ArrowfishEntity;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiPrimaryPatternTypeVariant;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ArrowfishModel<T extends ArrowfishEntity> extends ParentFishModel<T> {
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
		super(root);
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

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.5F, -1.0F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 1.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 7).addBox(-0.5F, -1.5F, 0.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 2.0F));

		PartDefinition tailFin = tail.addOrReplaceChild("tailFin", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.5F, -1.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition topFin = body.addOrReplaceChild("topFin", CubeListBuilder.create().texOffs(12, 0).addBox(0.0F, -2.5F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition bottomFin = body.addOrReplaceChild("bottomFin", CubeListBuilder.create().texOffs(12, 9).addBox(0.0F, -0.5F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.0F));

		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(0, 13).addBox(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(12, 18).addBox(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.5F, -4.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(25, 15).addBox(-0.5F, -1.0F, -6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -2.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}

	@Override
	public void setupAnim(ArrowfishEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float headYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.swimmingAnimationState, ArrowfishEntityAnimations.ARROWFISH_SWIM, animationProgress, 1f);
	}

	@Override
	protected ModelPart getHeadPart() {
		return null;
	}
}