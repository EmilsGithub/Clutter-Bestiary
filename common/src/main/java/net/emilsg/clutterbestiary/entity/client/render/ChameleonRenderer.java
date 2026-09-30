package net.emilsg.clutterbestiary.entity.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.BabyChameleonModel;
import net.emilsg.clutterbestiary.entity.client.model.ChameleonModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.ChameleonColorFeatureRenderer;
import net.emilsg.clutterbestiary.entity.client.render.parent.AbstractBestiaryMobRenderer;
import net.emilsg.clutterbestiary.entity.client.render.state.ChameleonRenderState;
import net.emilsg.clutterbestiary.entity.custom.ChameleonEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

public class ChameleonRenderer extends AbstractBestiaryMobRenderer<ChameleonEntity, ChameleonRenderState, ParentTameableModel<ChameleonEntity>> {
    private static final Identifier ADULT_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/chameleon/chameleon.png");
    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/chameleon/baby_chameleon.png");

    private final ChameleonModel<ChameleonEntity> adultModel;
    private final BabyChameleonModel<ChameleonEntity> babyModel;

    public ChameleonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ChameleonModel<>(ctx.bakeLayer(ModModelLayers.CHAMELEON)), 0.4f);
        this.addLayer(new ChameleonColorFeatureRenderer(this, ctx.getModelSet(), ADULT_TEXTURE, BABY_TEXTURE));

        this.adultModel = new ChameleonModel<>(ctx.bakeLayer(ModModelLayers.CHAMELEON));
        this.babyModel = new BabyChameleonModel<>(ctx.bakeLayer(ModModelLayers.BABY_CHAMELEON));
    }

    @Override
    public ChameleonRenderState createRenderState() {
        return new ChameleonRenderState();
    }

    @Override
    public void extractRenderState(ChameleonEntity entity, ChameleonRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        // The client-side colour fade targets the block beneath; the layer only reads the extracted colour.
        int environmentColor = ChameleonColorFeatureRenderer.getEnvironmentColor(entity);
        if (environmentColor != entity.getTargetColor()) {
            entity.setTargetColor(environmentColor);
        }
        state.color = entity.getCurrentColor();
    }

    @Override
    public Identifier getTextureLocation(ChameleonEntity chameleon) {
        return chameleon.isBaby() ? BABY_TEXTURE : ADULT_TEXTURE;
    }

    @Override
    public void submit(ChameleonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
