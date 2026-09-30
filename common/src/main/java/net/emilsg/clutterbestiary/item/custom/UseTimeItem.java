package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UseTimeItem extends Item {
    private final int useTimeInTicks;
    private final int cooldownInTicks;

    public UseTimeItem(Properties settings, int useTimeInTicks, int cooldownInTicks) {
        super(settings);
        this.useTimeInTicks = useTimeInTicks;
        this.cooldownInTicks = cooldownInTicks;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (user instanceof Player player) {
            player.getCooldowns().addCooldown(stack, cooldownInTicks);
        }
        return super.finishUsingItem(stack, world, user);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return useTimeInTicks;
    }
}
