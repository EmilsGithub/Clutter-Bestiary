package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.KiwiBirdModel;
import net.emilsg.clutter_bestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class KiwiBirdRenderer extends MobEntityRenderer<KiwiBirdEntity, KiwiBirdModel<KiwiBirdEntity>> {
    public static final Identifier TEXTURE = new Identifier(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird_box.png");

    public KiwiBirdRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new KiwiBirdModel<>(ctx.getPart(ModModelLayers.KIWI_BIRD)), 0.2f);
    }

    @Override
    public Identifier getTexture(KiwiBirdEntity kiwiBirdEntity) {
        return TEXTURE;
    }

    @Override
    public void render(KiwiBirdEntity kiwiBirdEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.3f;

        super.render(kiwiBirdEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
