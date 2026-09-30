package net.emilsg.clutterbestiary.entity.client.render;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.BabyEmperorPenguinModel;
import net.emilsg.clutterbestiary.entity.client.model.EmperorPenguinModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.EmperorPenguinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class EmperorPenguinRenderer extends BestiaryMobRenderer<EmperorPenguinEntity, BestiaryModel<EmperorPenguinEntity>> {
    public static final Identifier ADULT_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/emperor_penguin/adult_emperor_penguin.png");
    public static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/emperor_penguin/baby_emperor_penguin.png");

    private final EmperorPenguinModel<EmperorPenguinEntity> adultModel;
    private final BabyEmperorPenguinModel<EmperorPenguinEntity> babyModel;

    public EmperorPenguinRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EmperorPenguinModel<>(ctx.bakeLayer(ModModelLayers.EMPEROR_PENGUIN)), 0.3f);

        this.adultModel = new EmperorPenguinModel<>(ctx.bakeLayer(ModModelLayers.EMPEROR_PENGUIN));
        this.babyModel = new BabyEmperorPenguinModel<>(ctx.bakeLayer(ModModelLayers.BABY_EMPEROR_PENGUIN));
    }

    @Override
    public Identifier getTextureLocation(EmperorPenguinEntity emperorPenguinEntity) {
        return emperorPenguinEntity.isBaby() ? BABY_TEXTURE : ADULT_TEXTURE;
    }

    @Override
    public void submit(BestiaryRenderState<EmperorPenguinEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
