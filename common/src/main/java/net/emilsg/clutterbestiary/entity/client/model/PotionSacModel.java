package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.custom.PotionSacEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;


public class PotionSacModel<T extends PotionSacEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart potionSac;
    private final ModelPart outer;


    public PotionSacModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.potionSac = all.getChild("potionSac");
        this.outer = potionSac.getChild("outer");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData potionSac = all.addChild("potionSac", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData inner = potionSac.addChild("inner", ModelPartBuilder.create().uv(2, 35).cuboid(-3.0F, -7.5F, -4.0F, 5.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 8.0F, 1.5F));

        ModelPartData outer = potionSac.addChild("outer", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -5.0F, 7.0F, 8.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 8.0F, 1.5F));
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
    public void setAngles(PotionSacEntity potionSac, float limbSwing, float limbSwingAmount, float animationProgress, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
    }

    public void setOuterHidden(boolean hidden) {
        this.outer.hidden = hidden;
    }
}