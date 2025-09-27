package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiPrimaryPatternTypeVariant;
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

public class KoiPrimaryPatternColorFeatureRenderer extends FeatureRenderer<KoiEntity, KoiModel<KoiEntity>> {
    private final KoiModel<KoiEntity> layerModel;

    public KoiPrimaryPatternColorFeatureRenderer(FeatureRendererContext<KoiEntity, KoiModel<KoiEntity>> context, EntityModelLoader loader) {
        super(context);
        this.layerModel = new KoiModel<>(loader.getModelPart(ModModelLayers.KOI_PRIMARY_COLOR));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider providers, int light, KoiEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getBaseColorVariant().hasSeparateTexture()) return;
        if (entity.getPrimaryPatternTypeVariant() == KoiPrimaryPatternTypeVariant.NONE) return;
        int rgb = entity.getPrimaryPatternColorVariant().getColorHex();
        if(rgb == 0) return;
        int argb = 0xFF000000 | (rgb & 0x00FFFFFF);
        Identifier texture = entity.getPrimaryPatternTypeVariant().getTextureLocation();

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
