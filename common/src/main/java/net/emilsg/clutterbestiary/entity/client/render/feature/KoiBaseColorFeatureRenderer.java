package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.client.render.state.KoiRenderState;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiBaseColorVariant;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.awt.Color;

public class KoiBaseColorFeatureRenderer extends RenderLayer<KoiRenderState, KoiModel<KoiEntity>> {
    private final KoiModel<KoiEntity> layerModel;

    public KoiBaseColorFeatureRenderer(RenderLayerParent<KoiRenderState, KoiModel<KoiEntity>> context, EntityModelSet loader) {
        super(context);
        this.layerModel = new KoiModel<>(loader.bakeLayer(ModModelLayers.KOI_BASE));
    }

    /**
     * Base colour tint for the given koi, or {@code 0} when its variant uses a dedicated texture.
     */
    public static int getColor(KoiEntity koiEntity, float tickDelta) {
        int color;

        if (koiEntity.getBaseColorVariant().equals(KoiBaseColorVariant.IRIDESCENT_RAINBOW)) {
            float speed = 4.5f;
            float hue = ((koiEntity.tickCount + tickDelta) * speed % 360) / 360.0f;
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
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, KoiRenderState state, float yRot, float xRot) {
        if (state.baseColor == 0 || state.entity == null) return;
        int argb = 0xFF000000 | (state.baseColor & 0x00FFFFFF);
        coloredCutoutModelCopyLayerRender(this.layerModel, state.entity.getBaseColorVariant().getTextureLocation(), matrices, submitNodeCollector, light, state, argb, 1);
    }
}
