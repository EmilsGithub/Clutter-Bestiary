package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.BabyKoiModel;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentFishModel;
import net.emilsg.clutterbestiary.entity.client.render.state.KoiRenderState;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class KoiPrimaryPatternColorFeatureRenderer extends RenderLayer<KoiRenderState, ParentFishModel<KoiEntity>> {
    private final KoiModel<KoiEntity> adultLayerModel;
    private final BabyKoiModel<KoiEntity> babyLayerModel;

    public KoiPrimaryPatternColorFeatureRenderer(RenderLayerParent<KoiRenderState, ParentFishModel<KoiEntity>> context, EntityModelSet loader) {
        super(context);
        this.adultLayerModel = new KoiModel<>(loader.bakeLayer(ModModelLayers.KOI_PRIMARY_COLOR));
        this.babyLayerModel = new BabyKoiModel<>(loader.bakeLayer(ModModelLayers.BABY_KOI_PRIMARY_COLOR));
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, KoiRenderState state, float yRot, float xRot) {
        if (state.primaryPatternTexture == null || state.primaryPatternColor == 0) return;
        int argb = 0xFF000000 | (state.primaryPatternColor & 0x00FFFFFF);
        coloredCutoutModelCopyLayerRender(state.isBaby ? this.babyLayerModel : this.adultLayerModel, state.primaryPatternTexture, matrices, submitNodeCollector, light, state, argb, 2);
    }
}
