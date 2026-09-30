package net.emilsg.clutterbestiary.entity.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.emilsg.clutterbestiary.entity.client.model.BeaverModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BeaverRenderState;
import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class BeaverStripItemFeatureRenderer extends RenderLayer<BeaverRenderState, BeaverModel<BeaverEntity>> {

    public BeaverStripItemFeatureRenderer(RenderLayerParent<BeaverRenderState, BeaverModel<BeaverEntity>> context) {
        super(context);
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, BeaverRenderState state, float yRot, float xRot) {
        if (state.heldItem.isEmpty() || state.entity == null) return;

        BeaverEntity entity = state.entity;
        ItemStack stack = entity.getMainHandItem();
        Level world = entity.level();

        matrices.pushPose();
        ModelPart root = this.getParentModel().root();
        ModelPart all = root.getChild("all");
        ModelPart frontRightLeg = all.getChild("frontRightLeg");
        ModelPart heldItem = frontRightLeg.getChild("heldItem");

        all.translateAndRotate(matrices);
        frontRightLeg.translateAndRotate(matrices);
        heldItem.translateAndRotate(matrices);

        matrices.translate(0.05F, 0.0125F, -0.07F);
        matrices.rotateDegrees(Axis.XP, -90.0F);
        matrices.rotateDegrees(Axis.YP, 10.0F);
        matrices.scale(0.5F, 0.5F, 0.5F);

        state.heldItem.submit(matrices, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);

        var mat = matrices.last().pose();
        var v = new Vector4f(0, 0, 0, 1).mul(mat);

        Vec3 cam = Minecraft.getInstance().gameRenderer.mainCamera().position();
        double x = cam.x + v.x;
        double y = cam.y + v.y;
        double z = cam.z + v.z;

        if (!stack.isEmpty() && entity.shouldSpawnStrippingParticles() && (entity.tickCount % 5) == 0) {
            if (Block.byItem(stack.getItem()) != null) {
                world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Block.byItem(stack.getItem()).defaultBlockState()), x, y + 0.06f, z, 0, 0.02, 0);
            } else {
                world.addParticle(ParticleTypes.CRIT, x, y + 0.06f, z, 0, 0.02, 0);
            }
        }

        matrices.popPose();
    }
}
