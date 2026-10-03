package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.CapybaraEntityAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.CapybaraEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BabyCapybaraModel<T extends CapybaraEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<CapybaraEntity, CapybaraEntityAnimationState> ANIMATIONS = new AnimationBindings<CapybaraEntity, CapybaraEntityAnimationState>()
            .bind(CapybaraEntityAnimationState.LAYING_DOWN, entity -> entity.sleeperType() == 0 ? CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_BELLY_START : CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_SIDE_START, 1f)
            .bind(CapybaraEntityAnimationState.SLEEPING, entity -> entity.sleeperType() == 0 ? CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_BELLY : CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_SIDE, 1f)
            .bind(CapybaraEntityAnimationState.STANDING_UP, entity -> entity.sleeperType() == 0 ? CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_BELLY_STOP : CapybaraEntityAnimations.CAPYBARA_LAY_DOWN_SIDE_STOP, 1f);
    private final ModelPart all;
    private final ModelPart torso;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightEar;
    private final ModelPart leftEar;
    private final ModelPart frontRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;

    public BabyCapybaraModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.torso = this.all.getChild("torso");
        this.body = this.torso.getChild("body");
        this.head = this.torso.getChild("head");
        this.rightEar = this.head.getChild("rightEar");
        this.leftEar = this.head.getChild("leftEar");
        this.frontRightLeg = this.torso.getChild("frontRightLeg");
        this.frontLeftLeg = this.torso.getChild("frontLeftLeg");
        this.backLeftLeg = this.torso.getChild("backLeftLeg");
        this.backRightLeg = this.torso.getChild("backRightLeg");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(2.25F, 21.5F, -1.5F));

        PartDefinition torso = all.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(-2.25F, 4.5F, 3.5F));

        PartDefinition body = torso.addOrReplaceChild("body", CubeListBuilder.create().texOffs(8, 22).addBox(-2.5F, -0.5F, -7.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(8, 45).addBox(-2.5F, -0.5F, -7.0F, 5.0F, 3.0F, 8.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, -8.5F, 1.0F));

        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(3, 2).addBox(-2.0F, 1.0F, -5.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(35, 24).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.5F, -5.5F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create(), PartPose.offset(-1.5F, 0.0F, 0.5F));

        PartDefinition rightEar_r1 = rightEar.addOrReplaceChild("rightEar_r1", CubeListBuilder.create().texOffs(49, 19).addBox(-2.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create(), PartPose.offset(1.5F, 0.0F, 0.5F));

        PartDefinition leftEar_r1 = leftEar.addOrReplaceChild("leftEar_r1", CubeListBuilder.create().texOffs(33, 19).addBox(0.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition frontRightLeg = torso.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(52, 40).addBox(-0.5F, -0.5F, -1.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, -4.5F, -3.5F));

        PartDefinition frontLeftLeg = torso.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(39, 40).addBox(-1.5F, -0.5F, -1.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, -4.5F, -3.5F));

        PartDefinition backLeftLeg = torso.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(39, 48).addBox(-1.5F, -0.5F, -1.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, -4.5F, 1.25F));

        PartDefinition backRightLeg = torso.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(52, 48).addBox(-0.5F, -0.5F, -1.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, -4.5F, 1.25F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(CapybaraEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (!entity.isSleeping() && !entity.isForceSleeping()) {
            this.setHeadAngles(entity, netHeadYaw, headPitch, animationProgress);
        }

        if (!entity.isInWater()) {
            this.animateWalk(CapybaraEntityAnimations.CAPYBARA_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);
        }

        this.animate(entity.earTwitchAnimationStateOne, CapybaraEntityAnimations.CAPYBARA_EAR_TWITCH_ONE, animationProgress, 1f);
        this.animate(entity.earTwitchAnimationStateTwo, CapybaraEntityAnimations.CAPYBARA_EAR_TWITCH_TWO, animationProgress, 1f);
        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::animate);
        this.animate(entity.swimAnimationState, CapybaraEntityAnimations.CAPYBARA_SWIM, animationProgress, 1f);

    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
