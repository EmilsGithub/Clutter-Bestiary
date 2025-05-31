package net.emilsg.clutter_bestiary.item.custom;

import net.emilsg.clutter_bestiary.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ButterflyElytraItem extends BestiaryElytraItem {
    String color;

    public ButterflyElytraItem(Settings settings, Item component, String color) {
        super(settings, component);
        this.color = color;
    }

    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(ModItems.BUTTERFLY_IN_A_BOTTLE);
    }

    public String getType() {
        return color + "_butterfly_elytra";
    }
}
