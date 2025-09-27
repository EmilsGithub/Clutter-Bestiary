package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;

public class BestiaryElytraItem extends ElytraItem implements Equipment {
    private final Item component;

    public BestiaryElytraItem(Settings settings, Item component) {
        super(settings);
        this.component = component;
    }

    public Item getComponent() {
        return component;
    }

    public boolean isBroken(ItemStack stack) {
        return !isUsable(stack);
    }

}
