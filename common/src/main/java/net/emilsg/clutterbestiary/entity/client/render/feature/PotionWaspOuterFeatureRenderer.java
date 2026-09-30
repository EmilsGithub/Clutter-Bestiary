package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.PotionWaspModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class PotionWaspOuterFeatureRenderer extends RenderLayer<BestiaryRenderState<PotionWaspEntity>, PotionWaspModel<PotionWaspEntity>> {
    // Separate model instance that only draws the translucent outer sac.
    private final PotionWaspModel<PotionWaspEntity> outerModel;

    public PotionWaspOuterFeatureRenderer(RenderLayerParent<BestiaryRenderState<PotionWaspEntity>, PotionWaspModel<PotionWaspEntity>> context, EntityModelSet models) {
        super(context);
        this.outerModel = new PotionWaspModel<>(models.bakeLayer(ModModelLayers.POTION_WASP));
        this.outerModel.showOnlyOuter();
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, BestiaryRenderState<PotionWaspEntity> state, float yRot, float xRot) {
        if (state.entity == null || !state.entity.hasPotionSac()) return;
        submitNodeCollector.order(1).submitModel(this.outerModel, state, matrices, RenderTypes.entityTranslucent(state.entity.getVariant().getTextureLocation()), light, LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor);
    }
}
