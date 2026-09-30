package net.emilsg.clutterbestiary.entity.client.render.parent;

import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;

public abstract class BestiaryMobRenderer<E extends Mob, M extends BestiaryEntityModel<E>> extends AbstractBestiaryMobRenderer<E, BestiaryRenderState<E>, M> {

    protected BestiaryMobRenderer(EntityRendererProvider.Context ctx, M model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Override
    public BestiaryRenderState<E> createRenderState() {
        return new BestiaryRenderState<>();
    }
}
