package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiSecondaryPatternTypeVariant;
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

public class KoiSecondaryPatternColorFeatureRenderer extends FeatureRenderer<KoiEntity, KoiModel<KoiEntity>> {
    private final KoiModel<KoiEntity> layerModel;

    public KoiSecondaryPatternColorFeatureRenderer(FeatureRendererContext<KoiEntity, KoiModel<KoiEntity>> context, EntityModelLoader loader) {
        super(context);
        this.layerModel = new KoiModel<>(loader.getModelPart(ModModelLayers.KOI_SECONDARY_COLOR));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider providers, int light, KoiEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getBaseColorVariant().hasSeparateTexture()) return;
        if (entity.getSecondaryPatternTypeVariant() == KoiSecondaryPatternTypeVariant.NONE) return;
        int rgb = entity.getSecondaryPatternColorVariant().getColorHex();
        if(rgb == 0) return;
        int argb = 0xFF000000 | (rgb & 0x00FFFFFF);
        Identifier texture = entity.getSecondaryPatternTypeVariant().getTextureLocation();

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
