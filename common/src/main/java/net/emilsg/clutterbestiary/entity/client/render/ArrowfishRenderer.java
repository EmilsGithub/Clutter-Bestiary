package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ArrowfishModel;
import net.emilsg.clutterbestiary.entity.client.model.KoiModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiBaseColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiPrimaryPatternColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.feature.KoiSecondaryPatternColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.custom.ArrowfishEntity;
import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiBaseColorVariant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class ArrowfishRenderer extends BestiaryMobRenderer<ArrowfishEntity, ArrowfishModel<ArrowfishEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/arrowfish/arrowfish.png");

    public ArrowfishRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ArrowfishModel<>(ctx.bakeLayer(ModModelLayers.ARROWFISH)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(ArrowfishEntity arrowfishEntity) {
        return TEXTURE;
    }
}
