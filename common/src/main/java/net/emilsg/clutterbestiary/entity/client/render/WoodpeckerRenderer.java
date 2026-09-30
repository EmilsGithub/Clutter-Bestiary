package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.WoodpeckerModel;
import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class WoodpeckerRenderer extends BestiaryMobRenderer<WoodpeckerEntity, WoodpeckerModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/woodpecker/woodpecker.png");

    public WoodpeckerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WoodpeckerModel(ctx.bakeLayer(ModModelLayers.WOODPECKER)), 0.25f);
    }

    @Override
    public Identifier getTextureLocation(WoodpeckerEntity entity) {
        return TEXTURE;
    }
}
