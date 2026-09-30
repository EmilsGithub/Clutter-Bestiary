package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.RedPandaModel;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RedPandaRenderer extends BestiaryMobRenderer<RedPandaEntity, RedPandaModel<RedPandaEntity>> {

    public RedPandaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new RedPandaModel<>(ctx.bakeLayer(ModModelLayers.RED_PANDA)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(RedPandaEntity redPandaEntity) {
        return redPandaEntity.isSleeping() ? redPandaEntity.getVariant().getSleepingTextureLocation() : redPandaEntity.getVariant().getTextureLocation();
    }

}
