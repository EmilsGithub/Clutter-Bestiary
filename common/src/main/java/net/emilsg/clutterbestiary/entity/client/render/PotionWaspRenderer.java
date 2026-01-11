package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.PotionWaspModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.PotionWaspOuterFeatureRenderer;
import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PotionWaspRenderer extends MobEntityRenderer<PotionWaspEntity, PotionWaspModel<PotionWaspEntity>> {

    public PotionWaspRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PotionWaspModel<>(ctx.getPart(ModModelLayers.POTION_WASP)), 0.4f);
        this.addFeature(new PotionWaspOuterFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(PotionWaspEntity potionWasp) {
        return potionWasp.getVariant().getTextureLocation();
    }

    @Override
    public void render(PotionWaspEntity entity, float entityYaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        this.model.setOuterHidden(true);
        super.render(entity, entityYaw, tickDelta, matrices, vertexConsumers, light);
        this.model.setOuterHidden(false);
    }
}
