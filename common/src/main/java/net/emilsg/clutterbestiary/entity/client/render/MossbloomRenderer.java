package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.model.BabyMossbloomModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.MossbloomModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.custom.MossbloomEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

public class MossbloomRenderer extends BestiaryMobRenderer<MossbloomEntity, ParentTameableModel<MossbloomEntity>> {

    private final MossbloomModel<MossbloomEntity> adultModel;
    private final BabyMossbloomModel<MossbloomEntity> babyModel;

    public MossbloomRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MossbloomModel<>(ctx.bakeLayer(ModModelLayers.MOSSBLOOM)), 0.5f);
        this.addLayer(new EmissiveRenderer<>(this, MossbloomRenderer::getEmissiveTexture));

        this.adultModel = new MossbloomModel<>(ctx.bakeLayer(ModModelLayers.MOSSBLOOM));
        this.babyModel = new BabyMossbloomModel<>(ctx.bakeLayer(ModModelLayers.BABY_MOSSBLOOM));
    }

    private static Identifier getEmissiveTexture(MossbloomEntity mossbloomEntity) {
        return !mossbloomEntity.isBaby() ? mossbloomEntity.getVariant().getEmissiveTextureLocation() : null;
    }

    @Override
    public Identifier getTextureLocation(MossbloomEntity mossbloomEntity) {
        return mossbloomEntity.isBaby() ? mossbloomEntity.getVariant().getBabyTextureLocation() : mossbloomEntity.getVariant().getTextureLocation();
    }


    @Override
    public void submit(BestiaryRenderState<MossbloomEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

}
