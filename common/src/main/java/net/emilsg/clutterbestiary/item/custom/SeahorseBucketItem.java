package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.variants.SeahorseVariant;
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

public class SeahorseBucketItem extends EntityBucketItem {
    public static final MapCodec<SeahorseVariant> SEAHORSE_VARIANT_MAP_CODEC = SeahorseVariant.CODEC.fieldOf("Variant");
    private final Supplier<? extends EntityType<?>> type;

    public SeahorseBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(null, fluid, emptyingSound, settings);
        this.type = type;
    }

    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        if (world instanceof ServerWorld) {
            this.spawnEntity((ServerWorld) world, stack, pos);
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    private void spawnEntity(ServerWorld world, ItemStack stack, BlockPos pos) {
        Entity entity = this.type.get().spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            bucketable.copyDataFromNbt(nbtComponent.copyNbt());
            bucketable.setFromBucket(true);
        }

    }
}
