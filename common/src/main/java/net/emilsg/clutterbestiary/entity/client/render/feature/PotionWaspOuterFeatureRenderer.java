package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.model.PotionWaspModel;
import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;

public class PotionWaspOuterFeatureRenderer extends FeatureRenderer<PotionWaspEntity, PotionWaspModel<PotionWaspEntity>> {

    public PotionWaspOuterFeatureRenderer(FeatureRendererContext<PotionWaspEntity, PotionWaspModel<PotionWaspEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PotionWaspEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.hasPotionSac()) return;

        PotionWaspModel<PotionWaspEntity> model = this.getContextModel();

        model.showOnlyOuter();

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(entity.getVariant().getTextureLocation()));

        model.getPart().render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0F), -1);
        model.setAllPartsHidden(false);
    }
}
