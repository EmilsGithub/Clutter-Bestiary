package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.PotionWaspModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.PotionWaspOuterFeatureRenderer;
import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class PotionWaspRenderer extends BestiaryMobRenderer<PotionWaspEntity, PotionWaspModel<PotionWaspEntity>> {

    public PotionWaspRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PotionWaspModel<>(ctx.bakeLayer(ModModelLayers.POTION_WASP)), 0.4f);
        // The outer sac is drawn translucent by its own layer.
        this.model.setOuterHidden(true);
        this.addLayer(new PotionWaspOuterFeatureRenderer(this, ctx.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(PotionWaspEntity potionWasp) {
        return potionWasp.getVariant().getTextureLocation();
    }

}
