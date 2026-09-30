package net.emilsg.clutterbestiary.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.emilsg.clutterbestiary.block.entity.ButterflyBottleBlockEntity;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ButterflyBottleBlockEntityRenderer implements BlockEntityRenderer<ButterflyBottleBlockEntity, ButterflyBottleBlockEntityRenderer.State> {
    private final BottledButterflyModel model;

    public ButterflyBottleBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.model = new BottledButterflyModel(ctx.bakeLayer(ModModelLayers.BUTTERFLY));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ButterflyBottleBlockEntity be, State state, float tickDelta, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(be, state, tickDelta, cameraPosition, breakProgress);
        state.texture = null;

        Level world = be.getLevel();
        if (world == null) return;
        state.facing = be.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        state.texture = be.getButterflyTexture();

        long time = world.getGameTime();
        float base = (time + tickDelta) * 0.125f;
        long offset = be.getBlockPos().asLong() & 0xFF;
        state.flap = (float) (Math.sin((base + offset * 0.05f)) * 0.25f) + 0.25f;
    }

    @Override
    public void submit(State state, PoseStack matrices, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        Direction facingDirection = state.facing;
        Identifier butterflyTexture = state.texture;
        if (facingDirection == null || butterflyTexture == null) return;

        matrices.pushPose();

        if (facingDirection == Direction.NORTH) {
            matrices.translate(0.85, 1.3, -0.37125);
            matrices.rotateDegrees(Axis.XP, 140f);
            matrices.rotateDegrees(Axis.YP, 0f);
            matrices.rotateDegrees(Axis.ZP, 15f);
        } else if (facingDirection == Direction.EAST) {
            matrices.translate(1.43125, 1.25, 0.675);
            matrices.rotateDegrees(Axis.XP, 100);
            matrices.rotateDegrees(Axis.YP, 47.5f);
            matrices.rotateDegrees(Axis.ZP, 90);
        } else if (facingDirection == Direction.SOUTH) {
            matrices.translate(0.15, 1.3, 1.375);
            matrices.rotateDegrees(Axis.XP, 220f);
            matrices.rotateDegrees(Axis.ZP, -15f);
            matrices.rotateDegrees(Axis.YP, 180f);
        } else if (facingDirection == Direction.WEST) {
            matrices.translate(-0.395f, 1.275, 0.158);
            matrices.rotateDegrees(Axis.XP, 160f);
            matrices.rotateDegrees(Axis.ZP, -40f);
            matrices.rotateDegrees(Axis.YP, 280f);
        }

        submitNodeCollector.submitModel(this.model, state.flap, matrices, RenderTypes.entityCutout(butterflyTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        matrices.popPose();
    }

    public static class State extends BlockEntityRenderState {
        @Nullable
        public Direction facing;
        @Nullable
        public Identifier texture;
        public float flap;
    }

    /**
     * The butterfly entity model posed only by its wing flap, driven by the block entity's render state.
     */
    private static class BottledButterflyModel extends Model<Float> {
        private final ModelPart leftWing;
        private final ModelPart rightWing;

        BottledButterflyModel(ModelPart root) {
            super(root, RenderTypes::entityCutout);
            ModelPart body = root.getChild("all").getChild("body");
            this.leftWing = body.getChild("leftWing");
            this.rightWing = body.getChild("rightWing");
        }

        @Override
        public void setupAnim(Float flap) {
            super.setupAnim(flap);
            this.leftWing.yRot = flap;
            this.rightWing.yRot = -flap;
        }
    }
}
