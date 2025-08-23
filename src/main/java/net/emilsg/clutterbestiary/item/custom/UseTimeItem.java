package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class UseTimeItem extends Item {
    private final int useTimeInTicks;
    private final int cooldownInTicks;

    public UseTimeItem(Settings settings, int useTimeInTicks, int cooldownInTicks) {
        super(settings);
        this.useTimeInTicks = useTimeInTicks;
        this.cooldownInTicks = cooldownInTicks;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity) {
            ((PlayerEntity) user).getItemCooldownManager().set(this, cooldownInTicks);
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return useTimeInTicks;
    }
}
