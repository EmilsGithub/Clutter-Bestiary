package net.emilsg.clutterbestiary.entity.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyWoodpeckerModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryEntityModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.WoodpeckerModel;
import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class WoodpeckerRenderer extends BestiaryMobRenderer<WoodpeckerEntity, BestiaryEntityModel<WoodpeckerEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/woodpecker/woodpecker.png");
    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/woodpecker/baby_woodpecker.png");

    private final WoodpeckerModel adultModel;
    private final BabyWoodpeckerModel babyModel;

    public WoodpeckerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WoodpeckerModel(ctx.bakeLayer(ModModelLayers.WOODPECKER)), 0.25f);

        this.adultModel = new WoodpeckerModel(ctx.bakeLayer(ModModelLayers.WOODPECKER));
        this.babyModel = new BabyWoodpeckerModel(ctx.bakeLayer(ModModelLayers.BABY_WOODPECKER));
    }

    @Override
    public Identifier getTextureLocation(WoodpeckerEntity entity) {
        return entity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public void submit(BestiaryRenderState<WoodpeckerEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
