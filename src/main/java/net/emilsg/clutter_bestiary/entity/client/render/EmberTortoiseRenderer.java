package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.EmberTortoiseModel;
import net.emilsg.clutter_bestiary.entity.custom.EmberTortoiseEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class EmberTortoiseRenderer extends MobEntityRenderer<EmberTortoiseEntity, EmberTortoiseModel<EmberTortoiseEntity>> {
    private static final Identifier TEXTURE = new Identifier(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/ember_tortoise.png");
    private static final Identifier FIRE_TEXTURE = new Identifier(ClutterBestiary.MOD_ID, "textures/entity/ember_tortoise/ember_tortoise_fire.png");

    public EmberTortoiseRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EmberTortoiseModel<>(ctx.getPart(ModModelLayers.EMBER_TORTOISE)), 0.9f);
    }

    @Override
    public Identifier getTexture(EmberTortoiseEntity entity) {
        return entity.isShielding() ? FIRE_TEXTURE : TEXTURE;
    }
}
