package net.emilsg.clutterbestiary.entity.client.render.parent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

/**
 * Shared renderer for the Bestiary mobs: extracts the entity into a {@link BestiaryRenderState}, resolves textures from
 * the entity, and applies the per-model baby transform before the model is submitted.
 */
public abstract class AbstractBestiaryMobRenderer<E extends Mob, S extends BestiaryRenderState<E>, M extends BestiaryEntityModel<E>> extends MobRenderer<E, S, M> {
    private static final float MODEL_Y_OFFSET = 1.501F;

    protected AbstractBestiaryMobRenderer(EntityRendererProvider.Context ctx, M model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Override
    public void extractRenderState(E entity, S state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
    }

    @Override
    public final Identifier getTextureLocation(S state) {
        return this.getTextureLocation(state.entity);
    }

    public abstract Identifier getTextureLocation(E entity);

    @Override
    protected void scale(S state, PoseStack poseStack) {
        if (state.entity != null) {
            this.scale(state.entity, poseStack);
        }

        EntityModel<?> model = this.getModel();
        if (state.isBaby && model instanceof BestiaryEntityModel<?> bestiaryModel) {
            float babyScale = bestiaryModel.getBabyScale();
            if (babyScale != 1.0F) {
                // Equivalent to the old in-model transform, which ran after the renderer's model offset.
                poseStack.scale(babyScale, babyScale, babyScale);
                poseStack.translate(0.0F, bestiaryModel.getBabyYOffset() + MODEL_Y_OFFSET - MODEL_Y_OFFSET / babyScale, 0.0F);
            }
        }
    }

    protected void scale(E entity, PoseStack poseStack) {
    }
}
