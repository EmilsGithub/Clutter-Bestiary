package net.emilsg.clutter_bestiary.util;

import net.emilsg.clutter_bestiary.item.custom.ButterflyBottleItem;
import net.emilsg.clutter_bestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutter_bestiary.item.custom.SeahorseBucketItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {

    public static void register() {
        for (Item item : Registries.ITEM) {
            if (item instanceof BestiaryElytraItem) registerElytra(item);
            if (item instanceof ButterflyBottleItem) registerButterflyInABottle(item);
            if (item instanceof SeahorseBucketItem) registerSeahorseBucket(item);
        }
    }

    private static void registerElytra(Item elytra) {
        ModelPredicateProviderRegistry.register(elytra, new Identifier("broken"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof BestiaryElytraItem bestiaryElytraItem)) return 0;

            return bestiaryElytraItem.isBroken(stack) ? 1 : 0;
        });
    }

    private static void registerButterflyInABottle(Item bottle) {
        ModelPredicateProviderRegistry.register(bottle, new Identifier("type"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof ButterflyBottleItem)) return 0;

            NbtCompound nbtCompound = stack.getNbt();

            if (nbtCompound == null || !nbtCompound.contains("Variant")) return 0;

            int id = nbtCompound.getInt("Variant");

            return (float) id / 100;
        });
    }

    private static void registerSeahorseBucket(Item bucket) {
        ModelPredicateProviderRegistry.register(bucket, new Identifier("type"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof SeahorseBucketItem)) return 0;

            NbtCompound nbtCompound = stack.getNbt();

            if (nbtCompound == null || !nbtCompound.contains("Variant")) return 0;

            int id = nbtCompound.getInt("Variant");

            return (float) id / 10;
        });
    }
}
