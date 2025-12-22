package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KiwiBirdModel;
import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class KiwiBirdRenderer extends MobEntityRenderer<KiwiBirdEntity, KiwiBirdModel<KiwiBirdEntity>> {
    public static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird/kiwi_bird.png");
    public static final Identifier EASTER_EGG_TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird/kiwi_bird_talon.png");


    public KiwiBirdRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new KiwiBirdModel<>(ctx.getPart(ModModelLayers.KIWI_BIRD)), 0.2f);
    }

    @Override
    public Identifier getTexture(KiwiBirdEntity kiwiBirdEntity) {
        if (kiwiBirdEntity.getDisplayName() != null && kiwiBirdEntity.getDisplayName().contains(Text.of("Talon")))
            return EASTER_EGG_TEXTURE;
        return TEXTURE;
    }

    @Override
    public void render(KiwiBirdEntity kiwiBirdEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.3f;

        super.render(kiwiBirdEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
