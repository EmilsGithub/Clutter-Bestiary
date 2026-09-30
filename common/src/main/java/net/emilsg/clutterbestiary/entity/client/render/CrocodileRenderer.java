package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CrocodileModel;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CrocodileRenderer extends BestiaryMobRenderer<CrocodileEntity, CrocodileModel<CrocodileEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/crocodile/crocodile.png");

    public CrocodileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CrocodileModel<>(ctx.bakeLayer(ModModelLayers.CROCODILE)), 1.25f);
    }

    @Override
    public Identifier getTextureLocation(CrocodileEntity entity) {
        return TEXTURE;
    }

}
