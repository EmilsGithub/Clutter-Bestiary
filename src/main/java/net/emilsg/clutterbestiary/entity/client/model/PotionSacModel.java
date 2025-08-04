package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.custom.PotionSacEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;


public class PotionSacModel<T extends PotionSacEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart all;


    public PotionSacModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData potionSac = all.addChild("potionSac", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, 0.0F, -3.5F, 7.0F, 8.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(PotionSacEntity potionSac, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
    }
}