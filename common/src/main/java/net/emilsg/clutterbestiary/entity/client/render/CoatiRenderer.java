package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CoatiModel;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CoatiRenderer extends BestiaryMobRenderer<CoatiEntity, CoatiModel<CoatiEntity>> {

    public CoatiRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CoatiModel<>(ctx.bakeLayer(ModModelLayers.COATI)), 0.5f);
    }

    @Override
    public Identifier getTextureLocation(CoatiEntity coatiEntity) {
        return coatiEntity.getVariant().getTextureLocation();
    }

}
