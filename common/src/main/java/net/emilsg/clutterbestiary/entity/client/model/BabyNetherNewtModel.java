package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.NetherNewtEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


public class BabyNetherNewtModel<T extends AbstractNetherNewtEntity> extends ParentTameableModel<T> {

    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart head;

    private final ModelPart frontMushroom;
    private final ModelPart middleRightMushroom;
    private final ModelPart middleLeftMushroom;
    private final ModelPart backLeftMushroom;
    private final ModelPart backRightMushroom;

    public BabyNetherNewtModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.head = this.body.getChild("head");

        this.frontMushroom = this.body.getChild("frontMushroom");
        this.middleRightMushroom = this.body.getChild("middleRightMushroom");
        this.middleLeftMushroom = this.body.getChild("middleLeftMushroom");
        this.backLeftMushroom = this.body.getChild("backLeftMushroom");
        this.backRightMushroom = this.body.getChild("backRightMushroom");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -2.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(13, 4).addBox(0.0F, -3.25F, -4.75F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.75F, 2.75F));

        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.125F))
                .texOffs(0, 10).addBox(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, -2.25F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(3, 15).addBox(-2.3F, -2.5F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(13, 5).addBox(1.3F, -2.5F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(13, 0).addBox(-1.5F, -2.0F, -3.5F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.15F, -1.0F, -3.3F, 2.3F, 0.25F, 2.55F, new CubeDeformation(0.0F))
                .texOffs(18, 18).addBox(-2.5F, -2.975F, -4.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(13, 12).addBox(-2.5F, -2.975F, -4.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, -0.25F, -5.75F));

        PartDefinition Neck_r1 = head.addOrReplaceChild("Neck_r1", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -0.5F, -0.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 1.0908F, 0.0F, 0.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 31).addBox(-1.25F, 0.0F, -2.5F, 2.5F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-1.0F, -0.25F, -2.25F, 2.0F, 0.25F, 1.75F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -1.0F));

        PartDefinition beard = jaw.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -1.0F));

        PartDefinition frontMushroom = body.addOrReplaceChild("frontMushroom", CubeListBuilder.create().texOffs(12, 28).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 25).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(27, 25).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -4.25F));

        PartDefinition middleRightMushroom = body.addOrReplaceChild("middleRightMushroom", CubeListBuilder.create().texOffs(27, 6).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 22).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 25).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.9F, -1.0F, -2.25F));

        PartDefinition middleLeftMushroom = body.addOrReplaceChild("middleLeftMushroom", CubeListBuilder.create().texOffs(29, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(25, 10).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(27, 28).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.9F, -1.0F, -2.25F));

        PartDefinition backLeftMushroom = body.addOrReplaceChild("backLeftMushroom", CubeListBuilder.create().texOffs(20, 16).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(23, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(17, 5).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.9F, -1.0F, -0.25F));

        PartDefinition backRightMushroom = body.addOrReplaceChild("backRightMushroom", CubeListBuilder.create().texOffs(19, 29).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(25, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(15, 29).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.9F, -1.0F, 0.75F));

        PartDefinition upperLeftFrontLeg = body.addOrReplaceChild("upperLeftFrontLeg", CubeListBuilder.create().texOffs(8, 28).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.125F)), PartPose.offsetAndRotation(1.5F, -0.25F, -3.75F, 0.0F, 0.0F, -0.2618F));

        PartDefinition lowerLeftFrontLeg = upperLeftFrontLeg.addOrReplaceChild("lowerLeftFrontLeg", CubeListBuilder.create().texOffs(27, 3).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition upperRightFrontLeg = body.addOrReplaceChild("upperRightFrontLeg", CubeListBuilder.create().texOffs(24, 26).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.125F)), PartPose.offsetAndRotation(-1.5F, -0.25F, -3.75F, 0.0F, 0.0F, 0.2618F));

        PartDefinition lowerRightFrontLeg = upperRightFrontLeg.addOrReplaceChild("lowerRightFrontLeg", CubeListBuilder.create().texOffs(20, 26).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition upperLeftBackLeg = body.addOrReplaceChild("upperLeftBackLeg", CubeListBuilder.create().texOffs(16, 26).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.125F)), PartPose.offsetAndRotation(1.5F, -0.25F, 0.25F, 0.0F, 0.0F, -0.2618F));

        PartDefinition lowerLeftBackLeg = upperLeftBackLeg.addOrReplaceChild("lowerLeftBackLeg", CubeListBuilder.create().texOffs(7, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition upperRightBackLeg = body.addOrReplaceChild("upperRightBackLeg", CubeListBuilder.create().texOffs(13, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.125F)), PartPose.offsetAndRotation(-1.5F, -0.25F, 0.25F, 0.0F, 0.0F, 0.2618F));

        PartDefinition lowerRightBackLeg = upperRightBackLeg.addOrReplaceChild("lowerRightBackLeg", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition tailOne = body.addOrReplaceChild("tailOne", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.25F))
                .texOffs(0, 2).addBox(0.0F, -2.25F, 0.175F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.45F, 1.25F));

        PartDefinition tailTwo = tailOne.addOrReplaceChild("tailTwo", CubeListBuilder.create().texOffs(20, 5).addBox(-1.0F, -1.0F, -0.35F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(0.0F, -1.9F, 0.675F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.15F, 3.25F));

        PartDefinition tailThree = tailTwo.addOrReplaceChild("tailThree", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -0.3F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
                .texOffs(2, 1).addBox(0.0F, -1.7F, 0.675F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.15F, 2.6F));

        PartDefinition tailFour = tailThree.addOrReplaceChild("tailFour", CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.15F, 2.45F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(AbstractNetherNewtEntity newt, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(newt, netHeadYaw, headPitch, animationProgress);

        if (!newt.isOrderedToSit()) {
            this.animateWalk(NetherNewtEntityAnimations.NETHER_NEWT_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);
            this.animate(newt.idleAnimationState, NetherNewtEntityAnimations.NETHER_NEWT_IDLE, animationProgress, 1f);

        } else {
            this.animate(newt.sittingAnimationState, NetherNewtEntityAnimations.NETHER_NEWT_SIT, animationProgress, 1.0f);
        }

        this.updateVisibleParts(newt);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }

    private void updateVisibleParts(AbstractNetherNewtEntity netherNewtEntity) {
        int fungiCount = netherNewtEntity.getFungiCount();

        frontMushroom.visible = fungiCount >= 1;
        middleRightMushroom.visible = fungiCount >= 4;
        middleLeftMushroom.visible = fungiCount >= 2;
        backLeftMushroom.visible = fungiCount >= 5;
        backRightMushroom.visible = fungiCount >= 3;
    }
}
