package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.model.ChorusBeetleModel;
import net.emilsg.clutterbestiary.entity.client.render.state.ChorusBeetleRenderState;
import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ChorusBeetleFlowerFeatureRenderer extends RenderLayer<ChorusBeetleRenderState, ChorusBeetleModel<ChorusBeetleEntity>> {
    private static final float FLOWER_OFFSET_X = -0.0625f;
    private static final float FLOWER_OFFSET_Y = 0.15f;
    private static final float FLOWER_OFFSET_Z = 0.0f;
    private static final float FLOWER_SIZE = 0.8f;

    public ChorusBeetleFlowerFeatureRenderer(RenderLayerParent<ChorusBeetleRenderState, ChorusBeetleModel<ChorusBeetleEntity>> context) {
        super(context);
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, ChorusBeetleRenderState state, float yRot, float xRot) {
        if (state.chorusFlower.isEmpty()) return;

        matrices.pushPose();
        this.getParentModel().root().getChild("all").translateAndRotate(matrices);
        matrices.translate(FLOWER_OFFSET_X, FLOWER_OFFSET_Y, FLOWER_OFFSET_Z);
        matrices.scale(FLOWER_SIZE, FLOWER_SIZE, FLOWER_SIZE);

        state.chorusFlower.submit(matrices, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);

        matrices.popPose();
    }
}
