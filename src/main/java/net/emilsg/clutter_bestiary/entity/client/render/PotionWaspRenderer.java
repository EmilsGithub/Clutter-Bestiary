package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.PotionWaspModel;
import net.emilsg.clutter_bestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PotionWaspRenderer extends MobEntityRenderer<PotionWaspEntity, PotionWaspModel<PotionWaspEntity>> {

    public PotionWaspRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PotionWaspModel<>(ctx.getPart(ModModelLayers.POTION_WASP)), 0.4f);
    }

    @Override
    public Identifier getTexture(PotionWaspEntity potionWasp) {
        return potionWasp.getVariant().getTextureLocation();
    }

    @Override
    public void render(PotionWaspEntity potionWasp, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(potionWasp, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
