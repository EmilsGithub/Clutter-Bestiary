package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.entity.client.render.parent.BestiaryMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.KiwiBirdModel;
import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class KiwiBirdRenderer extends BestiaryMobRenderer<KiwiBirdEntity, KiwiBirdModel<KiwiBirdEntity>> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird/kiwi_bird.png");
    public static final Identifier EASTER_EGG_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/kiwi_bird/kiwi_bird_talon.png");


    public KiwiBirdRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new KiwiBirdModel<>(ctx.bakeLayer(ModModelLayers.KIWI_BIRD)), 0.3f);
    }

    @Override
    public Identifier getTextureLocation(KiwiBirdEntity kiwiBirdEntity) {
        if (kiwiBirdEntity.getDisplayName() != null && kiwiBirdEntity.getDisplayName().contains(Component.nullToEmpty("Talon")))
            return EASTER_EGG_TEXTURE;
        return TEXTURE;
    }

}
