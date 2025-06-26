package net.emilsg.clutter_bestiary.entity.client.render.feature;

import net.emilsg.clutter_bestiary.entity.client.model.KoiModel;
import net.emilsg.clutter_bestiary.entity.custom.KoiEntity;
import net.emilsg.clutter_bestiary.entity.variants.koi.KoiBaseColorVariant;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class KoiBaseColorFeatureRenderer extends FeatureRenderer<KoiEntity, KoiModel<KoiEntity>> {

    public KoiBaseColorFeatureRenderer(FeatureRendererContext<KoiEntity, KoiModel<KoiEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, KoiEntity koiEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        int color;
        Identifier texture;

        if (koiEntity.getBaseColorVariant().equals(KoiBaseColorVariant.IRIDESCENT_RAINBOW)) {
            float speed = 4.5f;
            float hue = ((koiEntity.age + tickDelta) * speed % 360) / 360.0f;
            float saturation = 0.4f;
            float brightness = 1.0f;
            color = Color.HSBtoRGB(hue, saturation, brightness);
            texture = koiEntity.getBaseColorVariant().getTextureLocation();
        } else {
            if (koiEntity.getBaseColorVariant().hasSeparateTexture()) return;
            color = koiEntity.getBaseColorVariant().getColorHex()[0];
            texture = koiEntity.getBaseColorVariant().getTextureLocation();
        }


        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture));
        this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
    }


    /**
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, KoiEntity koiEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (koiEntity.getBaseColorVariant().hasSeparateTexture()) return;
        int patternColor = koiEntity.getBaseColorVariant().getColorHex()[0];

        float r = ((patternColor >> 16) & 0xFF) / 255.0F;
        float g = ((patternColor >> 8) & 0xFF) / 255.0F;
        float b = (patternColor & 0xFF) / 255.0F;

        Identifier patternTexture = koiEntity.getBaseColorVariant().getTextureLocation();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(patternTexture));
        this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
    }
    */
}
