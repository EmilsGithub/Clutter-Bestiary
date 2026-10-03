package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyRiverTurtleModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.RiverTurtleModel;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RiverTurtleRenderer extends BestiaryMobRenderer<RiverTurtleEntity, BestiaryModel<RiverTurtleEntity>> {

    private final RiverTurtleModel<RiverTurtleEntity> adultModel;
    private final BabyRiverTurtleModel<RiverTurtleEntity> babyModel;

    public RiverTurtleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new RiverTurtleModel<>(ctx.bakeLayer(ModModelLayers.RIVER_TURTLE)), 0.4f);

        this.adultModel = new RiverTurtleModel<>(ctx.bakeLayer(ModModelLayers.RIVER_TURTLE));
        this.babyModel = new BabyRiverTurtleModel<>(ctx.bakeLayer(ModModelLayers.BABY_RIVER_TURTLE));
    }

    @Override
    public Identifier getTextureLocation(RiverTurtleEntity riverTurtleEntity) {
        return riverTurtleEntity.isBaby() ? riverTurtleEntity.getVariant().getBabyTextureLocation() : riverTurtleEntity.getVariant().getTextureLocation();
    }

    @Override
    public void submit(BestiaryRenderState<RiverTurtleEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
