package net.emilsg.clutterbestiary.menu.handler;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import io.netty.buffer.ByteBuf;

import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.emilsg.clutterbestiary.menu.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CoatiScreenHandler extends AbstractContainerMenu {
    /** Extra menu data sent to the client: the network id of the Coati whose inventory is open. */
    public static final StreamCodec<ByteBuf, Integer> ENTITY_ID_CODEC = ByteBufCodecs.INT;
    private final Container inventory;
    private final CoatiEntity coati;


    public CoatiScreenHandler(int syncId, Inventory playerInventory, CoatiEntity coati) {
        super(ModMenuTypes.COATI.get(), syncId);
        this.coati = coati;
        this.inventory = coati.getCoatiInventory();
        checkContainerSize(inventory, 12);
        inventory.startOpen(playerInventory.player);

        int leftX = 35, topY = 26, s = 18;

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                int index = col + row * 6;
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
    public boolean stillValid(Player player) {
        return coati != null && coati.isAlive() && coati.distanceToSqr(player) < 64 && this.inventory.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack2, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setByPlayer(ItemStack.EMPTY);
            } else {
                slot2.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTake(player, itemStack2);
        }

        return itemStack;
    }
}
