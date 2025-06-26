package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.BoopletModel;
import net.emilsg.clutter_bestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutter_bestiary.entity.custom.BoopletEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BoopletRenderer extends MobEntityRenderer<BoopletEntity, BoopletModel<BoopletEntity>> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/booplet/booplet.png");
    private static final Identifier EMISSIVE_TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/booplet/booplet_emissive.png");

    public BoopletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BoopletModel<>(ctx.getPart(ModModelLayers.BOOPLET)), 0.4f);
        this.addFeature(new EmissiveRenderer<>(this, BoopletRenderer::getEmissiveTexture, 1f));
    }

    @Override
    public Identifier getTexture(BoopletEntity entity) {
        return TEXTURE;
    }

    public static Identifier getEmissiveTexture(BoopletEntity entity) {
        return EMISSIVE_TEXTURE;
    }
}
