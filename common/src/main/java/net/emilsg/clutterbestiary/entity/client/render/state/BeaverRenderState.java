package net.emilsg.clutterbestiary.entity.client.render.state;

import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class BeaverRenderState extends BestiaryRenderState<BeaverEntity> {
    public final ItemStackRenderState heldItem = new ItemStackRenderState();
}
