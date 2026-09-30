package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.custom.PotionSacEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


public class PotionSacModel<T extends PotionSacEntity> extends BestiaryEntityModel<T> {
    private final ModelPart all;
    private final ModelPart potionSac;
    private final ModelPart outer;


    public PotionSacModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.potionSac = all.getChild("potionSac");
        this.outer = potionSac.getChild("outer");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition potionSac = all.addOrReplaceChild("potionSac", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition inner = potionSac.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(2, 35).addBox(-3.0F, -7.5F, -4.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 8.0F, 1.5F));

        PartDefinition outer = potionSac.addOrReplaceChild("outer", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -5.0F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 8.0F, 1.5F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(PotionSacEntity potionSac, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    public void setOuterHidden(boolean hidden) {
        this.outer.skipDraw = hidden;
    }
}