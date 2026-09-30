package net.emilsg.clutterbestiary.entity.client.render;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.MantaRayModel;
import net.emilsg.clutterbestiary.entity.custom.MantaRayEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class MantaRayRenderer extends BestiaryMobRenderer<MantaRayEntity, MantaRayModel<MantaRayEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/manta_ray/manta_ray.png");
    private static final Identifier OLD_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/manta_ray/manta_ray_old.png");

    public MantaRayRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MantaRayModel<>(ctx.bakeLayer(ModModelLayers.MANTA_RAY)), 0.7f);
    }

    @Override
    public Identifier getTextureLocation(MantaRayEntity entity) {
        return entity.getSize() >= 1.25f ? OLD_TEXTURE : TEXTURE;
    }

    @Override
    protected float getShadowRadius(BestiaryRenderState<MantaRayEntity> state) {
        float scale = state.entity != null ? state.entity.getSize() : 1.0F;
        return super.getShadowRadius(state) * scale;
    }

    @Override
    protected void scale(MantaRayEntity entity, PoseStack matrices) {
        float scale = entity.getSize();
        matrices.scale(scale, scale, scale);
    }
}
