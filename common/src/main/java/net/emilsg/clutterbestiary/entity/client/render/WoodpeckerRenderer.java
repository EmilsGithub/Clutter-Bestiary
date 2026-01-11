package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.WoodpeckerModel;
import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class WoodpeckerRenderer extends MobEntityRenderer<WoodpeckerEntity, WoodpeckerModel> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/woodpecker/woodpecker.png");

    public WoodpeckerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WoodpeckerModel(ctx.getPart(ModModelLayers.WOODPECKER)), 0.25f);
    }

    @Override
    public Identifier getTexture(WoodpeckerEntity entity) {
        return TEXTURE;
    }
}
