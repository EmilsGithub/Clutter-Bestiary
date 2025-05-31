package net.emilsg.clutter_bestiary.entity.custom.parent;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ParentAnimalEntity extends AnimalEntity {
    private static final TrackedData<Boolean> MOVING = DataTracker.registerData(ParentAnimalEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_FLEEING = DataTracker.registerData(ParentAnimalEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected ParentAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public boolean isFleeing() {
        return this.dataTracker.get(IS_FLEEING);
    }

    public boolean isMoving() {
        return this.dataTracker.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.dataTracker.set(MOVING, moving);
    }

    public void setIsFleeing(boolean moving) {
        this.dataTracker.set(IS_FLEEING, moving);
    }

    @Override
    public void tickMovement() {
        if (!this.getWorld().isClient) {
            Vec3d velocity = this.getVelocity();
            boolean isMoving = velocity.lengthSquared() > 1.0E-7;
            this.setMoving(isMoving);
        }
        super.tickMovement();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOVING, false);
        this.dataTracker.startTracking(IS_FLEEING, false);

    }
}
