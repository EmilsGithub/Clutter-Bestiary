package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyCrocodileModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.CrocodileModel;
import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CrocodileRenderer extends BestiaryMobRenderer<CrocodileEntity, ParentTameableModel<CrocodileEntity>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/crocodile/crocodile.png");
    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/crocodile/baby_crocodile.png");

    private final CrocodileModel<CrocodileEntity> adultModel;
    private final BabyCrocodileModel<CrocodileEntity> babyModel;

    public CrocodileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CrocodileModel<>(ctx.bakeLayer(ModModelLayers.CROCODILE)), 1.25f);

        this.adultModel = new CrocodileModel<>(ctx.bakeLayer(ModModelLayers.CROCODILE));
        this.babyModel = new BabyCrocodileModel<>(ctx.bakeLayer(ModModelLayers.BABY_CROCODILE));
    }

    @Override
    public Identifier getTextureLocation(CrocodileEntity entity) {
        return entity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public void submit(BestiaryRenderState<CrocodileEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
