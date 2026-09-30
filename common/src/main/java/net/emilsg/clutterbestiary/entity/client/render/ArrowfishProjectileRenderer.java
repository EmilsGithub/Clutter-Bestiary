package net.emilsg.clutterbestiary.entity.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.custom.ArrowfishProjectileEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ArrowfishProjectileRenderer extends EntityRenderer<ArrowfishProjectileEntity, ArrowRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/arrowfish/arrowfish.png");
    private final ModelPart model;

    public ArrowfishProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = context.bakeLayer(ModModelLayers.ARROWFISH);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    public void extractRenderState(ArrowfishProjectileEntity entity, ArrowRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = entity.shakeTime - partialTicks;
    }

    @Override
    public void submit(ArrowRenderState state, PoseStack matrices, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        matrices.pushPose();
        matrices.rotateDegrees(Axis.YP, state.yRot);
        matrices.rotateDegrees(Axis.XP, -state.xRot);

        float shake = state.shake;
        if (shake > 0.0F) {
            matrices.rotateDegrees(Axis.XP, -Mth.sin(shake * 3.0F) * shake);
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.rotateDegrees(Axis.YP, 180.0F);
        // Center the fish's body on the projectile instead of its authored ground-level pivot.
        matrices.translate(0.0F, -22.0F / 16.0F, 0.0F);
        submitNodeCollector.submitModelPart(this.model, matrices, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.outlineColor);
        matrices.popPose();
        super.submit(state, matrices, submitNodeCollector, camera);
    }
}
