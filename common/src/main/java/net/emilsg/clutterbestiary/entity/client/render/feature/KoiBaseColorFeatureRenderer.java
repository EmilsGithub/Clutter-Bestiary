package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiBaseColorVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class KoiBaseColorFeatureRenderer extends FeatureRenderer<KoiEntity, KoiModel<KoiEntity>> {
    private final KoiModel<KoiEntity> layerModel;

    public KoiBaseColorFeatureRenderer(FeatureRendererContext<KoiEntity, KoiModel<KoiEntity>> context, EntityModelLoader loader) {
        super(context);
        this.layerModel = new KoiModel<>(loader.getModelPart(ModModelLayers.KOI_BASE));
    }

    private static int getColor(KoiEntity koiEntity, float tickDelta) {
        int color;

        if (koiEntity.getBaseColorVariant().equals(KoiBaseColorVariant.IRIDESCENT_RAINBOW)) {
            float speed = 4.5f;
            float hue = ((koiEntity.age + tickDelta) * speed % 360) / 360.0f;
            float saturation = 0.4f;
            float brightness = 1.0f;
            color = Color.HSBtoRGB(hue, saturation, brightness);
        } else {
            if (koiEntity.getBaseColorVariant().hasSeparateTexture()) return 0;
            color = koiEntity.getBaseColorVariant().getColorHex()[0];
        }

        return color;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider providers, int light, KoiEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        int rgb = getColor(entity, tickDelta);
        if(rgb == 0) return;
        int argb = 0xFF000000 | (rgb & 0x00FFFFFF);
        Identifier texture = getTexture(entity);

        if (entity.isInvisible()) {
            var mc = MinecraftClient.getInstance();
            if (mc.hasOutline(entity)) {
                getContextModel().copyStateTo(layerModel);
                layerModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
                layerModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
                VertexConsumer vc = providers.getBuffer(RenderLayer.getOutline(texture));

                layerModel.render(matrices, vc, light, LivingEntityRenderer.getOverlay(entity, 0.0F), 0xFF000000);
            }
            return;
        }

        render(getContextModel(), layerModel, texture, matrices, providers, light, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, tickDelta, argb);
    }
}
