package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.DragonflyEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.DragonflyEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;


public class DragonflyModel<T extends DragonflyEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart head;

    public DragonflyModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 22.0F, -4.1F));

        ModelPartData head = all.addChild("head", ModelPartBuilder.create().uv(8, 25).cuboid(0.5F, -0.5F, -1.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.25F))
                .uv(0, 25).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.125F))
                .uv(12, 25).cuboid(-1.5F, -0.5F, -1.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -1.0F, -0.9F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, -2.0F, -0.9F, 2.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(18, 16).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(18, 22).cuboid(-1.5F, 0.0F, 4.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 6.1F));

        ModelPartData rightFrontWing = body.addChild("rightFrontWing", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -1.0F, 10.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.75F, -1.75F, 1.1F));

        ModelPartData leftFrontWing = body.addChild("leftFrontWing", ModelPartBuilder.create().uv(0, 12).cuboid(0.0F, 0.0F, -1.0F, 10.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, -1.75F, 1.1F));

        ModelPartData rightBackWing = body.addChild("rightBackWing", ModelPartBuilder.create().uv(0, 4).cuboid(-10.0F, 0.0F, -2.0F, 10.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.75F, -1.75F, 4.1F));

        ModelPartData leftBackWing = body.addChild("leftBackWing", ModelPartBuilder.create().uv(0, 8).cuboid(0.0F, 0.0F, -2.0F, 10.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, -1.75F, 4.1F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        float scale = child ? 0.5f : 1f;

        matrices.push();
        matrices.scale(scale, scale, scale);
        matrices.translate(0.0f, child ? 1.5f : 0.0f, 0.0f);
        this.getPart().render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    @Override
    public void setAngles(DragonflyEntity dragonfly, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(dragonfly, netHeadYaw, headPitch, ageInTicks);

        this.updateAnimation(dragonfly.flyingAnimState, DragonflyEntityAnimations.DRAGONFLY_FLY, ageInTicks, 2.5f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}