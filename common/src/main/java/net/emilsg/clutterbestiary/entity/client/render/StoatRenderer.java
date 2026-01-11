package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.StoatModel;
import net.emilsg.clutterbestiary.entity.custom.StoatEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class StoatRenderer extends MobEntityRenderer<StoatEntity, StoatModel<StoatEntity>> {

    public StoatRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new StoatModel<>(ctx.getPart(ModModelLayers.STOAT)), 0.4f);
    }

    @Override
    public Identifier getTexture(StoatEntity stoatEntity) {
        return stoatEntity.isSleeping() ? stoatEntity.getVariant().getSleepingTextureLocation() : stoatEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(StoatEntity stoatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.4f;
        super.render(stoatEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
