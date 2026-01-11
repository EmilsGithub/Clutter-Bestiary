package net.emilsg.clutterbestiary.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.neoforge.compat.curios.CuriosElytraUse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @ModifyExpressionValue(
            method = "tickMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack clutterbestiary$getFlightElytra(ItemStack chestStack) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (chestStack.canElytraFly(player) || !ClutterBestiary.IS_CURIOS_LOADED) return chestStack;

        ItemStack curiosElytra = CuriosElytraUse.getFlightElytra(player);
        return curiosElytra.isEmpty() ? chestStack : curiosElytra;
    }
}
