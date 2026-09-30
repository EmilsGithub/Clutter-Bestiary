package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.MantaRayEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryAquaticModel;
import net.emilsg.clutterbestiary.entity.custom.MantaRayEntity;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MantaRayModel<T extends MantaRayEntity> extends BestiaryAquaticModel<T> {
    private final ModelPart all;
    private float oldPitch = 0.0f;
    private float oldYaw = 0.0f;

    public MantaRayModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 1.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition leftMandible_r1 = body.addOrReplaceChild("leftMandible_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -1.5F, -7.5F, 0.0F, 3.1416F, 0.0F));

        PartDefinition rightMandible_r1 = body.addOrReplaceChild("rightMandible_r1", CubeListBuilder.create().texOffs(0, 0).addBox(4.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.5F, -7.5F, 0.0F, 3.1416F, 0.0F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.0F, -9.0F, 8.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create(), PartPose.offset(-4.0F, -1.5F, 2.5F));

        PartDefinition leftFin_r1 = leftFin.addOrReplaceChild("leftFin_r1", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-3.0F, -1.0F, -5.5F, 6.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition outerLeftFin = leftFin.addOrReplaceChild("outerLeftFin", CubeListBuilder.create(), PartPose.offset(-6.0F, 0.0F, -0.5F));

        PartDefinition outerLeftFin_r1 = outerLeftFin.addOrReplaceChild("outerLeftFin_r1", CubeListBuilder.create().texOffs(31, 0).mirror().addBox(-3.5F, -0.5F, -5.0F, 7.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create(), PartPose.offset(4.0F, -1.5F, 2.5F));

        PartDefinition rightFin_r1 = rightFin.addOrReplaceChild("rightFin_r1", CubeListBuilder.create().texOffs(23, 20).mirror().addBox(-3.0F, -1.0F, -5.5F, 6.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition outerRightFin = rightFin.addOrReplaceChild("outerRightFin", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, -0.5F));

        PartDefinition outerRightFin_r1 = outerRightFin.addOrReplaceChild("outerRightFin_r1", CubeListBuilder.create().texOffs(0, 33).mirror().addBox(-3.5F, -0.5F, -5.0F, 7.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(35, 35).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 9.0F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(0, 6).addBox(0.0F, -1.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 1.5F, 0.0F, 3.1416F, 0.0F));

        PartDefinition outerTail = tail.addOrReplaceChild("outerTail", CubeListBuilder.create().texOffs(24, 33).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 7.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(MantaRayEntity ray, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        oldPitch = ModUtil.lerp(oldPitch, headPitch * 0.011453292F, 0.05f);
        oldYaw = ModUtil.lerp(oldYaw, headYaw * 0.011453292F, 0.05f);

        this.root().xRot = oldPitch;
        this.root().yRot = oldYaw;

        if (ray.isInWater() || ray.isUnderWater()) {
            this.animateWalk(MantaRayEntityAnimations.MANTA_RAY_SWIM, limbAngle, limbDistance, 1.5f, 2f);
        } else {
            this.animate(ray.flopAnimationState, MantaRayEntityAnimations.MANTA_RAY_FLOP, animationProgress, 1.0f);
        }
    }
}
