package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.RiverTurtleModel;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RiverTurtleRenderer extends BestiaryMobRenderer<RiverTurtleEntity, RiverTurtleModel<RiverTurtleEntity>> {

    public RiverTurtleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new RiverTurtleModel<>(ctx.bakeLayer(ModModelLayers.RIVER_TURTLE)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(RiverTurtleEntity riverTurtleEntity) {
        return riverTurtleEntity.getVariant().getTextureLocation();
    }

}
