package net.emilsg.clutterbestiary.entity.custom.parent;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParentWaterEntity extends WaterCreatureEntity {
    private static final TrackedData<Boolean> MOVING = DataTracker.registerData(ParentWaterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected ParentWaterEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean isMoving() {
        return this.dataTracker.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.dataTracker.set(MOVING, moving);
    }

    @Override
    public void tickMovement() {
        if (!this.getWorld().isClient) {
            Vec3d velocity = this.getVelocity();
            boolean isMoving = velocity.lengthSquared() > 0.0005f;
            this.setMoving(isMoving);
        }
        super.tickMovement();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOVING, false);
    }
}
