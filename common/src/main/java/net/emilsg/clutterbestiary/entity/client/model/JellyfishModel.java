package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.JellyfishEntityAnimations;
import net.emilsg.clutterbestiary.entity.custom.JellyfishEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


public class JellyfishModel<T extends JellyfishEntity> extends BestiaryEntityModel<T> {
    private final ModelPart all;

    public JellyfishModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 27.0F, 0.0F));

        PartDefinition upperBody = all.addOrReplaceChild("upperBody", CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -8.5F, 0.0F));

        PartDefinition lowerBody = all.addOrReplaceChild("lowerBody", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition outerTentacles = all.addOrReplaceChild("outerTentacles", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition frontTop = outerTentacles.addOrReplaceChild("frontTop", CubeListBuilder.create().texOffs(0, 26).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -4.0F));

        PartDefinition frontBottom = frontTop.addOrReplaceChild("frontBottom", CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition rightTop = outerTentacles.addOrReplaceChild("rightTop", CubeListBuilder.create().texOffs(16, 12).addBox(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -6.0F, 0.0F));

        PartDefinition rightBottom = rightTop.addOrReplaceChild("rightBottom", CubeListBuilder.create().texOffs(16, 15).addBox(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition backTop = outerTentacles.addOrReplaceChild("backTop", CubeListBuilder.create().texOffs(16, 26).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 4.0F));

        PartDefinition backBottom = backTop.addOrReplaceChild("backBottom", CubeListBuilder.create().texOffs(16, 29).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition leftTop = outerTentacles.addOrReplaceChild("leftTop", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -6.0F, 0.0F));

        PartDefinition leftBottom = leftTop.addOrReplaceChild("leftBottom", CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition innerTentacles = all.addOrReplaceChild("innerTentacles", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition front = innerTentacles.addOrReplaceChild("front", CubeListBuilder.create().texOffs(52, 18).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -3.0F));

        PartDefinition right = innerTentacles.addOrReplaceChild("right", CubeListBuilder.create().texOffs(35, 12).addBox(0.0F, 0.0F, -3.0F, 0.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -5.0F, 0.0F));

        PartDefinition back = innerTentacles.addOrReplaceChild("back", CubeListBuilder.create().texOffs(35, 0).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 3.0F));

        PartDefinition left = innerTentacles.addOrReplaceChild("left", CubeListBuilder.create().texOffs(52, -6).addBox(0.0F, 0.0F, -3.0F, 0.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -5.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(JellyfishEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.swimmingAnimationState, JellyfishEntityAnimations.JELLYFISH_SWIM, animationProgress, 1f);
    }
}