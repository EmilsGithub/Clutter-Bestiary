package net.emilsg.clutterbestiary.item.custom;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.TypedEntityData;

public class BestiarySpawnEggItem extends SpawnEggItem {

    public BestiarySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, Properties properties) {
        // The entity type is resolved lazily because items can be registered before entity types.
        super(properties.delayedComponent(DataComponents.ENTITY_DATA, context -> TypedEntityData.of(entityType.get(), new CompoundTag())));
    }

}
