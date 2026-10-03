package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyDragonflyModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.DragonflyModel;
import net.emilsg.clutterbestiary.entity.custom.DragonflyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class DragonflyRenderer extends BestiaryMobRenderer<DragonflyEntity, BestiaryModel<DragonflyEntity>> {

    private final DragonflyModel<DragonflyEntity> adultModel;
    private final BabyDragonflyModel<DragonflyEntity> babyModel;

    public DragonflyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DragonflyModel<>(ctx.bakeLayer(ModModelLayers.DRAGONFLY)), 0.4f);

        this.adultModel = new DragonflyModel<>(ctx.bakeLayer(ModModelLayers.DRAGONFLY));
        this.babyModel = new BabyDragonflyModel<>(ctx.bakeLayer(ModModelLayers.BABY_DRAGONFLY));
    }

    @Override
    public Identifier getTextureLocation(DragonflyEntity dragonflyEntity) {
        return dragonflyEntity.isBaby() ? dragonflyEntity.getVariant().getBabyTextureLocation() : dragonflyEntity.getVariant().getTextureLocation();
    }

    @Override
    public void submit(BestiaryRenderState<DragonflyEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
