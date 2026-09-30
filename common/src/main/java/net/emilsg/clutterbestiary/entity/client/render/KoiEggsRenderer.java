package net.emilsg.clutterbestiary.entity.client.render;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiEggsModel;
import net.emilsg.clutterbestiary.entity.custom.KoiEggsEntity;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class KoiEggsRenderer extends BestiaryMobRenderer<KoiEggsEntity, KoiEggsModel<KoiEggsEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/koi/koi_eggs.png");

    public KoiEggsRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new KoiEggsModel<>(ctx.bakeLayer(ModModelLayers.KOI_EGGS)), 0.7f);
    }

    @Override
    public Identifier getTextureLocation(KoiEggsEntity entity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BestiaryRenderState<KoiEggsEntity> state, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(state, showBody, true, showOutline);
    }

}
