package net.emilsg.clutter_bestiary.entity.client.model;

import net.emilsg.clutter_bestiary.entity.client.animation.EmberTortoiseEntityAnimations;
import net.emilsg.clutter_bestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutter_bestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class EmberTortoiseModel<T extends EmberTortoiseEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart root;

    public EmberTortoiseModel(ModelPart root) {
        this.root = root;
        this.all = this.root.getChild("all");
        this.body = this.all.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, -10.0F, -12.0F, 20.0F, 4.0F, 24.0F, new Dilation(0.0F))
                .uv(7, 30).cuboid(-8.0F, -6.0F, -10.0F, 16.0F, 1.0F, 20.0F, new Dilation(0.0F))
                .uv(64, 41).cuboid(-6.0F, -14.0F, -12.0F, 12.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData shell = body.addChild("shell", ModelPartBuilder.create().uv(0, 55).cuboid(-9.0F, -3.0F, -11.0F, 18.0F, 6.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

        ModelPartData shellTwo = shell.addChild("shellTwo", ModelPartBuilder.create().uv(0, 91).cuboid(-8.0F, -18.0F, -10.0F, 16.0F, 3.0F, 20.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        ModelPartData frontLeftTube = shellTwo.addChild("frontLeftTube", ModelPartBuilder.create().uv(0, 67).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, -18.0F, -6.0F, 0.0F, 0.0F, 0.2182F));

        ModelPartData frontMiddleTube = shellTwo.addChild("frontMiddleTube", ModelPartBuilder.create().uv(0, 83).cuboid(-2.0F, -6.0F, -2.0F, 4.0F, 7.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -18.0F, -7.0F, 0.2182F, 0.0F, 0.0F));

        ModelPartData frontRightTube = shellTwo.addChild("frontRightTube", ModelPartBuilder.create().uv(0, 13).cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, -18.0F, -6.5F, 0.0F, 0.0F, -0.2182F));

        ModelPartData middleLeftTube = shellTwo.addChild("middleLeftTube", ModelPartBuilder.create().uv(80, 64).cuboid(-3.0F, -5.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        ModelPartData middleRightTube = shellTwo.addChild("middleRightTube", ModelPartBuilder.create().uv(58, 55).cuboid(-3.5F, -7.0F, -3.5F, 7.0F, 8.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, -18.0F, 0.5F, 0.0F, 0.0F, -0.2182F));

        ModelPartData backLeftTube = shellTwo.addChild("backLeftTube", ModelPartBuilder.create().uv(0, 55).cuboid(-2.0F, -7.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, -18.0F, 7.0F, 0.0F, 0.0F, 0.2182F));

        ModelPartData backMiddleTube = shellTwo.addChild("backMiddleTube", ModelPartBuilder.create().uv(82, 22).cuboid(-3.0F, -5.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -18.0F, 7.0F, -0.2182F, 0.0F, 0.0F));

        ModelPartData backRightTube = shellTwo.addChild("backRightTube", ModelPartBuilder.create().uv(16, 83).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -18.0F, 7.0F, 0.0F, 0.0F, -0.2182F));

        ModelPartData frontRightLeg = body.addChild("frontRightLeg", ModelPartBuilder.create().uv(64, 28).cuboid(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-7.25F, -6.5F, -9.25F));

        ModelPartData backRightLeg = body.addChild("backRightLeg", ModelPartBuilder.create().uv(0, 28).cuboid(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-7.25F, -6.5F, 9.25F));

        ModelPartData frontLeftLeg = body.addChild("frontLeftLeg", ModelPartBuilder.create().uv(74, 77).cuboid(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(7.25F, -6.5F, -9.25F));

        ModelPartData backLeftLeg = body.addChild("backLeftLeg", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -0.5F, -3.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(7.25F, -6.5F, 9.25F));

        ModelPartData neck = body.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -8.0F, -12.0F));

        ModelPartData throat = neck.addChild("throat", ModelPartBuilder.create().uv(0, 41).cuboid(-3.0F, -2.5F, -2.0F, 6.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.4239F, -1.3827F));

        ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(64, 11).cuboid(-4.0F, -3.0F, -7.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -1.25F, -4.0F));

        ModelPartData lowerHead = head.addChild("lowerHead", ModelPartBuilder.create().uv(64, 0).cuboid(-4.0F, -1.0F, -7.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (this.child) {
            float babyScale = 0.5f;
            matrices.push();
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5D, 0D);
            this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            matrices.pop();
            this.head.scale(createVec3f(0.9f));
        } else {
            matrices.push();
            this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            matrices.pop();
        }
    }

    @Override
    public void setAngles(EmberTortoiseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, netHeadYaw, headPitch, ageInTicks);

        if (!entity.isShielding())
            this.animateMovement(EmberTortoiseEntityAnimations.EMBER_TORTOISE_WALK, limbSwing, limbSwingAmount, 3f, 2f);
        if (entity.isShielding()) {
            this.updateAnimation(entity.shieldingAnimationState, EmberTortoiseEntityAnimations.EMBER_TORTOISE_SHIELD, ageInTicks, 1f);
            this.updateAnimation(entity.shieldingTubeAnimationState, EmberTortoiseEntityAnimations.EMBER_TORTOISE_SHIELD_TUBE_LOOP, ageInTicks, 1f);
        }

        this.updateAnimation(entity.attackAnimationState, EmberTortoiseEntityAnimations.EMBER_TORTOISE_ATTACK, ageInTicks, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}