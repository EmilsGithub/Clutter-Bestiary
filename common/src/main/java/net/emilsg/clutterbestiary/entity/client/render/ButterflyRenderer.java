package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ButterflyModel;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ButterflyRenderer extends MobEntityRenderer<ButterflyEntity, ButterflyModel<ButterflyEntity>> {

    public ButterflyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ButterflyModel<>(ctx.getPart(ModModelLayers.BUTTERFLY)), 0.4f);
    }

    @Override
    public Identifier getTexture(ButterflyEntity butterflyEntity) {
        return butterflyEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(ButterflyEntity butterflyEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.2f;

        super.render(butterflyEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
