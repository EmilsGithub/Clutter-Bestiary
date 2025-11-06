package net.emilsg.clutterbestiary.screen_handler.handler;

import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.emilsg.clutterbestiary.screen_handler.ModMenuTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class CoatiScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final CoatiEntity coati;


    public CoatiScreenHandler(int syncId, PlayerInventory playerInventory, CoatiEntity coati) {
        super(ModMenuTypes.COATI.get(), syncId);
        this.coati = coati;
        this.inventory = coati.getCoatiInventory();
        checkSize(inventory, 8);
        inventory.onOpen(playerInventory.player);

        int leftX = 53, topY = 26, s = 18;

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                int index = col + row * 4;
                this.addSlot(new Slot(inventory, index, leftX + col * s, topY + row * s));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return coati != null && coati.isAlive() && coati.squaredDistanceTo(player) < 64 && this.inventory.canPlayerUse(player);
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.inventory.size()) {
                if (!this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}
