package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.StoatModel;
import net.emilsg.clutterbestiary.entity.custom.StoatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class StoatRenderer extends BestiaryMobRenderer<StoatEntity, StoatModel<StoatEntity>> {

    public StoatRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new StoatModel<>(ctx.bakeLayer(ModModelLayers.STOAT)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(StoatEntity stoatEntity) {
        return stoatEntity.isSleeping() ? stoatEntity.getVariant().getSleepingTextureLocation() : stoatEntity.getVariant().getTextureLocation();
    }

}
