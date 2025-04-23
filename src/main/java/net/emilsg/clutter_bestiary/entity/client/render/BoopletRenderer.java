package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.BoopletModel;
import net.emilsg.clutter_bestiary.entity.custom.BoopletEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BoopletRenderer extends MobEntityRenderer<BoopletEntity, BoopletModel<BoopletEntity>> {
    private static final Identifier TEXTURE = new Identifier(ClutterBestiary.MOD_ID, "textures/entity/booplet/booplet.png");

    public BoopletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BoopletModel<>(ctx.getPart(ModModelLayers.BOOPLET)), 0.4f);
    }

    @Override
    public Identifier getTexture(BoopletEntity entity) {
        return TEXTURE;
    }
}
