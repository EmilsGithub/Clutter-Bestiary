package net.emilsg.clutterbestiary.fabric.mixin;

import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Fabric API has no registration hook for client item select properties, so the vanilla id mapper is exposed directly.
 */
@Mixin(SelectItemModelProperties.class)
public interface SelectItemModelPropertiesAccessor {

    @Accessor("ID_MAPPER")
    static ExtraCodecs.LateBoundIdMapper<Identifier, SelectItemModelProperty.Type<?, ?>> clutterbestiary$getIdMapper() {
        throw new AssertionError();
    }
}
