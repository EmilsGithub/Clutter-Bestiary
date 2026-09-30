package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.ButterflyEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ButterflyModel<T extends ButterflyEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public ButterflyModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = all.getChild("body");
        this.leftWing = body.getChild("leftWing");
        this.rightWing = body.getChild("rightWing");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 2).addBox(0.25F, -1.8848F, -0.3293F, 0.5F, 4.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -0.1152F, -0.4207F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 3).addBox(0.0F, -1.0F, 0.0F, 0.25F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -1.75F, -0.25F, 0.2182F, 0.0F, -0.2182F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 3).addBox(-0.25F, -1.0F, 0.0F, 0.25F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, -1.75F, -0.25F, 0.2182F, 0.0F, 0.2182F));

        PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(0.0F, -3.8848F, -0.0793F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.75F, 0.0F, 0.0F));

        PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(0, 8).addBox(-4.0F, -3.8848F, -0.0793F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 16, 16);
    }

    public ModelPart getLeftWing() {
        return leftWing;
    }

    public ModelPart getRightWing() {
        return rightWing;
    }

    @Override
    public void setupAnim(ButterflyEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        AnimationDefinition flightAnim;

        switch (entity.getFlyingTypeVariant()) {
            case 1 -> flightAnim = ButterflyEntityAnimations.BUTTERFLY_FLYING_TWO;
            case 2 -> flightAnim = ButterflyEntityAnimations.BUTTERFLY_FLYING_THREE;
            default -> flightAnim = ButterflyEntityAnimations.BUTTERFLY_FLYING_ONE;
        }

        this.animate(entity.flyingAnimState, flightAnim, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return null;
    }
}