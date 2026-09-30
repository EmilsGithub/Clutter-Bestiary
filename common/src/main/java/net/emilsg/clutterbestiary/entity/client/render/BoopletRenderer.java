package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.BoopletModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutterbestiary.entity.custom.BoopletEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class BoopletRenderer extends BestiaryMobRenderer<BoopletEntity, BoopletModel<BoopletEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/booplet/booplet.png");
    private static final Identifier EMISSIVE_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/booplet/booplet_emissive.png");

    public BoopletRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BoopletModel<>(ctx.bakeLayer(ModModelLayers.BOOPLET)), 0.4f);
        this.addLayer(new EmissiveRenderer<>(this, BoopletRenderer::getEmissiveTexture));
    }

    public static Identifier getEmissiveTexture(BoopletEntity entity) {
        return EMISSIVE_TEXTURE;
    }

    @Override
    public Identifier getTextureLocation(BoopletEntity entity) {
        return TEXTURE;
    }
}