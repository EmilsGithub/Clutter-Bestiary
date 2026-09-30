package net.emilsg.clutterbestiary.entity.custom.parent;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class ParentAnimalEntity extends Animal {
    private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(ParentAnimalEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FLEEING = SynchedEntityData.defineId(ParentAnimalEntity.class, EntityDataSerializers.BOOLEAN);

    protected ParentAnimalEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOVING, false);
        builder.define(IS_FLEEING, false);
    }

    @Nullable
    @Override
    public abstract AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity);

    @Override
    public abstract boolean isFood(ItemStack stack);

    public boolean isFleeing() {
        return this.entityData.get(IS_FLEEING);
    }

    public boolean isMoving() {
        return this.entityData.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(MOVING, moving);
    }

    public void setIsFleeing(boolean moving) {
        this.entityData.set(IS_FLEEING, moving);
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            Vec3 velocity = this.getDeltaMovement();
            boolean isMoving = velocity.lengthSqr() > 1.0E-7;
            this.setMoving(isMoving);
        }
        super.aiStep();
    }
}
