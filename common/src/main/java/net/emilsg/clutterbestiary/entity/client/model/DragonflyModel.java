package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.DragonflyEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.DragonflyEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


public class DragonflyModel<T extends DragonflyEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart head;

    public DragonflyModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, -4.1F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(8, 25).addBox(0.5F, -0.5F, -1.475F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(0, 25).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.125F))
                .texOffs(12, 25).addBox(-1.5F, -0.5F, -1.475F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -1.0F, -0.9F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -2.0F, -0.9F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(18, 16).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(18, 22).addBox(-1.5F, 0.0F, 4.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 6.1F));

        PartDefinition rightFrontWing = body.addOrReplaceChild("rightFrontWing", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -1.0F, 10.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.75F, -1.75F, 1.1F));

        PartDefinition leftFrontWing = body.addOrReplaceChild("leftFrontWing", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, 0.0F, -1.0F, 10.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, -1.75F, 1.1F));

        PartDefinition rightBackWing = body.addOrReplaceChild("rightBackWing", CubeListBuilder.create().texOffs(0, 4).addBox(-10.0F, 0.0F, -2.0F, 10.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.75F, -1.75F, 4.1F));

        PartDefinition leftBackWing = body.addOrReplaceChild("leftBackWing", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, 0.0F, -2.0F, 10.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, -1.75F, 4.1F));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(DragonflyEntity dragonfly, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(dragonfly, netHeadYaw, headPitch, animationProgress);

        this.animate(dragonfly.flyingAnimState, DragonflyEntityAnimations.DRAGONFLY_FLY, animationProgress, 2.5f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
