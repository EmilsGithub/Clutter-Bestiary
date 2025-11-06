package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CoatiModel;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CoatiRenderer extends MobEntityRenderer<CoatiEntity, CoatiModel<CoatiEntity>> {

    public CoatiRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CoatiModel<>(ctx.getPart(ModModelLayers.COATI)), 0.5f);
    }

    @Override
    public Identifier getTexture(CoatiEntity coatiEntity) {
        return coatiEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(CoatiEntity coatiEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(coatiEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
