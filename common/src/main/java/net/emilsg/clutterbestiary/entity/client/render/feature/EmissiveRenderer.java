package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

public class EmissiveRenderer<E extends LivingEntity, S extends BestiaryRenderState<E>, EM extends EntityModel<? super S>> extends RenderLayer<S, EM> {
    private final Function<E, Identifier> emissiveTextureProvider;

    public EmissiveRenderer(RenderLayerParent<S, EM> context, Function<E, Identifier> emissiveTextureProvider) {
        super(context);
        this.emissiveTextureProvider = emissiveTextureProvider;
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, S state, float yRot, float xRot) {
        if (state.entity == null) return;
        Identifier emissiveTexture = emissiveTextureProvider.apply(state.entity);
        if (emissiveTexture == null) return;
        submitNodeCollector.order(1).submitModel(this.getParentModel(), state, matrices, RenderTypes.eyes(emissiveTexture), light, OverlayTexture.NO_OVERLAY, state.outlineColor);
    }

}
