package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.model.PotionSacModel;
import net.emilsg.clutterbestiary.entity.custom.PotionSacEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;

public class PotionSacOuterFeatureRenderer extends FeatureRenderer<PotionSacEntity, PotionSacModel<PotionSacEntity>> {

    public PotionSacOuterFeatureRenderer(FeatureRendererContext<PotionSacEntity, PotionSacModel<PotionSacEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PotionSacEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        PotionSacModel<PotionSacEntity> model = this.getContextModel();
        model.setOuterHidden(false);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(entity.getVariant().getTextureLocation()));

        model.getPart().render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0F), -1);
    }
}

