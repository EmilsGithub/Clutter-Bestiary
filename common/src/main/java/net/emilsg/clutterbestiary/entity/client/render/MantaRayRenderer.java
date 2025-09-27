package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.MantaRayModel;
import net.emilsg.clutterbestiary.entity.custom.MantaRayEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MantaRayRenderer extends MobEntityRenderer<MantaRayEntity, MantaRayModel<MantaRayEntity>> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/manta_ray/manta_ray.png");
    private static final Identifier OLD_TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/manta_ray/manta_ray_old.png");

    public MantaRayRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MantaRayModel<>(ctx.getPart(ModModelLayers.MANTA_RAY)), 0.7f);
    }

    @Override
    public Identifier getTexture(MantaRayEntity entity) {
        return entity.getSize() >= 1.5f ? OLD_TEXTURE : TEXTURE;
    }

    @Override
    public void render(MantaRayEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float scale = livingEntity.getSize();

        this.shadowRadius = 0.7f * scale;
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(MantaRayEntity entity, MatrixStack matrices, float amount) {
        float scale = entity.getSize();
        matrices.scale(scale, scale, scale);
    }
}
