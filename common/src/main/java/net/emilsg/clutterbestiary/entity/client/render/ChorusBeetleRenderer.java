package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ChorusBeetleModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.ChorusBeetleFlowerFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.parent.AbstractBestiaryMobRenderer;
import net.emilsg.clutterbestiary.entity.client.render.state.ChorusBeetleRenderState;
import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ChorusBeetleRenderer extends AbstractBestiaryMobRenderer<ChorusBeetleEntity, ChorusBeetleRenderState, ChorusBeetleModel<ChorusBeetleEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/chorus_beetle/chorus_beetle.png");

    public ChorusBeetleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ChorusBeetleModel<>(ctx.bakeLayer(ModModelLayers.CHORUS_BEETLE)), 0.35f);
        this.addLayer(new ChorusBeetleFlowerFeatureRenderer(this));
    }

    @Override
    public ChorusBeetleRenderState createRenderState() {
        return new ChorusBeetleRenderState();
    }

    @Override
    public void extractRenderState(ChorusBeetleEntity entity, ChorusBeetleRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        if (entity.isCarryingChorusFlower()) {
            this.itemModelResolver.updateForLiving(state.chorusFlower, new ItemStack(Items.CHORUS_FLOWER), ItemDisplayContext.FIXED, entity);
        } else {
            state.chorusFlower.clear();
        }
    }

    @Override
    public Identifier getTextureLocation(ChorusBeetleEntity entity) {
        return TEXTURE;
    }
}
