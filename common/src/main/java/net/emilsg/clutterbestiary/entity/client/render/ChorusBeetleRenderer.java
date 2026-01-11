package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ChorusBeetleModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.ChorusBeetleFlowerFeatureRenderer;
import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ChorusBeetleRenderer extends MobEntityRenderer<ChorusBeetleEntity, ChorusBeetleModel<ChorusBeetleEntity>> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/chorus_beetle/chorus_beetle.png");

    public ChorusBeetleRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChorusBeetleModel<>(ctx.getPart(ModModelLayers.CHORUS_BEETLE)), 0.35f);
        this.addFeature(new ChorusBeetleFlowerFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(ChorusBeetleEntity entity) {
        return TEXTURE;
    }
}
