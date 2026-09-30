package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.BoopletEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.BoopletEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BoopletModel<T extends BoopletEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart main;
    private final ModelPart fluff;

    public BoopletModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.main = this.body.getChild("main");
        this.fluff = this.main.getChild("fluff");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition main = body.addOrReplaceChild("main", CubeListBuilder.create().texOffs(31, 47).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftEar = main.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -2.0F, 0.0F));

        PartDefinition rightEar = main.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(8, 32).mirror().addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -2.0F, 0.0F));

        PartDefinition tail = main.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(34, 12).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(34, 28).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.125F)), PartPose.offsetAndRotation(0.0F, 2.0F, 3.5F, -0.4363F, 0.0F, 0.0F));

        PartDefinition fluff = main.addOrReplaceChild("fluff", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition eyes = main.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition leftEye = eyes.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(57, 48).addBox(-1.0F, -1.0F, 0.2667F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(58, 47).addBox(1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 48).addBox(-1.0F, -1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 47).addBox(-1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 48).addBox(-1.0F, 1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(57, 51).addBox(-0.5F, -0.5F, 0.1667F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -5.0F, -3.2667F));

        PartDefinition rightEye = eyes.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(57, 48).addBox(-1.0F, -1.0F, 0.2667F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(58, 47).addBox(-1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 48).addBox(-1.0F, 1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 48).addBox(-1.0F, -1.0F, -0.7333F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 47).addBox(1.0F, -1.0F, -0.7333F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(57, 51).addBox(-0.5F, -0.5F, 0.1667F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -5.0F, -3.2667F));

        PartDefinition leftHorn = body.addOrReplaceChild("leftHorn", CubeListBuilder.create().texOffs(19, 38).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -3.5F, -2.0F));

        PartDefinition rightHorn = body.addOrReplaceChild("rightHorn", CubeListBuilder.create().texOffs(33, 38).mirror().addBox(-1.0F, -1.5F, -3.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -3.5F, -2.0F));

        PartDefinition frontRightLeg = all.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(10, 42).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.5F, -2.0F));

        PartDefinition frontLeftLeg = all.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(0, 42).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.5F, -2.0F));

        PartDefinition backRightLeg = all.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(10, 49).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.5F, 2.0F));

        PartDefinition backLeftLeg = all.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.5F, 2.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(BoopletEntity boopletEntity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (!boopletEntity.isInWater()) {
            this.animateWalk(boopletEntity.isFleeing() ? BoopletEntityAnimations.BOOPLET_RUN : BoopletEntityAnimations.BOOPLET_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);
        }

        this.animate(boopletEntity.swimAnimationState, BoopletEntityAnimations.BOOPLET_SWIM, animationProgress, 1f);
        this.animate(boopletEntity.boopAnimationState, BoopletEntityAnimations.BOOPLET_BOOP, animationProgress, 1f);
        this.animate(boopletEntity.happyAnimationState, BoopletEntityAnimations.BOOPLET_HAPPY, animationProgress, 1f);

        this.fluff.visible = boopletEntity.isFluffy();
    }

    @Override
    protected ModelPart getHeadPart() {
        return null;
    }
}