package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyRedPandaModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.RedPandaModel;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RedPandaRenderer extends BestiaryMobRenderer<RedPandaEntity, ParentTameableModel<RedPandaEntity>> {

    private final RedPandaModel<RedPandaEntity> adultModel;
    private final BabyRedPandaModel<RedPandaEntity> babyModel;

    public RedPandaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new RedPandaModel<>(ctx.bakeLayer(ModModelLayers.RED_PANDA)), 0.4f);

        this.adultModel = new RedPandaModel<>(ctx.bakeLayer(ModModelLayers.RED_PANDA));
        this.babyModel = new BabyRedPandaModel<>(ctx.bakeLayer(ModModelLayers.BABY_RED_PANDA));
    }

    @Override
    public Identifier getTextureLocation(RedPandaEntity redPandaEntity) {
        if (redPandaEntity.isBaby()) return redPandaEntity.isSleeping() ? redPandaEntity.getVariant().getBabySleepingTextureLocation() : redPandaEntity.getVariant().getBabyTextureLocation();
        return redPandaEntity.isSleeping() ? redPandaEntity.getVariant().getSleepingTextureLocation() : redPandaEntity.getVariant().getTextureLocation();
    }

    @Override
    public void submit(BestiaryRenderState<RedPandaEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
