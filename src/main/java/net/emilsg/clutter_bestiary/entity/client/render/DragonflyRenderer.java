package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.DragonflyModel;
import net.emilsg.clutter_bestiary.entity.custom.DragonflyEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class DragonflyRenderer extends MobEntityRenderer<DragonflyEntity, DragonflyModel<DragonflyEntity>> {

    public DragonflyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DragonflyModel<>(ctx.getPart(ModModelLayers.DRAGONFLY)), 0.4f);
    }

    @Override
    public Identifier getTexture(DragonflyEntity dragonflyEntity) {
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/dragonfly/dragonfly.png");
    }

    @Override
    public void render(DragonflyEntity dragonflyEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(dragonflyEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
