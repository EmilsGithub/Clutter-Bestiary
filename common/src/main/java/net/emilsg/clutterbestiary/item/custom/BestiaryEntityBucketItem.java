package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BestiaryEntityBucketItem extends EntityBucketItem {
    private final Supplier<? extends EntityType<?>> entityType;

    public BestiaryEntityBucketItem(Supplier<? extends EntityType<?>> entityType, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(null, fluid, emptyingSound, settings);
        this.entityType = entityType;
    }

    @Override
    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        if (!(world instanceof ServerWorld serverWorld)) return;

        Entity entity = this.entityType.get().spawnFromItemStack(serverWorld, stack, null, pos, SpawnReason.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            NbtComponent entityData = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            bucketable.copyDataFromNbt(entityData.copyNbt());
            bucketable.setFromBucket(true);
        }

        world.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);
    }
}
