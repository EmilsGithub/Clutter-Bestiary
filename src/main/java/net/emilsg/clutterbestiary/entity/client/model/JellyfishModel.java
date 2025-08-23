package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.JellyfishEntityAnimations;
import net.emilsg.clutterbestiary.entity.custom.JellyfishEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;


public class JellyfishModel<T extends JellyfishEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart all;

    public JellyfishModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 27.0F, 0.0F));

        ModelPartData upperBody = all.addChild("upperBody", ModelPartBuilder.create().uv(0, 10).cuboid(-3.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -8.5F, 0.0F));

        ModelPartData lowerBody = all.addChild("lowerBody", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));

        ModelPartData outerTentacles = all.addChild("outerTentacles", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData frontTop = outerTentacles.addChild("frontTop", ModelPartBuilder.create().uv(0, 26).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, -4.0F));

        ModelPartData frontBottom = frontTop.addChild("frontBottom", ModelPartBuilder.create().uv(0, 29).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData rightTop = outerTentacles.addChild("rightTop", ModelPartBuilder.create().uv(16, 12).cuboid(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -6.0F, 0.0F));

        ModelPartData rightBottom = rightTop.addChild("rightBottom", ModelPartBuilder.create().uv(16, 15).cuboid(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData backTop = outerTentacles.addChild("backTop", ModelPartBuilder.create().uv(16, 26).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 4.0F));

        ModelPartData backBottom = backTop.addChild("backBottom", ModelPartBuilder.create().uv(16, 29).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData leftTop = outerTentacles.addChild("leftTop", ModelPartBuilder.create().uv(0, 12).cuboid(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -6.0F, 0.0F));

        ModelPartData leftBottom = leftTop.addChild("leftBottom", ModelPartBuilder.create().uv(0, 15).cuboid(0.0F, 0.0F, -4.0F, 0.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData innerTentacles = all.addChild("innerTentacles", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData front = innerTentacles.addChild("front", ModelPartBuilder.create().uv(52, 18).cuboid(-3.0F, 0.0F, 0.0F, 6.0F, 14.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.0F, -3.0F));

        ModelPartData right = innerTentacles.addChild("right", ModelPartBuilder.create().uv(35, 12).cuboid(0.0F, 0.0F, -3.0F, 0.0F, 14.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -5.0F, 0.0F));

        ModelPartData back = innerTentacles.addChild("back", ModelPartBuilder.create().uv(35, 0).cuboid(-3.0F, 0.0F, 0.0F, 6.0F, 14.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.0F, 3.0F));

        ModelPartData left = innerTentacles.addChild("left", ModelPartBuilder.create().uv(52, -6).cuboid(0.0F, 0.0F, -3.0F, 0.0F, 14.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -5.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.getPart().render(matrices, vertices, light, overlay, color);
    }

    @Override
    public void setAngles(JellyfishEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.updateAnimation(entity.swimmingAnimationState, JellyfishEntityAnimations.JELLYFISH_SWIM, animationProgress, 1f);
    }
}