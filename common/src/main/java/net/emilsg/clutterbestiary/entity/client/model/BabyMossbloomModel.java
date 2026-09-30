package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.animation_handling.animation_states.MossbloomAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.MossbloomEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.MossbloomEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BabyMossbloomModel<T extends MossbloomEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<MossbloomEntity, MossbloomAnimationState> ANIMATIONS = new AnimationBindings<MossbloomEntity, MossbloomAnimationState>()
            .bind(MossbloomAnimationState.SHAKING, MossbloomEntityAnimations.MOSSBLOOM_SHAKE_HEAD);

    private final ModelPart all;
    private final ModelPart neck;
    private final ModelPart neckTwo;
    private final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;
    private final ModelPart leftHorn;
    private final ModelPart rightHorn;
    private final ModelPart torso;
    private final ModelPart tail;
    private final ModelPart saddle;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;

    public BabyMossbloomModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.neck = this.all.getChild("neck");
        this.neckTwo = this.neck.getChild("neckTwo");
        this.head = this.neckTwo.getChild("head");
        this.leftEar = this.head.getChild("leftEar");
        this.rightEar = this.head.getChild("rightEar");
        this.leftHorn = this.head.getChild("leftHorn");
        this.rightHorn = this.head.getChild("rightHorn");
        this.torso = this.all.getChild("torso");
        this.tail = this.torso.getChild("tail");
        this.saddle = this.torso.getChild("saddle");
        this.frontLeftLeg = this.all.getChild("frontLeftLeg");
        this.frontRightLeg = this.all.getChild("frontRightLeg");
        this.backLeftLeg = this.all.getChild("backLeftLeg");
        this.backRightLeg = this.all.getChild("backRightLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition neck = all.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 5.5F, -4.5F));

        PartDefinition cube_r1 = neck.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 1).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition neckTwo = neck.addOrReplaceChild("neckTwo", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, -1.0F));

        PartDefinition head = neckTwo.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 22).addBox(-1.5F, -6.0F, -7.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 20).addBox(-0.5F, -4.0F, -9.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 3.0F, 4.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create(), PartPose.offset(2.5F, -5.5F, -3.5F));

        PartDefinition leftEar_r1 = leftEar.addOrReplaceChild("leftEar_r1", CubeListBuilder.create().texOffs(24, 15).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create(), PartPose.offset(-1.5F, -5.5F, -3.5F));

        PartDefinition rightEar_r1 = rightEar.addOrReplaceChild("rightEar_r1", CubeListBuilder.create().texOffs(24, 11).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition leftHorn = head.addOrReplaceChild("leftHorn", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -7.0F, -5.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition rightHorn = head.addOrReplaceChild("rightHorn", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -7.0F, -5.0F, 0.0F, 0.3491F, 0.0F));

        PartDefinition torso = all.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(1, 1).addBox(-3.0F, -1.0F, -5.5F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(1, 12).addBox(-3.0F, 0.0F, -0.5F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 32).addBox(-3.0F, -1.0F, -5.5F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 5.0F, 0.5F));

        PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 3.5F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(24, 6).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition saddle = torso.addOrReplaceChild("saddle", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition frontLeftLeg = all.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(1, 32).addBox(-2.01F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 9.0F, -3.0F));

        PartDefinition frontRightLeg = all.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(10, 32).addBox(0.01F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 9.0F, -3.0F));

        PartDefinition backLeftLeg = all.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(1, 39).addBox(-2.01F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 9.0F, 3.0F));

        PartDefinition backRightLeg = all.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(10, 39).addBox(0.01F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 9.0F, 3.0F));

        return LayerDefinition.create(meshdefinition, 96, 64);
    }

    @Override
    public void setupAnim(MossbloomEntity mossbloom, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(mossbloom, netHeadYaw, headPitch, animationProgress);

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
}
