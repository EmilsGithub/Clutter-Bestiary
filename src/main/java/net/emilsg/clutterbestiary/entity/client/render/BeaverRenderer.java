package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.BeaverModel;
import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BeaverRenderer extends MobEntityRenderer<BeaverEntity, BeaverModel<BeaverEntity>> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/beaver/beaver.png");

    public BeaverRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BeaverModel<>(ctx.getPart(ModModelLayers.BEAVER)), 0.4f);
    }

    @Override
    public Identifier getTexture(BeaverEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(BeaverEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.4f;

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
