package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.StoatEntityAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.StoatEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.StoatEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BabyStoatModel<T extends StoatEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<StoatEntity, StoatEntityAnimationState> ANIMATIONS = new AnimationBindings<StoatEntity, StoatEntityAnimationState>()
            .bind(StoatEntityAnimationState.LAYING_DOWN, StoatEntityAnimations.STOAT_LAYING_DOWN)
            .bind(StoatEntityAnimationState.SLEEPING, StoatEntityAnimations.STOAT_SLEEPING)
            .bind(StoatEntityAnimationState.STANDING_UP, StoatEntityAnimations.STOAT_STANDING_UP)
            .bind(StoatEntityAnimationState.SIT_START, StoatEntityAnimations.STOAT_SIT_START)
            .bind(StoatEntityAnimationState.SITTING, StoatEntityAnimations.STOAT_SITTING)
            .bind(StoatEntityAnimationState.SITTING_UP, StoatEntityAnimations.STOAT_SITTING_UP)
            .bind(StoatEntityAnimationState.SIT_END, StoatEntityAnimations.STOAT_SIT_END);
    private final ModelPart all;
    private final ModelPart back;
    private final ModelPart tail;
    private final ModelPart tailTip;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart front;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;

    public BabyStoatModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.back = this.all.getChild("back");
        this.tail = this.back.getChild("tail");
        this.tailTip = this.tail.getChild("tailTip");
        this.backLeftLeg = this.back.getChild("backLeftLeg");
        this.backRightLeg = this.back.getChild("backRightLeg");
        this.front = this.all.getChild("front");
        this.frontLeftLeg = this.front.getChild("frontLeftLeg");
        this.frontRightLeg = this.front.getChild("frontRightLeg");
        this.neck = this.front.getChild("neck");
        this.head = this.neck.getChild("head");
        this.leftEar = this.head.getChild("leftEar");
        this.rightEar = this.head.getChild("rightEar");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 23.5F, -2.0F));

        PartDefinition back = all.addOrReplaceChild("back", CubeListBuilder.create().texOffs(3, 10).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 1.0F));

        PartDefinition tail = back.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, 16).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 3.0F));

        PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(21, 18).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

        PartDefinition backLeftLeg = back.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 27).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.75F, 1.5F, 2.5F));

        PartDefinition backRightLeg = back.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(5, 27).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offset(-0.75F, 1.5F, 2.5F));

        PartDefinition front = all.addOrReplaceChild("front", CubeListBuilder.create().texOffs(3, 2).addBox(-1.0F, -1.5F, -4.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 2.0F));

        PartDefinition frontLeftLeg = front.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(0, 23).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.75F, 1.5F, -2.5F));

        PartDefinition frontRightLeg = front.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(5, 23).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offset(-0.75F, 1.5F, -2.5F));

        PartDefinition neck = front.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, -4.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -2.5F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 9).addBox(-0.5F, -1.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.5F, -0.5F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(1.25F, -2.25F, 0.25F, 0.0F, -0.7854F, 0.0F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(16, 6).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-1.25F, -2.25F, 0.25F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(StoatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(entity, netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(entity.isFleeing() ? StoatEntityAnimations.STOAT_HOP_WALK : StoatEntityAnimations.STOAT_WALK, limbSwing, limbSwingAmount, 3.5f, 3.0f);

        this.animate(entity.rightEarTwitchAnimationState, StoatEntityAnimations.STOAT_RIGHT_EAR_TWITCH, ageInTicks, 1f);
        this.animate(entity.leftEarTwitchAnimationState, StoatEntityAnimations.STOAT_LEFT_EAR_TWITCH, ageInTicks, 1f);

        ANIMATIONS.apply(entity, entity.getAnimationController(), ageInTicks, this::animate);
    }

    @Override
    protected ModelPart getHeadPart() {
        return this.head;
    }
}
