package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.PotionWaspEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


public class PotionWaspModel<T extends PotionWaspEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart potionSac;
    private final ModelPart inner;
    private final ModelPart outer;
    private boolean outerOnly;
    private final ModelPart head;

    public PotionWaspModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.potionSac = this.body.getChild("potionSac");
        this.inner = this.potionSac.getChild("inner");
        this.outer = this.potionSac.getChild("outer");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(2.5F, 15.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 15).addBox(-2.0F, -1.5F, -3.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -0.5F, -0.5F));

        PartDefinition potionSac = body.addOrReplaceChild("potionSac", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 0.5F));

        PartDefinition inner = potionSac.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(2, 35).addBox(-3.0F, -7.5F, -4.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 8.0F, 1.5F));

        PartDefinition outer = potionSac.addOrReplaceChild("outer", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -5.0F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 8.0F, 1.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 24).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(-1, 15).addBox(-1.0F, 0.0F, 0.0F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.5F, -2.0F));

        PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(0, 24).addBox(-3.0F, 0.0F, 0.0F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.5F, -2.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -3.0F));

        PartDefinition leftInnerMandible = head.addOrReplaceChild("leftInnerMandible", CubeListBuilder.create().texOffs(24, 30).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.5F, -1.5F, 0.0F, -0.1309F, 0.0F));

        PartDefinition leftOuterMandible = leftInnerMandible.addOrReplaceChild("leftOuterMandible", CubeListBuilder.create().texOffs(26, 30).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition rightInnerMandible = head.addOrReplaceChild("rightInnerMandible", CubeListBuilder.create().texOffs(28, 30).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, -1.5F, 0.0F, 0.1309F, 0.0F));

        PartDefinition rightOuterMandible = rightInnerMandible.addOrReplaceChild("rightOuterMandible", CubeListBuilder.create().texOffs(30, 29).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition innerRightFrontLeg = body.addOrReplaceChild("innerRightFrontLeg", CubeListBuilder.create().texOffs(28, 10).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.5F, -1.5F, 0.0F, 0.0F, -0.3491F));

        PartDefinition outerRightFrontLeg = innerRightFrontLeg.addOrReplaceChild("outerRightFrontLeg", CubeListBuilder.create().texOffs(28, 11).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        PartDefinition innerRightMiddleLeg = body.addOrReplaceChild("innerRightMiddleLeg", CubeListBuilder.create().texOffs(28, 12).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.5F, 0.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition outerRightMiddleLeg = innerRightMiddleLeg.addOrReplaceChild("outerRightMiddleLeg", CubeListBuilder.create().texOffs(28, 13).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5272F));

        PartDefinition innerRightBackLeg = body.addOrReplaceChild("innerRightBackLeg", CubeListBuilder.create().texOffs(28, 14).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.5F, 2.5F, 0.0F, 0.0F, -0.3491F));

        PartDefinition outerRightBackLeg = innerRightBackLeg.addOrReplaceChild("outerRightBackLeg", CubeListBuilder.create().texOffs(24, 29).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        PartDefinition innerLeftFrontLeg = body.addOrReplaceChild("innerLeftFrontLeg", CubeListBuilder.create().texOffs(28, 4).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.5F, -1.5F, 0.0F, 0.0F, 0.3491F));

        PartDefinition outerLeftFrontLeg = innerLeftFrontLeg.addOrReplaceChild("outerLeftFrontLeg", CubeListBuilder.create().texOffs(28, 5).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition innerLeftMiddleLeg = body.addOrReplaceChild("innerLeftMiddleLeg", CubeListBuilder.create().texOffs(28, 6).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition outerLeftMiddleLeg = innerLeftMiddleLeg.addOrReplaceChild("outerLeftMiddleLeg", CubeListBuilder.create().texOffs(28, 7).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5272F));

        PartDefinition innerLeftBackLeg = body.addOrReplaceChild("innerLeftBackLeg", CubeListBuilder.create().texOffs(28, 8).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.5F, 2.5F, 0.0F, 0.0F, 0.3491F));

        PartDefinition outerLeftBackLeg = innerLeftBackLeg.addOrReplaceChild("outerLeftBackLeg", CubeListBuilder.create().texOffs(28, 9).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    public void setAllPartsHidden(boolean hidden) {
        this.root.getAllParts().forEach(part -> part.skipDraw = hidden);
    }

    @Override
    public void setupAnim(PotionWaspEntity potionWasp, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(potionWasp, netHeadYaw, headPitch, animationProgress);

        if (!this.outerOnly) {
            this.inner.skipDraw = !potionWasp.hasPotionSac();
        }

        this.animate(potionWasp.flyingAnimState, potionWasp.hasPotionSac() ? PotionWaspEntityAnimations.POTIONWASP_FLY : PotionWaspEntityAnimations.POTIONWASP_FLY_NO_SAC, animationProgress, 1f);
    }

    public void setOuterHidden(boolean hidden) {
        this.outer.skipDraw = hidden;
    }

    public void showOnlyOuter() {
        this.outerOnly = true;
        this.setAllPartsHidden(true);

        this.root.skipDraw = false;
        this.all.skipDraw = false;
        this.body.skipDraw = false;
        this.potionSac.skipDraw = false;
        this.outer.skipDraw = false;
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}