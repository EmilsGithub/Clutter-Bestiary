package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.EmberTortoiseModel;
import net.emilsg.clutterbestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class EmberTortoiseRenderer extends BestiaryMobRenderer<EmberTortoiseEntity, EmberTortoiseModel<EmberTortoiseEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/ember_tortoise.png");
    private static final Identifier FIRE_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/ember_tortoise_fire.png");

    public EmberTortoiseRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EmberTortoiseModel<>(ctx.bakeLayer(ModModelLayers.EMBER_TORTOISE)), 0.9f);
    }

    @Override
    public Identifier getTextureLocation(EmberTortoiseEntity entity) {
        return entity.isShielding() ? FIRE_TEXTURE : TEXTURE;
    }
}
