package net.emilsg.clutterbestiary.entity.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyKoiModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentFishModel;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiBaseColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiPrimaryPatternColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiSecondaryPatternColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.parent.AbstractBestiaryMobRenderer;
import net.emilsg.clutterbestiary.entity.client.render.state.KoiRenderState;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiBaseColorVariant;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiPrimaryPatternTypeVariant;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiSecondaryPatternTypeVariant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class KoiRenderer extends AbstractBestiaryMobRenderer<KoiEntity, KoiRenderState, ParentFishModel<KoiEntity>> {

    private final KoiModel<KoiEntity> adultModel;
    private final BabyKoiModel<KoiEntity> babyModel;

    public KoiRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new KoiModel<>(ctx.bakeLayer(ModModelLayers.KOI_BASE)), 0.4f);
        this.addLayer(new KoiBaseColorFeatureRenderer(this, ctx.getModelSet()));
        this.addLayer(new KoiPrimaryPatternColorFeatureRenderer(this, ctx.getModelSet()));
        this.addLayer(new KoiSecondaryPatternColorFeatureRenderer(this, ctx.getModelSet()));
        this.addLayer(new EmissiveRenderer<>(this, KoiBaseColorVariant::getEmissiveTextureFromEntity));

        this.adultModel = new KoiModel<>(ctx.bakeLayer(ModModelLayers.KOI_BASE));
        this.babyModel = new BabyKoiModel<>(ctx.bakeLayer(ModModelLayers.BABY_KOI_BASE));
    }

    @Override
    public KoiRenderState createRenderState() {
        return new KoiRenderState();
    }

    @Override
    public void extractRenderState(KoiEntity entity, KoiRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.baseColor = KoiBaseColorFeatureRenderer.getColor(entity, partialTick);

        boolean patterned = !entity.getBaseColorVariant().hasSeparateTexture();
        KoiPrimaryPatternTypeVariant primaryType = entity.getPrimaryPatternTypeVariant();
        boolean hasPrimary = patterned && primaryType != KoiPrimaryPatternTypeVariant.NONE;
        state.primaryPatternTexture = hasPrimary ? (entity.isBaby() ? primaryType.getBabyTextureLocation() : primaryType.getTextureLocation()) : null;
        state.primaryPatternColor = hasPrimary ? entity.getPrimaryPatternColorVariant().getColorHex() : 0;

        KoiSecondaryPatternTypeVariant secondaryType = entity.getSecondaryPatternTypeVariant();
        boolean hasSecondary = patterned && secondaryType != KoiSecondaryPatternTypeVariant.NONE;
        state.secondaryPatternTexture = hasSecondary ? (entity.isBaby() ? secondaryType.getBabyTextureLocation() : secondaryType.getTextureLocation()) : null;
        state.secondaryPatternColor = hasSecondary ? entity.getSecondaryPatternColorVariant().getColorHex() : 0;
    }

    @Override
    public Identifier getTextureLocation(KoiEntity koiEntity) {
        return koiEntity.isBaby() ? koiEntity.getBaseColorVariant().getBabyTextureLocation() : koiEntity.getBaseColorVariant().getTextureLocation();
    }

    @Override
    public void submit(KoiRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
