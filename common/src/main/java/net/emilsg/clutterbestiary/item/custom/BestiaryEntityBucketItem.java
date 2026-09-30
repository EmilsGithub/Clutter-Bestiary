package net.emilsg.clutterbestiary.item.custom;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public class BestiaryEntityBucketItem extends MobBucketItem {
    private final Supplier<? extends EntityType<?>> entityType;

    public BestiaryEntityBucketItem(Supplier<? extends EntityType<?>> entityType, Fluid fluid, SoundEvent emptyingSound, Properties settings) {
        super(null, fluid, emptyingSound, settings);
        this.entityType = entityType;
    }

    @Override
    public void checkExtraContent(@Nullable LivingEntity player, Level world, ItemStack stack, BlockPos pos) {
        if (!(world instanceof ServerLevel serverWorld)) return;

        Entity entity = this.entityType.get().spawn(serverWorld, stack, null, pos, EntitySpawnReason.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            CustomData entityData = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
            bucketable.loadFromBucketTag(entityData.copyTag());
            bucketable.setFromBucket(true);
        }

        world.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
    }
}
