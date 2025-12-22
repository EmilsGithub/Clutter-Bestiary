package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.RiverTurtleModel;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RiverTurtleRenderer extends MobEntityRenderer<RiverTurtleEntity, RiverTurtleModel<RiverTurtleEntity>> {

    public RiverTurtleRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RiverTurtleModel<>(ctx.getPart(ModModelLayers.RIVER_TURTLE)), 0.4f);
    }

    @Override
    public Identifier getTexture(RiverTurtleEntity riverTurtleEntity) {
        return riverTurtleEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(RiverTurtleEntity riverTurtleEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(riverTurtleEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
