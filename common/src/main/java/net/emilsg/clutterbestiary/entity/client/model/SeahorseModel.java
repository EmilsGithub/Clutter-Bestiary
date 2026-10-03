package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.entity.client.animation.SeahorseEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentFishModel;
import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SeahorseModel<T extends SeahorseEntity> extends ParentFishModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart stomach;
    private final ModelPart head;


    public SeahorseModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = all.getChild("body");
        this.head = body.getChild("head");
        this.stomach = body.getChild("stomach");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -2.0F, 1.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 2).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(2, 6).addBox(-0.5F, 1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition stomach = body.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(6, 7).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(-0.5F, 1.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(8, 0).addBox(-0.5F, -1.0F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 7).addBox(0.0F, -3.0F, -0.5F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -0.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 2.0F));

        PartDefinition tailOne = tail.addOrReplaceChild("tailOne", CubeListBuilder.create().texOffs(10, 3).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 5).addBox(0.0F, 0.0F, 0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -1.5F));

        PartDefinition tailTwo = tailOne.addOrReplaceChild("tailTwo", CubeListBuilder.create().texOffs(3, 10).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        return LayerDefinition.create(modelData, 16, 16);
    }

    @Override
    public void setupAnim(SeahorseEntity seahorse, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.updateParts(seahorse);
        if (seahorse.isInWater() && !seahorse.isDeadOrDying())
            this.animate(seahorse.swimmingAnimationState, SeahorseEntityAnimations.SEAHORSE_SWIM, animationProgress, 1.0f);
        else if (!seahorse.isDeadOrDying())
            this.animate(seahorse.flopAnimationState, SeahorseEntityAnimations.SEAHORSE_FLOP, animationProgress, 1.0f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }

    private void updateParts(SeahorseEntity seahorse) {
        boolean hasChildren = seahorse.hasChildren();
        if (hasChildren) {
            this.stomach.offsetScale(createVec3f(seahorse.getHasChildrenTimer()));
        }
    }
}
