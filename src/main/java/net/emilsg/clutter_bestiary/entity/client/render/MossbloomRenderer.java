package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.MossbloomModel;
import net.emilsg.clutter_bestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutter_bestiary.entity.custom.MossbloomEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MossbloomRenderer extends MobEntityRenderer<MossbloomEntity, MossbloomModel<MossbloomEntity>> {

    public MossbloomRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MossbloomModel<>(ctx.getPart(ModModelLayers.MOSSBLOOM)), 0.5f);
        this.addFeature(new EmissiveRenderer<>(this, MossbloomRenderer::getEmissiveTexture, 1f));
    }

    private static Identifier getEmissiveTexture(MossbloomEntity mossbloomEntity) {
        return !mossbloomEntity.isBaby() ? mossbloomEntity.getVariant().getEmissiveTextureLocation() : null;
    }

    @Override
    public Identifier getTexture(MossbloomEntity mossbloomEntity) {
        return mossbloomEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(MossbloomEntity mossbloomEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.5f;

        super.render(mossbloomEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
