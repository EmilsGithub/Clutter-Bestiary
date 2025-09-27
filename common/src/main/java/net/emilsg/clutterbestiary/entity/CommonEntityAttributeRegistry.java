package net.emilsg.clutterbestiary.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.emilsg.clutterbestiary.entity.custom.*;

public class CommonEntityAttributeRegistry {

    public static void register() {
        EntityAttributeRegistry.register(ModEntityTypes.BUTTERFLY, ButterflyEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.CHAMELEON, ChameleonEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.ECHOFIN, EchofinEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.MOSSBLOOM, MossbloomEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.KIWI_BIRD, KiwiBirdEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.EMPEROR_PENGUIN, EmperorPenguinEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.BEAVER, BeaverEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.CAPYBARA, CapybaraEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.CRIMSON_NEWT, CrimsonNewtEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.WARPED_NEWT, WarpedNewtEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.EMBER_TORTOISE, EmberTortoiseEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.JELLYFISH, JellyfishEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.MANTA_RAY, MantaRayEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.SEAHORSE, SeahorseEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.POTION_WASP, PotionWaspEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.POTION_SAC, PotionSacEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.DRAGONFLY, DragonflyEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.BOOPLET, BoopletEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.KOI, KoiEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.KOI_EGGS, KoiEntity::setAttributes);
    }
}
