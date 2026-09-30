package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BestiaryElytraItem extends Item {
    private final Item component;

    public BestiaryElytraItem(Properties settings, Item component) {
        super(settings);
        this.component = component;
    }

    public Item getComponent() {
        return component;
    }

    public boolean isBroken(ItemStack stack) {
        return stack.nextDamageWillBreak();
    }

}
