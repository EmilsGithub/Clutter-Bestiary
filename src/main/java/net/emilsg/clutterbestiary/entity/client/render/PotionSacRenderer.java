package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.PotionSacModel;
import net.emilsg.clutterbestiary.entity.custom.PotionSacEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class PotionSacRenderer extends MobEntityRenderer<PotionSacEntity, PotionSacModel<PotionSacEntity>> {

    public PotionSacRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PotionSacModel<>(ctx.getPart(ModModelLayers.POTION_SAC)), 0.4f);
    }

    @Override
    public Identifier getTexture(PotionSacEntity potionSac) {
        return potionSac.getVariant().getTextureLocation();
    }

    @Override
    protected boolean hasLabel(PotionSacEntity livingEntity) {
        return false;
    }
}
