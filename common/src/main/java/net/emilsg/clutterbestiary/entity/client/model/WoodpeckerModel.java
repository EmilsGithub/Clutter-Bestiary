package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.WoodpeckerAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.WoodpeckerAnimations;
import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class WoodpeckerModel extends BestiaryEntityModel<WoodpeckerEntity> {
    private static final AnimationBindings<WoodpeckerEntity, WoodpeckerAnimationState> ANIMATIONS = new AnimationBindings<WoodpeckerEntity, WoodpeckerAnimationState>()
            .bind(WoodpeckerAnimationState.FLYING, WoodpeckerAnimations.WOODPECKER_FLY)
            .bind(WoodpeckerAnimationState.HOVERING, WoodpeckerAnimations.WOODPECKER_HOVERING)
            .bind(WoodpeckerAnimationState.ATTACHED, WoodpeckerAnimations.WOODPECKER_ATTACHED)
            .bind(WoodpeckerAnimationState.PECKING, WoodpeckerAnimations.WOODPECKER_PECKING);

    private final ModelPart all;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart leftWingBottom;
    private final ModelPart rightWing;
    private final ModelPart rightWingBottom;
    private final ModelPart tail;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public WoodpeckerModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
        this.body = this.all.getChild("body");
        this.leftWing = this.body.getChild("leftWing");
        this.leftWingBottom = this.leftWing.getChild("leftWingBottom");
        this.rightWing = this.body.getChild("rightWing");
        this.rightWingBottom = this.rightWing.getChild("rightWingBottom");
        this.tail = this.body.getChild("tail");
        this.leftLeg = this.body.getChild("leftLeg");
        this.rightLeg = this.body.getChild("rightLeg");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 16.1F, -1.9F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, -1.5F, -1.75F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(11, 10).addBox(-1.0F, -2.5F, -2.75F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(-0.5F, -1.5F, -4.65F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(5, 25).addBox(-0.5F, -0.5F, -2.65F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(0.0F, -3.8F, -1.85F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.6F, -0.15F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.4F, -0.1F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(13, 0).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.4F, 0.2F, 0.1745F, 0.0F, 0.0F));

        PartDefinition leftWingBottom = leftWing.addOrReplaceChild("leftWingBottom", CubeListBuilder.create().texOffs(22, 5).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.5F));

        PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(11, 16).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.4F, 0.2F, 0.1745F, 0.0F, 0.0F));

        PartDefinition rightWingBottom = rightWing.addOrReplaceChild("rightWingBottom", CubeListBuilder.create().texOffs(20, 23).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 16).addBox(-1.5F, -0.25F, -0.5F, 3.0F, 5.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 5.5F, 1.45F, 0.8727F, 0.0F, 0.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -0.25F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F))
                .texOffs(24, 14).addBox(-1.0F, 1.75F, -1.75F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 6.0F, -1.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, -0.25F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F))
                .texOffs(10, 25).addBox(0.0F, 1.75F, -1.75F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 6.0F, -1.0F, -0.4363F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(WoodpeckerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::animate);

        if (entity.getAnimationController().getState() == WoodpeckerAnimationState.GROUND_IDLE) {
            this.head.yRot = Mth.clamp(headYaw, -30.0f, 30.0f) * Mth.DEG_TO_RAD;
            this.head.xRot = Mth.clamp(headPitch, -25.0f, 45.0f) * Mth.DEG_TO_RAD;
        }
    }
}
