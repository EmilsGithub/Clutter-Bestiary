package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.BabyEmperorPenguinModel;
import net.emilsg.clutter_bestiary.entity.client.model.EmperorPenguinModel;
import net.emilsg.clutter_bestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutter_bestiary.entity.custom.EmperorPenguinEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EmperorPenguinRenderer extends MobEntityRenderer<EmperorPenguinEntity, BestiaryModel<EmperorPenguinEntity>> {
    public static final Identifier ADULT_TEXTURE = new Identifier(ClutterBestiary.MOD_ID, "textures/entity/emperor_penguin/adult_emperor_penguin.png");
    public static final Identifier BABY_TEXTURE = new Identifier(ClutterBestiary.MOD_ID, "textures/entity/emperor_penguin/baby_emperor_penguin.png");

    private final BabyEmperorPenguinModel<EmperorPenguinEntity> babyModel;
    private final EmperorPenguinModel<EmperorPenguinEntity> adultModel;


    public EmperorPenguinRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EmperorPenguinModel<>(ctx.getPart(ModModelLayers.EMPEROR_PENGUIN)), 0.3f);

        this.adultModel = new EmperorPenguinModel<>(ctx.getPart(ModModelLayers.EMPEROR_PENGUIN));
        this.babyModel = new BabyEmperorPenguinModel<>(ctx.getPart(ModModelLayers.BABY_EMPEROR_PENGUIN));
    }

    @Override
    public Identifier getTexture(EmperorPenguinEntity emperorPenguinEntity) {
        return emperorPenguinEntity.isBaby() ? BABY_TEXTURE : ADULT_TEXTURE;
    }

    @Override
    public void render(EmperorPenguinEntity emperorPenguinEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.model = emperorPenguinEntity.isBaby() ? babyModel : adultModel;
        super.render(emperorPenguinEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
