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

    // Non-spawn configs remain as before.
    public static final String doTrinketsElytraFlight = ModConfigManager.register(
            "do_trinkets_elytra_flight", true,
            "Will the Elytra and its variants work while worn in the cape slot provided by Trinkets?"
    ).getKey();

    public static final String doCuriosElytraFlight = ModConfigManager.register(
            "do_curios_elytra_flight", true,
            "Will the Elytra and its variants work while worn in the back slot provided by Curios?"
    ).getKey();

    static {
        ModConfigManager.registerSpawnConfig("butterfly", true, 20, 3, 6);
        ModConfigManager.registerSpawnConfig("chameleon", true, 15, 1, 2);
        ModConfigManager.registerSpawnConfig("echofin", true, 30, 1, 3);
        ModConfigManager.registerSpawnConfig("mossbloom", true, 30, 1, 2);
        ModConfigManager.registerSpawnConfig("kiwi", true, 30, 2, 3);
        ModConfigManager.registerSpawnConfig("emperor_penguin", true, 10, 2, 4);
        ModConfigManager.registerSpawnConfig("beaver", true, 10, 2, 3);
        ModConfigManager.registerSpawnConfig("capybara", true, 10, 3, 5);
        ModConfigManager.registerSpawnConfig("crimson_newt", true, 60, 2, 3);
        ModConfigManager.registerSpawnConfig("warped_newt", true, 60, 2, 3);
        ModConfigManager.registerSpawnConfig("ember_tortoise", true, 60, 1, 2);
        ModConfigManager.registerSpawnConfig("jellyfish", true, 6, 5, 9);
        ModConfigManager.registerSpawnConfig("seahorse", true, 20, 4, 7);
        ModConfigManager.registerSpawnConfig("manta_ray", true, 20, 1, 3);
        ModConfigManager.registerSpawnConfig("koi", true, 20, 4, 7);
        ModConfigManager.registerSpawnConfig("dragonfly", true, 20, 2, 4);
        ModConfigManager.registerSpawnConfig("booplet", true, 20, 4, 7);
        ModConfigManager.registerSpawnConfig("potion_wasp", true, 20, 1, 2);
        ModConfigManager.registerSpawnConfig("river_turtle", true, 20, 2, 3);
        ModConfigManager.registerSpawnConfig("coati", true, 20, 2, 4);
        ModConfigManager.registerSpawnConfig("red_panda", true, 20, 2, 3);
        ModConfigManager.registerSpawnConfig("stoat", true, 25, 2, 3);
        ModConfigManager.registerSpawnConfig("crocodile", true, 10, 1, 2);
        ModConfigManager.registerSpawnConfig("chorus_beetle", false, 10, 1, 2);
        ModConfigManager.registerSpawnConfig("woodpecker", false, 10, 1, 2);
        ModConfigManager.registerSpawnConfig("arrowfish", true, 1, 1, 2);
    }

    public static void initConfigs() {
    }
}
