package net.emilsg.clutterbestiary.entity.client.render.feature;
import net.minecraft.client.renderer.block.BlockAndTintGetter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.BabyChameleonModel;
import net.emilsg.clutterbestiary.entity.client.model.ChameleonModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.state.ChameleonRenderState;
import net.emilsg.clutterbestiary.entity.custom.ChameleonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.Map;
import java.util.WeakHashMap;

public class ChameleonColorFeatureRenderer extends RenderLayer<ChameleonRenderState, ParentTameableModel<ChameleonEntity>> {
    private static final Map<ChameleonEntity, CachedColor> COLOR_CACHE = new WeakHashMap<>();
    private final ChameleonModel<ChameleonEntity> adultLayerModel;
    private final BabyChameleonModel<ChameleonEntity> babyLayerModel;
    private final Identifier adultTexture;
    private final Identifier babyTexture;

    public ChameleonColorFeatureRenderer(RenderLayerParent<ChameleonRenderState, ParentTameableModel<ChameleonEntity>> ctx, EntityModelSet loader, Identifier adultTexture, Identifier babyTexture) {
        super(ctx);
        this.adultLayerModel = new ChameleonModel<>(loader.bakeLayer(ModModelLayers.CHAMELEON));
        this.babyLayerModel = new BabyChameleonModel<>(loader.bakeLayer(ModModelLayers.BABY_CHAMELEON));
        this.adultTexture = adultTexture;
        this.babyTexture = babyTexture;
    }

    /**
     * Samples the colour of the block the chameleon stands on; used while extracting the render state.
     */
    public static int getEnvironmentColor(ChameleonEntity chameleonEntity) {
        if (chameleonEntity.isDeadOrDying()) return 0xFF7070;

        Level world = chameleonEntity.level();
        BlockPos entityPos = chameleonEntity.blockPosition();

        BlockState blockState = world.getBlockState(entityPos);
        BlockPos blockColorPos = entityPos;

        if (blockState.isAir() || blockState.getBlock() instanceof FlowerBlock) {
            BlockPos belowPos = entityPos.below();
            BlockState belowState = world.getBlockState(belowPos);

            if (belowState.isAir()) {
                return chameleonEntity.getTargetColor();
            }

            blockState = belowState;
            blockColorPos = belowPos;
        }

        CachedColor cachedColor = COLOR_CACHE.get(chameleonEntity);
        if (cachedColor != null && cachedColor.pos.equals(blockColorPos) && cachedColor.state == blockState) {
            return cachedColor.color;
        }

        BlockColors blockColorProvider = Minecraft.getInstance().getBlockColors();
        int color = -1;

        if (blockColorProvider != null) {
            BlockTintSource tintSource = blockColorProvider.getTintSource(blockState, 0);
            if (tintSource != null && world instanceof BlockAndTintGetter tintGetter) {
                color = tintSource.colorInWorld(blockState, tintGetter, blockColorPos);
            }
        }

        if (color == -1 || color == 0) {
            MapColor mapColor = blockState.getMapColor(world, blockColorPos);
            color = (mapColor != null && mapColor.col != 0) ? mapColor.col : 0x90C47C;
        }
        COLOR_CACHE.put(chameleonEntity, new CachedColor(blockColorPos, blockState, color));
        return color;
    }

    private record CachedColor(BlockPos pos, BlockState state, int color) {
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, ChameleonRenderState state, float yRot, float xRot) {
        int argb = 0xFF000000 | (state.color & 0x00FFFFFF);
        if (state.isBaby) {
            coloredCutoutModelCopyLayerRender(this.babyLayerModel, this.babyTexture, matrices, submitNodeCollector, light, state, argb, 1);
        } else {
            coloredCutoutModelCopyLayerRender(this.adultLayerModel, this.adultTexture, matrices, submitNodeCollector, light, state, argb, 1);
        }
    }
}
