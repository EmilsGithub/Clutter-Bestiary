package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.PotionSacModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.custom.PotionSacEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class PotionSacOuterFeatureRenderer extends RenderLayer<BestiaryRenderState<PotionSacEntity>, PotionSacModel<PotionSacEntity>> {
    // Separate model instance with the outer shell visible; the renderer's own model keeps it hidden.
    private final PotionSacModel<PotionSacEntity> outerModel;

    public PotionSacOuterFeatureRenderer(RenderLayerParent<BestiaryRenderState<PotionSacEntity>, PotionSacModel<PotionSacEntity>> context, EntityModelSet models) {
        super(context);
        this.outerModel = new PotionSacModel<>(models.bakeLayer(ModModelLayers.POTION_SAC));
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, BestiaryRenderState<PotionSacEntity> state, float yRot, float xRot) {
        if (state.entity == null) return;
        submitNodeCollector.order(1).submitModel(this.outerModel, state, matrices, RenderTypes.entityTranslucent(state.entity.getVariant().getTextureLocation()), light, LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor);
    }
}
