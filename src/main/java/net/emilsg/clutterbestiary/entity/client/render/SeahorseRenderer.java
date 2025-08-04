package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.SeahorseModel;
import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SeahorseRenderer extends MobEntityRenderer<SeahorseEntity, SeahorseModel<SeahorseEntity>> {

    public SeahorseRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SeahorseModel<>(ctx.getPart(ModModelLayers.SEAHORSE)), 0.2f);
    }

    @Override
    public Identifier getTexture(SeahorseEntity seahorseEntity) {
        return seahorseEntity.getVariant().getTextureLocation();
    }

    @Override
    public void render(SeahorseEntity seahorseEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.2f;

        super.render(seahorseEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
