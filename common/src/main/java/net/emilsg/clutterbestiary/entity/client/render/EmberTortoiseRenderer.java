package net.emilsg.clutterbestiary.entity.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyEmberTortoiseModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.EmberTortoiseModel;
import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class EmberTortoiseRenderer extends BestiaryMobRenderer<EmberTortoiseEntity, BestiaryModel<EmberTortoiseEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/ember_tortoise.png");
    private static final Identifier FIRE_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/ember_tortoise_fire.png");
    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/baby_ember_tortoise.png");
    private static final Identifier BABY_FIRE_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/baby_ember_tortoise_fire.png");

    private final EmberTortoiseModel<EmberTortoiseEntity> adultModel;
    private final BabyEmberTortoiseModel<EmberTortoiseEntity> babyModel;

    public EmberTortoiseRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EmberTortoiseModel<>(ctx.bakeLayer(ModModelLayers.EMBER_TORTOISE)), 0.9f);

        this.adultModel = new EmberTortoiseModel<>(ctx.bakeLayer(ModModelLayers.EMBER_TORTOISE));
        this.babyModel = new BabyEmberTortoiseModel<>(ctx.bakeLayer(ModModelLayers.BABY_EMBER_TORTOISE));
    }

    @Override
    public Identifier getTextureLocation(EmberTortoiseEntity entity) {
        if (entity.isBaby()) return entity.isShielding() ? BABY_FIRE_TEXTURE : BABY_TEXTURE;
        return entity.isShielding() ? FIRE_TEXTURE : TEXTURE;
    }

    @Override
    public void submit(BestiaryRenderState<EmberTortoiseEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
