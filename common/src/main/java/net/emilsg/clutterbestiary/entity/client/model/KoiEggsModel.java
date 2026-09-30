package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.custom.KoiEggsEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class KoiEggsModel<T extends KoiEggsEntity> extends BestiaryEntityModel<T> {
    private final ModelPart all;
    private final ModelPart egg;
    private final ModelPart eggTwo;
    private final ModelPart eggThree;
    private final ModelPart eggFour;
    private final ModelPart eggFive;
    private final ModelPart eggSix;
    private final ModelPart eggSeven;
    private final ModelPart eggEight;


    public KoiEggsModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.egg = this.all.getChild("egg");
        this.eggTwo = this.all.getChild("eggTwo");
        this.eggThree = this.all.getChild("eggThree");
        this.eggFour = this.all.getChild("eggFour");
        this.eggFive = this.all.getChild("eggFive");
        this.eggSix = this.all.getChild("eggSix");
        this.eggSeven = this.all.getChild("eggSeven");
        this.eggEight = this.all.getChild("eggEight");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition egg = all.addOrReplaceChild("egg", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(7, 1).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -4.0F));

        PartDefinition eggTwo = all.addOrReplaceChild("eggTwo", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(7, 1).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 4.0F));

        PartDefinition eggThree = all.addOrReplaceChild("eggThree", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(7, 1).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -2.0F, 1.0F));

        PartDefinition eggFour = all.addOrReplaceChild("eggFour", CubeListBuilder.create().texOffs(11, 13).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 11).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, -2.0F));

        PartDefinition eggFive = all.addOrReplaceChild("eggFive", CubeListBuilder.create().texOffs(11, 13).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 11).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, 0.0F));

        PartDefinition eggSix = all.addOrReplaceChild("eggSix", CubeListBuilder.create().texOffs(11, 13).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 11).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -1.0F, -3.0F));

        PartDefinition eggSeven = all.addOrReplaceChild("eggSeven", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(7, 1).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -4.0F, 1.0F));

        PartDefinition eggEight = all.addOrReplaceChild("eggEight", CubeListBuilder.create().texOffs(11, 13).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 11).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 4.0F, 0.0F));
        return LayerDefinition.create(modelData, 16, 16);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        applyBob(egg, animationProgress, 0.012f, 0.5f, 0f);
        applyBob(eggTwo, animationProgress, 0.01f, 0.4f, 0.5f);
        applyBob(eggThree, animationProgress, 0.008f, 0.35f, 1.0f);
        applyBob(eggFour, animationProgress, 0.014f, 0.45f, 1.5f);
        applyBob(eggFive, animationProgress, 0.011f, 0.3f, 0.25f);
        applyBob(eggSix, animationProgress, 0.009f, 0.5f, 0.75f);
        applyBob(eggSeven, animationProgress, 0.013f, 0.35f, 1.25f);
        applyBob(eggEight, animationProgress, 0.01f, 0.4f, 1.75f);
    }

    private void applyBob(ModelPart part, float time, float speed, float amplitude, float offset) {
        part.y += (float) (Math.sin((time + offset) * speed * Math.PI * 2) * amplitude);
    }
}