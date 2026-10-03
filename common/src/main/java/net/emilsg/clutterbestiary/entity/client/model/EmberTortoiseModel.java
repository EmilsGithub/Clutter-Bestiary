package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.EmberTortoiseEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EmberTortoiseModel<T extends EmberTortoiseEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;

    public EmberTortoiseModel(ModelPart root) {
        super(root);
        this.all = this.root.getChild("all");
        this.body = this.all.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -10.0F, -12.0F, 20.0F, 4.0F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(7, 30).addBox(-8.0F, -6.0F, -10.0F, 16.0F, 1.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(64, 41).addBox(-6.0F, -14.0F, -12.0F, 12.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shell = body.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 55).addBox(-9.0F, -3.0F, -11.0F, 18.0F, 6.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition shellTwo = shell.addOrReplaceChild("shellTwo", CubeListBuilder.create().texOffs(0, 91).addBox(-8.0F, -18.0F, -10.0F, 16.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition frontLeftTube = shellTwo.addOrReplaceChild("frontLeftTube", CubeListBuilder.create().texOffs(0, 67).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -18.0F, -6.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition frontMiddleTube = shellTwo.addOrReplaceChild("frontMiddleTube", CubeListBuilder.create().texOffs(0, 83).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -18.0F, -7.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition frontRightTube = shellTwo.addOrReplaceChild("frontRightTube", CubeListBuilder.create().texOffs(0, 13).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -18.0F, -6.5F, 0.0F, 0.0F, -0.2182F));

        PartDefinition middleLeftTube = shellTwo.addOrReplaceChild("middleLeftTube", CubeListBuilder.create().texOffs(80, 64).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition middleRightTube = shellTwo.addOrReplaceChild("middleRightTube", CubeListBuilder.create().texOffs(58, 55).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -18.0F, 0.5F, 0.0F, 0.0F, -0.2182F));

        PartDefinition backLeftTube = shellTwo.addOrReplaceChild("backLeftTube", CubeListBuilder.create().texOffs(0, 55).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -18.0F, 7.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition backMiddleTube = shellTwo.addOrReplaceChild("backMiddleTube", CubeListBuilder.create().texOffs(82, 22).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.0F, 7.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition backRightTube = shellTwo.addOrReplaceChild("backRightTube", CubeListBuilder.create().texOffs(16, 83).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -18.0F, 7.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition frontRightLeg = body.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(64, 28).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.25F, -6.5F, -9.25F));

        PartDefinition backRightLeg = body.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.25F, -6.5F, 9.25F));

        PartDefinition frontLeftLeg = body.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(74, 77).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(7.25F, -6.5F, -9.25F));

        PartDefinition backLeftLeg = body.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(7.25F, -6.5F, 9.25F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, -12.0F));

        PartDefinition throat = neck.addOrReplaceChild("throat", CubeListBuilder.create().texOffs(0, 41).addBox(-3.0F, -2.5F, -2.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.4239F, -1.3827F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 11).addBox(-4.0F, -3.0F, -7.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -1.25F, -4.0F));

        PartDefinition lowerHead = head.addOrReplaceChild("lowerHead", CubeListBuilder.create().texOffs(64, 0).addBox(-4.0F, -1.0F, -7.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(EmberTortoiseEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(entity, netHeadYaw, headPitch, animationProgress);

        if (!entity.isShielding())
            this.animateWalk(EmberTortoiseEntityAnimations.EMBER_TORTOISE_WALK, limbSwing, limbSwingAmount, 3f, 2f);
        if (entity.isShielding()) {
            this.animate(entity.shieldingAnimationState, EmberTortoiseEntityAnimations.EMBER_TORTOISE_SHIELD, animationProgress, 1f);
            this.animate(entity.shieldingTubeAnimationState, EmberTortoiseEntityAnimations.EMBER_TORTOISE_SHIELD_TUBE_LOOP, animationProgress, 1f);
        }

        this.animate(entity.attackAnimationState, EmberTortoiseEntityAnimations.EMBER_TORTOISE_ATTACK, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
