package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.DragonflyModel;
import net.emilsg.clutterbestiary.entity.custom.DragonflyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class DragonflyRenderer extends BestiaryMobRenderer<DragonflyEntity, DragonflyModel<DragonflyEntity>> {

    public DragonflyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DragonflyModel<>(ctx.bakeLayer(ModModelLayers.DRAGONFLY)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(DragonflyEntity dragonflyEntity) {
        return dragonflyEntity.getVariant().getTextureLocation();
    }

}
