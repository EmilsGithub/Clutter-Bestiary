package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.KiwiBirdEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BabyKiwiBirdModel<T extends KiwiBirdEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart head;

    public BabyKiwiBirdModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.5F));

        PartDefinition torso = all.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(13, 8).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(13, 21).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail_r1 = torso.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(1, 26).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.5F, -0.1309F, 0.0F, 0.0F));

        PartDefinition leftLeg = all.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(13, 21).addBox(-0.5F, 3.01F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(11, 18).addBox(-0.5F, 1.01F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(1, 21).addBox(-1.0F, -0.74F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, 0.5F));

        PartDefinition rightLeg = all.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(10, 21).addBox(-0.5F, 3.01F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 18).addBox(-0.5F, 1.01F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(1, 16).addBox(-1.0F, -0.74F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.0F, 0.5F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 6).addBox(-1.5F, -1.5F, -2.75F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.75F, -2.0F));

        PartDefinition innerBeak_r1 = head.addOrReplaceChild("innerBeak_r1", CubeListBuilder.create().texOffs(8, 1).addBox(-0.5F, -0.75F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 3).addBox(-0.5F, 0.25F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(-0.5F, -0.75F, -3.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -2.25F, 0.0873F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(KiwiBirdEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(entity, netHeadYaw, headPitch, animationProgress);


        this.animateWalk(KiwiBirdEntityAnimations.KIWI_WALK, limbSwing, limbSwingAmount, 3f, 2f);

        this.animate(entity.idleAnimationState, KiwiBirdEntityAnimations.KIWI_IDLE, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
