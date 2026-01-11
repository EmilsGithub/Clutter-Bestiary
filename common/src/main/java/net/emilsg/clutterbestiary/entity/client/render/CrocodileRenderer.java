package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CrocodileModel;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CrocodileRenderer extends MobEntityRenderer<CrocodileEntity, CrocodileModel<CrocodileEntity>> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/crocodile/crocodile.png");

    public CrocodileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CrocodileModel<>(ctx.getPart(ModModelLayers.CROCODILE)), 1.25f);
    }

    @Override
    public Identifier getTexture(CrocodileEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(CrocodileEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 1.25f;
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
