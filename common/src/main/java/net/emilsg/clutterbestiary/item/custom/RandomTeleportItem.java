package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class RandomTeleportItem extends Item {
    private final int cooldownInTicks;
    private final int useTimeInTicks;
    private final int maxTeleportDistance;

    public RandomTeleportItem(Properties settings, int cooldownInTicks, int useTimeInTicks, int maxTeleportDistance) {
        super(settings);
        this.useTimeInTicks = useTimeInTicks;
        this.cooldownInTicks = cooldownInTicks;
        this.maxTeleportDistance = maxTeleportDistance;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        // Keep a copy for the cooldown group; the used stack may be empty after consumption.
        ItemStack usedStack = stack.copy();
        ItemStack itemStack = super.finishUsingItem(stack, world, user);
        if (!world.isClientSide()) {
            double d = user.getX();
            double e = user.getY();
            double f = user.getZ();

            for (int i = 0; i < 16; ++i) {
                double g = user.getX() + (user.getRandom().nextDouble() - 0.5) * maxTeleportDistance;
                double h = Mth.clamp(user.getY() + (double) (user.getRandom().nextInt(16) - 8), world.getMinY(), world.getMinY() + ((ServerLevel) world).getLogicalHeight() - 1);
                double j = user.getZ() + (user.getRandom().nextDouble() - 0.5) * maxTeleportDistance;
                if (user.isPassenger()) {
                    user.stopRiding();
                }

                Vec3 vec3d = user.position();
                if (user.randomTeleport(g, h, j, true, state -> false)) {
                    world.gameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Context.of(user));
                    SoundEvent soundEvent = user instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    world.playSound(null, d, e, f, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    user.playSound(soundEvent, 1.0F, 1.0F);
                    break;
                }
            }
            if (user instanceof Player player) {
                player.getCooldowns().addCooldown(usedStack, cooldownInTicks);
            }
        }

        return itemStack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return useTimeInTicks;
    }
}
