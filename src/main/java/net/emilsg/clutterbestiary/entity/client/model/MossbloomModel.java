package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.MossbloomEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.MossbloomEntity;
import net.emilsg.clutterbestiary.entity.variants.MossbloomVariant;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class MossbloomModel<T extends MossbloomEntity> extends ParentTameableModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart neck;
    private final ModelPart leftHorn;
    private final ModelPart rightHorn;
    private final ModelPart torso;
    private final ModelPart neckTwo;
    private final ModelPart head;
    private final ModelPart saddle;

    public MossbloomModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.neck = this.all.getChild("neck");
        this.neckTwo = this.neck.getChild("neckTwo");
        this.head = this.neckTwo.getChild("head");
        this.leftHorn = this.head.getChild("leftHorn");
        this.rightHorn = this.head.getChild("rightHorn");
        this.torso = this.all.getChild("torso");
        this.saddle = this.torso.getChild("saddle");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.0F, 0.0F));

        ModelPartData neck = all.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, -6.5F));

        ModelPartData neck_r1 = neck.addChild("neck_r1", ModelPartBuilder.create().uv(26, 0).cuboid(-2.0F, -2.5F, -2.5F, 4.0F, 3.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 1.0784F, -0.0982F, -0.4363F, 0.0F, 0.0F));

        ModelPartData neckTwo = neck.addChild("neckTwo", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, -1.0F));

        ModelPartData neckTwo_r1 = neckTwo.addChild("neckTwo_r1", ModelPartBuilder.create().uv(40, 13).cuboid(-2.0F, -2.0F, -5.275F, 4.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -1.0F, -1.1345F, 0.0F, 0.0F));

        ModelPartData head = neckTwo.addChild("head", ModelPartBuilder.create().uv(27, 11).cuboid(-2.5F, -7.0F, -7.0F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(52, 0).cuboid(-1.5F, -5.0F, -10.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 37).cuboid(0.0F, -2.0F, -10.0F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 1.0F));

        ModelPartData leftEar = head.addChild("leftEar", ModelPartBuilder.create(), ModelTransform.pivot(2.5F, -6.0F, -3.5F));

        ModelPartData leftEar_r1 = leftEar.addChild("leftEar_r1", ModelPartBuilder.create().uv(24, 21).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        ModelPartData rightEar = head.addChild("rightEar", ModelPartBuilder.create(), ModelTransform.pivot(-2.5F, -6.0F, -3.5F));

        ModelPartData rightEar_r1 = rightEar.addChild("rightEar_r1", ModelPartBuilder.create().uv(34, 8).cuboid(-2.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        ModelPartData leftHorn = head.addChild("leftHorn", ModelPartBuilder.create().uv(32, 48).mirrored().cuboid(-4.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 32).mirrored().cuboid(-4.0F, -13.0F, -0.01F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.0F, -7.0F, -5.0F, 0.0F, -0.3491F, 0.0F));

        ModelPartData rightHorn = head.addChild("rightHorn", ModelPartBuilder.create().uv(32, 32).cuboid(-12.0F, -13.0F, -0.01F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(32, 48).cuboid(-12.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -7.0F, -5.0F, 0.0F, 0.3491F, 0.0F));

        ModelPartData torso = all.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -8.5F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-4.0F, -3.0F, -0.5F, 8.0F, 7.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 48).cuboid(-4.0F, -4.0F, -8.5F, 8.0F, 8.0F, 8.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.5F));

        ModelPartData tail = torso.addChild("tail", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, 6.5F));

        ModelPartData tail_r1 = tail.addChild("tail_r1", ModelPartBuilder.create().uv(36, 2).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData saddle = torso.addChild("saddle", ModelPartBuilder.create().uv(62, 0).cuboid(-4.5F, -4.35F, -5.0F, 9.0F, 9.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData frontLeftLeg = all.addChild("frontLeftLeg", ModelPartBuilder.create().uv(24, 35).cuboid(-2.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 4.0F, -6.0F));

        ModelPartData frontRightLeg = all.addChild("frontRightLeg", ModelPartBuilder.create().uv(16, 31).cuboid(0.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 4.0F, -6.0F));

        ModelPartData backLeftLeg = all.addChild("backLeftLeg", ModelPartBuilder.create().uv(8, 31).cuboid(-2.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 4.0F, 6.0F));

        ModelPartData backRightLeg = all.addChild("backRightLeg", ModelPartBuilder.create().uv(0, 31).cuboid(0.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 4.0F, 6.0F));
        return TexturedModelData.of(modelData, 96, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        if (this.child) {
            float babyScale = 0.75f;
            matrices.push();
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 0.5D, 0D);
            this.getPart().render(matrices, vertices, light, overlay, color);
            matrices.pop();
            this.getHeadPart().scale(createVec3f(1.0f));
        } else {
            matrices.push();
            this.getPart().render(matrices, vertices, light, overlay, color);
            matrices.pop();
        }    }

    @Override
    public void setAngles(MossbloomEntity mossbloom, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(mossbloom, netHeadYaw, headPitch, ageInTicks);
        this.updateVisibleParts(mossbloom);

        this.animateMovement(mossbloom.isFleeing() || mossbloom.getSprinting() ? MossbloomEntityAnimations.MOSSBLOOM_RUN : MossbloomEntityAnimations.MOSSBLOOM_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);

        if (mossbloom.isVariantOf(MossbloomVariant.HORNED))
            this.updateAnimation(mossbloom.shakingAnimationState, MossbloomEntityAnimations.MOSSBLOOM_SHAKE_HEAD, ageInTicks, 1f);
        this.updateAnimation(mossbloom.idleAnimationState, MossbloomEntityAnimations.MOSSBLOOM_IDLE, ageInTicks, 1f);

        this.updateAnimation(mossbloom.earTwitchAnimationStateLE, MossbloomEntityAnimations.MOSSBLOOM_LE_DROP, ageInTicks, 2f);
        this.updateAnimation(mossbloom.earTwitchAnimationStateRE, MossbloomEntityAnimations.MOSSBLOOM_RE_DROP, ageInTicks, 2f);
        this.updateAnimation(mossbloom.earTwitchAnimationStateBE, MossbloomEntityAnimations.MOSSBLOOM_EARS_DROP, ageInTicks, 2f);
        this.updateAnimation(mossbloom.wagTailAnimationStateBE, MossbloomEntityAnimations.MOSSBLOOM_WAG_TAIL, ageInTicks, 2f);
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
}
