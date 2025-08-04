package net.emilsg.clutterbestiary.util;

import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.emilsg.clutterbestiary.entity.variants.koi.KoiBaseColorVariant;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.item.custom.ButterflyBottleItem;
import net.emilsg.clutterbestiary.item.custom.KoiBucketItem;
import net.emilsg.clutterbestiary.item.custom.SeahorseBucketItem;
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
            if (item instanceof KoiBucketItem) registerKoiBucket(item);
        }
    }

    private static void registerElytra(Item elytra) {
        ModelPredicateProviderRegistry.register(elytra, Identifier.of("minecraft","broken"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof BestiaryElytraItem bestiaryElytraItem)) return 0;

            return bestiaryElytraItem.isBroken(stack) ? 1 : 0;
        });
    }

    private static void registerButterflyInABottle(Item bottle) {
        ModelPredicateProviderRegistry.register(bottle, Identifier.of("minecraft","type"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof ButterflyBottleItem)) return 0;

            NbtCompound nbtCompound = stack.getNbt();

            if (nbtCompound == null || !nbtCompound.contains("Variant")) return 0;

            int id = nbtCompound.getInt("Variant");

            return (float) id / 100;
        });
    }

    private static void registerSeahorseBucket(Item bucket) {
        ModelPredicateProviderRegistry.register(bucket, Identifier.of("minecraft","type"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof SeahorseBucketItem)) return 0;

            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound == null || !nbtCompound.contains("Variant")) return 0;

            float type;

            SeahorseVariant seahorseVariant = SeahorseVariant.fromId(nbtCompound.getString("Variant"));

            type = switch (seahorseVariant) {
                case LIGHT_BLUE -> 0.1f;
                case RED -> 0.2f;
                case PURPLE -> 0.3f;
                default -> 0.0f;
            };

            return type;
        });
    }

    private static void registerKoiBucket(Item bucket) {
        ModelPredicateProviderRegistry.register(bucket, Identifier.of("minecraft","type"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof KoiBucketItem)) return 0;

            NbtCompound nbtCompound = stack.getNbt();

            if (nbtCompound == null || !nbtCompound.contains("BaseColor")) return 0;
            float type;
            KoiBaseColorVariant id = KoiBaseColorVariant.fromId(nbtCompound.getString("BaseColor"));

            type = switch (id) {
                case ORANGE -> 0.1f;
                case YELLOW -> 0.2f;
                case BLACK -> 0.3f;
                case PEARL -> 0.4f;
                default -> 0.0f;
            };

            return type;
        });
    }
}
