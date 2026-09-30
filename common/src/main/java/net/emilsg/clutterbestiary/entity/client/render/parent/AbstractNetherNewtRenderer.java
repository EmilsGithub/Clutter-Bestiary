package net.emilsg.clutterbestiary.entity.client.render.parent;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.NetherNewtModel;
import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public abstract class AbstractNetherNewtRenderer extends MobEntityRenderer<AbstractNetherNewtEntity, NetherNewtModel<AbstractNetherNewtEntity>> {

    public AbstractNetherNewtRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NetherNewtModel<>(ctx.getPart(ModModelLayers.NETHER_NEWT)), 0.35f);
    }

    @Override
    public abstract Identifier getTexture(AbstractNetherNewtEntity entity);
}
