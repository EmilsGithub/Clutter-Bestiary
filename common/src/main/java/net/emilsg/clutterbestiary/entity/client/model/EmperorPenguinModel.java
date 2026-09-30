package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.EmperorPenguinEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.EmperorPenguinEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EmperorPenguinModel<T extends EmperorPenguinEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart head;

    public EmperorPenguinModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.head = all.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 41).addBox(-4.0F, -17.0F, -4.0F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(-4.0F, -17.0F, -5.0F, 8.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(45, 17).addBox(-3.0F, -2.0F, 2.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(38, 52).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -1.0F));

        PartDefinition beak = head.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upperBeak = beak.addOrReplaceChild("upperBeak", CubeListBuilder.create().texOffs(32, 46).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0625F))
                .texOffs(1, 1).addBox(-0.5F, 0.125F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0625F)), PartPose.offset(0.0F, -2.0F, -3.0F));

        PartDefinition lowerBeak = beak.addOrReplaceChild("lowerBeak", CubeListBuilder.create().texOffs(33, 42).addBox(-0.5F, 0.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -3.0F));

        PartDefinition rightWing = all.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(43, 37).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -6.0F, -1.0F));

        PartDefinition leftWing = all.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(54, 37).addBox(0.0F, 0.0F, -1.0F, 1.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -6.0F, -1.0F));

        PartDefinition leftLeg = all.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(47, 5).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 13).addBox(-1.5F, 1.95F, -4.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 9.0F, -0.5F));

        PartDefinition rightLeg = all.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(2, 5).addBox(-1.5F, 1.95F, -4.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(47, 11).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 9.0F, -0.5F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(EmperorPenguinEntity emperorPenguinEntity, float limbSwing, float limbSwingAmount, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(emperorPenguinEntity, headYaw, headPitch, animationProgress);

        if (!emperorPenguinEntity.isInWater()) {
            this.animateWalk(EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_WALK, limbSwing, limbSwingAmount, 2.0f, 2.5f);
        } else {
            this.animateWalk(EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_PADDLE, limbSwing, limbSwingAmount, 2.0f, 2.5f);
        }
        this.animate(emperorPenguinEntity.flapAnimationStateOne, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP, animationProgress, 1f);
        this.animate(emperorPenguinEntity.flapAnimationStateTwo, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP_TWO, animationProgress, 1f);
        this.animate(emperorPenguinEntity.preenAnimationState, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_PREEN, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}