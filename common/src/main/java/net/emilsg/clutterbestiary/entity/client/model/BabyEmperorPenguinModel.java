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

public class BabyEmperorPenguinModel<T extends EmperorPenguinEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart head;

    public BabyEmperorPenguinModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.head = all.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition All = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = All.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = All.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 20).addBox(-0.5F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition leftWing = All.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -8.0F, 0.0F));

        PartDefinition rightWing = All.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(24, 8).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -8.0F, 0.0F));

        PartDefinition leftLeg = All.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(24, 21).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-2, 27).addBox(-1.0F, 1.0F, -3.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition rightLeg = All.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(24, 17).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(2, 27).addBox(-1.0F, 1.0F, -3.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -1.0F, 0.0F));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(EmperorPenguinEntity emperorPenguinEntity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(emperorPenguinEntity, netHeadYaw, headPitch, animationProgress);

        this.animateWalk(EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_WALK, limbSwing, limbSwingAmount, 1.5f, 1f);

        this.animate(emperorPenguinEntity.flapAnimationStateOne, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP, animationProgress, 1f);
        this.animate(emperorPenguinEntity.flapAnimationStateTwo, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_RANDOM_FLAP_TWO, animationProgress, 1f);
        this.animate(emperorPenguinEntity.preenAnimationState, EmperorPenguinEntityAnimations.EMPEROR_PENGUIN_PREEN, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}