package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.SeahorseModel;
import net.emilsg.clutterbestiary.entity.custom.SeahorseEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class SeahorseRenderer extends BestiaryMobRenderer<SeahorseEntity, SeahorseModel<SeahorseEntity>> {

    public SeahorseRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SeahorseModel<>(ctx.bakeLayer(ModModelLayers.SEAHORSE)), 0.2f);
    }

    @Override
    public Identifier getTextureLocation(SeahorseEntity seahorseEntity) {
        return seahorseEntity.getVariant().getTextureLocation();
    }

}
