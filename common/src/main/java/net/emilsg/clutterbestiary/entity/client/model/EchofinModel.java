package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.EchofinEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.EchofinEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EchofinModel<T extends EchofinEntity> extends BestiaryModel<T> {
    private final ModelPart all;


    public EchofinModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, -2.0F));

        PartDefinition bodyOne = all.addOrReplaceChild("bodyOne", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -5.5F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 30).addBox(0.0F, 2.5F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-1.0F, -2.5F, -4.0F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, -5.0F));

        PartDefinition bodyTwo = bodyOne.addOrReplaceChild("bodyTwo", CubeListBuilder.create().texOffs(20, 28).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(20, 16).addBox(0.0F, -6.5F, 0.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(20, 31).addBox(0.0F, 1.5F, 0.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition tailOne = bodyTwo.addOrReplaceChild("tailOne", CubeListBuilder.create().texOffs(28, 19).addBox(-4.5F, 0.0F, 0.0F, 9.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, -1).addBox(0.0F, -4.5F, 0.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

        PartDefinition rightWingOne = bodyOne.addOrReplaceChild("rightWingOne", CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, -0.5F, -0.75F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(4, 16).addBox(-2.0F, 0.0F, 2.25F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.75F));

        PartDefinition rightWingTwo = rightWingOne.addOrReplaceChild("rightWingTwo", CubeListBuilder.create().texOffs(-9, 9).addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.75F));

        PartDefinition rightWingThree = rightWingTwo.addOrReplaceChild("rightWingThree", CubeListBuilder.create().texOffs(-9, 0).addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 1.0F));

        PartDefinition leftWingOne = bodyOne.addOrReplaceChild("leftWingOne", CubeListBuilder.create().texOffs(0, 41).addBox(0.0F, -0.5F, -0.75F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(4, 41).addBox(0.0F, 0.0F, 2.25F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.75F));

        PartDefinition leftWingTwo = leftWingOne.addOrReplaceChild("leftWingTwo", CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.75F));

        PartDefinition leftWingThree = leftWingTwo.addOrReplaceChild("leftWingThree", CubeListBuilder.create().texOffs(36, 9).addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 1.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(EchofinEntity echofin, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(echofin.movingAnimState, EchofinEntityAnimations.ECHOFIN_SWIMMING, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return null;
    }
}
