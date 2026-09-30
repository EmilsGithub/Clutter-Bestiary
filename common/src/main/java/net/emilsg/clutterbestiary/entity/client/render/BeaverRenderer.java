package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.BeaverModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.BeaverStripItemFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.parent.AbstractBestiaryMobRenderer;
import net.emilsg.clutterbestiary.entity.client.render.state.BeaverRenderState;
import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

public class BeaverRenderer extends AbstractBestiaryMobRenderer<BeaverEntity, BeaverRenderState, BeaverModel<BeaverEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/beaver/beaver.png");

    public BeaverRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BeaverModel<>(ctx.bakeLayer(ModModelLayers.BEAVER)), 0.4f);
        this.addLayer(new BeaverStripItemFeatureRenderer(this));
    }

    @Override
    public BeaverRenderState createRenderState() {
        return new BeaverRenderState();
    }

    @Override
    public void extractRenderState(BeaverEntity entity, BeaverRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        this.itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.FIXED, entity);
    }

    @Override
    public Identifier getTextureLocation(BeaverEntity entity) {
        return TEXTURE;
    }

}
