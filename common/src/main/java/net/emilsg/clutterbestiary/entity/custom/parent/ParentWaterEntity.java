package net.emilsg.clutterbestiary.entity.custom.parent;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParentWaterEntity extends WaterAnimal {
    private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(ParentWaterEntity.class, EntityDataSerializers.BOOLEAN);

    protected ParentWaterEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOVING, false);
    }

    public boolean isMoving() {
        return this.entityData.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(MOVING, moving);
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            Vec3 velocity = this.getDeltaMovement();
            boolean isMoving = velocity.lengthSqr() > 0.0005f;
            this.setMoving(isMoving);
        }
        super.aiStep();
    }
}
