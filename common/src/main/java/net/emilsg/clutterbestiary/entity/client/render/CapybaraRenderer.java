package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyCapybaraModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CapybaraModel;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CapybaraRenderer extends BestiaryMobRenderer<CapybaraEntity, ParentTameableModel<CapybaraEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/capybara/capybara.png");
    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/capybara/baby_capybara.png");

    private final CapybaraModel<CapybaraEntity> adultModel;
    private final BabyCapybaraModel<CapybaraEntity> babyModel;

    public CapybaraRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CapybaraModel<>(ctx.bakeLayer(ModModelLayers.CAPYBARA)), 0.4f);

        this.adultModel = new CapybaraModel<>(ctx.bakeLayer(ModModelLayers.CAPYBARA));
        this.babyModel = new BabyCapybaraModel<>(ctx.bakeLayer(ModModelLayers.BABY_CAPYBARA));
    }

    @Override
    public Identifier getTextureLocation(CapybaraEntity entity) {
        return entity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public void submit(BestiaryRenderState<CapybaraEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
