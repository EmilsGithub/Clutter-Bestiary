package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.world.item.Item;

public class ButterflyElytraItem extends BestiaryElytraItem {
    String color;

    public ButterflyElytraItem(Properties settings, Item component, String color) {
        super(settings, component);
        this.color = color;
    }

    public String getType() {
        return color + "_butterfly_elytra";
    }
}
