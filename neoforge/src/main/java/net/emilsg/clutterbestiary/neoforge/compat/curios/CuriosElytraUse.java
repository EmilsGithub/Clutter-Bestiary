package net.emilsg.clutterbestiary.neoforge.compat.curios;

import dev.architectury.platform.Platform;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.config.Configs;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.event.GameEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;

public final class CuriosElytraUse {
    private static final String BACK_SLOT = "back";
    private static final String ELYTRA_SLOT_MOD_ID = "elytraslot";

    private CuriosElytraUse() {
    }

    public static ItemStack getEquippedElytra(LivingEntity livingEntity) {
        return findEquippedElytra(livingEntity).map(SlotResult::stack).orElse(ItemStack.EMPTY);
    }

    public static ItemStack getVisibleEquippedElytra(LivingEntity livingEntity) {
        return findEquippedElytra(livingEntity)
                .filter(result -> result.slotContext().visible())
                .map(SlotResult::stack)
                .orElse(ItemStack.EMPTY);
    }

    public static ItemStack getFlightElytra(LivingEntity livingEntity) {
        if (!isFlightEnabled()) return ItemStack.EMPTY;
        return getEquippedElytra(livingEntity);
    }

    public static boolean tickFlight(LivingEntity livingEntity, ItemStack itemStack) {
        if (!itemStack.canElytraFly(livingEntity)) return false;

        int nextFlightTick = livingEntity.getFallFlyingTicks() + 1;
        if (livingEntity.getWorld() instanceof ServerWorld serverWorld && nextFlightTick % 10 == 0) {
            if (nextFlightTick % 20 == 0) {
                findEquippedElytra(livingEntity).ifPresent(result ->
                        itemStack.hurtAndBreak(1, serverWorld, livingEntity, item -> CuriosApi.broadcastCurioBreakEvent(result.slotContext()))
                );
            }

            livingEntity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
        }

        return true;
    }

    private static Optional<SlotResult> findEquippedElytra(LivingEntity livingEntity) {
        if (!isCompatibilityAvailable()) return Optional.empty();

        Optional<ICuriosItemHandler> optionalInventory = CuriosApi.getCuriosInventory(livingEntity);
        if (optionalInventory.isEmpty()) return Optional.empty();

        ICuriosItemHandler inventory = optionalInventory.get();
        Optional<ICurioStacksHandler> optionalStacks = inventory.getStacksHandler(BACK_SLOT);
        if (optionalStacks.isEmpty()) return Optional.empty();

        ICurioStacksHandler stacks = optionalStacks.get();
        for (int index = 0; index < stacks.getSlots(); index++) {
            Optional<SlotResult> optionalResult = inventory.findCurio(BACK_SLOT, index);
            if (optionalResult.isPresent() && optionalResult.get().stack().getItem() instanceof ElytraItem) {
                return optionalResult;
            }
        }

        return Optional.empty();
    }

    private static boolean isCompatibilityAvailable() {
        return ClutterBestiary.IS_CURIOS_LOADED && !Platform.isModLoaded(ELYTRA_SLOT_MOD_ID);
    }

    private static boolean isFlightEnabled() {
        return isCompatibilityAvailable() && ModConfigManager.get(Configs.doCuriosElytraFlight, true);
    }
}
