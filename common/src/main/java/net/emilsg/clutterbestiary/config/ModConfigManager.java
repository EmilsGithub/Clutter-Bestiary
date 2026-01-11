/*
 * Copyright (c) 2024 EmilSG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Edited 2026 for startup-loaded configs.
 */

package net.emilsg.clutterbestiary.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModConfigManager {
    // Logger for recording configuration-related activities and errors.
    private static final Logger LOGGER = LogManager.getLogger("Clutter: Bestiary Config");
    // Gson instance for reading and writing JSON files with pretty printing enabled.
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Configuration file and folder names.
    private static final String CONFIG_FILE_NAME = "clutterbestiary_config.json";
    private static final String CONFIG_FILE_FOLDER = "clutter";

    private static final Map<String, SpawnConfig> SPAWN_CONFIGS = new HashMap<>();

    // Stores registered configuration entries by their key.
    private static final Map<String, ModConfigEntry<?>> CONFIG_ENTRIES = new HashMap<>();
    // Maps class types to their corresponding JSON parsing functions.
    private static final Map<Class<?>, Function<JsonElement, ?>> PARSERS = new HashMap<>();

    static {
        // Initialize JSON parsers for various data types.
        PARSERS.put(Boolean.class, JsonElement::getAsBoolean);
        PARSERS.put(Integer.class, JsonElement::getAsInt);
        PARSERS.put(Float.class, JsonElement::getAsFloat);
        PARSERS.put(Double.class, JsonElement::getAsDouble);
        PARSERS.put(String.class, JsonElement::getAsString);
    }

    /**
     * Registers a new configuration entry with a key, default value, and an optional comment.
     *
     * @param key          The unique key for this configuration entry.
     * @param defaultValue The default value for this configuration entry.
     * @param comment      An optional comment describing the configuration entry.
     * @param <T>          The type of the configuration value.
     * @return The created ModConfigEntry object.
     */
    public static <T> ModConfigEntry<T> register(String key, T defaultValue, String comment) {
        ModConfigEntry<T> entry = new ModConfigEntry<>(key, defaultValue, comment);
        CONFIG_ENTRIES.put(key, entry);
        return entry;
    }

    /**
     * Retrieves the value associated with the specified key, or returns the provided default value if the key doesn't exist.
     *
     * @param key          The key of the configuration entry.
     * @param defaultValue The default value to return if the key is not found.
     * @param <T>          The type of the configuration value.
     * @return The configuration value or the default value if the key doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, T defaultValue) {
        ModConfigEntry<?> entry = CONFIG_ENTRIES.get(key);
        if (entry != null && entry.getValue() != null) {
            return (T) entry.getValue();
        }
        return defaultValue;
    }

    /**
     * Sets the value of a registered configuration entry and immediately saves the updated configuration.
     *
     * @param key    The key of the configuration entry to update.
     * @param value  The new value to assign to the entry.
     */
    public static void set(String key, Object value) {
        ModConfigEntry<?> entry = CONFIG_ENTRIES.get(key);
        if (entry == null) {
            LOGGER.error("Tried to set unknown config key: {}", key);
            return;
        }

        try {
            setEntryValueUnchecked(entry, value);
            saveConfig();
        } catch (Exception e) {
            LOGGER.error("Failed to set config key {}", key, e);
        }
    }

    /**
     * Loads the configuration from the global JSON file, updating registered entries
     * with the loaded values. If the configuration file is missing, empty, or corrupted, a new
     * one is created with default values in the config folder.
     */
    public static void loadConfig() {
        Configs.initConfigs();

        Path configFile = getConfigFile();
        boolean needsSave = false;

        if (Files.exists(configFile)) {
            try (BufferedReader reader = Files.newBufferedReader(configFile)) {
                JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);

                // If the file exists but cannot be parsed into a valid JSON object,
                // reset all values to defaults and recreate the file.
                if (jsonObject == null) {
                    LOGGER.error("Config file was empty or invalid, recreating defaults.");
                    resetConfigsInternal();
                    saveConfig();
                    return;
                }

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    ModConfigEntry<?> configEntry = CONFIG_ENTRIES.get(entry.getKey());
                    if (configEntry != null) {
                        try {
                            JsonObject entryObject = entry.getValue().getAsJsonObject();
                            JsonElement valueElement = entryObject.get("value");

                            // Each entry is expected to contain a "value" field.
                            if (valueElement == null) {
                                throw new IllegalStateException("Missing value element");
                            }

                            setConfigEntryValue(configEntry, valueElement, entry.getKey());
                        } catch (Exception e) {
                            LOGGER.error("Invalid value for key: {}. Using default value.", entry.getKey());
                            configEntry.resetToDefault();
                            needsSave = true;
                        }
                    } else {
                        LOGGER.warn("Unknown config entry in file: {}", entry.getKey());
                        needsSave = true;
                    }
                }

                // Ensure all registered entries are present in the config file.
                for (Map.Entry<String, ModConfigEntry<?>> registeredEntry : CONFIG_ENTRIES.entrySet()) {
                    if (!jsonObject.has(registeredEntry.getKey())) {
                        LOGGER.warn("Adding missing config entry: {}", registeredEntry.getKey());
                        needsSave = true;
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Failed to load config file: {}", configFile, e);
                needsSave = true;
            }
        } else {
            needsSave = true;
        }

        // Save the configuration file if changes were made or it didn't exist.
        if (needsSave) {
            saveConfig();
        }
        rebuildSpawnSnapshots();
    }

    /**
     * Saves the current configuration entries to the current world's JSON file.
     * The file is stored in the game's config/clutter folder.
     * If the directory does not exist, it is created.
     *
     */
    public static void saveConfig() {
        Path configDir = getConfigDir();
        Path configFile = getConfigFile();

        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory: {}", configDir, e);
            return;
        }

        JsonObject jsonObject = new JsonObject();

        // Serialize each configuration entry to JSON format.
        CONFIG_ENTRIES.forEach((key, entry) -> {
            JsonObject entryObject = new JsonObject();
            entryObject.addProperty("comment", entry.getComment());
            entryObject.add("value", GSON.toJsonTree(entry.getValue()));
            jsonObject.add(key, entryObject);
        });

        // Write the serialized JSON to the configuration file.
        try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
            GSON.toJson(jsonObject, writer);
        } catch (IOException e) {
            LOGGER.error("Error when saving config file: {}", configFile, e);
        }
    }

    /**
     * Resets all configuration entries to their default values and saves the updated configuration.
     */
    public static void resetConfigs() {
        resetConfigsInternal();
        saveConfig();
    }

    /**
     * Resets all registered configuration entries to their default values without saving.
     * This is used internally before a full save operation.
     */
    private static void resetConfigsInternal() {
        CONFIG_ENTRIES.forEach((key, entry) -> entry.resetToDefault());
    }

    /**
     * Resolves the configuration directory.
     */
    private static Path getConfigDir() {
        return Platform.getConfigFolder().resolve(CONFIG_FILE_FOLDER);
    }

    /**
     * Registers four config entries for a creature's spawn behaviour and records
     * the creature name so snapshots can be built at load time.
     * Keys registered: <name>_spawn_enabled, <name>_spawn_weight,
     * <name>_min_group_size, <name>_max_group_size.
     *
     * @param name          The creature name used as the key prefix (e.g. "butterfly").
     * @param defaultEnable Whether spawning is enabled by default.
     * @param defaultWeight The default spawn weight.
     * @param defaultMin    The default minimum group size.
     * @param defaultMax    The default maximum group size.
     */
    public static void registerSpawnConfig(String name, boolean defaultEnable,
                                           int defaultWeight, int defaultMin, int defaultMax) {
        register(name + "_spawn_enabled", defaultEnable, "Enable " + name + " spawning?");
        register(name + "_spawn_weight", defaultWeight, "Spawn weight for " + name + ".");
        register(name + "_min_group_size", defaultMin, "Minimum group size for " + name + ".");
        register(name + "_max_group_size", defaultMax, "Maximum group size for " + name + ".");
        SPAWN_CONFIGS.put(name, new SpawnConfig(defaultEnable, defaultWeight, defaultMin, defaultMax));
    }

    /**
     * Returns the current spawn config snapshot for the given creature name.
     * The snapshot is updated every time loadConfig() is called.
     *
     * @param name The creature name (e.g. "butterfly").
     * @return The SpawnConfig snapshot, or null if the name was never registered.
     */
    public static SpawnConfig getSpawnConfig(String name) {
        return SPAWN_CONFIGS.get(name);
    }

    /**
     * Rebuilds all SpawnConfig snapshots from the currently loaded ModConfigEntry values.
     * Called internally at the end of loadConfig().
     */
    private static void rebuildSpawnSnapshots() {
        for (String name : SPAWN_CONFIGS.keySet()) {
            boolean enabled = get(name + "_spawn_enabled", true);
            int weight = get(name + "_spawn_weight", 10);
            int min = get(name + "_min_group_size", 1);
            int max = get(name + "_max_group_size", 4);
            SPAWN_CONFIGS.put(name, new SpawnConfig(enabled, weight, min, max));
        }
    }

    /**
     * Resolves the full path to the configuration file.
     */
    private static Path getConfigFile() {
        return getConfigDir().resolve(CONFIG_FILE_NAME);
    }

    /**
     * Sets the value of a specific configuration entry using a JSON element.
     * If the type of the value is unsupported, an error is logged.
     *
     * @param configEntry  The configuration entry to update.
     * @param valueElement The JSON element containing the new value.
     * @param key          The key associated with the configuration entry.
     * @param <T>          The type of the configuration value.
     */
    @SuppressWarnings("unchecked")
    private static <T> void setConfigEntryValue(ModConfigEntry<T> configEntry, JsonElement valueElement, String key) {
        Function<JsonElement, ?> parser = PARSERS.get(configEntry.getDefaultValue().getClass());
        if (parser != null) {
            configEntry.setValue((T) parser.apply(valueElement));
        } else {
            LOGGER.error("Unsupported type for key: {}. Skipping entry.", key);
        }
    }

    /**
     * Sets a configuration entry value without generic type checking.
     * Used internally by the public setter when the value type is already known by the caller.
     *
     * @param entry The configuration entry to update.
     * @param value The value to assign.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setEntryValueUnchecked(ModConfigEntry entry, Object value) {
        entry.setValue(value);
    }
}
