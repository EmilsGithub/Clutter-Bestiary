package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyCoatiModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CoatiModel;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CoatiRenderer extends BestiaryMobRenderer<CoatiEntity, ParentTameableModel<CoatiEntity>> {

    private final CoatiModel<CoatiEntity> adultModel;
    private final BabyCoatiModel<CoatiEntity> babyModel;

    public CoatiRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CoatiModel<>(ctx.bakeLayer(ModModelLayers.COATI)), 0.5f);

        this.adultModel = new CoatiModel<>(ctx.bakeLayer(ModModelLayers.COATI));
        this.babyModel = new BabyCoatiModel<>(ctx.bakeLayer(ModModelLayers.BABY_COATI));
    }

    @Override
    public Identifier getTextureLocation(CoatiEntity coatiEntity) {
        return coatiEntity.isBaby() ? coatiEntity.getVariant().getBabyTextureLocation() : coatiEntity.getVariant().getTextureLocation();
    }

    @Override
    public void submit(BestiaryRenderState<CoatiEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
