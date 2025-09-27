package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CapybaraModel;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CapybaraRenderer extends MobEntityRenderer<CapybaraEntity, CapybaraModel<CapybaraEntity>> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/capybara/capybara.png");

    public CapybaraRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CapybaraModel<>(ctx.getPart(ModModelLayers.CAPYBARA)), 0.4f);
    }

    @Override
    public Identifier getTexture(CapybaraEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(CapybaraEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.4f;

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
