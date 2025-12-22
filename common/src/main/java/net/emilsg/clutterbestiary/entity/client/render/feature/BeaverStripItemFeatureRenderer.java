package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.model.BeaverModel;
import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.joml.Vector4f;

public class BeaverStripItemFeatureRenderer<T extends BeaverEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    public BeaverStripItemFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack stack = entity.getMainHandStack();
        if (stack.isEmpty()) return;

        EntityModel<T> ctxModel = this.getContextModel();
        if (!(ctxModel instanceof BeaverModel<?> beaverModel)) return;

        World world = entity.getWorld();

        matrices.push();
        ModelPart root = beaverModel.getPart();
        ModelPart all = root.getChild("all");
        ModelPart frontRightLeg = all.getChild("frontRightLeg");
        ModelPart heldItem = frontRightLeg.getChild("heldItem");

        all.rotate(matrices);
        frontRightLeg.rotate(matrices);
        heldItem.rotate(matrices);

        matrices.translate(0.05F, 0.0125F, -0.07F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(10.0F));
        matrices.scale(0.5F, 0.5F, 0.5F);

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                entity, stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers,
                world, light, OverlayTexture.DEFAULT_UV, 0
        );

        var mat = matrices.peek().getPositionMatrix();
        var v = new Vector4f(0, 0, 0, 1).mul(mat);

        var cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        double x = cam.x + v.x;
        double y = cam.y + v.y;
        double z = cam.z + v.z;

        if (entity.shouldSpawnStrippingParticles() && (entity.age % 5) == 0) {
            if (Block.getBlockFromItem(stack.getItem()) != null) {
                world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Block.getBlockFromItem(stack.getItem()).getDefaultState()), x, y + 0.06f, z, 0, 0.02, 0);
            } else {
                world.addParticle(ParticleTypes.CRIT, x, y + 0.06f, z, 0, 0.02, 0);
            }
        }

        matrices.pop();
    }
}
