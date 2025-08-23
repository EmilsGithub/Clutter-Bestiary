package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiBaseColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiPrimaryPatternColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiSecondaryPatternColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiBaseColorVariant;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class KoiRenderer extends MobEntityRenderer<KoiEntity, KoiModel<KoiEntity>> {

    public KoiRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new KoiModel<>(ctx.getPart(ModModelLayers.KOI_BASE)), 0.4f);
        this.addFeature(new KoiBaseColorFeatureRenderer(this, ctx.getModelLoader()));
        this.addFeature(new KoiPrimaryPatternColorFeatureRenderer(this, ctx.getModelLoader()));
        this.addFeature(new KoiSecondaryPatternColorFeatureRenderer(this, ctx.getModelLoader()));
        this.addFeature(new EmissiveRenderer<>(this, KoiBaseColorVariant::getEmissiveTextureFromEntity));
    }

    @Override
    public Identifier getTexture(KoiEntity koiEntity) {
        return koiEntity.getBaseColorVariant().getTextureLocation();
    }
}
