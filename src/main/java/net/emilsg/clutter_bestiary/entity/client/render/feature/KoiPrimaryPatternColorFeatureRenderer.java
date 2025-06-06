package net.emilsg.clutter_bestiary.entity.client.render.feature;

import net.emilsg.clutter_bestiary.entity.client.model.KoiModel;
import net.emilsg.clutter_bestiary.entity.custom.KoiEntity;
import net.emilsg.clutter_bestiary.entity.variants.koi.KoiPrimaryPatternTypeVariant;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class KoiPrimaryPatternColorFeatureRenderer extends FeatureRenderer<KoiEntity, KoiModel<KoiEntity>> {

    public KoiPrimaryPatternColorFeatureRenderer(FeatureRendererContext<KoiEntity, KoiModel<KoiEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, KoiEntity koiEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(koiEntity.getPrimaryPatternTypeVariant() == KoiPrimaryPatternTypeVariant.NONE) return;
        int patternColor = koiEntity.getPrimaryPatternColorVariant().getColorHex();

        float r = ((patternColor >> 16) & 0xFF) / 255.0F;
        float g = ((patternColor >> 8) & 0xFF) / 255.0F;
        float b = (patternColor & 0xFF) / 255.0F;

        Identifier patternTexture = koiEntity.getPrimaryPatternTypeVariant().getTextureLocation();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(patternTexture));
        this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
    }
}
