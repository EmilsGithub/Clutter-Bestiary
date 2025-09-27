package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.ButterflyEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;

public class ButterflyModel<T extends ButterflyEntity> extends BestiaryModel<T> {
    private final ModelPart all;
    private final ModelPart root;

    public ButterflyModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.of(0.0F, 20.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(3, 3).cuboid(0.25F, -1.8848F, -0.3293F, 0.5F, 4.0F, 0.5F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, -0.1152F, -0.4207F));

        ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(4, 3).cuboid(0.0F, -1.0F, 0.0F, 0.25F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.25F, -1.75F, -0.25F, 0.2182F, 0.0F, -0.2182F));

        ModelPartData cube_r2 = body.addChild("cube_r2", ModelPartBuilder.create().uv(4, 3).cuboid(-0.25F, -1.0F, 0.0F, 0.25F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.75F, -1.75F, -0.25F, 0.2182F, 0.0F, 0.2182F));

        ModelPartData leftWing = body.addChild("leftWing", ModelPartBuilder.create().uv(0, 8).mirrored().cuboid(0.0F, -3.8848F, -0.0793F, 4.0F, 8.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.75F, 0.0F, 0.0F));

        ModelPartData rightWing = body.addChild("rightWing", ModelPartBuilder.create().uv(0, 8).cuboid(-4.0F, -3.8848F, -0.0793F, 4.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.25F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 16, 16);
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
    public void setAngles(ButterflyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        Animation flightAnim;

        switch (entity.getFlyingTypeVariant()) {
            case 1 -> flightAnim = ButterflyEntityAnimations.BUTTERFLY_FLYING_TWO;
            case 2 -> flightAnim = ButterflyEntityAnimations.BUTTERFLY_FLYING_THREE;
            default -> flightAnim = ButterflyEntityAnimations.BUTTERFLY_FLYING_ONE;
        }

        this.updateAnimation(entity.flyingAnimState, flightAnim, ageInTicks, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return null;
    }
}