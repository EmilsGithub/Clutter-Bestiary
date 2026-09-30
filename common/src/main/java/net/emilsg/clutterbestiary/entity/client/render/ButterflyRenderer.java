package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ButterflyModel;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class ButterflyRenderer extends BestiaryMobRenderer<ButterflyEntity, ButterflyModel<ButterflyEntity>> {

    public ButterflyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ButterflyModel<>(ctx.bakeLayer(ModModelLayers.BUTTERFLY)), 0.2f);
    }

    @Override
    public Identifier getTextureLocation(ButterflyEntity butterflyEntity) {
        return butterflyEntity.getVariant().getTextureLocation();
    }

}
