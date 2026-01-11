package net.emilsg.clutterbestiary.config;

/**
 * A snapshot of spawn-related configuration values for a single creature type.
 * Values are copied from their underlying ModConfigEntry instances at load time.
 * Use ModConfigManager.getSpawnConfig(name) to retrieve an instance.
 */
public class SpawnConfig {
    private final boolean enabled;
    private final int spawnWeight;
    private final int minGroupSize;
    private final int maxGroupSize;

    public SpawnConfig(boolean enabled, int spawnWeight, int minGroupSize, int maxGroupSize) {
        this.enabled = enabled;
        this.spawnWeight = spawnWeight;
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
    }

    /**
     * The maximum number of creatures per spawn group.
     */
    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    /**
     * The minimum number of creatures per spawn group.
     */
    public int getMinGroupSize() {
        return minGroupSize;
    }

    /**
     * The spawn weight used in biome spawn lists.
     */
    public int getSpawnWeight() {
        return spawnWeight;
    }

    /**
     * Whether this creature is allowed to spawn.
     */
    public boolean isEnabled() {
        return enabled;
    }
}
