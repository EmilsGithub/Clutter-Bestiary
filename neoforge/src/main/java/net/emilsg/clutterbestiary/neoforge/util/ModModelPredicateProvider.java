package net.emilsg.clutterbestiary.neoforge.util;

import net.emilsg.clutterbestiary.entity.client.ModModelPredicates;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public final class ModModelPredicateProvider {

    public static void register() {
        ModItems.BUTTERFLY_ELYTRAS.forEach(elytra -> ModelPredicateProviderRegistry.register(elytra.get(), Identifier.of("minecraft", "broken"), (stack, world, entity, seed) -> ModModelPredicates.getElytraBroken(stack)));
        ModelPredicateProviderRegistry.register(ModItems.BUTTERFLY_IN_A_BOTTLE.get(), Identifier.of("type"), (stack, world, entity, seed) -> ModModelPredicates.getButterflyType(stack));
        ModelPredicateProviderRegistry.register(ModItems.SEAHORSE_BUCKET.get(), Identifier.of("type"), (stack, world, entity, seed) -> ModModelPredicates.getSeahorseType(stack));
        ModelPredicateProviderRegistry.register(ModItems.KOI_BUCKET.get(), Identifier.of("type"), (stack, world, entity, seed) -> ModModelPredicates.getKoiType(stack));
        ModelPredicateProviderRegistry.register(ModItems.RIVER_TURTLE_BUCKET.get(), Identifier.of("type"), (stack, world, entity, seed) -> ModModelPredicates.getRiverTurtleType(stack));
        ModelPredicateProviderRegistry.register(ModItems.JELLYFISH_BUCKET.get(), Identifier.of("type"), (stack, world, entity, seed) -> ModModelPredicates.getJellyfishType(stack));
    }
}
