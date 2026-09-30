package net.emilsg.clutterbestiary.entity.client.render.parent;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.NetherNewtModel;
import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * Newt size is stored in the SCALE attribute, which the living entity renderer already applies to the model and
 * shadow, so this renderer doesn't scale by it again (that squared the size and let babies outgrow small adults).
 */
public abstract class AbstractNetherNewtRenderer extends BestiaryMobRenderer<AbstractNetherNewtEntity, NetherNewtModel<AbstractNetherNewtEntity>> {

    public AbstractNetherNewtRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new NetherNewtModel<>(ctx.bakeLayer(ModModelLayers.NETHER_NEWT)), 0.35f);
    }

    @Override
    public abstract Identifier getTextureLocation(AbstractNetherNewtEntity entity);
}
