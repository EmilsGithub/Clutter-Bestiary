package net.emilsg.clutterbestiary.util;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChorusBeetleSpawner {
    private static final float SPAWN_CHANCE = 0.05F;

    public static void trySpawn(World world, BlockPos pos) {
        if (!(world instanceof ServerWorld serverWorld) || world.getRandom().nextFloat() >= SPAWN_CHANCE) return;

        ModEntityTypes.CHORUS_BEETLE.get().spawn(serverWorld, pos, SpawnReason.TRIGGERED);
    }
}
