package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.MantaRayEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryAquaticModel;
import net.emilsg.clutterbestiary.entity.custom.MantaRayEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class MantaRayModel<T extends MantaRayEntity> extends BestiaryAquaticModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private float oldPitch = 0.0f;
    private float oldYaw = 0.0f;

    public MantaRayModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 1.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -3.0F));

        ModelPartData leftMandible_r1 = body.addChild("leftMandible_r1", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-5.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.5F, -1.5F, -7.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData rightMandible_r1 = body.addChild("rightMandible_r1", ModelPartBuilder.create().uv(0, 0).cuboid(4.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -1.5F, -7.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData body_r1 = body.addChild("body_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -3.0F, -9.0F, 8.0F, 3.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData leftFin = body.addChild("leftFin", ModelPartBuilder.create(), ModelTransform.pivot(-4.0F, -1.5F, 2.5F));

        ModelPartData leftFin_r1 = leftFin.addChild("leftFin_r1", ModelPartBuilder.create().uv(0, 18).mirrored().cuboid(-3.0F, -1.0F, -5.5F, 6.0F, 2.0F, 11.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData outerLeftFin = leftFin.addChild("outerLeftFin", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 0.0F, -0.5F));

        ModelPartData outerLeftFin_r1 = outerLeftFin.addChild("outerLeftFin_r1", ModelPartBuilder.create().uv(31, 0).mirrored().cuboid(-3.5F, -0.5F, -5.0F, 7.0F, 1.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.5F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData rightFin = body.addChild("rightFin", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, -1.5F, 2.5F));

        ModelPartData rightFin_r1 = rightFin.addChild("rightFin_r1", ModelPartBuilder.create().uv(23, 20).mirrored().cuboid(-3.0F, -1.0F, -5.5F, 6.0F, 2.0F, 11.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData outerRightFin = rightFin.addChild("outerRightFin", ModelPartBuilder.create(), ModelTransform.pivot(6.0F, 0.0F, -0.5F));

        ModelPartData outerRightFin_r1 = outerRightFin.addChild("outerRightFin_r1", ModelPartBuilder.create().uv(0, 33).mirrored().cuboid(-3.5F, -0.5F, -5.0F, 7.0F, 1.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.5F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(35, 35).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 9.0F));

        ModelPartData tail_r1 = tail.addChild("tail_r1", ModelPartBuilder.create().uv(0, 6).cuboid(0.0F, -1.0F, -1.5F, 0.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 1.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData outerTail = tail.addChild("outerTail", ModelPartBuilder.create().uv(24, 33).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 7.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return all;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.getPart().render(matrices, vertices, light, overlay, color);
    }

    @Override
    public void setAngles(MantaRayEntity ray, float limbAngle, float limbDistance, float ageInTicks, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        oldPitch = lerp(oldPitch, headPitch * 0.011453292F, 0.05f);
        oldYaw = lerp(oldYaw, headYaw * 0.011453292F, 0.05f);

        this.getPart().pitch = oldPitch;
        this.getPart().yaw = oldYaw;

        if (ray.isTouchingWater() || ray.isSubmergedInWater()) {
            this.animateMovement(MantaRayEntityAnimations.MANTA_RAY_SWIM, limbAngle, limbDistance, 1.5f, 2f);
        } else {
            this.updateAnimation(ray.flopAnimationState, MantaRayEntityAnimations.MANTA_RAY_FLOP, ageInTicks, 1.0f);
        }
    }

    private float lerp(float a, float b, float alpha) {
        return a + (b - a) * alpha;
    }

}
