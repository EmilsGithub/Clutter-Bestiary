package net.emilsg.clutterbestiary.item.custom;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;

public class BestiarySpawnEggItem extends ArchitecturySpawnEggItem {

    public BestiarySpawnEggItem(RegistrySupplier<? extends EntityType<? extends MobEntity>> entityType, int backgroundColor, int highlightColor, Settings properties) {
        super(entityType, backgroundColor, highlightColor, properties);
    }

}
