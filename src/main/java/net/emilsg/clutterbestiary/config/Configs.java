
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
 */

package net.emilsg.clutterbestiary.config;

/**
 * A utility class for initializing and storing configuration keys.
 * Contains static configuration entries that are registered with the ModConfigManager.
 */
public class Configs {

    public static final String spawnBestiaryMobs = ModConfigManager.register("spawn_bestiary_mobs", true, "Spawn Clutter Mobs.").getKey();
    public static final String spawnButterflies = ModConfigManager.register("spawn_butterflies", true, "Spawn Butterflies.").getKey();
    public static final String spawnChameleons = ModConfigManager.register("spawn_chameleons", true, "Spawn Chameleons.").getKey();
    public static final String spawnEchofins = ModConfigManager.register("spawn_echofins", true, "Spawn Echofins.").getKey();
    public static final String spawnMossblooms = ModConfigManager.register("spawn_mossblooms", true, "Spawn Mossblooms.").getKey();
    public static final String spawnKiwis = ModConfigManager.register("spawn_kiwis", true, "Spawn Kiwis.").getKey();
    public static final String spawnEmperorPenguins = ModConfigManager.register("spawn_emperor_penguins", true, "Spawn Emperor Penguins.").getKey();
    public static final String spawnBeavers = ModConfigManager.register("spawn_beavers", true, "Spawn Beavers.").getKey();
    public static final String spawnCapybaras = ModConfigManager.register("spawn_capybaras", true, "Spawn Capybaras.").getKey();
    public static final String spawnCrimsonNewts = ModConfigManager.register("spawn_crimson_newts", true, "Spawn Crimson Newts.").getKey();
    public static final String spawnWarpedNewts = ModConfigManager.register("spawn_warped_newts", true, "Spawn Warped Newts.").getKey();
    public static final String spawnEmberTortoises = ModConfigManager.register("spawn_ember_tortoises", true, "Spawn Ember Tortoises.").getKey();
    public static final String spawnJellyfishes = ModConfigManager.register("spawn_jellyfishes", true, "Spawn Jellyfishes.").getKey();
    public static final String spawnSeahorses = ModConfigManager.register("spawn_seahorses", true, "Spawn Seahorses.").getKey();
    public static final String spawnMantaRays = ModConfigManager.register("spawn_manta_rays", true, "Spawn Manta Rays.").getKey();
    public static final String spawnKoi = ModConfigManager.register("spawn_koi", true, "Spawn Koi.").getKey();
    public static final String doTrinketsElytraFlight = ModConfigManager.register("do_trinkets_elytra_flight", true, "Will the Elytra and it´s variants work while worn in the cape slot provided by Trinkets?").getKey();

    public static void initConfigs() {
    }

}
