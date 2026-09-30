package net.emilsg.clutterbestiary.entity.client.render.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Render state shared by the Bestiary mobs.
 * <p>
 * The models drive a large number of authored animation states that live on the entity itself, so the state keeps a
 * reference to the entity it was extracted from. Extraction and submission both happen on the render thread within
 * the same frame, so the reference is only read while the entity is current. Values that renderers or layers compute
 * (colours, visibility toggles, partial ticks) are stored as plain fields instead of being written back to the entity.
 */
public class BestiaryRenderState<E extends LivingEntity> extends LivingEntityRenderState {
    @Nullable
    public E entity;
    public float partialTick;
}
