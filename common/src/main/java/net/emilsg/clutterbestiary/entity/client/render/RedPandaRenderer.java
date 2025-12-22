package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.RedPandaModel;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RedPandaRenderer extends MobEntityRenderer<RedPandaEntity, RedPandaModel<RedPandaEntity>> {

    public RedPandaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RedPandaModel<>(ctx.getPart(ModModelLayers.RED_PANDA)), 0.4f);
    }

    @Override
    public Identifier getTexture(RedPandaEntity redPandaEntity) {
        return redPandaEntity.isSleeping() ? redPandaEntity.getVariant().getSleepingTextureLocation() : redPandaEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(RedPandaEntity redPandaEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.4f;
        super.render(redPandaEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
