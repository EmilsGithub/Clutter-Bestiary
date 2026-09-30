package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ButterflyLarvaModel;
import net.emilsg.clutterbestiary.entity.custom.ButterflyLarvaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class ButterflyLarvaRenderer extends BestiaryMobRenderer<ButterflyLarvaEntity, ButterflyLarvaModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/block/butterfly_cocoon.png");

    public ButterflyLarvaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ButterflyLarvaModel(ctx.bakeLayer(ModModelLayers.BUTTERFLY_LARVA)), 0.2F);
    }

    @Override
    public Identifier getTextureLocation(ButterflyLarvaEntity entity) {
        return TEXTURE;
    }
}
