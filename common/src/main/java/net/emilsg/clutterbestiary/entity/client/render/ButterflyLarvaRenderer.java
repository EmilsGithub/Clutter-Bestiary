package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ButterflyLarvaModel;
import net.emilsg.clutterbestiary.entity.custom.ButterflyLarvaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ButterflyLarvaRenderer extends MobEntityRenderer<ButterflyLarvaEntity, ButterflyLarvaModel> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/block/butterfly_cocoon.png");

    public ButterflyLarvaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ButterflyLarvaModel(ctx.getPart(ModModelLayers.BUTTERFLY_LARVA)), 0.2F);
    }

    @Override
    public Identifier getTexture(ButterflyLarvaEntity entity) {
        return TEXTURE;
    }
}
