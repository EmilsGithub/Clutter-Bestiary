package net.emilsg.clutter_bestiary.entity.client.render;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.emilsg.clutter_bestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class CrimsonNewtRenderer extends AbstractNetherNewtRenderer {
    private static final Identifier TEXTURE = new Identifier(ClutterBestiary.MOD_ID, "textures/entity/crimson_newt_box.png");

    public CrimsonNewtRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(AbstractNetherNewtEntity entity) {
        return TEXTURE;
    }
}
