package net.emilsg.clutterbestiary.neoforge.compat.curios;

import dev.architectury.platform.Platform;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.config.Configs;
import net.emilsg.clutterbestiary.config.ModConfigManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

/**
 * Lets gliders (the vanilla Elytra and every Butterfly Elytra) work from the Curios back slot. Only reference this
 * class after checking {@link ClutterBestiary#IS_CURIOS_LOADED}.
 */
public final class CuriosElytraUse {
    private static final String BACK_SLOT = "back";
    private static final String ELYTRA_SLOT_MOD_ID = "elytraslot";

    private CuriosElytraUse() {
    }

    public static ItemStack getVisibleEquippedElytra(LivingEntity livingEntity) {
        return findEquippedElytra(livingEntity)
                .filter(result -> result.slotContext().visible())
                .map(SlotResult::stack)
                .orElse(ItemStack.EMPTY);
    }

    /**
     * Whether a usable glider is worn in the back slot and back-slot flight is enabled.
     */
    public static boolean canGlide(LivingEntity livingEntity) {
        if (!isFlightEnabled()) return false;
        return findEquippedElytra(livingEntity).isPresent();
    }

    /**
     * Applies the periodic glide durability loss to the back-slot glider, mirroring vanilla's chest-slot handling.
     */
    public static void damageGlider(LivingEntity livingEntity) {
        if (!(livingEntity.level() instanceof ServerLevel serverWorld)) return;

        findEquippedElytra(livingEntity).ifPresent(result -> {
            SlotContext slotContext = result.slotContext();
            ItemStack damaged = result.stack().copy();
            damaged.hurtAndBreak(1, serverWorld, livingEntity instanceof ServerPlayer player ? player : null, item -> CuriosApi.broadcastCurioBreakEvent(slotContext));
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(inventory -> inventory.setEquippedCurio(slotContext.identifier(), slotContext.index(), damaged));
        });
    }

    private static Optional<SlotResult> findEquippedElytra(LivingEntity livingEntity) {
        if (!isCompatibilityAvailable()) return Optional.empty();

        return CuriosApi.getCuriosInventory(livingEntity)
                .flatMap(inventory -> inventory.findFirstCurio(CuriosElytraUse::isUsableGlider, BACK_SLOT));
    }

    private static boolean isUsableGlider(ItemStack stack) {
        return stack.has(DataComponents.GLIDER) && !stack.nextDamageWillBreak();
    }

    private static boolean isCompatibilityAvailable() {
        return ClutterBestiary.IS_CURIOS_LOADED && !Platform.isModLoaded(ELYTRA_SLOT_MOD_ID);
    }

    private static boolean isFlightEnabled() {
        return isCompatibilityAvailable() && ModConfigManager.get(Configs.doCuriosElytraFlight, true);
    }
}
