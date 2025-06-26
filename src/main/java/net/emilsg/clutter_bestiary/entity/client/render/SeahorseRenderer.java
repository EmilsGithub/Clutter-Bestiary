package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutter_bestiary.entity.client.model.SeahorseModel;
import net.emilsg.clutter_bestiary.entity.custom.SeahorseEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SeahorseRenderer extends MobEntityRenderer<SeahorseEntity, SeahorseModel<SeahorseEntity>> {
    public static final Identifier YELLOW_TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/seahorse/yellow_seahorse.png");
    public static final Identifier LIGHT_BLUE_TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/seahorse/light_blue_seahorse.png");
    public static final Identifier RED_TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/seahorse/red_seahorse.png");
    public static final Identifier PURPLE_TEXTURE = Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/seahorse/purple_seahorse.png");


    public SeahorseRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SeahorseModel<>(ctx.getPart(ModModelLayers.SEAHORSE)), 0.2f);
    }

    @Override
    public Identifier getTexture(SeahorseEntity seahorseEntity) {
        return switch (seahorseEntity.getVariant()) {
            case YELLOW -> YELLOW_TEXTURE;
            case LIGHT_BLUE -> LIGHT_BLUE_TEXTURE;
            case RED -> RED_TEXTURE;
            case PURPLE -> PURPLE_TEXTURE;
        };
    }

    @Override
    public void render(SeahorseEntity seahorseEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.2f;

        super.render(seahorseEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
