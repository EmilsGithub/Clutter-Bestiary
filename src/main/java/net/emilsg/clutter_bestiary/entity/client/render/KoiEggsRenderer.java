package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.KoiEggsModel;
import net.emilsg.clutter_bestiary.entity.custom.KoiEggsEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class KoiEggsRenderer extends MobEntityRenderer<KoiEggsEntity, KoiEggsModel<KoiEggsEntity>> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_eggs.png");

    public KoiEggsRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new KoiEggsModel<>(ctx.getPart(ModModelLayers.KOI_EGGS)), 0.7f);
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(KoiEggsEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(entity, showBody, true, showOutline);
    }

    @Override
    public Identifier getTexture(KoiEggsEntity entity) {
        return TEXTURE;
    }

}
