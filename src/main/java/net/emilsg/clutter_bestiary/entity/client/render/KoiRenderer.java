package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.KoiModel;
import net.emilsg.clutter_bestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutter_bestiary.entity.client.render.feature.KoiBaseColorFeatureRenderer;
import net.emilsg.clutter_bestiary.entity.client.render.feature.KoiPrimaryPatternColorFeatureRenderer;
import net.emilsg.clutter_bestiary.entity.client.render.feature.KoiSecondaryPatternColorFeatureRenderer;
import net.emilsg.clutter_bestiary.entity.custom.KoiEntity;
import net.emilsg.clutter_bestiary.entity.variants.koi.KoiBaseColorVariant;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class KoiRenderer extends MobEntityRenderer<KoiEntity, KoiModel<KoiEntity>> {

    public KoiRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new KoiModel<>(ctx.getPart(ModModelLayers.KOI)), 0.4f);
        this.addFeature(new KoiBaseColorFeatureRenderer(this));
        this.addFeature(new KoiPrimaryPatternColorFeatureRenderer(this));
        this.addFeature(new KoiSecondaryPatternColorFeatureRenderer(this));
        this.addFeature(new EmissiveRenderer<>(this, KoiBaseColorVariant::getEmissiveTextureFromEntity, 0.5f));
    }

    @Override
    public Identifier getTexture(KoiEntity koiEntity) {
        return koiEntity.getBaseColorVariant().getTextureLocation();
    }
}
