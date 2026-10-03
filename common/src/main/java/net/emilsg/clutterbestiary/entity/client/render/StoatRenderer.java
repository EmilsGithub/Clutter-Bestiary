package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyStoatModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.StoatModel;
import net.emilsg.clutterbestiary.entity.custom.StoatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class StoatRenderer extends BestiaryMobRenderer<StoatEntity, ParentTameableModel<StoatEntity>> {

    private final StoatModel<StoatEntity> adultModel;
    private final BabyStoatModel<StoatEntity> babyModel;

    public StoatRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new StoatModel<>(ctx.bakeLayer(ModModelLayers.STOAT)), 0.4f);

        this.adultModel = new StoatModel<>(ctx.bakeLayer(ModModelLayers.STOAT));
        this.babyModel = new BabyStoatModel<>(ctx.bakeLayer(ModModelLayers.BABY_STOAT));
    }

    @Override
    public Identifier getTextureLocation(StoatEntity stoatEntity) {
        if (stoatEntity.isBaby()) return stoatEntity.isSleeping() ? stoatEntity.getVariant().getBabySleepingTextureLocation() : stoatEntity.getVariant().getBabyTextureLocation();
        return stoatEntity.isSleeping() ? stoatEntity.getVariant().getSleepingTextureLocation() : stoatEntity.getVariant().getTextureLocation();
    }

    @Override
    public void submit(BestiaryRenderState<StoatEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
