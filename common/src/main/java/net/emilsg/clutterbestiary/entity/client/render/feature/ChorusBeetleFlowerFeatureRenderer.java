package net.emilsg.clutterbestiary.entity.client.render.feature;

import net.emilsg.clutterbestiary.entity.client.model.ChorusBeetleModel;
import net.emilsg.clutterbestiary.entity.custom.ChorusBeetleEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ChorusBeetleFlowerFeatureRenderer<T extends ChorusBeetleEntity, M extends ChorusBeetleModel<T>> extends FeatureRenderer<T, M> {
    private static final float FLOWER_OFFSET_X = -0.0625f;
    private static final float FLOWER_OFFSET_Y = 0.15f;
    private static final float FLOWER_OFFSET_Z = 0.0f;
    private static final float FLOWER_SIZE = 0.8f;

    public ChorusBeetleFlowerFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isCarryingChorusFlower()) return;

        matrices.push();
        this.getContextModel().getPart().getChild("all").rotate(matrices);
        matrices.translate(FLOWER_OFFSET_X, FLOWER_OFFSET_Y, FLOWER_OFFSET_Z);
        matrices.scale(FLOWER_SIZE, FLOWER_SIZE, FLOWER_SIZE);
        
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                entity, new ItemStack(Items.CHORUS_FLOWER), ModelTransformationMode.FIXED, false, matrices, vertexConsumers, entity.getWorld(), light, OverlayTexture.DEFAULT_UV, entity.getId()
        );

        matrices.pop();
    }
}
