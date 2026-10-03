package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabySeahorseModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentFishModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.SeahorseModel;
import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class SeahorseRenderer extends BestiaryMobRenderer<SeahorseEntity, ParentFishModel<SeahorseEntity>> {

    private final SeahorseModel<SeahorseEntity> adultModel;
    private final BabySeahorseModel<SeahorseEntity> babyModel;

    public SeahorseRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SeahorseModel<>(ctx.bakeLayer(ModModelLayers.SEAHORSE)), 0.2f);

        this.adultModel = new SeahorseModel<>(ctx.bakeLayer(ModModelLayers.SEAHORSE));
        this.babyModel = new BabySeahorseModel<>(ctx.bakeLayer(ModModelLayers.BABY_SEAHORSE));
    }

    @Override
    public Identifier getTextureLocation(SeahorseEntity seahorseEntity) {
        return seahorseEntity.isBaby() ? seahorseEntity.getVariant().getBabyTextureLocation() : seahorseEntity.getVariant().getTextureLocation();
    }

    @Override
    public void submit(BestiaryRenderState<SeahorseEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
