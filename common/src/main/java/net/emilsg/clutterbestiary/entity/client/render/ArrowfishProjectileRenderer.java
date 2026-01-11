package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.custom.ArrowfishProjectileEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ArrowfishProjectileRenderer extends EntityRenderer<ArrowfishProjectileEntity> {
    private static final Identifier TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/arrowfish/arrowfish.png");
    private final ModelPart model;

    public ArrowfishProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = context.getPart(ModModelLayers.ARROWFISH);
    }

    @Override
    public void render(ArrowfishProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw())));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));

        float shake = entity.shake - tickDelta;
        if (shake > 0.0F) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-MathHelper.sin(shake * 3.0F) * shake));
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        // Center the fish's body on the projectile instead of its authored ground-level pivot.
        matrices.translate(0.0F, -22.0F / 16.0F, 0.0F);
        this.model.traverse().forEach(ModelPart::resetTransform);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(ArrowfishProjectileEntity entity) {
        return TEXTURE;
    }
}
