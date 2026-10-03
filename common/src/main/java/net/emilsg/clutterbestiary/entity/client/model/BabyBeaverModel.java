package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.BeaverEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BabyBeaverModel<T extends BeaverEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart frontRightLeg;
    private final ModelPart heldItem;
    private final ModelPart backRightLeg;
    private final ModelPart rightFlipper;
    private final ModelPart backLeftLeg;
    private final ModelPart leftFlipper;
    private final ModelPart frontLeftLeg;
    private final ModelPart head;
    private final ModelPart nose;

    public BabyBeaverModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.tail = this.body.getChild("tail");
        this.frontRightLeg = this.all.getChild("frontRightLeg");
        this.heldItem = this.frontRightLeg.getChild("heldItem");
        this.backRightLeg = this.all.getChild("backRightLeg");
        this.rightFlipper = this.backRightLeg.getChild("rightFlipper");
        this.backLeftLeg = this.all.getChild("backLeftLeg");
        this.leftFlipper = this.backLeftLeg.getChild("leftFlipper");
        this.frontLeftLeg = this.all.getChild("frontLeftLeg");
        this.head = this.all.getChild("head");
        this.nose = this.head.getChild("nose");
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 20.5F, 3.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(7, 6).addBox(-2.0F, 1.0F, -10.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 4.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(7, 24).addBox(-2.5F, 0.0F, 1.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(29, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -2.0F));

        PartDefinition frontRightLeg = all.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.5F, -5.0F));

        PartDefinition heldItem = frontRightLeg.addOrReplaceChild("heldItem", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0F, 0.0F));

        PartDefinition backRightLeg = all.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(29, 4).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.5F, 0.0F));

        PartDefinition rightFlipper = backRightLeg.addOrReplaceChild("rightFlipper", CubeListBuilder.create().texOffs(16, 20).addBox(-1.5F, -0.125F, -1.5F, 2.0F, 0.25F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.865F, -0.5F));

        PartDefinition backLeftLeg = all.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.5F, 0.0F));

        PartDefinition leftFlipper = backLeftLeg.addOrReplaceChild("leftFlipper", CubeListBuilder.create().texOffs(16, 17).addBox(-0.5F, -0.125F, -1.5F, 2.0F, 0.25F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.865F, -0.5F));

        PartDefinition frontLeftLeg = all.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.5F, -5.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 19).addBox(-3.0F, -2.0F, -5.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.5F, -6.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 9).addBox(0.0F, 0.0F, -0.75F, 1.0F, 1.0F, 0.25F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-1.0F, 0.0F, -0.75F, 1.0F, 1.0F, 0.25F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, -5.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(BeaverEntity beaverEntity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(beaverEntity, netHeadYaw, headPitch, animationProgress);
        boolean isTouchingWater = beaverEntity.isInWater();

        if (!isTouchingWater) {
            this.animateWalk(BeaverEntityAnimations.BEAVER_WALK, limbSwing, limbSwingAmount, 2.0f, 2.5f);
            this.animate(beaverEntity.idleAnimationState, BeaverEntityAnimations.BEAVER_IDLE, animationProgress, 1.0f);
        } else {
            float animationSpeed = (float) (beaverEntity.getDeltaMovement().length() * 5) + Math.abs(0.5f);
            if (animationSpeed >= 1.2f) animationSpeed = 1.2f;
            this.animate(beaverEntity.waterAnimationState, BeaverEntityAnimations.BEAVER_SWIM, animationProgress, animationSpeed);
        }

        this.animate(beaverEntity.strippingItemsAnimationState, BeaverEntityAnimations.BEAVER_STRIP_ITEMS, animationProgress, 1.0f);
        this.animate(beaverEntity.idleAnimationState, BeaverEntityAnimations.BEAVER_SNIFF_IDLE, animationProgress, 1.0f);
        this.animate(beaverEntity.idlingAnimationState, BeaverEntityAnimations.BEAVER_IDLE, animationProgress, 1.0f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
