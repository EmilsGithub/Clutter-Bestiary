package net.emilsg.clutterbestiary.block.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;

public class ModBlockEntityTypes {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ClutterBestiary.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);

    public static void register() {
        BLOCK_ENTITIES.register();
    }

    public static final RegistrySupplier<BlockEntityType<ButterflyBottleBlockEntity>> BUTTERFLY_IN_A_BOTTLE = BLOCK_ENTITIES.register("butterfly_in_a_bottle", () -> BlockEntityType.Builder.create(ButterflyBottleBlockEntity::new,
            ModBlocks.BUTTERFLY_IN_A_BOTTLE.get()
    ).build(null));


}
