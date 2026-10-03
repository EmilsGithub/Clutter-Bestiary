package net.emilsg.clutterbestiary.entity.client.render.parent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.emilsg.clutterbestiary.entity.client.model.BabyNetherNewtModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.emilsg.clutterbestiary.entity.client.layer.ModModelLayers;
import net.emilsg.clutterbestiary.entity.client.model.NetherNewtModel;
import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * Newt size is stored in the SCALE attribute, which the living entity renderer already applies to the model and
 * shadow, so this renderer doesn't scale by it again (that squared the size and let babies outgrow small adults).
 */
public abstract class AbstractNetherNewtRenderer extends BestiaryMobRenderer<AbstractNetherNewtEntity, ParentTameableModel<AbstractNetherNewtEntity>> {

    private final NetherNewtModel<AbstractNetherNewtEntity> adultModel;
    private final BabyNetherNewtModel<AbstractNetherNewtEntity> babyModel;

    public AbstractNetherNewtRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new NetherNewtModel<>(ctx.bakeLayer(ModModelLayers.NETHER_NEWT)), 0.35f);

        this.adultModel = new NetherNewtModel<>(ctx.bakeLayer(ModModelLayers.NETHER_NEWT));
        this.babyModel = new BabyNetherNewtModel<>(ctx.bakeLayer(ModModelLayers.BABY_NETHER_NEWT));
    }

    @Override
    public abstract Identifier getTextureLocation(AbstractNetherNewtEntity entity);

    @Override
    public void submit(BestiaryRenderState<AbstractNetherNewtEntity> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? babyModel : adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
