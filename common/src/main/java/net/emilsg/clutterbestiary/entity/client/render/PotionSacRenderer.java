package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.PotionSacModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.PotionSacOuterFeatureRenderer;
import net.emilsg.clutterbestiary.entity.custom.PotionSacEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class PotionSacRenderer extends BestiaryMobRenderer<PotionSacEntity, PotionSacModel<PotionSacEntity>> {

    public PotionSacRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PotionSacModel<>(ctx.bakeLayer(ModModelLayers.POTION_SAC)), 0.4f);
        // The outer shell is drawn translucent by its own layer.
        this.model.setOuterHidden(true);
        this.addLayer(new PotionSacOuterFeatureRenderer(this, ctx.getModelSet()));

    }

    @Override
    public Identifier getTextureLocation(PotionSacEntity potionSac) {
        return potionSac.getVariant().getTextureLocation();
    }

    @Override
    protected boolean shouldShowName(PotionSacEntity livingEntity, double distanceToCameraSq) {
        return false;
    }
}
