package net.emilsg.clutterbestiary.entity.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyBeaverModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
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

public class BeaverRenderer extends AbstractBestiaryMobRenderer<BeaverEntity, BeaverRenderState, BestiaryModel<BeaverEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/beaver/beaver.png");
    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/beaver/baby_beaver.png");

    private final BeaverModel<BeaverEntity> adultModel;
    private final BabyBeaverModel<BeaverEntity> babyModel;

    public BeaverRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BeaverModel<>(ctx.bakeLayer(ModModelLayers.BEAVER)), 0.4f);
        this.addLayer(new BeaverStripItemFeatureRenderer(this));

        this.adultModel = new BeaverModel<>(ctx.bakeLayer(ModModelLayers.BEAVER));
        this.babyModel = new BabyBeaverModel<>(ctx.bakeLayer(ModModelLayers.BABY_BEAVER));
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
        return entity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public void submit(BeaverRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
