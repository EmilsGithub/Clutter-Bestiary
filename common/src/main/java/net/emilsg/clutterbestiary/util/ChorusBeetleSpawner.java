package net.emilsg.clutterbestiary.util;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;

public class ChorusBeetleSpawner {
    private static final float SPAWN_CHANCE = 0.05F;

    public static void trySpawn(Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel serverWorld) || world.getRandom().nextFloat() >= SPAWN_CHANCE) return;

        ModEntityTypes.CHORUS_BEETLE.get().spawn(serverWorld, pos, EntitySpawnReason.TRIGGERED);
    }
}
