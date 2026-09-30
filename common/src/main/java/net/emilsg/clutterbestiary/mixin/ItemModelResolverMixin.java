package net.emilsg.clutterbestiary.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Shows the Arrowfish-loaded crossbow through its own client item without overriding the vanilla crossbow
 * definition, so resource packs that change the crossbow keep working.
 */
@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {
    @Unique
    private static final Identifier clutterbestiary$ARROWFISH_CROSSBOW = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "crossbow_arrowfish");

    @ModifyExpressionValue(
            method = "appendItemLayers",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;")
    )
    private Object clutterbestiary$useArrowfishCrossbowModel(Object original, ItemStackRenderState output, ItemStack item) {
        if (!item.is(Items.CROSSBOW)) return original;
        ChargedProjectiles projectiles = item.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        return projectiles.contains(ModItems.ARROWFISH.get()) ? clutterbestiary$ARROWFISH_CROSSBOW : original;
    }
}
