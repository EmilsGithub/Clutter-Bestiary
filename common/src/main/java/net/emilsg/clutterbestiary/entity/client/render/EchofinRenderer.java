package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.EchofinModel;
import net.emilsg.clutterbestiary.entity.client.render.feature.EmissiveRenderer;
import net.emilsg.clutterbestiary.entity.custom.EchofinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class EchofinRenderer extends BestiaryMobRenderer<EchofinEntity, EchofinModel<EchofinEntity>> {

    public EchofinRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EchofinModel<>(ctx.bakeLayer(ModModelLayers.ECHOFIN)), 0.2f);
        this.addLayer(new EmissiveRenderer<>(this, EchofinRenderer::getEmissiveTexture));
    }

    public static Identifier getEmissiveTexture(EchofinEntity echofinEntity) {
        return echofinEntity.hasAbility() ? echofinEntity.getVariant().getEmissiveTextureLocation() : null;
    }

    @Override
    public Identifier getTextureLocation(EchofinEntity echofinEntity) {
        return echofinEntity.getVariant().getTextureLocation();
    }

}
