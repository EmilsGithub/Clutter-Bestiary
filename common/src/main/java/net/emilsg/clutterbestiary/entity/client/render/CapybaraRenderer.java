package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CapybaraModel;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CapybaraRenderer extends BestiaryMobRenderer<CapybaraEntity, CapybaraModel<CapybaraEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/capybara/capybara.png");

    public CapybaraRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CapybaraModel<>(ctx.bakeLayer(ModModelLayers.CAPYBARA)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(CapybaraEntity entity) {
        return TEXTURE;
    }

}
