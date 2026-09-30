package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Inject(method = {"getSupportedHeldProjectiles", "getAllSupportedProjectiles"}, at = @At("RETURN"), cancellable = true)
    private void clutterbestiary$allowArrowfish(CallbackInfoReturnable<Predicate<ItemStack>> callbackInfo) {
        callbackInfo.setReturnValue(callbackInfo.getReturnValue().or(stack -> stack.is(ModItems.ARROWFISH.get())));
    }
}
