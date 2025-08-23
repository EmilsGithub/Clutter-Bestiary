package net.emilsg.clutterbestiary.util;

import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.item.custom.ButterflyBottleItem;
import net.emilsg.clutterbestiary.item.custom.KoiBucketItem;
import net.emilsg.clutterbestiary.item.custom.SeahorseBucketItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

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
        ModelPredicateProviderRegistry.register(bottle, Identifier.of("type"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof ButterflyBottleItem)) return 0;
            NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            Optional<ButterflyVariant> optional = nbtComponent.get(ButterflyBottleItem.BUTTERFLY_VARIANT_MAP_CODEC).result();

            if (optional.isEmpty()) return 0;

            int id = optional.get().getOrderedID();

            return (float) id / 100;
        });
    }

    private static void registerSeahorseBucket(Item bucket) {
        ModelPredicateProviderRegistry.register(bucket, Identifier.of("type"), (stack, world, entity, seed) -> {
            if (!(stack.getItem() instanceof SeahorseBucketItem)) return 0;
            NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            Optional<SeahorseVariant> optional = nbtComponent.get(SeahorseBucketItem.SEAHORSE_VARIANT_MAP_CODEC).result();

            if (optional.isEmpty()) return 0;

            float type;

            SeahorseVariant seahorseVariant = optional.get();

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
        ModelPredicateProviderRegistry.register(
                bucket,
                Identifier.of("type"),
                (stack, world, entity, seed) -> {
                    if (!(stack.getItem() instanceof KoiBucketItem)) return 0f;

                    var cmp = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
                    var base = cmp.get(KoiBucketItem.BASE_COLOR_CODEC).result();
                    if (base.isEmpty()) return 0f;

                    return switch (base.get()) {
                        case ORANGE -> 0.1f;
                        case YELLOW -> 0.2f;
                        case BLACK -> 0.3f;
                        case PEARL -> 0.4f;
                        default -> 0.0f;
                    };
                }
        );
    }
}
