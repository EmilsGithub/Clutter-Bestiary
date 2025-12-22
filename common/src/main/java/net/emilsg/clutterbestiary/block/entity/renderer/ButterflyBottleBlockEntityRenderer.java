package net.emilsg.clutterbestiary.block.entity.renderer;

import net.emilsg.clutterbestiary.block.entity.ButterflyBottleBlockEntity;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ButterflyModel;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class ButterflyBottleBlockEntityRenderer implements BlockEntityRenderer<ButterflyBottleBlockEntity> {
    private final ButterflyModel<ButterflyEntity> model;
    private float ticker;

    public ButterflyBottleBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new ButterflyModel<>(ctx.getLayerModelPart(ModModelLayers.BUTTERFLY));
    }

    @Override
    public void render(ButterflyBottleBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vcp, int light, int overlay) {
        World world = be.getWorld();
        if (world == null) return;
        Direction facingDirection = be.getCachedState().get(HorizontalFacingBlock.FACING);
        if (facingDirection == null) return;
        Identifier butterflyTexture = be.getButterflyTexture();
        if (butterflyTexture == null) return;

        long time = world.getTime();
        float base = (time + tickDelta) * 0.125f;
        long offset = be.getPos().asLong() & 0xFF;
        float flap = (float) (Math.sin((base + offset * 0.05f)) * 0.25f) + 0.25f;

        matrices.push();

        if (facingDirection == Direction.NORTH) {
            matrices.translate(0.85, 1.3, -0.37125);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(140f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(0f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(15f));
        } else if (facingDirection == Direction.EAST) {
            matrices.translate(1.43125, 1.25, 0.675);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(100));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(47.5f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
        } else if (facingDirection == Direction.SOUTH) {
            matrices.translate(0.15, 1.3, 1.375);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(220f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-15f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
        } else if (facingDirection == Direction.WEST) {
            matrices.translate(-0.395f, 1.275, 0.158);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(160f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-40f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(280f));
        }

        model.getLeftWing().yaw = flap;
        model.getRightWing().yaw = -flap;

        VertexConsumer vc = vcp.getBuffer(RenderLayer.getEntityCutoutNoCull(butterflyTexture));
        model.render(matrices, vc, light, overlay);
        matrices.pop();
    }

}

