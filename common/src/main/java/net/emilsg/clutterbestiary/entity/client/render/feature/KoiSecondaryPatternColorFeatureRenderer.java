package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.client.render.state.KoiRenderState;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class KoiSecondaryPatternColorFeatureRenderer extends RenderLayer<KoiRenderState, KoiModel<KoiEntity>> {
    private final KoiModel<KoiEntity> layerModel;

    public KoiSecondaryPatternColorFeatureRenderer(RenderLayerParent<KoiRenderState, KoiModel<KoiEntity>> context, EntityModelSet loader) {
        super(context);
        this.layerModel = new KoiModel<>(loader.bakeLayer(ModModelLayers.KOI_SECONDARY_COLOR));
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, KoiRenderState state, float yRot, float xRot) {
        if (state.secondaryPatternTexture == null || state.secondaryPatternColor == 0) return;
        int argb = 0xFF000000 | (state.secondaryPatternColor & 0x00FFFFFF);
        coloredCutoutModelCopyLayerRender(this.layerModel, state.secondaryPatternTexture, matrices, submitNodeCollector, light, state, argb, 3);
    }
}
