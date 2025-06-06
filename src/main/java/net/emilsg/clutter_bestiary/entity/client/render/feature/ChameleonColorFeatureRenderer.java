package net.emilsg.clutter_bestiary.entity.client.render.feature;

import net.emilsg.clutter_bestiary.entity.client.model.ChameleonModel;
import net.emilsg.clutter_bestiary.entity.custom.ChameleonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.MapColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChameleonColorFeatureRenderer extends FeatureRenderer<ChameleonEntity, ChameleonModel<ChameleonEntity>> {

    public ChameleonColorFeatureRenderer(FeatureRendererContext<ChameleonEntity, ChameleonModel<ChameleonEntity>> context) {
        super(context);
    }

    private static int getColor(ChameleonEntity chameleonEntity) {
        if(chameleonEntity.isDead()) return 0xFF7070;

        World world = chameleonEntity.getWorld();
        BlockPos entityPos = chameleonEntity.getBlockPos();

        BlockState blockState = world.getBlockState(entityPos);
        BlockPos blockColorPos = entityPos;

        if (blockState.isAir() || blockState.getBlock() instanceof FlowerBlock) {
            BlockPos belowPos = entityPos.down();
            BlockState belowState = world.getBlockState(belowPos);

            if (belowState.isAir()) {
                return chameleonEntity.getTargetColor();
            }

            blockState = belowState;
            blockColorPos = belowPos;
        }

        BlockColors blockColorProvider = MinecraftClient.getInstance().getBlockColors();
        int color = -1;

        if (blockColorProvider != null) {
            color = blockColorProvider.getColor(blockState, world, blockColorPos, 1);
        }

        if (color == -1 || color == 0) {
            MapColor mapColor = blockState.getMapColor(world, blockColorPos);
            color = (mapColor != null && mapColor.color != 0) ? mapColor.color : 0x90C47C;
        }
        return color;
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ChameleonEntity chameleonEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        int color = getColor(chameleonEntity);

        if (color != chameleonEntity.getTargetColor()) {
            chameleonEntity.setTargetColor(color);
        }

        int currentColor = chameleonEntity.getCurrentColor();

        float r = ((currentColor >> 16) & 0xFF) / 255.0f;
        float g = ((currentColor >> 8) & 0xFF) / 255.0f;
        float b = (currentColor & 0xFF) / 255.0f;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(chameleonEntity)));
        this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0f);
    }

}
