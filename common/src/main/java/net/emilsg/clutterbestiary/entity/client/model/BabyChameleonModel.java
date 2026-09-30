package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.ChameleonEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.ChameleonEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BabyChameleonModel<T extends ChameleonEntity> extends ParentTameableModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart backCrest;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart tounge;
    private final ModelPart rightEye;
    private final ModelPart leftEye;
    private final ModelPart tail;
    private final ModelPart frontRightLeg;
    private final ModelPart backRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart backLeftLeg;

    public BabyChameleonModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.backCrest = this.body.getChild("backCrest");
        this.head = this.body.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.tounge = this.head.getChild("tounge");
        this.rightEye = this.head.getChild("rightEye");
        this.leftEye = this.head.getChild("leftEye");
        this.tail = this.body.getChild("tail");
        this.frontRightLeg = this.body.getChild("frontRightLeg");
        this.backRightLeg = this.body.getChild("backRightLeg");
        this.frontLeftLeg = this.body.getChild("frontLeftLeg");
        this.backLeftLeg = this.body.getChild("backLeftLeg");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 20.5F, 4.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(2, 16).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.5F, -2.0F));

        PartDefinition backCrest = body.addOrReplaceChild("backCrest", CubeListBuilder.create().texOffs(22, 18).mirror().addBox(0.0F, -2.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -4.0F, -2.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 7).addBox(-1.025F, -0.5F, -3.0F, 2.05F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, 9).addBox(-1.025F, 0.5F, -1.0F, 2.05F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(25, 17).mirror().addBox(-0.5F, -1.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(19, 2).addBox(-1.5F, -0.51F, -3.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, -3.5F, -4.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(17, 14).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -1.0F));

        PartDefinition tounge = head.addOrReplaceChild("tounge", CubeListBuilder.create().texOffs(8, 2).addBox(-0.5F, 0.75F, -3.0F, 1.0F, 0.5F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.25F, 0.25F));

        PartDefinition rightEye = head.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(14, 9).addBox(0.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, -0.5F, -2.0F));

        PartDefinition leftEye = head.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(14, 9).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, -0.5F, -2.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(2, 26).addBox(-0.5F, -1.5F, 1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 23).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 0.0F));

        PartDefinition frontRightLeg = body.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(17, 28).addBox(-0.25F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -2.5F, -3.5F));

        PartDefinition backRightLeg = body.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(25, 28).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.75F, -2.5F, -0.5F));

        PartDefinition frontLeftLeg = body.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(13, 28).addBox(-0.75F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.5F, -3.5F));

        PartDefinition backLeftLeg = body.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(21, 28).addBox(-0.75F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(ChameleonEntity chameleon, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(chameleon, headYaw, headPitch, headPitch);
        if (!chameleon.isOrderedToSit()) {
            this.animateWalk(ChameleonEntityAnimations.CHAMELEON_WALK, limbAngle, limbDistance, 1.5f, 2f);
        } else {
            this.animate(chameleon.sittingAnimationState, ChameleonEntityAnimations.BABY_CHAMELEON_LAY_DOWN, animationProgress, 1.0f);
        }

        this.animate(chameleon.toungeIdleAnimationState, ChameleonEntityAnimations.CHAMELEON_LICK_IDLE, animationProgress, 1f);
        this.animate(chameleon.tailIdleAnimationState, ChameleonEntityAnimations.CHAMELEON_TAIL_IDLE, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }

    protected void setHeadAngles(ChameleonEntity entity, float headYaw, float headPitch, float animationProgress) {
        if (getHeadPart() == null) return;

        boolean isAttacking = entity.isAggressive();

        float maxYaw = isAttacking ? 60.0F : 30.0F;
        float minYaw = -maxYaw;

        float maxPitch = isAttacking ? 60.0F : 45.0F;
        float minPitch = isAttacking ? -45.0F : -25.0F;

        headYaw = Mth.clamp(headYaw, minYaw, maxYaw);
        headPitch = Mth.clamp(headPitch, minPitch, maxPitch);

        getHeadPart().yRot = headYaw * 0.017453292F;
        getHeadPart().xRot = headPitch * 0.017453292F;
    }
}
