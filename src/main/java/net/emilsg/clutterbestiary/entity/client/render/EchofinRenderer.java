package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.EchofinModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutterbestiary.entity.custom.EchofinEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EchofinRenderer extends MobEntityRenderer<EchofinEntity, EchofinModel<EchofinEntity>> {

    public EchofinRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EchofinModel<>(ctx.getPart(ModModelLayers.ECHOFIN)), 0.4f);
        this.addFeature(new EmissiveRenderer<>(this, EchofinRenderer::getEmissiveTexture, 1f, false));
    }

    public static Identifier getEmissiveTexture(EchofinEntity echofinEntity) {
        return echofinEntity.hasAbility() ? echofinEntity.getVariant().getEmissiveTextureLocation() : null;
    }

    @Override
    public Identifier getTexture(EchofinEntity echofinEntity) {
        return echofinEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(EchofinEntity echofinEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.2f;

        super.render(echofinEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
