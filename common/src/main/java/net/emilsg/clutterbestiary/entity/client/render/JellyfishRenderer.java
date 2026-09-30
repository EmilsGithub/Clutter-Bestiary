package net.emilsg.clutterbestiary.entity.client.render;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.JellyfishModel;
import net.emilsg.clutterbestiary.entity.custom.JellyfishEntity;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class JellyfishRenderer extends BestiaryMobRenderer<JellyfishEntity, JellyfishModel<JellyfishEntity>> {

    public JellyfishRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new JellyfishModel<>(ctx.bakeLayer(ModModelLayers.JELLYFISH)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(JellyfishEntity entity) {
        return entity.getVariant().getTextureLocation();
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BestiaryRenderState<JellyfishEntity> state, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(state, showBody, true, showOutline);
    }

    @Override
    protected void setupRotations(BestiaryRenderState<JellyfishEntity> state, PoseStack matrices, float bodyYaw, float scale) {
        super.setupRotations(state, matrices, bodyYaw, scale);
        JellyfishEntity jellyfishEntity = state.entity;
        if (jellyfishEntity == null) return;
        float tickDelta = state.partialTick;
        float i = Mth.lerp(tickDelta, jellyfishEntity.prevTiltAngle, jellyfishEntity.tiltAngle);
        float j = Mth.lerp(tickDelta, jellyfishEntity.prevRollAngle, jellyfishEntity.rollAngle);
        matrices.translate(0.0f, 0.25f, 0.0f);
        matrices.rotateDegrees(Axis.YP, 180.0f - bodyYaw);
        matrices.rotateDegrees(Axis.XP, i);
        matrices.rotateDegrees(Axis.YP, j);
        matrices.translate(0.0f, 0.0f, 0.0f);
    }
}
