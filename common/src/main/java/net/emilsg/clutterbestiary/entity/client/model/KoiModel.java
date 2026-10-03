package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.KoiAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentFishModel;
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

public class KoiModel<T extends KoiEntity> extends ParentFishModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart bodyPatternLayer;
    private final ModelPart head;
    private final ModelPart headPatternLayer;

    public KoiModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.bodyPatternLayer = this.body.getChild("bodyPatternLayer");
        this.head = this.body.getChild("head");
        this.headPatternLayer = this.head.getChild("headPatternLayer");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, -5.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bodyPatternLayer = body.addOrReplaceChild("bodyPatternLayer", CubeListBuilder.create().texOffs(0, 13).addBox(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bodySecondaryPatternLayer = body.addOrReplaceChild("bodySecondaryPatternLayer", CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition backFin = body.addOrReplaceChild("backFin", CubeListBuilder.create().texOffs(26, -1).addBox(0.0F, -2.0F, -3.5F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -4.0F, 3.5F));

        PartDefinition frontLeftFin = body.addOrReplaceChild("frontLeftFin", CubeListBuilder.create().texOffs(28, 16).addBox(0.0F, 0.0F, -1.5F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.001F)), PartPose.offset(2.0F, 0.0F, 0.5F));

        PartDefinition frontRightFin = body.addOrReplaceChild("frontRightFin", CubeListBuilder.create().texOffs(23, 16).addBox(-2.0F, 0.0F, -1.5F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.001F)), PartPose.offset(-2.0F, 0.0F, 0.5F));

        PartDefinition backLeftFin = body.addOrReplaceChild("backLeftFin", CubeListBuilder.create().texOffs(31, 20).addBox(0.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.001F)), PartPose.offset(2.0F, 0.0F, 5.5F));

        PartDefinition backRightFin = body.addOrReplaceChild("backRightFin", CubeListBuilder.create().texOffs(24, 20).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.001F)), PartPose.offset(-2.0F, 0.0F, 5.5F));

        PartDefinition tailFin = body.addOrReplaceChild("tailFin", CubeListBuilder.create().texOffs(0, 26).addBox(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 6.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -2.0F, 8.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 9).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -1.0F));

        PartDefinition headPatternLayer = head.addOrReplaceChild("headPatternLayer", CubeListBuilder.create().texOffs(12, 26).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftWhisker = head.addOrReplaceChild("leftWhisker", CubeListBuilder.create().texOffs(27, 23).addBox(0.0F, 0.0F, -0.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offset(1.5F, 1.5F, -1.5F));

        PartDefinition rightWhisker = head.addOrReplaceChild("rightWhisker", CubeListBuilder.create().texOffs(30, 23).addBox(0.0F, 1.0F, 1.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offset(-1.5F, 0.5F, -3.5F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(KoiEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.updatePatternVisibility(entity.getPrimaryPatternTypeVariant() != KoiPrimaryPatternTypeVariant.NONE);

        this.animate(entity.swimmingAnimationState, KoiAnimations.KOI_SWIM, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return null;
    }

    private void updatePatternVisibility(boolean patternVisibility) {
        this.bodyPatternLayer.visible = patternVisibility;
    }
}
