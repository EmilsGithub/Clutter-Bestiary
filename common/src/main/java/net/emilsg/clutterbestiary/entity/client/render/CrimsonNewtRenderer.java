package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.entity.client.render.parent.AbstractNetherNewtRenderer;
import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CrimsonNewtRenderer extends AbstractNetherNewtRenderer {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/nether_newt/crimson_newt.png");
    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "textures/entity/nether_newt/baby_crimson_newt.png");

    public CrimsonNewtRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTextureLocation(AbstractNetherNewtEntity entity) {
        return entity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }
}
