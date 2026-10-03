package net.emilsg.clutterbestiary.entity.client.render;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyKiwiBirdModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KiwiBirdModel;
import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class KiwiBirdRenderer extends BestiaryMobRenderer<KiwiBirdEntity, BestiaryModel<KiwiBirdEntity>> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird/kiwi_bird.png");
    public static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird/baby_kiwi_bird.png");
    public static final Identifier EASTER_EGG_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird/kiwi_bird_talon.png");


    private final KiwiBirdModel<KiwiBirdEntity> adultModel;
    private final BabyKiwiBirdModel<KiwiBirdEntity> babyModel;

    public KiwiBirdRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new KiwiBirdModel<>(ctx.bakeLayer(ModModelLayers.KIWI_BIRD)), 0.3f);

        this.adultModel = new KiwiBirdModel<>(ctx.bakeLayer(ModModelLayers.KIWI_BIRD));
        this.babyModel = new BabyKiwiBirdModel<>(ctx.bakeLayer(ModModelLayers.BABY_KIWI_BIRD));
    }

    @Override
    public Identifier getTextureLocation(KiwiBirdEntity kiwiBirdEntity) {
        if (kiwiBirdEntity.getDisplayName() != null && kiwiBirdEntity.getDisplayName().contains(Component.nullToEmpty("Talon")))
            return EASTER_EGG_TEXTURE;
        return kiwiBirdEntity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public void submit(BestiaryRenderState<KiwiBirdEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
