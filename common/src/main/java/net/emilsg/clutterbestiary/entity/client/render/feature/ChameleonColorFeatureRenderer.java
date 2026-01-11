package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.ChameleonModel;
import net.emilsg.clutterbestiary.entity.custom.ChameleonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.MapColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class ChameleonColorFeatureRenderer extends FeatureRenderer<ChameleonEntity, ChameleonModel<ChameleonEntity>> {
    private final ChameleonModel<ChameleonEntity> layerModel;
    private final Map<ChameleonEntity, CachedColor> colorCache = new WeakHashMap<>();

    public ChameleonColorFeatureRenderer(FeatureRendererContext<ChameleonEntity, ChameleonModel<ChameleonEntity>> ctx, EntityModelLoader loader) {
        super(ctx);
        this.layerModel = new ChameleonModel<>(loader.getModelPart(ModModelLayers.CHAMELEON));
    }

    private int getColor(ChameleonEntity chameleonEntity) {
        if (chameleonEntity.isDead()) return 0xFF7070;

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

        CachedColor cachedColor = this.colorCache.get(chameleonEntity);
        if (cachedColor != null && cachedColor.pos.equals(blockColorPos) && cachedColor.state == blockState) {
            return cachedColor.color;
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
        this.colorCache.put(chameleonEntity, new CachedColor(blockColorPos, blockState, color));
        return color;
    }

    private record CachedColor(BlockPos pos, BlockState state, int color) {
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider providers, int light, ChameleonEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        int envColor = getColor(entity);

        if (envColor != entity.getTargetColor()) {
            entity.setTargetColor(envColor);
        }

        int rgb = entity.getCurrentColor();
        int argb = 0xFF000000 | (rgb & 0x00FFFFFF);
        Identifier texture = getTexture(entity);

        if (entity.isInvisible()) {
            var mc = MinecraftClient.getInstance();
            if (mc.hasOutline(entity)) {
                getContextModel().copyStateTo(layerModel);
                layerModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
                layerModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
                VertexConsumer vc = providers.getBuffer(RenderLayer.getOutline(texture));

                layerModel.render(matrices, vc, light, LivingEntityRenderer.getOverlay(entity, 0.0F), 0xFF000000);
            }
            return;
        }

        render(getContextModel(), layerModel, texture,
                matrices, providers, light, entity,
                limbAngle, limbDistance, animationProgress, headYaw, headPitch,
                tickDelta, argb);
    }
}
