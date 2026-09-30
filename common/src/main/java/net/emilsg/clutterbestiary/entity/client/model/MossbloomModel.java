package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.MossbloomAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.MossbloomEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.MossbloomEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MossbloomModel<T extends MossbloomEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<MossbloomEntity, MossbloomAnimationState> ANIMATIONS = new AnimationBindings<MossbloomEntity, MossbloomAnimationState>()
            .bind(MossbloomAnimationState.SHAKING, MossbloomEntityAnimations.MOSSBLOOM_SHAKE_HEAD);

    private final ModelPart all;
    private final ModelPart neck;
    private final ModelPart leftHorn;
    private final ModelPart rightHorn;
    private final ModelPart torso;
    private final ModelPart neckTwo;
    private final ModelPart head;
    private final ModelPart saddle;

    public MossbloomModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.neck = this.all.getChild("neck");
        this.neckTwo = this.neck.getChild("neckTwo");
        this.head = this.neckTwo.getChild("head");
        this.leftHorn = this.head.getChild("leftHorn");
        this.rightHorn = this.head.getChild("rightHorn");
        this.torso = this.all.getChild("torso");
        this.saddle = this.torso.getChild("saddle");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition neck = all.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -6.5F));

        PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create().texOffs(26, 0).addBox(-2.0F, -2.5F, -2.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 1.0784F, -0.0982F, -0.4363F, 0.0F, 0.0F));

        PartDefinition neckTwo = neck.addOrReplaceChild("neckTwo", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -1.0F));

        PartDefinition neckTwo_r1 = neckTwo.addOrReplaceChild("neckTwo_r1", CubeListBuilder.create().texOffs(40, 13).addBox(-2.0F, -2.0F, -5.275F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, -1.1345F, 0.0F, 0.0F));

        PartDefinition head = neckTwo.addOrReplaceChild("head", CubeListBuilder.create().texOffs(27, 11).addBox(-2.5F, -7.0F, -7.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(52, 0).addBox(-1.5F, -5.0F, -10.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(0.0F, -2.0F, -10.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 1.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create(), PartPose.offset(2.5F, -6.0F, -3.5F));

        PartDefinition leftEar_r1 = leftEar.addOrReplaceChild("leftEar_r1", CubeListBuilder.create().texOffs(24, 21).addBox(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create(), PartPose.offset(-2.5F, -6.0F, -3.5F));

        PartDefinition rightEar_r1 = rightEar.addOrReplaceChild("rightEar_r1", CubeListBuilder.create().texOffs(34, 8).addBox(-2.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition leftHorn = head.addOrReplaceChild("leftHorn", CubeListBuilder.create().texOffs(32, 48).mirror().addBox(-4.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 32).mirror().addBox(-4.0F, -13.0F, -0.01F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -7.0F, -5.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition rightHorn = head.addOrReplaceChild("rightHorn", CubeListBuilder.create().texOffs(32, 32).addBox(-12.0F, -13.0F, -0.01F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 48).addBox(-12.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -7.0F, -5.0F, 0.0F, 0.3491F, 0.0F));

        PartDefinition torso = all.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.5F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, -3.0F, -0.5F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-4.0F, -4.0F, -8.5F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.5F));

        PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 6.5F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(36, 2).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition saddle = torso.addOrReplaceChild("saddle", CubeListBuilder.create().texOffs(62, 0).addBox(-4.5F, -4.35F, -5.0F, 9.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition frontLeftLeg = all.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(24, 35).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 4.0F, -6.0F));

        PartDefinition frontRightLeg = all.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(16, 31).addBox(0.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 4.0F, -6.0F));

        PartDefinition backLeftLeg = all.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(8, 31).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 4.0F, 6.0F));

        PartDefinition backRightLeg = all.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 4.0F, 6.0F));
        return LayerDefinition.create(modelData, 96, 64);
    }

    @Override
    public void setupAnim(MossbloomEntity mossbloom, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(mossbloom, netHeadYaw, headPitch, animationProgress);
        this.updateVisibleParts(mossbloom);

        if (!mossbloom.getIsShaking()) {
            this.animateWalk(mossbloom.isFleeing() || mossbloom.getSprinting() ? MossbloomEntityAnimations.MOSSBLOOM_RUN : MossbloomEntityAnimations.MOSSBLOOM_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);
        }

        ANIMATIONS.apply(mossbloom, mossbloom.getAnimationController(), animationProgress, this::animate);

        this.animate(mossbloom.earTwitchAnimationStateLE, MossbloomEntityAnimations.MOSSBLOOM_LE_DROP, animationProgress, 2f);
        this.animate(mossbloom.earTwitchAnimationStateRE, MossbloomEntityAnimations.MOSSBLOOM_RE_DROP, animationProgress, 2f);
        this.animate(mossbloom.earTwitchAnimationStateBE, MossbloomEntityAnimations.MOSSBLOOM_EARS_DROP, animationProgress, 2f);
        this.animate(mossbloom.wagTailAnimationStateBE, MossbloomEntityAnimations.MOSSBLOOM_WAG_TAIL, animationProgress, 2f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return neck;
    }

    private void updateVisibleParts(MossbloomEntity mossbloom) {
        boolean horns = mossbloom.getHasHorns();
        boolean hasSaddle = mossbloom.getIsSaddled();

        leftHorn.visible = horns && !mossbloom.isBaby();
        rightHorn.visible = horns && !mossbloom.isBaby();

        saddle.visible = hasSaddle && !mossbloom.isBaby();
    }

    @Override
    public float getBabyScale() {
        return DEFAULT_BABY_SCALE;
    }

    @Override
    public float getBabyYOffset() {
        return DEFAULT_BABY_Y_OFFSET;
    }

    @Override
    protected ModelPart getBabyHead() {
        return this.getHeadPart();
    }
}
